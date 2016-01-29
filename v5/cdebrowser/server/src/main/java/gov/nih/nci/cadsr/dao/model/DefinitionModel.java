package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;
import java.util.Set;


public class DefinitionModel extends BaseModel
{

    private String definIdseq;
    private String acIdseq;
    private String definition;
    private ContextModel context;
    private String laeName;
    private String deflName;
    private Set<String> csiIdseqs;

    public DefinitionModel()
    {
    }

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

    public ContextModel getContext()
    {
        return context;
    }

    public void setContext( ContextModel context )
    {
        this.context = context;
    }

    public String getLaeName()
    {
        return laeName;
    }

    public void setLaeName( String laeName )
    {
        this.laeName = laeName;
    }

    public String getDeflName()
    {
        return deflName;
    }

    public void setDeflName( String deflName )
    {
        this.deflName = deflName;
    }

    public Set<String> getCsiIdseqs()
    {
        return csiIdseqs;
    }

    public void setCsiIdseqs( Set<String> csiIdseqs )
    {
        this.csiIdseqs = csiIdseqs;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof DefinitionModel ) ) return false;

        DefinitionModel that = ( DefinitionModel ) o;

        if( getDefinIdseq() != null ? !getDefinIdseq().equals( that.getDefinIdseq() ) : that.getDefinIdseq() != null )
            return false;
        if( getAcIdseq() != null ? !getAcIdseq().equals( that.getAcIdseq() ) : that.getAcIdseq() != null ) return false;
        if( getDefinition() != null ? !getDefinition().equals( that.getDefinition() ) : that.getDefinition() != null )
            return false;
        if( getContext() != null ? !getContext().equals( that.getContext() ) : that.getContext() != null ) return false;
        if( getLaeName() != null ? !getLaeName().equals( that.getLaeName() ) : that.getLaeName() != null ) return false;
        if( getDeflName() != null ? !getDeflName().equals( that.getDeflName() ) : that.getDeflName() != null )
            return false;
        return !( getCsiIdseqs() != null ? !getCsiIdseqs().equals( that.getCsiIdseqs() ) : that.getCsiIdseqs() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getDefinIdseq() != null ? getDefinIdseq().hashCode() : 0;
        result = 31 * result + ( getAcIdseq() != null ? getAcIdseq().hashCode() : 0 );
        result = 31 * result + ( getDefinition() != null ? getDefinition().hashCode() : 0 );
        result = 31 * result + ( getContext() != null ? getContext().hashCode() : 0 );
        result = 31 * result + ( getLaeName() != null ? getLaeName().hashCode() : 0 );
        result = 31 * result + ( getDeflName() != null ? getDeflName().hashCode() : 0 );
        result = 31 * result + ( getCsiIdseqs() != null ? getCsiIdseqs().hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "DefinitionModel{" +
                "definIdseq='" + definIdseq + '\'' +
                ", acIdseq='" + acIdseq + '\'' +
                ", definition='" + definition + '\'' +
                ", context=" + context +
                ", laeName='" + laeName + '\'' +
                ", deflName='" + deflName + '\'' +
                ", csiIdseqs=" + csiIdseqs +
                '}';
    }
}
