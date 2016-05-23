package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.CaDSRConstants;

public class SearchModel extends BaseModel
{
    String deIdseq;
    String dePreferredName;
    String longName;
    String docText;
    String name;
    String aslName;
    String deCdeid;
    String deVersion;
    String deUsedby;
    String vdIdseq;
    String decIdseq;
    String conteIdseq;
    String preferredDefinition;
    String registrationStatus;
    String displayOrder;
    String workflowOrder;
    String cdeid;


    public String getDeIdseq()
    {
        return deIdseq;
    }

    public void setDeIdseq( String deIdseq )
    {
        this.deIdseq = deIdseq;
    }

    public String getDePreferredName()
    {
        return dePreferredName;
    }

    public void setDePreferredName( String dePreferredName )
    {
        this.dePreferredName = dePreferredName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getDocText()
    {
        return docText;
    }

    public void setDocText( String docText )
    {
        this.docText = docText;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public String getDeCdeid()
    {
        return deCdeid;
    }

    public void setDeCdeid( String deCdeid )
    {
        this.deCdeid = deCdeid;
    }

    public String getDeVersion()
    {
        return deVersion;
    }

    public void setDeVersion( String deVersion )
    {
        //this will give us at least one digit to the right of the decimal place
        this.deVersion = Float.toString( Float.valueOf( deVersion ) );
    }

    public String getDeUsedby()
    {
        return deUsedby;
    }

    public void setDeUsedby( String deUsedby )
    {
        this.deUsedby = deUsedby;
    }

    public String getVdIdseq()
    {
        return vdIdseq;
    }

    public void setVdIdseq( String vdIdseq )
    {
        this.vdIdseq = vdIdseq;
    }

    public String getDecIdseq()
    {
        return decIdseq;
    }

    public void setDecIdseq( String decIdseq )
    {
        this.decIdseq = decIdseq;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public String getDisplayOrder()
    {
        return displayOrder;
    }

    public void setDisplayOrder( String displayOrder )
    {
        this.displayOrder = displayOrder;
    }

    public String getWorkflowOrder()
    {
        return workflowOrder;
    }

    public void setWorkflowOrder( String workflowOrder )
    {
        this.workflowOrder = workflowOrder;
    }

    public String getCdeid()
    {
        return cdeid;
    }

    public void setCdeid( String cdeid )
    {
        this.cdeid = cdeid;
    }

    public String toString()
    {

        StringBuffer sb = new StringBuffer();
        sb.append( CaDSRConstants.OBJ_SEPARATOR_START );
        sb.append( "deIdseq=" + getDeIdseq() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "dePreferredName=" + getDePreferredName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "longName=" + getLongName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "docText=" + getDocText() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "name=" + getName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "aslName=" + getAslName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "deCdeid=" + getCdeid() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "deVersion=" + getDeVersion() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "deUsedby=" + getDeUsedby() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "vdIdseq=" + getVdIdseq() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "decIdseq=" + getDecIdseq() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "conteIdseq=" + getConteIdseq() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "preferredDefinition=" + getPreferredDefinition() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "registrationStatus=" + getRegistrationStatus() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "displayOrder=" + getDisplayOrder() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "workflowOrder=" + getDisplayOrder() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "cdeid=" + getCdeid() );

        sb.append( CaDSRConstants.OBJ_SEPARATOR_END );
        sb.toString();


        return sb.toString();
    }
}
