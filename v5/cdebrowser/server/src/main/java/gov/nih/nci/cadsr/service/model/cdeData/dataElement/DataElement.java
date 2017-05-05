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
    private CsCsiForCdeDetails csCsisCdeDetails;//CDEBROWSER-809 regrouping Alternate names and definitions

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

	public CsCsiForCdeDetails getCsCsisCdeDetails() {
		return csCsisCdeDetails;
	}

	public void setCsCsisCdeDetails(CsCsiForCdeDetails csCsisCdeDetails) {
		this.csCsisCdeDetails = csCsisCdeDetails;
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
