package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;

import java.util.List;

/**
 * Created by lavezzojl on 4/15/15.
 */
public interface ReferenceDocDAO {
    List<ReferenceDocModel> getRefDocsByRdIdseq(String rdIdseq);
    List<ReferenceDocModel> getRefDocsByAcIdseq(String acIdseq);
}
