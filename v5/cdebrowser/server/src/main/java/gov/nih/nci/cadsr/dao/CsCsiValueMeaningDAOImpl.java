/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
/**
 * This is a DAO to retrieve Classification information based on VM for Value Domain tab.
 * @author asafievan
 *
 */
public class CsCsiValueMeaningDAOImpl extends AbstractDAOOperations implements CsCsiValueMeaningDAO {
	public final static String  sqlRetrieveCsCsiByVm = "SELECT cs.long_name cs_long_name, " +
		"cs.preferred_definition cs_definition, " +
		"csi.long_name csi_name, " +
		"csi.csitl_name csitl_name, " +
		"csi.csi_id csi_id, " +
		"csi.csi_idseq, " +
		"csi.version csi_version, " +
		"ext.att_idseq," +
		"ext.aca_idseq, " +
		"cs.cs_idseq, " +
		"csi.csi_idseq, " +
		"cscsi.cs_csi_idseq " +
		"FROM sbrext.ac_att_cscsi_view_ext ext, sbr.cs_csi_view cscsi, " +
		"sbr.cs_items_view csi, sbr.classification_schemes_view cs " +
		"WHERE ( (ext.att_idseq in (select DESIG_IDSEQ from designations where AC_IDSEQ = ?)) or " +
		"(ext.att_idseq in (select DEFIN_IDSEQ from definitions where AC_IDSEQ = ?))) " +
		"AND ext.cs_csi_idseq = cscsi.cs_csi_idseq " +
		"AND cscsi.csi_idseq = csi.csi_idseq " +
		"AND cscsi.cs_idseq = cs.cs_idseq " +
		"ORDER BY cs.long_name, csi.long_name";
	
	private static final Logger logger = LogManager.getLogger(CsCsiValueMeaningDAOImpl.class.getName());

	private JdbcTemplate jdbcTemplate;
	

	@Autowired
	CsCsiValueMeaningDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = getJdbcTemplate();
	}

	@Override
	public List<CsCsiValueMeaningModel> getCsCsisByVmId(String vmIdseq) {
		@SuppressWarnings("unchecked")
		List<CsCsiValueMeaningModel> models = jdbcTemplate.query(sqlRetrieveCsCsiByVm, 
			new Object[]{vmIdseq, vmIdseq}, new CsCsiValueMeaningRowMapper());
		if (models != null) {
			return models;
		}
		else {
			return new ArrayList<CsCsiValueMeaningModel>();
		}
	}

	public static class CsCsiValueMeaningRowMapper implements RowMapper<CsCsiValueMeaningModel> {
		@Override
		public CsCsiValueMeaningModel mapRow(ResultSet rs, int counts) throws SQLException {
			CsCsiValueMeaningModel model = new CsCsiValueMeaningModel();

			model.setCsLongName(rs.getString("cs_long_name"));
			model.setCsDefinition(rs.getString("cs_definition"));
			model.setCsiName(rs.getString("csi_name"));
			model.setCsitlName(rs.getString("csitl_name"));
			model.setCsiId(rs.getInt("csi_id"));
			model.setCsiIdseq(rs.getString("csi_idseq"));
			model.setCsiVersion(rs.getFloat("csi_version"));
			model.setAttIdseq(rs.getString("att_idseq"));
			model.setAcaIdseq(rs.getString("aca_idseq"));
			model.setCsIdseq(rs.getString("cs_idseq"));
			model.setCsiIdseq(rs.getString("csi_idseq"));
			model.setCsCsiIdseq(rs.getString("cs_csi_idseq"));
			return model;
		}

	}
}

