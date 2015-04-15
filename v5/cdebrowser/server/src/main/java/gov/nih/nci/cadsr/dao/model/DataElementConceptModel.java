package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lavezzojl on 4/6/15.
 */
public class DataElementConceptModel extends BaseModel {
    private String preferredName;
    private String preferredDefinition;
    private String longName;
    private String aslName;
    private Float version;
    private String deletedInd;
    private String latestVerInd;
    private int publicId;
    private String origin;
    private String idseq;
    private String decIdseq;
    private String cdIdseq;
    private String proplName;
    private String oclName;
    private String objClassQualifier;
    private String propertyQualifier;
    private String changeNote;
    private String objClassPrefName;
    private String objClassContextName;
    private String propertyPrefName;
    private String propertyContextName;
    private Float propertyVersion;
    private Float objClassVersion;
    private String conteName;
    private String cdPrefName;
    private String cdContextName;
    private Float cdVersion;
    private int cdPublicId;
    private String objClassPublicId;
    private PropertyModel property;
    private ObjectClassModel objectClassModel;

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

    public String getObjClassPublicId()
    {
        return objClassPublicId;
    }

    public void setObjClassPublicId( String objClassPublicId )
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
}
