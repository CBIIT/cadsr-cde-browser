package gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

import java.util.List;

public class DataElementConcept
{
    private SelectedDataElement selectedDataElement = null;
    private DataElementConceptDetails dataElementConceptDetails = null;
    private ObjectClass objectClass = null;
    private List<ConceptModel> objectClassConcepts = null;
    private Property property = null;
    private List<ConceptModel> propertyConcepts = null;


    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }

    public DataElementConceptDetails getDataElementConceptDetails()
    {
        return dataElementConceptDetails;
    }

    public void setDataElementConceptDetails( DataElementConceptDetails dataElementConceptDetails )
    {
        this.dataElementConceptDetails = dataElementConceptDetails;
    }

    public ObjectClass getObjectClass()
    {
        return objectClass;
    }

    public void setObjectClass( ObjectClass objectClass )
    {
        this.objectClass = objectClass;
    }

    public List<ConceptModel> getObjectClassConcepts()
    {
        return objectClassConcepts;
    }

    public void setObjectClassConcepts( List<ConceptModel> objectClassConcepts )
    {
        this.objectClassConcepts = objectClassConcepts;
    }

    public Property getProperty()
    {
        return property;
    }

    public void setProperty( Property property )
    {
        this.property = property;
    }

    public List<ConceptModel> getPropertyConcepts()
    {
        return propertyConcepts;
    }

    public void setPropertyConcepts( List<ConceptModel> propertyConcepts )
    {
        this.propertyConcepts = propertyConcepts;
    }
}
