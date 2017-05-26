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
    
	protected static final String sqlRetrieveReferenceDocBlobByAcIdseq = 
		"select rb.RD_IDSEQ, rb.NAME DOC_NAME, rb.MIME_TYPE, rb.BLOB_CONTENT DOC_CONTENT from "
			+ "REFERENCE_BLOBS rb where rb.RD_IDSEQ = ? "
			+ "and rb.BLOB_CONTENT is not NULL";
	protected static final String sqlRetrieveLatestRdIdseqByAcIdseq = 
		"SELECT rd_idseq FROM sbr.reference_documents WHERE ac_idseq = ? order by display_order desc";
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
	 * @see gov.nih.nci.cadsr.dao.ReferenceDocBlobDAO#getReferenceDocBlobByAcIdseq(java.lang.String)
	 */
	@Override
	public ReferenceDocBlobModel getReferenceDocBlobByAcIdseq(String acIdseq) {
		String rdIdseq = getLatestRdIdseqByAcIdseq(acIdseq);
		if (StringUtils.isNotBlank(rdIdseq)) {
			logger.debug("Found rd_idseq by ac_idseq: " + acIdseq + ", rdIdseq: " + rdIdseq);
			try {
				logger.debug(sqlRetrieveReferenceDocBlobByAcIdseq.replace("?", rdIdseq) + " <<<<<<<");
				ReferenceDocBlobModel referenceDocBlobModel = jdbcTemplate.queryForObject(sqlRetrieveReferenceDocBlobByAcIdseq,
				new Object[] {rdIdseq}, MAPPER_ReferenceDocBlobModel);
				return referenceDocBlobModel;
			}
			catch (EmptyResultDataAccessException e) {
				logger.info("getReferenceDocBlobByAcIdseq Reference Document Context is not found by rdIdseq: " + rdIdseq);
			}
		}
		else {
			logger.debug("Not Found rd_idseq by ac_idseq: " + acIdseq);
		}
		return null;
	}

	@Override
	public String getLatestRdIdseqByAcIdseq(String acIdseq) {
		logger.debug(sqlRetrieveLatestRdIdseqByAcIdseq.replace("?", acIdseq) + " <<<<<<<");
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
				logger.info("getReferenceDocBlobByAcIdseq Reference Document IDSEQ is not found by acIdseq: " + acIdseq);
		}
		return referenceDocModelId;
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
