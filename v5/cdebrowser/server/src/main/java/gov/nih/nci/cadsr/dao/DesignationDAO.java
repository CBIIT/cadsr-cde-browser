package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DesignationModel;

import java.util.List;

/**
 * Created by lavezzojl on 4/15/15.
 */
public interface DesignationDAO {
    List<DesignationModel> getDesignationModelsByAcIdseq(String acIdseq);
}
