package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class DataElementConceptModel extends BaseModel
{
    private String preferredName;
    private String preferredDefinition;
    private String longName;
    private String aslName;
    private Float version;
    private String deletedInd;
    private String latestVerInd;//LATEST_VERSION_IND
    private int publicId;
    private String origin;//ORIGIN
    private String idseq;
    private String decIdseq;
    private String cdIdseq;
    private String proplName; //PROPL_NAME
    private String oclName; // Object Class
    private String objClassQualifier; // Object Class
    private String propertyQualifier; //PROPERTY_QUALIFIER
    private String changeNote; //CHANGE_NOTE
    private String objClassPrefName; // Object Class
    private String objClassContextName; // Object Class
    private String propertyPrefName;
    private String propertyContextName;
    private Float propertyVersion;
    private Float objClassVersion; // Object Class
    private String conteName;
    private String cdPrefName;
    private String cdContextName;
    private Float cdVersion;
    private int cdPublicId;
    private int objClassPublicId; // Object Class
    private PropertyModel property;
    private ObjectClassModel objectClassModel; // Object Class

    public DataElementConceptModel()
    {
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
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

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd( String deletedInd )
    {
        this.deletedInd = deletedInd;
    }

    public String getLatestVerInd()
    {
        return latestVerInd;
    }

    public void setLatestVerInd( String latestVerInd )
    {
        this.latestVerInd = latestVerInd;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getIdseq()
    {
        return idseq;
    }

    public void setIdseq( String idseq )
    {
        this.idseq = idseq;
    }

    public String getDecIdseq()
    {
        return decIdseq;
    }

    public void setDecIdseq( String decIdseq )
    {
        this.decIdseq = decIdseq;
    }

    public String getCdIdseq()
    {
        return cdIdseq;
    }

    public void setCdIdseq( String cdIdseq )
    {
        this.cdIdseq = cdIdseq;
    }

    public String getProplName()
    {
        return proplName;
    }

    public void setProplName( String proplName )
    {
        this.proplName = proplName;
    }

    public String getOclName()
    {
        return oclName;
    }

    public void setOclName( String oclName )
    {
        this.oclName = oclName;
    }

    public String getObjClassQualifier()
    {
        return objClassQualifier;
    }

    public void setObjClassQualifier( String objClassQualifier )
    {
        this.objClassQualifier = objClassQualifier;
    }

    public String getPropertyQualifier()
    {
        return propertyQualifier;
    }

    public void setPropertyQualifier( String propertyQualifier )
    {
        this.propertyQualifier = propertyQualifier;
    }

    public String getChangeNote()
    {
        return changeNote;
    }

    public void setChangeNote( String changeNote )
    {
        this.changeNote = changeNote;
    }

    public String getObjClassPrefName()
    {
        return objClassPrefName;
    }

    public void setObjClassPrefName( String objClassPrefName )
    {
        this.objClassPrefName = objClassPrefName;
    }

    public String getObjClassContextName()
    {
        return objClassContextName;
    }

    public void setObjClassContextName( String objClassContextName )
    {
        this.objClassContextName = objClassContextName;
    }

    public String getPropertyPrefName()
    {
        return propertyPrefName;
    }

    public void setPropertyPrefName( String propertyPrefName )
    {
        this.propertyPrefName = propertyPrefName;
    }

    public String getPropertyContextName()
    {
        return propertyContextName;
    }

    public void setPropertyContextName( String propertyContextName )
    {
        this.propertyContextName = propertyContextName;
    }

    public Float getPropertyVersion()
    {
        return propertyVersion;
    }

    public void setPropertyVersion( Float propertyVersion )
    {
        this.propertyVersion = propertyVersion;
    }

    public Float getObjClassVersion()
    {
        return objClassVersion;
    }

    public void setObjClassVersion( Float objClassVersion )
    {
        this.objClassVersion = objClassVersion;
    }

    public String getConteName()
    {
        return conteName;
    }

    public void setConteName( String conteName )
    {
        this.conteName = conteName;
    }

    public String getCdPrefName()
    {
        return cdPrefName;
    }

    public void setCdPrefName( String cdPrefName )
    {
        this.cdPrefName = cdPrefName;
    }

    public String getCdContextName()
    {
        return cdContextName;
    }

    public void setCdContextName( String cdContextName )
    {
        this.cdContextName = cdContextName;
    }

    public Float getCdVersion()
    {
        return cdVersion;
    }

    public void setCdVersion( Float cdVersion )
    {
        this.cdVersion = cdVersion;
    }

    public int getCdPublicId()
    {
        return cdPublicId;
    }

    public void setCdPublicId( int cdPublicId )
    {
        this.cdPublicId = cdPublicId;
    }

    public int getObjClassPublicId()
    {
        return objClassPublicId;
    }

    public void setObjClassPublicId( int objClassPublicId )
    {
        this.objClassPublicId = objClassPublicId;
    }

    public PropertyModel getProperty()
    {
        return property;
    }

    public void setProperty( PropertyModel property )
    {
        this.property = property;
    }

    public ObjectClassModel getObjectClassModel()
    {
        return objectClassModel;
    }

    public void setObjectClassModel( ObjectClassModel objectClassModel )
    {
        this.objectClassModel = objectClassModel;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof DataElementConceptModel ) ) return false;

        DataElementConceptModel that = ( DataElementConceptModel ) o;

        if( getPublicId() != that.getPublicId() ) return false;
        if( getCdPublicId() != that.getCdPublicId() ) return false;
        if( getObjClassPublicId() != that.getObjClassPublicId() ) return false;
        if( getPreferredName() != null ? !getPreferredName().equals( that.getPreferredName() ) : that.getPreferredName() != null )
            return false;
        if( getPreferredDefinition() != null ? !getPreferredDefinition().equals( that.getPreferredDefinition() ) : that.getPreferredDefinition() != null )
            return false;
        if( getLongName() != null ? !getLongName().equals( that.getLongName() ) : that.getLongName() != null )
            return false;
        if( getAslName() != null ? !getAslName().equals( that.getAslName() ) : that.getAslName() != null ) return false;
        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getDeletedInd() != null ? !getDeletedInd().equals( that.getDeletedInd() ) : that.getDeletedInd() != null )
            return false;
        if( getLatestVerInd() != null ? !getLatestVerInd().equals( that.getLatestVerInd() ) : that.getLatestVerInd() != null )
            return false;
        if( getOrigin() != null ? !getOrigin().equals( that.getOrigin() ) : that.getOrigin() != null ) return false;
        if( getIdseq() != null ? !getIdseq().equals( that.getIdseq() ) : that.getIdseq() != null ) return false;
        if( getDecIdseq() != null ? !getDecIdseq().equals( that.getDecIdseq() ) : that.getDecIdseq() != null )
            return false;
        if( getCdIdseq() != null ? !getCdIdseq().equals( that.getCdIdseq() ) : that.getCdIdseq() != null ) return false;
        if( getProplName() != null ? !getProplName().equals( that.getProplName() ) : that.getProplName() != null )
            return false;
        if( getOclName() != null ? !getOclName().equals( that.getOclName() ) : that.getOclName() != null ) return false;
        if( getObjClassQualifier() != null ? !getObjClassQualifier().equals( that.getObjClassQualifier() ) : that.getObjClassQualifier() != null )
            return false;
        if( getPropertyQualifier() != null ? !getPropertyQualifier().equals( that.getPropertyQualifier() ) : that.getPropertyQualifier() != null )
            return false;
        if( getChangeNote() != null ? !getChangeNote().equals( that.getChangeNote() ) : that.getChangeNote() != null )
            return false;
        if( getObjClassPrefName() != null ? !getObjClassPrefName().equals( that.getObjClassPrefName() ) : that.getObjClassPrefName() != null )
            return false;
        if( getObjClassContextName() != null ? !getObjClassContextName().equals( that.getObjClassContextName() ) : that.getObjClassContextName() != null )
            return false;
        if( getPropertyPrefName() != null ? !getPropertyPrefName().equals( that.getPropertyPrefName() ) : that.getPropertyPrefName() != null )
            return false;
        if( getPropertyContextName() != null ? !getPropertyContextName().equals( that.getPropertyContextName() ) : that.getPropertyContextName() != null )
            return false;
        if( getPropertyVersion() != null ? !getPropertyVersion().equals( that.getPropertyVersion() ) : that.getPropertyVersion() != null )
            return false;
        if( getObjClassVersion() != null ? !getObjClassVersion().equals( that.getObjClassVersion() ) : that.getObjClassVersion() != null )
            return false;
        if( getConteName() != null ? !getConteName().equals( that.getConteName() ) : that.getConteName() != null )
            return false;
        if( getCdPrefName() != null ? !getCdPrefName().equals( that.getCdPrefName() ) : that.getCdPrefName() != null )
            return false;
        if( getCdContextName() != null ? !getCdContextName().equals( that.getCdContextName() ) : that.getCdContextName() != null )
            return false;
        if( getCdVersion() != null ? !getCdVersion().equals( that.getCdVersion() ) : that.getCdVersion() != null )
            return false;
        if( getProperty() != null ? !getProperty().equals( that.getProperty() ) : that.getProperty() != null )
            return false;
        return !( getObjectClassModel() != null ? !getObjectClassModel().equals( that.getObjectClassModel() ) : that.getObjectClassModel() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getPreferredName() != null ? getPreferredName().hashCode() : 0;
        result = 31 * result + ( getPreferredDefinition() != null ? getPreferredDefinition().hashCode() : 0 );
        result = 31 * result + ( getLongName() != null ? getLongName().hashCode() : 0 );
        result = 31 * result + ( getAslName() != null ? getAslName().hashCode() : 0 );
        result = 31 * result + ( getVersion() != null ? getVersion().hashCode() : 0 );
        result = 31 * result + ( getDeletedInd() != null ? getDeletedInd().hashCode() : 0 );
        result = 31 * result + ( getLatestVerInd() != null ? getLatestVerInd().hashCode() : 0 );
        result = 31 * result + getPublicId();
        result = 31 * result + ( getOrigin() != null ? getOrigin().hashCode() : 0 );
        result = 31 * result + ( getIdseq() != null ? getIdseq().hashCode() : 0 );
        result = 31 * result + ( getDecIdseq() != null ? getDecIdseq().hashCode() : 0 );
        result = 31 * result + ( getCdIdseq() != null ? getCdIdseq().hashCode() : 0 );
        result = 31 * result + ( getProplName() != null ? getProplName().hashCode() : 0 );
        result = 31 * result + ( getOclName() != null ? getOclName().hashCode() : 0 );
        result = 31 * result + ( getObjClassQualifier() != null ? getObjClassQualifier().hashCode() : 0 );
        result = 31 * result + ( getPropertyQualifier() != null ? getPropertyQualifier().hashCode() : 0 );
        result = 31 * result + ( getChangeNote() != null ? getChangeNote().hashCode() : 0 );
        result = 31 * result + ( getObjClassPrefName() != null ? getObjClassPrefName().hashCode() : 0 );
        result = 31 * result + ( getObjClassContextName() != null ? getObjClassContextName().hashCode() : 0 );
        result = 31 * result + ( getPropertyPrefName() != null ? getPropertyPrefName().hashCode() : 0 );
        result = 31 * result + ( getPropertyContextName() != null ? getPropertyContextName().hashCode() : 0 );
        result = 31 * result + ( getPropertyVersion() != null ? getPropertyVersion().hashCode() : 0 );
        result = 31 * result + ( getObjClassVersion() != null ? getObjClassVersion().hashCode() : 0 );
        result = 31 * result + ( getConteName() != null ? getConteName().hashCode() : 0 );
        result = 31 * result + ( getCdPrefName() != null ? getCdPrefName().hashCode() : 0 );
        result = 31 * result + ( getCdContextName() != null ? getCdContextName().hashCode() : 0 );
        result = 31 * result + ( getCdVersion() != null ? getCdVersion().hashCode() : 0 );
        result = 31 * result + getCdPublicId();
        result = 31 * result + getObjClassPublicId();
        result = 31 * result + ( getProperty() != null ? getProperty().hashCode() : 0 );
        result = 31 * result + ( getObjectClassModel() != null ? getObjectClassModel().hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "DataElementConceptModel{" +
                "preferredName='" + preferredName + '\'' +
                ", preferredDefinition='" + preferredDefinition + '\'' +
                ", longName='" + longName + '\'' +
                ", aslName='" + aslName + '\'' +
                ", version=" + version +
                ", deletedInd='" + deletedInd + '\'' +
                ", latestVerInd='" + latestVerInd + '\'' +
                ", publicId=" + publicId +
                ", origin='" + origin + '\'' +
                ", idseq='" + idseq + '\'' +
                ", decIdseq='" + decIdseq + '\'' +
                ", cdIdseq='" + cdIdseq + '\'' +
                ", proplName='" + proplName + '\'' +
                ", oclName='" + oclName + '\'' +
                ", objClassQualifier='" + objClassQualifier + '\'' +
                ", propertyQualifier='" + propertyQualifier + '\'' +
                ", changeNote='" + changeNote + '\'' +
                ", objClassPrefName='" + objClassPrefName + '\'' +
                ", objClassContextName='" + objClassContextName + '\'' +
                ", propertyPrefName='" + propertyPrefName + '\'' +
                ", propertyContextName='" + propertyContextName + '\'' +
                ", propertyVersion=" + propertyVersion +
                ", objClassVersion=" + objClassVersion +
                ", conteName='" + conteName + '\'' +
                ", cdPrefName='" + cdPrefName + '\'' +
                ", cdContextName='" + cdContextName + '\'' +
                ", cdVersion=" + cdVersion +
                ", cdPublicId=" + cdPublicId +
                ", objClassPublicId=" + objClassPublicId +
                ", property=" + property +
                ", objectClassModel=" + objectClassModel +
                '}';
    }
}
