package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;
import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.model.RepresentationConceptModel;
import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

import java.util.List;

public class ValueDomain
{
    private SelectedDataElement selectedDataElement = null;
    private ValueDomainDetails valueDomainDetails = null;
    private String valueDomainConcepts = "";
    private Representation representation = null;
    private List<RepresentationConceptModel> representationConcepts = null;
    private List<PermissibleValuesModel> permissibleValues = null;
/*
    private List<ValueDomainReferenceDocument> referenceDocuments = null;
*/
    private List<ReferenceDocModel> referenceDocuments = null;

    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }

    public ValueDomainDetails getValueDomainDetails()
    {
        return valueDomainDetails;
    }

    public void setValueDomainDetails( ValueDomainDetails valueDomainDetails )
    {
        this.valueDomainDetails = valueDomainDetails;
    }

    public String getValueDomainConcepts()
    {
        return valueDomainConcepts;
    }

    public void setValueDomainConcepts( String valueDomainConcepts )
    {
        this.valueDomainConcepts = valueDomainConcepts;
    }

    public Representation getRepresentation()
    {
        return representation;
    }

    public void setRepresentation( Representation representation )
    {
        this.representation = representation;
    }

    public List<RepresentationConceptModel> getRepresentationConcepts()
    {
        return representationConcepts;
    }

    public void setRepresentationConcepts( List<RepresentationConceptModel> representationConcepts )
    {
        this.representationConcepts = representationConcepts;
    }

    public List<PermissibleValuesModel> getPermissibleValues()
    {
        return permissibleValues;
    }

    public void setPermissibleValues( List<PermissibleValuesModel> permissibleValues )
    {
        this.permissibleValues = permissibleValues;
    }

    public List<ReferenceDocModel> getValueDomainReferenceDocuments()
    {
        return referenceDocuments;
    }

    public void setValueDomainReferenceDocuments( List<ReferenceDocModel> referenceDocuments )
    {
        this.referenceDocuments = referenceDocuments;
    }
/*

    public List<ValueDomainReferenceDocument> getValueDomainReferenceDocuments()
    {
        return referenceDocuments;
    }

    public void setValueDomainReferenceDocuments( List<ValueDomainReferenceDocument> referenceDocuments )
    {
        this.referenceDocuments = referenceDocuments;
    }
*/
}
