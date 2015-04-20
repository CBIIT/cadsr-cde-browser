package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import java.util.List;


public class DataElement
{
    private DataElementDetails dataElementDetails = null;
    private List<ReferenceDocument> referenceDocuments = null;
    private List<AlternateName> alternateNames = null;
    private List<AlternateDefinition> alternateDefinitions = null;
    private List<OtherVersion> otherVersions = null;

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
}
