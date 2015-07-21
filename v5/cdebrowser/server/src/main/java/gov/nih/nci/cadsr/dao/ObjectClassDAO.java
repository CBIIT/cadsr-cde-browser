package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ObjectClassModel;

/**
 * Created by lavezzojl on 4/16/15.
 */
public interface ObjectClassDAO {
    ObjectClassModel getObjectClassByIdseq(String propIdseq);
}
