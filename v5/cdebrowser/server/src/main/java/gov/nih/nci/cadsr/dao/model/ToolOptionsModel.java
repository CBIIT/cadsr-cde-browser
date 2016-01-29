package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class ToolOptionsModel extends BaseModel
{
    private String toolIdseq;
    private String toolName;
    private String property;
    private String value;
    private String uaName;
    private String description;
    private String locale;

    public ToolOptionsModel()
    {
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale( String locale )
    {
        this.locale = locale;
    }

    public String getToolIdseq()
    {
        return toolIdseq;
    }

    public void setToolIdseq( String toolIdseq )
    {
        this.toolIdseq = toolIdseq;
    }

    public String getToolName()
    {
        return toolName;
    }

    public void setToolName( String toolName )
    {
        this.toolName = toolName;
    }

    public String getProperty()
    {
        return property;
    }

    public void setProperty( String property )
    {
        this.property = property;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public String getUaName()
    {
        return uaName;
    }

    public void setUaName( String uaName )
    {
        this.uaName = uaName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
}
