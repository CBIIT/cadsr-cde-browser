package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;

import java.util.List;

/**
 * Created by lernermh on 7/14/15.
 */
public interface ValueDomainConceptDAO
{
    List<ConceptModel> getValueDomainConceptByVdIdseq( String vdIdseq );
}
