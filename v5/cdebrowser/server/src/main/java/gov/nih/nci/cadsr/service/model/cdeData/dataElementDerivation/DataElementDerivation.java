package gov.nih.nci.cadsr.service.model.cdeData.dataElementDerivation;

import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

/**
 * There is probably more to this tha just a String, I am making inquiries to get a good example.
 */
public class DataElementDerivation
{
    private SelectedDataElement selectedDataElement = null;
    private String dataElementDerivationDetails;

    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }

    public String getDataElementDerivationDetails()
    {
        return dataElementDerivationDetails;
    }

    public void setDataElementDerivationDetails( String dataElementDerivationDetails )
    {
        this.dataElementDerivationDetails = dataElementDerivationDetails;
    }
}
