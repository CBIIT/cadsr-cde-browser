package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CSRefDocModel;

import java.util.List;

/**
 * Created by lavezzojl on 5/27/15.
 */
public interface CSRefDocDAO {
    List<CSRefDocModel> getCSRefDocsByDEIdseq(String deIdseq);
}
