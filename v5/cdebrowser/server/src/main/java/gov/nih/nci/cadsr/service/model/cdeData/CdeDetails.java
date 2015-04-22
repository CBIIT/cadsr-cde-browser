package gov.nih.nci.cadsr.service.model.cdeData;

import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.DataElementConcept;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElement;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.ValueDomain;


public class CdeDetails
{
    private DataElement dataElement = null;
    private DataElementConcept dataElementConcept = null;
    private ValueDomain valueDomain = null;

    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    public DataElementConcept getDataElementConcept()
    {
        return dataElementConcept;
    }

    public void setDataElementConcept( DataElementConcept dataElementConcept )
    {
        this.dataElementConcept = dataElementConcept;
    }

    public ValueDomain getValueDomain()
    {
        return valueDomain;
    }

    public void setValueDomain( ValueDomain valueDomain )
    {
        this.valueDomain = valueDomain;
    }
}
