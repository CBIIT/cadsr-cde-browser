/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
/**
 * This class provides DB support for Typeahead search feature
 * CDEBROWSER-808
 * @author asafievan
 *
 */
public class TypeaheadSearchDAOImpl extends AbstractDAOOperations implements TypeaheadSearchDAO {
	//private static final Logger logger = LogManager.getLogger(TypeaheadDAOImpl.class.getName());
	private static final int maxLongNamesToReturn = 20;

	private JdbcTemplate jdbcTemplate;
	
	public static final String sqlRetrieveTypeaheadLongNameStartsWith = "select th from (select distinct substr(long_name, 1, 20) th from sbr.data_elements " + 
			"where (substr(long_name, 1, "
			+ maxLongNamesToReturn
			+ ") like ? ) order by th) where rownum < "
			+ maxLongNamesToReturn;	
	public static final String sqlRetrieveTypeaheadLongName = "select th from (select distinct substr(long_name, 1, 20) th from sbr.data_elements " + 
			"where (instr(substr(long_name, 1, "
			+ maxLongNamesToReturn
			+ "), ?, 1) > 0) order by th) where rownum < "
			+ maxLongNamesToReturn;

	@Autowired
	TypeaheadSearchDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = getJdbcTemplate();
	}
	@Override
	public List<String> getSearchTypeaheadLongName(String pattern) {
		if (StringUtils.isNotEmpty(pattern)) {
			List<String> models1 = jdbcTemplate.query(sqlRetrieveTypeaheadLongNameStartsWith, new Object[]{pattern + '%'}, new StringPropertyMapper(String.class));
			if (models1.size() >= 20) {
				return models1;
			}
			
			List<String> models2 = jdbcTemplate.query(sqlRetrieveTypeaheadLongName, new Object[]{pattern}, new StringPropertyMapper(String.class));
			for (int index = 0; (index < models2.size()) && (models1.size() < maxLongNamesToReturn); index++) {
				models1.add(models2.get(index));
			}
			return models1;
		}
		return new ArrayList<String>();	
	}

	public final class StringPropertyMapper extends BeanPropertyRowMapper<String> {
		public StringPropertyMapper(Class<String> mappedClass) {
			super(mappedClass);
		}

		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	}
}
