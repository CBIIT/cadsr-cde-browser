package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class AlternateDefinitionModel  extends BaseModel
{
    private String definIdseq;
    private String acIdseq;
    private String definition;
    private String conteIdseq;
    private String laeName;
    private String defl_name;

    public String getDefinIdseq()
    {
        return definIdseq;
    }

    public void setDefinIdseq( String definIdseq )
    {
        this.definIdseq = definIdseq;
    }

    public String getAcIdseq()
    {
        return acIdseq;
    }

    public void setAcIdseq( String acIdseq )
    {
        this.acIdseq = acIdseq;
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition( String definition )
    {
        this.definition = definition;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getLaeName()
    {
        return laeName;
    }

    public void setLaeName( String laeName )
    {
        this.laeName = laeName;
    }

    public String getDefl_name()
    {
        return defl_name;
    }

    public void setDefl_name( String defl_name )
    {
        this.defl_name = defl_name;
    }

    @Override
    public String toString()
    {
        return "AlternateDefinitionModel{" +
                "definIdseq='" + definIdseq + '\'' +
                ", acIdseq='" + acIdseq + '\'' +
                ", definition='" + definition + '\'' +
                ", conteIdseq='" + conteIdseq + '\'' +
                ", laeName='" + laeName + '\'' +
                ", defl_name='" + defl_name + '\'' +
                '}';
    }
}
