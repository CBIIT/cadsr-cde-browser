/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.util.List;

import gov.nih.nci.cadsr.dao.model.ValidValueCdeCartModel;
/**
 * @author asafievan
 * This interface encapsulates methods related to Value Domain section of CDE Cart XML as getting ValueDomainTransferObject list.
 */
public interface CdeCartValidValueDAO {
    public List<ValidValueCdeCartModel> getValueDomainTransferObjectsById(String vdIdseq);
}
