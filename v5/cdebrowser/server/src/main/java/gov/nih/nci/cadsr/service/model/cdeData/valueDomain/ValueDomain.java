package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

import java.util.List;

public class ValueDomain
{
    private SelectedDataElement selectedDataElement = null;
    private ValueDomainDetails valueDomainDetails = null;
    private List<ConceptModel> valueDomainConcepts = null;
    private Representation representation = null;
    private List<ConceptModel> representationConcepts = null;
    private List<PermissibleValuesModel> permissibleValues = null;
/*
    private List<ValueDomainReferenceDocument> referenceDocuments = null;
*/
    private List<PermissibleValueExt> permissibleValueExtList;

    private List<ReferenceDocModel> referenceDocuments = null;

    private List<ValueMeaningUiModel> valueMeaning = null;

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

    public List<ConceptModel> getValueDomainConcepts()
    {
        return valueDomainConcepts;
    }

    public void setValueDomainConcepts( List<ConceptModel> valueDomainConcepts )
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

    public List<ConceptModel> getRepresentationConcepts()
    {
        return representationConcepts;
    }

    public void setRepresentationConcepts( List<ConceptModel> representationConcepts )
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

	public List<PermissibleValueExt> getPermissibleValueExtList() {
		return permissibleValueExtList;
	}

	public void setPermissibleValueExtList(List<PermissibleValueExt> permissibleValueExtList) {
		this.permissibleValueExtList = permissibleValueExtList;
	}

    public List<ValueMeaningUiModel> getValueMeaning()
    {
        return valueMeaning;
    }

    public void setValueMeaning( List<ValueMeaningUiModel> valueMeaningModels )
    {
        this.valueMeaning = valueMeaningModels;
    }

	@Override
	public String toString() {
		return "ValueDomain [selectedDataElement=" + selectedDataElement + ", valueDomainDetails=" + valueDomainDetails
				+ ", valueDomainConcepts=" + valueDomainConcepts + ", representation=" + representation
				+ ", representationConcepts=" + representationConcepts + ", permissibleValues=" + permissibleValues
				+ ", permissibleValueExtList=" + permissibleValueExtList + ", referenceDocuments=" + referenceDocuments
				+ ", valueMeaning=" + valueMeaning + "]";
	}

}
