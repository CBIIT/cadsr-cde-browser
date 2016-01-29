package gov.nih.nci.cadsr.service.model.cdeData.classifications;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;

import java.util.List;

/**
 * Too many things named variations of "Classification" in this tab.
 * This is the parent class for this tab, it will have a Classifications section that will have list of Classification
 * <p/>
 * This tab will be forced to break the naming convention - be sure to tell Shaun
 */
public class Classifications
{
    private SelectedDataElement selectedDataElement;
    private List<CsCsi> classificationList; // This breaks our naming convention of using (s) not (List) to name our lists
    private List<ClassificationsSchemeReferenceDocument> classificationsSchemeReferenceDocuments;
    private List<ClassificationsSchemeItemReferenceDocument> classificationsSchemeItemReferenceDocuments;


    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }

    public List<CsCsi> getClassificationList()
    {
        return classificationList;
    }

    public void setClassificationList( List<CsCsi> classificationList )
    {
        this.classificationList = classificationList;
    }

    public List<ClassificationsSchemeReferenceDocument> getClassificationsSchemeReferenceDocuments()
    {
        return classificationsSchemeReferenceDocuments;
    }

    public void setClassificationsSchemeReferenceDocuments( List<ClassificationsSchemeReferenceDocument> classificationsSchemeReferenceDocuments )
    {
        this.classificationsSchemeReferenceDocuments = classificationsSchemeReferenceDocuments;
    }

    public List<ClassificationsSchemeItemReferenceDocument> getClassificationsSchemeItemReferenceDocuments()
    {
        return classificationsSchemeItemReferenceDocuments;
    }

    public void setClassificationsSchemeItemReferenceDocuments( List<ClassificationsSchemeItemReferenceDocument> classificationsSchemeItemReferenceDocuments )
    {
        this.classificationsSchemeItemReferenceDocuments = classificationsSchemeItemReferenceDocuments;
    }
}
