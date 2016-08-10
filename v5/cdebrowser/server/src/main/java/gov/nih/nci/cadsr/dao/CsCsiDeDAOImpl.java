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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;
import gov.nih.nci.cadsr.dao.model.CsCsiDeModelList;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
/**
 * This is a DAO to retrieve Classification information based on DE for DE tab.
 * @author asafievan
 *
 */
public class CsCsiDeDAOImpl extends AbstractDAOOperations implements CsCsiDeDAO {
	//This SQL retrieves all Classification and Classification Items which are related to DE. This is done using AC_CSI table
	public final static String  sqlRetrieveCsCsiByDe = "SELECT " +
		"cscsi.cs_csi_idseq, "+
		"ClassificationSchemes.LONG_NAME cs_long_name, "+ 
		"ClassificationSchemes.PREFERRED_DEFINITION cs_definition, "+
		"ClassSchemeItems.LONG_NAME csi_name, "+
		"ClassSchemeItems.csitl_name "+
		"FROM sbr.AC_CSI AcCsi, sbr.ADMINISTERED_COMPONENTS AdministeredComponents, " +
		"sbr.CLASSIFICATION_SCHEMES ClassificationSchemes, sbr.CS_ITEMS_VIEW ClassSchemeItems, sbr.CS_CSI CsCsi " +
		"WHERE " +
		"(((AcCsi.AC_IDSEQ = AdministeredComponents.AC_IDSEQ) AND (CsCsi.CS_IDSEQ = ClassificationSchemes.CS_IDSEQ)) AND "+ 
		"(CsCsi.CSI_IDSEQ = ClassSchemeItems.CSI_IDSEQ)) AND (AcCsi.CS_CSI_IDSEQ = CsCsi.CS_CSI_IDSEQ) " +
		"and AdministeredComponents.ac_idseq = ? " +
		"order by upper(ClassificationSchemes.LONG_NAME), upper(ClassSchemeItems.LONG_NAME)";
	//This SQL retrieves all Alt Names related to DE and CS-CSI combinations. ac_att_cscsi_view_ext tracks CS-CSI and Alt Names relationships
	private final static String sqlRetrieveCsCsiAltNamesBegin = "SELECT ext.cs_csi_idseq desigIdseq, " +
       "desig.NAME name, desig.DETL_NAME type, desig.LAE_NAME lang, con.NAME contextName " +
       "FROM sbrext.ac_att_cscsi_view_ext ext, designations desig, contexts con " +
       "WHERE "
       + "ext.att_idseq = desig.DESIG_IDSEQ and desig.CONTE_IDSEQ = con.CONTE_IDSEQ "
       + "and desig.AC_IDSEQ = ? and ext.cs_csi_idseq IN ";
	
	private final static String sqlRetrieveCsCsiAltNamesEnd = "order by upper (desig.NAME), desig.DETL_NAME";
	
	//This SQL retrieves all Definitions related to DE and CS-CSI combinations. ac_att_cscsi_view_ext tracks CS-CSI and Definitions relationships	
	private final static String sqlRetrieveCsCsiDefinBegin = "SELECT ext.cs_csi_idseq definIdseq, " +
       "defin.DEFINITION definition, defin.DEFL_NAME type, con.NAME contextName " +
       "FROM sbrext.ac_att_cscsi_view_ext ext, definitions defin, contexts con " +
       "WHERE "
       + "ext.att_idseq = defin.DEFIN_IDSEQ and defin.CONTE_IDSEQ = con.CONTE_IDSEQ "
       + "and defin.AC_IDSEQ = ? and ext.cs_csi_idseq IN ";
			
	private final static String sqlRetrieveCsCsiDefinEnd = "order by upper (defin.DEFINITION), defin.DEFL_NAME";
			
	private static final Logger logger = LogManager.getLogger(CsCsiValueMeaningDAOImpl.class.getName());

	private JdbcTemplate jdbcTemplate;
	

	@Autowired
	CsCsiDeDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = getJdbcTemplate();
	}

	@Override
	public List<CsCsiDeModel> getCsCsisByDeId(String deIdseq) {
		logger.debug("deIdseq: " + deIdseq + "\n, sqlRetrieveCsCsiByDe: " + sqlRetrieveCsCsiByDe);
		List<CsCsiDeModel> models = jdbcTemplate.query(sqlRetrieveCsCsiByDe, 
			new Object[]{deIdseq}, new CsCsiDeRowMapper());
		if (models != null) {
			logger.debug("...found CsCsiDeModel amount: " + models.size());
			return models;
		}
		else {
			return new ArrayList<CsCsiDeModel>();
		}
	}
	
	public List<DesignationModelAlt> getCsCsiDeAltNamesById(String deIdseq, CsCsiDeModelList csCsiDeModelList) {
		String csCsiIdStr = csCsiDeModelList.buildInSql();
		String altNamesSql = sqlRetrieveCsCsiAltNamesBegin + csCsiIdStr + sqlRetrieveCsCsiAltNamesEnd;
		logger.debug("altNamesSql: " + altNamesSql);
		List<DesignationModelAlt> altNames = getAll(altNamesSql, deIdseq, DesignationModelAlt.class);
		if (altNames != null) {
			return altNames;
		}
		else {
			return new ArrayList<DesignationModelAlt>();
		}		
	}
	
	public List<DefinitionModelAlt> getCsCsiDeDefinitionsById(String deIdseq, CsCsiDeModelList csCsiDeModelList) {
		String csCsiIdStr = csCsiDeModelList.buildInSql();
		String definSql = sqlRetrieveCsCsiDefinBegin + csCsiIdStr + sqlRetrieveCsCsiDefinEnd;
		logger.debug("definSql: " + definSql);
		List<DefinitionModelAlt> defins = getAll(definSql, deIdseq, DefinitionModelAlt.class);
		if (defins != null) {
			return defins;
		}
		else {
			return new ArrayList<DefinitionModelAlt>();
		}		
	}
	
	public static class CsCsiDeRowMapper implements RowMapper<CsCsiDeModel> {
		@Override
		public CsCsiDeModel mapRow(ResultSet rs, int counts) throws SQLException {
			CsCsiDeModel model = new CsCsiDeModel();

			model.setCsLongName(rs.getString("cs_long_name"));
			model.setCsDefinition(rs.getString("cs_definition"));
			model.setCsiName(rs.getString("csi_name"));
			model.setCsitlName(rs.getString("csitl_name"));
			model.setCsCsiIdseq(rs.getString("cs_csi_idseq"));
			return model;
		}

	}
}

