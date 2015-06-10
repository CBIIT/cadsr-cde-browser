package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.RepresentationConceptModel;

import java.util.List;

/**
 * Created by lernermh on 6/9/15.
 */
public interface RepresentationConceptsDAO
{
    public List<RepresentationConceptModel> getRepresentationConceptByRepresentationId( String representationId );
    public List<RepresentationConceptModel> getRepresentationConceptByRepresentationId( int representationId );
}
