package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;

/**
 * Created by lavezzojl on 4/15/15.
 */
public interface DataElementConceptDAO {

    DataElementConceptModel getDecByDecIdseq(String decIdseq);

}
