/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

/**
 * 
 * @author asafievan
 *
 */
public class SearchQueryBuilderUtils {
	private static Logger logger = LogManager.getLogger(SearchQueryBuilderUtils.class.getName());

	public static String buildAltNamesWhere(String text, String altNameTypesStr) {
		String altWhere = "";
		String newSearchStr = "";
		String typeWhere = "";
		String altTypeStr = "";
		String searchWhere = "";
		
		if (text == null)
			return "";
		
		String[] altNameTypes = StringUtilities.buildArrayFromParameter(altNameTypesStr);
		
		newSearchStr = StringReplace.strReplace(text, "*", "%");
		newSearchStr = StringReplace.strReplace(newSearchStr, "'", "''");
		if (altNameTypes == null || StringUtilities.containsKeyLoop(altNameTypes, "ALL"))
			typeWhere = "";
		else if (altNameTypes.length == 1) {
			altTypeStr = altNameTypes[0];
			typeWhere = " and dsn.detl_name = '" + altTypeStr + "'";
		} 
		else {
			for (int i = 0; i < altNameTypes.length; i++) {
				if (i == 0)
					altTypeStr = "'" + altNameTypes[0] + "'";
				else
					altTypeStr = altTypeStr + "," + "'" + altNameTypes[i] + "'";
			}
			typeWhere = " and dsn.detl_name IN (" + altTypeStr + ")";
		}

		searchWhere = " and upper (nvl(dsn.name,'%')) like upper ('" + newSearchStr + "') ";

		altWhere = " and de.de_idseq IN " + "(select de_idseq "
				+ " from sbr.designations_view dsn, sbr.data_elements_view de1 "
				+ " where  de1.de_idseq  = dsn.ac_idseq " + typeWhere + searchWhere + " ) ";
		
		return altWhere;
	}
	
	public static String buildRegistrationWhere(String paramValue, String tableColumn) {
		String resultWhere = buildListStatusWhere(paramValue, ",", "ALL", tableColumn);
		return resultWhere;
	}
	
	public static String buildWorkflowWhere(String paramValue, String tableColumn) {
		String resultWhere = buildListStatusWhere(paramValue, ",", "ALL", tableColumn);
		return resultWhere;
	}
	
	public static String buildConceptWhere(SearchCriteria searchCriteria) {
		if (searchCriteria == null) return "";
		String conceptNameFilter = searchCriteria.getConceptInput();

		if (StringUtils.isBlank(conceptNameFilter)) {
				return "";
		}

		//in CDE Browser 5.3 we assume search by both: Concept code or Concept Name
		return buildConceptWhere(conceptNameFilter);

	}
	/**
	 * This function searches by both long and preferred names.
	 * SQL is taken from 4.0.5.
	 * 
	 * @param conceptName
	 * @param conceptCode
	 * @return DE where clause part
	 */
	public static String buildConceptWhere(String conceptFilter) {
		String conceptWhere = "";
		String conceptCodeWhere = "";
		String conceptNameWhere = "";

		boolean isNameEmpty = StringUtils.isBlank(conceptFilter);
		
		if (isNameEmpty) {
			logger.debug("No Concept filters given to DE search");
			return conceptWhere;
		}
		
		String newConceptFilter = conceptFilter.replaceAll("\\*", "%").trim();
		
		conceptNameWhere = " where upper(long_name) like upper('" + newConceptFilter + "')";		

		conceptCodeWhere += " or upper(preferred_name) like upper('"+ newConceptFilter + "')";

		conceptWhere = "and    de.de_idseq IN (" + "select de_idseq " + "from   sbr.data_elements_view "
					+ "where  dec_idseq IN (" + "select dec.dec_idseq " + "from   sbr.data_element_concepts_view dec, "
					+ "       sbrext.object_classes_view_ext oc " + "where  oc.oc_idseq = dec.oc_idseq "
					+ "and    oc.condr_idseq in(select cdr.condr_idseq "
					+ "from   sbrext.con_derivation_rules_view_ext cdr, "
					+ "       sbrext.component_concepts_view_ext cc " + "where  cdr.condr_idseq = cc.condr_idseq "
					+ "and    cc.con_idseq in (select con_idseq " + "from   sbrext.concepts_view_ext "
					+ conceptNameWhere + conceptCodeWhere + ")) " + "UNION " + "select dec.dec_idseq "
					+ "from   sbr.data_element_concepts_view dec, sbrext.properties_view_ext pc "
					+ "where  pc.prop_idseq = dec.prop_idseq " + "and    pc.condr_idseq in(select cdr.condr_idseq "
					+ "from   sbrext.con_derivation_rules_view_ext cdr, "
					+ "       sbrext.component_concepts_view_ext cc " + "where  cdr.condr_idseq = cc.condr_idseq "
					+ "and    cc.con_idseq in (select con_idseq " + "from   sbrext.concepts_view_ext "
					+ conceptNameWhere + conceptCodeWhere + "))) " + "UNION " + "select de_idseq "
					+ "from   sbr.data_elements_view " + "where  vd_idseq IN (select vd.vd_idseq "
					+ "from   sbr.value_domains_view vd, " + "       sbr.vd_pvs_view vp, "
					+ "       sbr.permissible_values_view pv, " + "       sbr.value_meanings_view vm "
					+ "where  vd.vd_idseq = vp.vd_idseq " + "and    pv.pv_idseq = vp.pv_idseq "
					+ "and    vm.vm_idseq = pv.vm_idseq " + "and     vm.condr_idseq in(select cdr.condr_idseq "
					+ "from   sbrext.con_derivation_rules_view_ext cdr, "
					+ "       sbrext.component_concepts_view_ext cc " + "where  cdr.condr_idseq = cc.condr_idseq "
					+ "and    cc.con_idseq in (select con_idseq " + "from   sbrext.concepts_view_ext "
					+ conceptNameWhere + conceptCodeWhere + ")))) ";
		
		logger.debug("conceptWhere: " + conceptWhere);
		return conceptWhere;
	}
	
	public static String buildListStatusWhere(String sourceStr, String separator, String allInclusive, String tableFieldName) {
		String resultWhere = "";
		String auxStr = "";
		
		if ((sourceStr == null) || (tableFieldName == null))
			return "";
		
		String[] arrvalues = StringUtilities.buildArrayFromParameter(sourceStr, separator);
		
		if (arrvalues == null || StringUtilities.containsKeyLoop(arrvalues, allInclusive))
			resultWhere = "";
		else if (arrvalues.length == 1) {
			auxStr = arrvalues[0];
			resultWhere = " AND " + tableFieldName + " = '" + auxStr + "'";
		} 
		else {
			for (int i = 0; i < arrvalues.length; i++) {
				if (i == 0)
					auxStr = "'" + arrvalues[0] + "'";
				else
					auxStr = auxStr + "," + "'" + arrvalues[i] + "'";
			}
			resultWhere = " AND " + tableFieldName + " IN (" + auxStr + ")";
		}
		
		return resultWhere;
	}
}
