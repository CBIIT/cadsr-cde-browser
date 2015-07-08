package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;

import java.util.List;

/**
 * Created by lernermh on 7/6/15.
 */
public interface ObjectClassConceptDAO
{
    List<ConceptModel> getObjectClassConceptByDecIdseq( String decIdseq);
}
