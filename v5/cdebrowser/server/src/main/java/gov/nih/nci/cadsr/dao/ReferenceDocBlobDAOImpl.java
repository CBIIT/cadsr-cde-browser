/**
 * Copyright (C) 2017 Leidos Biomedical Research, Inc. - All rights reserved.
 */
/**
 * 
 */
package gov.nih.nci.cadsr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cadsr.dao.model.ReferenceDocBlobModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;

/**
 * @author asafievan
 *
 */
public class ReferenceDocBlobDAOImpl extends AbstractDAOOperations implements ReferenceDocBlobDAO {
    private static Logger logger = LogManager.getLogger(ReferenceDocBlobDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;
    
    //This SQL retrieves the latest Ref Doc BLOB BLOB itself
	protected static final String sqlRetrieveReferenceDocBlobByRdIdseq = 
		"select rb.RD_IDSEQ, rb.NAME DOC_NAME, rb.MIME_TYPE, rb.BLOB_CONTENT DOC_CONTENT from "
			+ "sbr.REFERENCE_BLOBS rb where rb.RD_IDSEQ = ? "
			+ "and rb.BLOB_CONTENT is not NULL order by rb.DATE_CREATED desc";
	
	//This SQL is used to check if RD_IDSEQ has any BLOB to download
	protected static final String sqlDownloadBlobIdseqByAcIdseq = 
			"select rb.RD_IDSEQ from "
				+ "sbr.REFERENCE_BLOBS rb where rb.RD_IDSEQ = ? "
				+ "and rb.BLOB_CONTENT is not NULL";
	
	//Used to retrieve a Reference Document of a CDE based on Ref Doc Display Order
	protected static final String sqlRetrieveLatestRdIdseqByAcIdseq = 
		"SELECT rd_idseq FROM sbr.reference_documents WHERE ac_idseq = ? order by display_order";

	ReferenceDocBlobDAO referenceDocBlobDAO;
	@Autowired
	ReferenceDocBlobDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = getJdbcTemplate();
	}

	/**
	 * This is to use in unit tests only
	 * 
	 * @param jdbcTemplate
	 */
	protected ReferenceDocBlobDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.dao.ReferenceDocBlobDAO#retrieveReferenceDocBlobByRdIdseq(java.lang.String)
	 */
	@Override
	public ReferenceDocBlobModel retrieveReferenceDocBlobByRdIdseq(String rdIdseq) {
		//logger.debug("retrieveReferenceDocBlobByRdIdseq by rdIdseq: " + rdIdseq);
		if (StringUtils.isNotBlank(rdIdseq)) {
			try {
				logger.debug(sqlRetrieveReferenceDocBlobByRdIdseq.replace("?", rdIdseq) + " <<<<<<<");
				List<ReferenceDocBlobModel> referenceDocBlobModelList = jdbcTemplate.query(sqlRetrieveReferenceDocBlobByRdIdseq,
				new Object[] {rdIdseq}, MAPPER_ReferenceDocBlobModel);
				if ((referenceDocBlobModelList != null) && (referenceDocBlobModelList.size() > 0)) {
					return referenceDocBlobModelList.get(0);
				}
			}
			catch (Exception e) {
				logger.error("retrieveReferenceDocBlobByRdIdseq Reference Document Context is not found by rdIdseq: " + rdIdseq + e);
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.dao.ReferenceDocBlobDAO#existReferenceDocBlobByAcIdseq(java.lang.String)
	 */
	@Override
	public String retrieveDownloadBlobIdseqByAcIdseq(String acIdseq) {
		//logger.debug(sqlRetrieveLatestRdIdseqByAcIdseq.replace("?", acIdseq) + " <<<<<<<");
		//find the reference document with the preferred display order
		String rdIdseq = retrieveLatestRdIdseqByAcIdseq(acIdseq);
		String downloadIdseq = null;
		if (StringUtils.isNotBlank(rdIdseq)) {//check that this rdIdseq has any BLOBs
			try {
				//logger.debug(sqlDownloadBlobIdseqByAcIdseq.replace("?", rdIdseq) + " <<<<<<<");
				//check if this Reference Document found above has any BLOB to Download Template. If yes this found rd_idseq will be attached to Protocol Form.
				//This not-null rd_idseq in Protocol shows that we need to show Download Template button in UI.
				List<String> rdIdseqList = jdbcTemplate.query(sqlDownloadBlobIdseqByAcIdseq,
					new Object[] {rdIdseq}, new StringPropertyMapper(String.class));
				if ((rdIdseqList != null) && (rdIdseqList.size() > 0)) {
					return rdIdseq;
				}
			}
			catch (Exception e) {
					logger.debug("retrieveDownloadBlobIdseqByAcIdseq Reference Document IDSEQ is not found by acIdseq: " + acIdseq + ", and rdIdseq: " + rdIdseq + e);
			}
		}
		return downloadIdseq;
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.dao.ReferenceDocBlobDAO#retrieveLatestRdIdseqByAcIdseq(java.lang.String)
	 */
	@Override
	public String retrieveLatestRdIdseqByAcIdseq(String acIdseq) {
		//logger.debug(sqlRetrieveLatestRdIdseqByAcIdseq.replace("?", acIdseq) + " <<<<<<<");
		String referenceDocModelId = null;
		try {
			List<String> rdIdSeqList = jdbcTemplate.query(
					sqlRetrieveLatestRdIdseqByAcIdseq,
				new Object[] {acIdseq}, new StringPropertyMapper(String.class));
			if ((rdIdSeqList != null) && (rdIdSeqList.size() > 0)) {
				return rdIdSeqList.get(0);
			}
		}
		catch (EmptyResultDataAccessException e) {
				logger.debug("retrieveLatestRdIdseqByAcIdseq Reference Document IDSEQ is not found by acIdseq: " + acIdseq);
		}
		return referenceDocModelId;
	}
	
	//This method is not used
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.dao.ReferenceDocBlobDAO#retrieveReferenceDocBlobByAcIdseq(java.lang.String)
	 */
	@Override
	public ReferenceDocBlobModel retrieveReferenceDocBlobByAcIdseq(String acIdseq) {
		String rdIdseq = retrieveLatestRdIdseqByAcIdseq(acIdseq);
		if (StringUtils.isNotBlank(rdIdseq)) {
			logger.debug("Found rd_idseq by ac_idseq: " + acIdseq + ", rdIdseq: " + rdIdseq);
			try {
				logger.debug(sqlRetrieveReferenceDocBlobByRdIdseq.replace("?", rdIdseq) + " <<<<<<<");
				ReferenceDocBlobModel referenceDocBlobModel = jdbcTemplate.queryForObject(sqlRetrieveReferenceDocBlobByRdIdseq,
				new Object[] {rdIdseq}, MAPPER_ReferenceDocBlobModel);
				return referenceDocBlobModel;
			}
			catch (EmptyResultDataAccessException e) {
				logger.info("retrieveReferenceDocBlobByAcIdseq Reference Document Context is not found by rdIdseq: " + rdIdseq);
			}
		}
		else {
			logger.debug("Not Found rd_idseq by ac_idseq: " + acIdseq);
		}
		return null;
	}

	protected static final RowMapper<ReferenceDocBlobModel> MAPPER_ReferenceDocBlobModel =
			(rs, i) -> new ReferenceDocBlobModel
		(rs.getString("RD_IDSEQ"),
		rs.getString("DOC_NAME"),
		rs.getString("MIME_TYPE"),
		rs.getBinaryStream("DOC_CONTENT"));

	public final class StringPropertyMapper extends BeanPropertyRowMapper<String> {
		public StringPropertyMapper(Class<String> mappedClass) {
			super(mappedClass);
		}

		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(1);
		}
	}


}
