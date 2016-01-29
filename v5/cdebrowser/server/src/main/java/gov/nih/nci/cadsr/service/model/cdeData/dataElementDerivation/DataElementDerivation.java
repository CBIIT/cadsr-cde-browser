package gov.nih.nci.cadsr.service.model.cdeData.dataElementDerivation;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;
import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

import java.util.List;

/**
 * There is probably more to this than just a String, I am making inquiries to get a good example.
 */
public class DataElementDerivation
{
    private SelectedDataElement selectedDataElement = null;
    private DataElementDerivationModel dataElementDerivationDetails;
    private List<DataElementDerivationComponentModel> dataElementDerivationComponentModels;

    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }

    public DataElementDerivationModel getDataElementDerivationDetails()
    {
        return dataElementDerivationDetails;
    }

    public void setDataElementDerivationDetails( DataElementDerivationModel dataElementDerivationDetails )
    {
        this.dataElementDerivationDetails = dataElementDerivationDetails;
    }

    public List<DataElementDerivationComponentModel> getDataElementDerivationComponentModels()
    {
        return dataElementDerivationComponentModels;
    }

    public void setDataElementDerivationComponentModels( List<DataElementDerivationComponentModel> dataElementDerivationComponentModels )
    {
        this.dataElementDerivationComponentModels = dataElementDerivationComponentModels;
    }
}
