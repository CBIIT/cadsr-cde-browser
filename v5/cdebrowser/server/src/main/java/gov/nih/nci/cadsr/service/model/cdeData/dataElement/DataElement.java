package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;


public class DataElement
{
    private DataElementDetails dataElementDetails;
    //CDEBROWSER-809 Separate out the Documents with Document Type containing "*Question Text"
    private List<ReferenceDocument> questionTextReferenceDocuments;
    private List<ReferenceDocument> otherReferenceDocuments;//other than Document Type containing "*Question Text"
    
    private List<ReferenceDocument> referenceDocuments;
    private List<CsCsi> csCsis;
    private List<AlternateNameCsCsi> alternateNames;//CDEBROWSER-809
    private List<AlternateDefinitionCsCsi> alternateDefinitions;//CDEBROWSER-809
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

    public List<AlternateNameCsCsi> getAlternateNames() {
		return alternateNames;
	}

	public void setAlternateNames(List<AlternateNameCsCsi> alternateNames) {
		this.alternateNames = alternateNames;
	}

    public List<AlternateDefinitionCsCsi> getAlternateDefinitions() {
		return alternateDefinitions;
	}

	public void setAlternateDefinitions(List<AlternateDefinitionCsCsi> alternateDefinitions) {
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

	public List<ReferenceDocument> getQuestionTextReferenceDocuments() {
		return questionTextReferenceDocuments;
	}

	public void setQuestionTextReferenceDocuments(List<ReferenceDocument> questionTextReferenceDocuments) {
		this.questionTextReferenceDocuments = questionTextReferenceDocuments;
	}

	public List<ReferenceDocument> getOtherReferenceDocuments() {
		return otherReferenceDocuments;
	}

	public void setOtherReferenceDocuments(List<ReferenceDocument> otherReferenceDocuments) {
		this.otherReferenceDocuments = otherReferenceDocuments;
	}
    
}
