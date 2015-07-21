package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ValueDomainModel;

/**
 * Created by lavezzojl on 4/15/15.
 */
public interface ValueDomainDAO {
    ValueDomainModel getValueDomainByIdseq(String vdIdseq);
}
