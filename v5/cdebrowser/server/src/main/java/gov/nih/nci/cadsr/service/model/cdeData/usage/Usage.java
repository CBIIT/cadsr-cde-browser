package gov.nih.nci.cadsr.service.model.cdeData.usage;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

import java.util.List;

public class Usage
{
    private SelectedDataElement selectedDataElement = null;
    private List<FormUsage> formUsages = null;

    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }

    public List<FormUsage> getFormUsages()
    {
        return formUsages;
    }

    public void setFormUsages( List<FormUsage> formUsages )
    {
        this.formUsages = formUsages;
    }
}
