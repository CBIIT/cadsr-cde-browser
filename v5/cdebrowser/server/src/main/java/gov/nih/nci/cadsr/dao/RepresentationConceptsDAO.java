package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;

import java.util.List;

/**
 * Created by lernermh on 6/9/15.
 */
public interface RepresentationConceptsDAO
{
    public List<ConceptModel> getRepresentationConceptByRepresentationId( String representationId );
    public List<ConceptModel> getRepresentationConceptByRepresentationId( int representationId );
}
