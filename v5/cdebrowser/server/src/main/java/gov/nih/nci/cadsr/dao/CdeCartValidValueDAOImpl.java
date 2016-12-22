/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
/**
 * 
 */
package gov.nih.nci.cadsr.dao;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cadsr.dao.model.ValidValueCdeCartModel;

/**
 * @author asafievan
 *
 */
public class CdeCartValidValueDAOImpl implements CdeCartValidValueDAO {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.dao.CdeCartValidValueDAO#getValueDomainTransferObjectsById(java.lang.String)
	 */
	@Override
	public List<ValidValueCdeCartModel> getValueDomainTransferObjectsById(String vdIdseq) {
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
		
		return res;
	}

}
