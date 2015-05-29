package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CSIRefDocModel;

import java.util.List;

/**
 * Created by lavezzojl on 5/27/15.
 */
public interface CSIRefDocDAO {
    List<CSIRefDocModel> getCSIRefDocsByDEIdseq(String deIdseq);
}
