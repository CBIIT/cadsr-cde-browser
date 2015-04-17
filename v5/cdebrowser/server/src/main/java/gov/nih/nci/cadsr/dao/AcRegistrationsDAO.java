package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.AcRegistrationsModel;

/**
 * Created by lavezzojl on 4/17/15.
 */
public interface AcRegistrationsDAO {
    AcRegistrationsModel getAcRegistrationByAcIdseq(String acIdseq);
}
