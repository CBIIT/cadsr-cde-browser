package gov.nih.nci.cadsr.service.model.cdeData.classifications;

import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;

import java.util.List;

/**
 * Too many things named variations of "Classification" in this tab.
 * This is the parent class for this tab, it will have a Classifications section that will have list of Classification
 *
 * This tab will be forced to break the naming convention - be sure to tell Shaun
 */
public class Classifications
{
    private SelectedDataElement selectedDataElement = null;
    private List<CsCsi> classificationList = null; // This breaks our naming convention of using (s) not (List) to name our lists
    private List<ClassificationsScheneRefernceDocument> classificationsScheneRefernceDocuments = null;
    private ClassificationsSchemeItemReferenceDocument classificationsSchemeItemReferenceDocument = null;


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

    public List<ClassificationsScheneRefernceDocument> getClassificationsScheneRefernceDocuments()
    {
        return classificationsScheneRefernceDocuments;
    }

    public void setClassificationsScheneRefernceDocuments( List<ClassificationsScheneRefernceDocument> classificationsScheneRefernceDocuments )
    {
        this.classificationsScheneRefernceDocuments = classificationsScheneRefernceDocuments;
    }

    public ClassificationsSchemeItemReferenceDocument getClassificationsSchemeItemReferenceDocument()
    {
        return classificationsSchemeItemReferenceDocument;
    }

    public void setClassificationsSchemeItemReferenceDocument( ClassificationsSchemeItemReferenceDocument classificationsSchemeItemReferenceDocument )
    {
        this.classificationsSchemeItemReferenceDocument = classificationsSchemeItemReferenceDocument;
    }
}
