package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.AcAttCsCsiModel;

import java.util.List;

/**
 * Created by lavezzojl on 5/8/15.
 */
public interface AcAttCsCsiDAO {
    List<AcAttCsCsiModel> getAllAcAttCsCsiByAttIdseq(String attIdseq);
}
