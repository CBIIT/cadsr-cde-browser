package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class RepresentationModel extends BaseModel
{
    private String preferredName;
    private String longName;
    private Float version;
    private ContextModel context;
    private int publicId;
    private String idseq;
    private ConceptDerivationRuleModel conceptDerivationRuleModel;

    public String getPreferredName()
    {
        return preferredName;
    }

    public RepresentationModel()
    {
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
        setFormattedVersion( version );
    }

    public ContextModel getContext()
    {
        return context;
    }

    public void setContext( ContextModel context )
    {
        this.context = context;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public String getIdseq()
    {
        return idseq;
    }

    public void setIdseq( String idseq )
    {
        this.idseq = idseq;
    }

    public ConceptDerivationRuleModel getConceptDerivationRuleModel()
    {
        return conceptDerivationRuleModel;
    }

    public void setConceptDerivationRuleModel( ConceptDerivationRuleModel conceptDerivationRuleModel )
    {
        this.conceptDerivationRuleModel = conceptDerivationRuleModel;
    }

    @Override
    public String toString()
    {
        return "RepresentationModel{" +
                "preferredName='" + preferredName + '\'' +
                ", longName='" + longName + '\'' +
                ", version=" + version +
                ", context=" + context +
                ", publicId=" + publicId +
                ", idseq='" + idseq + '\'' +
                ", conceptDerivationRuleModel=" + conceptDerivationRuleModel +
                '}';
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof RepresentationModel ) ) return false;

        RepresentationModel that = ( RepresentationModel ) o;

        if( getPublicId() != that.getPublicId() ) return false;
        if( getPreferredName() != null ? !getPreferredName().equals( that.getPreferredName() ) : that.getPreferredName() != null )
            return false;
        if( getLongName() != null ? !getLongName().equals( that.getLongName() ) : that.getLongName() != null )
            return false;
        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getContext() != null ? !getContext().equals( that.getContext() ) : that.getContext() != null ) return false;
        if( getIdseq() != null ? !getIdseq().equals( that.getIdseq() ) : that.getIdseq() != null ) return false;
        return !( getConceptDerivationRuleModel() != null ? !getConceptDerivationRuleModel().equals( that.getConceptDerivationRuleModel() ) : that.getConceptDerivationRuleModel() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getPreferredName() != null ? getPreferredName().hashCode() : 0;
        result = 31 * result + ( getLongName() != null ? getLongName().hashCode() : 0 );
        result = 31 * result + ( getVersion() != null ? getVersion().hashCode() : 0 );
        result = 31 * result + ( getContext() != null ? getContext().hashCode() : 0 );
        result = 31 * result + getPublicId();
        result = 31 * result + ( getIdseq() != null ? getIdseq().hashCode() : 0 );
        result = 31 * result + ( getConceptDerivationRuleModel() != null ? getConceptDerivationRuleModel().hashCode() : 0 );
        return result;
    }
}
