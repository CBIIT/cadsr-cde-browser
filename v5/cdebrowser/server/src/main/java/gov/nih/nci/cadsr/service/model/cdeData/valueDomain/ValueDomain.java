package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

import java.util.List;

public class ValueDomain
{
    private SelectedDataElement selectedDataElement = null;
    private ValueDomainDetails valueDomainDetails = null;
    private String valueDomainConcepts = "";
    private Representation representation = null;
    private List<RepresentationConcept> representationConcepts = null;
    private List<PermissibleValue> permissibleValues = null;
    private List<ReferenceDocument> referenceDocuments = null;

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

    public List<RepresentationConcept> getRepresentationConcepts()
    {
        return representationConcepts;
    }

    public void setRepresentationConcepts( List<RepresentationConcept> representationConcepts )
    {
        this.representationConcepts = representationConcepts;
    }

    public List<PermissibleValue> getPermissibleValues()
    {
        return permissibleValues;
    }

    public void setPermissibleValues( List<PermissibleValue> permissibleValues )
    {
        this.permissibleValues = permissibleValues;
    }

    public List<ReferenceDocument> getReferenceDocuments()
    {
        return referenceDocuments;
    }

    public void setReferenceDocuments( List<ReferenceDocument> referenceDocuments )
    {
        this.referenceDocuments = referenceDocuments;
    }
}
