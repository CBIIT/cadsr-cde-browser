package gov.nih.nci.cadsr.service.model.cdeData;

import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElement;


public class CdeDetails
{
    private DataElement dataElement = null;


    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }
}
