/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
/**
 * This class provides DB support for Typeahead search feature
 * CDEBROWSER-808
 * @author asafievan
 *
 */
public class TypeaheadSearchDAOImpl extends AbstractDAOOperations implements TypeaheadSearchDAO {
	private static final Logger logger = LogManager.getLogger(TypeaheadSearchDAOImpl.class.getName());
	
	private static final int maxLongNamesToReturn = 21;
	private JdbcTemplate jdbcTemplate;
	//TODO decide do we need to consider Search Preferences when we generate typeahead results
	//Do we want to use All words if a filter text contains a space? We use the exact received token now.
	
	//These are SQLs for final implementation
	protected static final String ASL_NAME_NOT_USED_BEGIN = " AND de.ASL_NAME NOT IN (";
	protected static final String ASL_NAME_NOT_USED_END = ")";
	//This is a formatted String to add excluded contexts later
	protected static final String sqlRetrieve_END = ASL_NAME_NOT_USED_END + "%s order by th) where rownum < " + maxLongNamesToReturn + ") ";
	protected static final String START_TYPEAHEAD_SELECT = "select th from (";
	protected static final String END_TYPEAHEAD_SELECT = ") where rownum < " + maxLongNamesToReturn;
	protected static final String UNION = "UNION ";
	protected static final String ALL = "ALL Fields";
	protected static final String CONTEXT_EXCLUDED_NONE = "";
	protected static final String CONTEXT_EXCLUDED_TEST = " AND de.CONTE_IDSEQ <> '29A8FB18-0AB1-11D6-A42F-0010A4C1E842'";
	protected static final String CONTEXT_EXCLUDED_TRAINING = " AND de.CONTE_IDSEQ <> 'E5CA1CEF-E2C6-3073-E034-0003BA3F9857'";
	protected static final String CONTEXT_EXCLUDED_BOTH = " AND de.CONTE_IDSEQ NOT IN ('E5CA1CEF-E2C6-3073-E034-0003BA3F9857', '29A8FB18-0AB1-11D6-A42F-0010A4C1E842')";
	//TODO consider to get IDSEQ values from DB and to parametrize SQL Strings. We have the same CONTE_IDSEQ on all tiers at the moment
	//E5CA1CEF-E2C6-3073-E034-0003BA3F9857 Training
	//29A8FB18-0AB1-11D6-A42F-0010A4C1E842 TEST
	protected static final String sqlRetrieveLongName = 
			UNION
			+ "(select th from (select distinct lower(de.long_name) th from sbr.data_elements de " + 
			"WHERE "
			+ "instr(UPPER(de.long_name), ?, 1) > 0 "
			+ ASL_NAME_NOT_USED_BEGIN;
	
	protected static final String sqlRetrieveShortName = 
			UNION
			+ "(select th from (select distinct lower(de.preferred_name) th from sbr.data_elements de " + 
			"WHERE "
			+ "instr(UPPER(de.preferred_name), ?, 1) > 0 "
			+ ASL_NAME_NOT_USED_BEGIN;
	
	protected static final String sqlRetrievePrefQuestionText = 
			UNION
			+ "(select th from (select distinct lower(rd1.doc_text) th from sbr.reference_documents rd1, sbr.data_elements de "
			+ "WHERE "
			+ "rd1.ac_idseq = de.de_idseq " //we are interested only in Ref docs which connect to CDEs
			+ "AND instr(UPPER(rd1.doc_text), ?, 1) > 0 "
			+ "AND rd1.dctl_name = 'Preferred Question Text' "
			+ ASL_NAME_NOT_USED_BEGIN;
	
	protected static final String sqlRetrieveAltQuestionText = 
			UNION
			+ "(select th from (select distinct lower(rd2.doc_text) th from sbr.reference_documents rd2, sbr.data_elements de "
			+ "WHERE "
			+ "rd2.ac_idseq = de.de_idseq " //we are interested only in Ref docs which connect to CDEs
			+ "AND instr(UPPER(rd2.doc_text), ?, 1) > 0 "
			+ "AND rd2.dctl_name = 'Alternate Question Text' "
			+ ASL_NAME_NOT_USED_BEGIN;
	
	protected static final String sqlRetrieveUMLStartsWith = 
			UNION
			+ "(select th from (SELECT distinct lower(dsn.name) th FROM sbr.designations dsn, sbr.data_elements de "
			+ "WHERE "
			+ "dsn.ac_idseq = de.de_idseq "
			+ "AND instr(UPPER(dsn.name), ?, 1) > 0 "
			+ "AND dsn.detl_name = 'UML Class:UML Attr' "
			+ ASL_NAME_NOT_USED_BEGIN;
	
	protected static final Map<String, String> filterInputSqls = new HashMap<>();
	{
		filterInputSqls.put("Long Name", sqlRetrieveLongName);
		filterInputSqls.put("Short Name", sqlRetrieveShortName);
		filterInputSqls.put("Preferred Question Text", sqlRetrievePrefQuestionText);
		filterInputSqls.put("Alternate Question Text", sqlRetrieveAltQuestionText);
		filterInputSqls.put("UML Class: UML Attr Alternate Name", sqlRetrieveUMLStartsWith);
	}

	@Autowired
	TypeaheadSearchDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = getJdbcTemplate();
	}


	public final class StringPropertyMapper extends BeanPropertyRowMapper<String> {
		public StringPropertyMapper(Class<String> mappedClass) {
			super(mappedClass);
		}

		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	}

	@Override
	public List<String> buildSearchTypeaheadByNameAndDomain(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer) {
		String filteredInput = searchCriteria.getFilteredinput();
		String pattern = searchCriteria.getName();
		List<String> workflowStatusExcludedList = searchPreferencesServer.getWorkflowStatusExcluded();
		if ((StringUtils.isNotEmpty(pattern)) && (StringUtils.isNotEmpty(filteredInput))) {
			//We do not sanitize in here because we use prepared statements. We pass the value, we do not create a concatenated SQL with this value.
			//pattern = StringUtilities.sanitizeForSql(pattern);
			String[] searchDomain = filteredInput.split( "\\s*,\\s*" );//comma separated string with domain values example: filteredinput=Long+Name,Short+Name
			int numOfDomains = searchDomain.length;
			if (numOfDomains > 0) {
				String sqlForTypeahead = buildTypeaheadDomainSql(searchDomain, searchPreferencesServer);
				if (sqlForTypeahead != null) {
					if ((StringUtilities.containsKey(searchDomain, ALL))) {//we ignore other received if there is ALL, we just take all domains  
						numOfDomains = filterInputSqls.size();
					}
					Object[] sqlParamArr = buildSqlParamList(numOfDomains, pattern.toUpperCase(), workflowStatusExcludedList);
					List<String> models1;
					models1 = jdbcTemplate.query(sqlForTypeahead, sqlParamArr, new StringPropertyMapper(String.class));
					//logger.debug("buildSearchTypeaheadName found matches: " + models1.size() + models1);
					return models1;
				}
			}
		}
		return new ArrayList<String>();	
	}
	//Add generation Workflow status excluded for Prepare Statement
	/**
	 * The parameter list is never empty, contains at least RETIRED_DELETED.
	 * 
	 * @param workflowStatusExcludedList List<String>
	 * @return String "?, ?, ...?"
	 */
	protected static String generateWorkflowPalceholder(int workflowStatusExcludedAmount) {
		StringBuilder sb  = new StringBuilder();
		for (int i = 0; i <  workflowStatusExcludedAmount; i++) {
			sb.append("?, ");
		}
		String res = sb.toString();
		return res.substring(0, res.length() - 2);
	}
	/**
	 * 
	 * @param numOfDomains shall be more than 0
	 * @param upperCase
	 * @return array Object[]
	 */
	protected static Object[] buildSqlParamList(int numOfDomains, String upperCase, List<String> workflowStatusExcludedList) {
		int numOfExcluded = workflowStatusExcludedList.size()*numOfDomains;//each domain has the same list of excluded Workflow statuses
		int numParams = numOfDomains + numOfExcluded;
		Object[] arr = new Object[numParams];
		int currRow = 0;
		for (int i = 0; i < numOfDomains; i++) {
			arr[currRow++] = upperCase; 
			for (int index = 0; index < workflowStatusExcludedList.size(); index++) {
				arr[currRow++] = workflowStatusExcludedList.get(index);
			}
		}
		return arr;
	}
	protected static String buildTypeaheadDomainSql(String[] searchDomain, SearchPreferencesServer searchPreferencesServer) {
		List<String> domainSqlList = new ArrayList<>();
		List<String> workflowStatusExcludedList = searchPreferencesServer.getWorkflowStatusExcluded();
		int workflowStatusExcludedAmount = workflowStatusExcludedList.size();
		String res = null;
		if (StringUtilities.containsKey(searchDomain, ALL)) {
			res = buildAlldDomainSql(searchPreferencesServer);
		}
		else {
			String placeholders = generateWorkflowPalceholder(workflowStatusExcludedAmount);
			for (String curr : searchDomain) {
				if (curr != null) {
					String sqlCurr = filterInputSqls.get(curr);
					if (sqlCurr != null) {
						domainSqlList.add(sqlCurr);
					}
					else {
						logger.error("buildTypeaheadDomainSql unknown filterInput value: " + curr);
					}
				}
			}
			if (domainSqlList.size() > 0) {
				String excludeContext = buildExcludeContext(searchPreferencesServer);
				String fornmattedExcludes = String.format(sqlRetrieve_END, excludeContext);
				StringBuilder sb = new StringBuilder();
				StringBuilder sbUnion = new StringBuilder();
				for (String curr : domainSqlList) {
					sbUnion.append(curr);
					sbUnion.append(placeholders);
					sbUnion.append(fornmattedExcludes);
				}
				sbUnion.delete(0, UNION.length());
				sb.append(START_TYPEAHEAD_SELECT);
				sb.append(sbUnion);
				sb.append(END_TYPEAHEAD_SELECT);
				res = sb.toString();
				//logger.debug("buildTypeaheadDomainSql: " + res);
			}
		}
		return res;
	}
	protected static String buildAlldDomainSql(SearchPreferencesServer searchPreferencesServer) {
		List<String> workflowStatusExcludedList = searchPreferencesServer.getWorkflowStatusExcluded();
		int workflowStatusExcludedAmount = workflowStatusExcludedList.size();
		String excludeContext = buildExcludeContext(searchPreferencesServer);
		String fornmattedExcludes = String.format(sqlRetrieve_END, excludeContext);
		StringBuilder sb = new StringBuilder();
		StringBuilder sbUnion = new StringBuilder();
		String placeholders = generateWorkflowPalceholder(workflowStatusExcludedAmount);
		//we have 5 groups here
		sbUnion.append(sqlRetrieveLongName);
		sbUnion.append(placeholders);
		sbUnion.append(fornmattedExcludes);
		
		sbUnion.append(sqlRetrieveShortName);
		sbUnion.append(placeholders);
		sbUnion.append(fornmattedExcludes);
		
		sbUnion.append(sqlRetrievePrefQuestionText);
		sbUnion.append(placeholders);
		sbUnion.append(fornmattedExcludes);
		
		sbUnion.append(sqlRetrieveAltQuestionText);
		sbUnion.append(placeholders);
		sbUnion.append(fornmattedExcludes);
		
		sbUnion.append(sqlRetrieveUMLStartsWith);		
		sbUnion.append(placeholders);
		sbUnion.append(fornmattedExcludes);
		
		sbUnion.delete(0, UNION.length());//the first word UNION deleted
		sb.append(START_TYPEAHEAD_SELECT);
		sb.append(sbUnion);
		sb.append(END_TYPEAHEAD_SELECT);
		String resSql = sb.toString();
		//logger.debug("buildAlldDomainSql: " + resSql);
		return resSql;
	}
	protected static String buildExcludeContext(SearchPreferencesServer searchPreferencesServer) {
		if ((searchPreferencesServer.isExcludeTest()) && (searchPreferencesServer.isExcludeTraining())) {
			return CONTEXT_EXCLUDED_BOTH;
		}
		else if (searchPreferencesServer.isExcludeTest()) {
			return CONTEXT_EXCLUDED_TEST;
		}
		else if (searchPreferencesServer.isExcludeTraining()) {
			return CONTEXT_EXCLUDED_TRAINING;
		}
		else return CONTEXT_EXCLUDED_NONE;
	}
	
	//TODO Remove when design is finalized SQLs
	//These are SQLs for a chance we want to restrict returned strings lengths
//	public static final String sqlRetrieveTypeaheadLongNameStartsWith = "select th from (select distinct substr(lower(long_name), 1, 20) th from sbr.data_elements " + 
//			"where (substr(lower(long_name), 1, "
//			+ maxLongNamesToReturn
//			+ ") like ? ) order by th) where rownum < "
//			+ maxLongNamesToReturn;
//	public static final String sqlRetrieveTypeaheadLongName = "select th from (select distinct substr(lower(long_name), 1, 20) th from sbr.data_elements " + 
//			"where (instr(substr(lower(long_name), 1, "
//			+ maxLongNamesToReturn
//			+ "), ?, 1) > 0) order by th) where rownum < "
//			+ maxLongNamesToReturn;
//
//	//These are SQLs to return full length strings - this one is currently used for the client
//	public static final String sqlRetrieveTypeaheadLongNameStartsWithFull = "select th from (select distinct lower(long_name) th from sbr.data_elements " + 
//			"where lower(long_name) "
//			+ "like ? order by th) where rownum < "
//			+ maxLongNamesToReturn;	
//	public static final String sqlRetrieveTypeaheadLongNameFull = "select th from (select distinct lower(long_name) th from sbr.data_elements " + 
//			"where instr(lower(long_name), ?, 1) > 0 order by th) where rownum < "
//			+ maxLongNamesToReturn;
	//TODO Remove when design is finalized SQLs - end	
	//TODO remove commented initial versions
//	@Override
//	public List<String> getSearchTypeaheadLongName(String pattern) {
//		if (StringUtils.isNotEmpty(pattern)) {
//			List<String> models1 = jdbcTemplate.query(sqlRetrieveTypeaheadLongNameStartsWith, new Object[]{pattern.toLowerCase() + '%'}, new StringPropertyMapper(String.class));
//			if (models1.size() >= 20) {
//				return models1;
//			}
//			
//			List<String> models2 = jdbcTemplate.query(sqlRetrieveTypeaheadLongName, new Object[]{pattern.toLowerCase()}, new StringPropertyMapper(String.class));
//			for (int index = 0; (index < models2.size()) && (models1.size() < maxLongNamesToReturn); index++) {
//				models1.add(models2.get(index));
//			}
//			return models1;
//		}
//		return new ArrayList<String>();	
//	}
	//TODO remove commented initial versions
//	@Override
//	public List<String> getSearchTypeaheadLongNameFull(String pattern) {
//		if (StringUtils.isNotEmpty(pattern)) {
//			List<String> models1 = jdbcTemplate.query(sqlRetrieveTypeaheadLongNameStartsWithFull, new Object[]{pattern.toLowerCase() + '%'}, new StringPropertyMapper(String.class));
//			if (models1.size() >= maxLongNamesToReturn) {
//				return models1;
//			}
//			
//			List<String> models2 = jdbcTemplate.query(sqlRetrieveTypeaheadLongNameFull, new Object[]{pattern.toLowerCase()}, new StringPropertyMapper(String.class));
//			for (int index = 0; (index < models2.size()) && (models1.size() < maxLongNamesToReturn); index++) {
//				models1.add(models2.get(index));
//			}
//			return models1;
//		}
//		return new ArrayList<String>();	
//	}
}
