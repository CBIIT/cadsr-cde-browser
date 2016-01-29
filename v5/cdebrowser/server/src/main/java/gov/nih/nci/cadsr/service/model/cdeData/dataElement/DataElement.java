package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;


public class DataElement
{
    private DataElementDetails dataElementDetails;
    private List<ReferenceDocument> referenceDocuments;
    private List<AlternateName> alternateNames; // probably taking this out
    private List<AlternateDefinition> alternateDefinitions; // probably taking this out
    private List<CsCsi> csCsis;
    private List<OtherVersion> otherVersions;

    public DataElementDetails getDataElementDetails()
    {
        return dataElementDetails;
    }

    public void setDataElementDetails( DataElementDetails dataElementDetails )
    {
        this.dataElementDetails = dataElementDetails;
    }

    public List<ReferenceDocument> getReferenceDocuments()
    {
        return referenceDocuments;
    }

    public void setReferenceDocuments( List<ReferenceDocument> referenceDocuments )
    {
        this.referenceDocuments = referenceDocuments;
    }

    public List<AlternateName> getAlternateNames()
    {
        return alternateNames;
    }

    public void setAlternateNames( List<AlternateName> alternateNames )
    {
        this.alternateNames = alternateNames;
    }

    public List<AlternateDefinition> getAlternateDefinitions()
    {
        return alternateDefinitions;
    }

    public void setAlternateDefinitions( List<AlternateDefinition> alternateDefinitions )
    {
        this.alternateDefinitions = alternateDefinitions;
    }

    public List<OtherVersion> getOtherVersions()
    {
        return otherVersions;
    }

    public void setOtherVersions( List<OtherVersion> otherVersions )
    {
        this.otherVersions = otherVersions;
    }

    public List<CsCsi> getCsCsis()
    {
        return csCsis;
    }

    public void setCsCsis( List<CsCsi> csCsis )
    {
        this.csCsis = csCsis;
    }
}
