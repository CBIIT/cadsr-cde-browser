package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class AlternateNameModel extends BaseModel
{
    private String desigIdseq;
    private String acIdseq;
    private String conteIdseq;
    private String name;
    private String detlName;
    private String laeName;

    public String getDesigIdseq()
    {
        return desigIdseq;
    }

    public void setDesigIdseq( String desigIdseq )
    {
        this.desigIdseq = desigIdseq;
    }

    public String getAcIdseq()
    {
        return acIdseq;
    }

    public void setAcIdseq( String acIdseq )
    {
        this.acIdseq = acIdseq;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getDetlName()
    {
        return detlName;
    }

    public void setDetlName( String detlName )
    {
        this.detlName = detlName;
    }

    public String getLaeName()
    {
        return laeName;
    }

    public void setLaeName( String laeName )
    {
        this.laeName = laeName;
    }

    @Override
    public String toString()
    {
        return "AlternateNameModel{" +
                "desigIdseq='" + desigIdseq + '\'' +
                ", acIdseq='" + acIdseq + '\'' +
                ", conteIdseq='" + conteIdseq + '\'' +
                ", name='" + name + '\'' +
                ", detlName='" + detlName + '\'' +
                ", laeName='" + laeName + '\'' +
                '}';
    }
}
