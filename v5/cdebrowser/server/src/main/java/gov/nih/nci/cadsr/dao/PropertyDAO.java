package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.PropertyModel;

/**
 * Created by lavezzojl on 4/16/15.
 */
public interface PropertyDAO {
    PropertyModel getPropertyByIdseq(String propIdseq);
}
