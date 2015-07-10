package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;

import java.util.List;

/**
 * Created by lernermh on 7/8/15.
 */
public interface PropertyConceptDAO
{
    List<ConceptModel> getPropertyConceptByDecIdseq( String decIdseq );
}
