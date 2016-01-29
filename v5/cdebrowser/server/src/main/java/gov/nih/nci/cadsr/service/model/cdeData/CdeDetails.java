package gov.nih.nci.cadsr.service.model.cdeData;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.DataElementConcept;
import gov.nih.nci.cadsr.service.model.cdeData.adminInfo.AdminInfo;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.Classifications;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElement;
import gov.nih.nci.cadsr.service.model.cdeData.dataElementDerivation.DataElementDerivation;
import gov.nih.nci.cadsr.service.model.cdeData.usage.Usage;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.ValueDomain;


public class CdeDetails
{
    private DataElement dataElement = null;
    private DataElementConcept dataElementConcept = null;
    private ValueDomain valueDomain = null;
    private Classifications classifications = null;
    private Usage usage = null;
    private DataElementDerivation dataElementDerivation = null;
    private AdminInfo adminInfo = null;

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

    public Classifications getClassifications()
    {
        return classifications;
    }

    public void setClassifications( Classifications classifications )
    {
        this.classifications = classifications;
    }

    public Usage getUsage()
    {
        return usage;
    }

    public void setUsage( Usage usage )
    {
        this.usage = usage;
    }

    public DataElementDerivation getDataElementDerivation()
    {
        return dataElementDerivation;
    }

    public void setDataElementDerivation( DataElementDerivation dataElementDerivation )
    {
        this.dataElementDerivation = dataElementDerivation;
    }

    public AdminInfo getAdminInfo()
    {
        return adminInfo;
    }

    public void setAdminInfo( AdminInfo adminInfo )
    {
        this.adminInfo = adminInfo;
    }
}
