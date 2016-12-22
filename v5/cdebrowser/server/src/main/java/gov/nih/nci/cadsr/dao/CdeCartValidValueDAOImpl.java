/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
/**
 *
 */
package gov.nih.nci.cadsr.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nih.nci.cadsr.dao.model.ValidValueCdeCartModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;

/**
 * @author asafievan
 *
 */
public class CdeCartValidValueDAOImpl extends AbstractDAOOperations implements CdeCartValidValueDAO {
    private static Logger logger = LogManager.getLogger( CdeCartValidValueDAOImpl.class.getName() );
    @SuppressWarnings("unused")
	private JdbcTemplate jdbcTemplate;

	public static final String RETRIEVE_VALID_VALUES_BY_VD_SQL = "select conte.NAME context, "
			+ "vm.PUBLIC_ID vm_id, pv.short_meaning short_meaning, vm.ASL_NAME workflowstatus, "
			+ "vm.SHORT_MEANING short_meaning_value, vm.version vm_version, "
			+ "vm.CONDR_IDSEQ concept_derivation_rule_idseq, vm.DESCRIPTION description, "
			+ "vdpvs.vd_idseq vd_idseq "
			+ "from sbrext.CABIO31_VM_VIEW vm, sbr.contexts conte, sbrext.CABIO31_PV_VIEW pv, SBR.VD_PVS vdpvs "
			+ "where vm.CONTE_IDSEQ = conte.CONTE_IDSEQ and "
			+ "vm.VM_IDSEQ = pv.VM_IDSEQ and "
			+ "vdpvs.vd_idseq = ? and "
			+ "pv.PV_IDSEQ = vdpvs.pv_idseq";

    @Autowired
    public CdeCartValidValueDAOImpl(DataSource dataSource) {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();

	}

	public List<ValidValueCdeCartModel> getValueDomainTransferObjectsById1(String vdIdseq) {
		//FIXME this is a sample code, to be changed to DB code
		List<ValidValueCdeCartModel> res = new ArrayList<>();
		ValidValueCdeCartModel validValueCdeCartModel = new ValidValueCdeCartModel();
		validValueCdeCartModel.setVdIdseq(vdIdseq);
		validValueCdeCartModel.setContext("NCI");
		validValueCdeCartModel.setShortMeaning("test ShortMeaning");
		validValueCdeCartModel.setShortMeaningValue("test ShortMeaningValue");
		validValueCdeCartModel.setVmId(2006475);//public ID
		validValueCdeCartModel.setVmVersion(2.0f);
		validValueCdeCartModel.setWorkflowstatus("CANDIDATE");
		res.add(validValueCdeCartModel);
		return res;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see gov.nih.nci.cadsr.dao.CdeCartValidValueDAO#
	 * getValueDomainTransferObjectsById(java.lang.String)
	 */
	@Override
	public List<ValidValueCdeCartModel> getValueDomainTransferObjectsById(String vdIdseq) {
		String sql = RETRIEVE_VALID_VALUES_BY_VD_SQL;
		// TODO remove this debug log
		logger.debug("Searching ValidValueCdeCartModel data for vdIdseq: " + vdIdseq + ", using SQL:  " + sql);

		List<ValidValueCdeCartModel> res = getAll(sql, vdIdseq, ValidValueCdeCartModel.class);
		logger.debug("Found ValidValueCdeCartModel data for vdIdseq: " + vdIdseq + ", in the amount:  "
				+ (res != null ? res.size() : "none"));

		// TODO remove this debug log
		logger.debug("List<ValidValueCdeCartModel>: " + res);

		return (res);
	}

}
