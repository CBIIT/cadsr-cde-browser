/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.model;

public class CsCsiModel extends BaseModel
{

    private String csiName = null;
    private String csiType = null;
    private String csiIdseq = null;
    private String csCsiIdseq = null;
    private String csiDescription = null;
    private String parentCsiIdseq = null;
    private String csIdseq = null;
    private String csPreffredDefinition = null; //Spelled wrong to match database
    private String csLongName = null;
    private String csPrefName = null;
    private String csConteIdseq = null;
    private String acCsiIdseq = null;
    private String cstlName = null;

    private String csID = null; //Unused
    private Float csVersion = null;
    private Integer csiId = null;
    private Float csiVersion = null;


    public String getCsiName()
    {
        return csiName;
    }

    public void setCsiName( String csiName )
    {
        this.csiName = csiName;
    }

    public String getCsiType()
    {
        return csiType;
    }

    public void setCsiType( String csiType )
    {
        this.csiType = csiType;
    }

    public String getCsiIdseq()
    {
        return csiIdseq;
    }

    public void setCsiIdseq( String csiIdseq )
    {
        this.csiIdseq = csiIdseq;
    }

    public String getCsCsiIdseq()
    {
        return csCsiIdseq;
    }

    public void setCsCsiIdseq( String csCsiIdseq )
    {
        this.csCsiIdseq = csCsiIdseq;
    }

    public String getCsiDescription()
    {
        return csiDescription;
    }

    public void setCsiDescription( String csiDescription )
    {
        this.csiDescription = csiDescription;
    }

    public String getParentCsiIdseq()
    {
        return parentCsiIdseq;
    }

    public void setParentCsiIdseq( String parentCsiIdseq )
    {
        this.parentCsiIdseq = parentCsiIdseq;
    }

    public String getCsIdseq()
    {
        return csIdseq;
    }

    public void setCsIdseq( String csIdseq )
    {
        this.csIdseq = csIdseq;
    }

    public String getCsDefinition()
    {
        return csPreffredDefinition;
    }


    public String getCsLongName()
    {
        return csLongName;
    }

    public void setCsLongName( String csLongName )
    {
        this.csLongName = csLongName;
    }

    public String getCsPrefName()
    {
        return csPrefName;
    }

    public void setCsPrefName( String csPrefName )
    {
        this.csPrefName = csPrefName;
    }

    public String getCsConteIdseq()
    {
        return csConteIdseq;
    }

    public void setCsConteIdseq( String csConteIdseq )
    {
        this.csConteIdseq = csConteIdseq;
    }

    public String getAcCsiIdseq()
    {
        return acCsiIdseq;
    }

    public void setAcCsiIdseq( String acCsiIdseq )
    {
        this.acCsiIdseq = acCsiIdseq;
    }

    public String getCsType()
    {
        return cstlName;
    }

    public String getCsID()
    {
        return csID;
    }

    public void setCsID( String csID )
    {
        this.csID = csID;
    }

    public Float getCsVersion()
    {
        return csVersion;
    }

    public void setCsVersion( Float csVersion )
    {
        this.csVersion = csVersion;
    }

    public Integer getCsiId()
    {
        return csiId;
    }

    public void setCsiId( Integer csiId )
    {
        this.csiId = csiId;
    }

    public Float getCsiVersion()
    {
        return csiVersion;
    }

    public void setCsiVersion( Float csiVersion )
    {
        this.csiVersion = csiVersion;
    }

    public String getCsPreffredDefinition()
    {
        return csPreffredDefinition;
    }

    public void setCsPreffredDefinition( String csPreffredDefinition )
    {
        this.csPreffredDefinition = csPreffredDefinition;
    }

    public String getCstlName()
    {
        return cstlName;
    }

    public void setCstlName( String cstlName )
    {
        this.cstlName = cstlName;
    }

    public String toString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "csiName: " + csiName + "\n" );
        stringBuffer.append( "csiType: " + csiType + "\n" );
        stringBuffer.append( "csiIdseq: " + csiIdseq + "\n" );
        stringBuffer.append( "csCsiIdseq: " + csCsiIdseq + "\n" );
        stringBuffer.append( "csiDescription: " + csiDescription + "\n" );
        stringBuffer.append( "parentCsiIdseq: " + parentCsiIdseq + "\n" );
        stringBuffer.append( "csIdseq: " + csIdseq + "\n" );
        stringBuffer.append( "csPreffredDefinition: " + csPreffredDefinition + "\n" );
        stringBuffer.append( "csLongName: " + csLongName + "\n" );
        stringBuffer.append( "csPrefName: " + csPrefName + "\n" );
        stringBuffer.append( "csConteIdseq: " + csConteIdseq + "\n" );
        stringBuffer.append( "acCsiIdseq: " + acCsiIdseq + "\n" );
        stringBuffer.append( "cstlName: " + cstlName + "\n" );
        stringBuffer.append( "csID: " + csID + "\n" );
        stringBuffer.append( "csVersion: " + csVersion + "\n" );
        stringBuffer.append( "csiId: " + csiId + "\n" );
        stringBuffer.append( "csiVersion: " + csiVersion + "\n" );
        stringBuffer.append( super.toString() );
        return stringBuffer.toString();
    }

}
