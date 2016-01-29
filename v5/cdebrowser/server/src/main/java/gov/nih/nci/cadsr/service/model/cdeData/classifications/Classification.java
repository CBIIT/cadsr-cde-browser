package gov.nih.nci.cadsr.service.model.cdeData.classifications;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

/**
 * A list of these makes the Classifications section of the Classifications tab
 */
public class Classification
{
    private String csLongName;
    private String csDefinition;
    private String cdPublicIdVersion;
    private String csiName;
    private String csiType;
    private String csiPublicIdVersion;

    public String getCsLongName()
    {
        return csLongName;
    }

    public void setCsLongName( String csLongName )
    {
        this.csLongName = csLongName;
    }

    public String getCsDefinition()
    {
        return csDefinition;
    }

    public void setCsDefinition( String csDefinition )
    {
        this.csDefinition = csDefinition;
    }

    public String getCdPublicIdVersion()
    {
        return cdPublicIdVersion;
    }

    public void setCdPublicIdVersion( String cdPublicIdVersion )
    {
        this.cdPublicIdVersion = cdPublicIdVersion;
    }

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

    public String getCsiPublicIdVersion()
    {
        return csiPublicIdVersion;
    }

    public void setCsiPublicIdVersion( String csiPublicIdVersion )
    {
        this.csiPublicIdVersion = csiPublicIdVersion;
    }
}
