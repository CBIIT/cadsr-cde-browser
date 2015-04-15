package gov.nih.nci.cadsr.dao.model;


/**
 * Created by lavezzojl on 4/6/15.
 */
public class ValueDomainModel extends BaseModel {
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
    private String vdIdseq;
    private String datatype;
    private String uom;
    private String dispFormat;
    private String maxLength;
    private String minLength;
    private String highVal;
    private String lowVal;
    private String charSet;
    private String decimalPlace;
    private String cdPrefName;
    private String cdContextName;
    private Float cdVersion;
    private int cdPublicId;
    private String vdType;
    private RepresentationModel representationModel;
    private ConceptDerivationRuleModel conceptDerivationRuleModel;

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

    public String getVdIdseq()
    {
        return vdIdseq;
    }

    public void setVdIdseq( String vdIdseq )
    {
        this.vdIdseq = vdIdseq;
    }

    public String getDatatype()
    {
        return datatype;
    }

    public void setDatatype( String datatype )
    {
        this.datatype = datatype;
    }

    public String getUom()
    {
        return uom;
    }

    public void setUom( String uom )
    {
        this.uom = uom;
    }

    public String getDispFormat()
    {
        return dispFormat;
    }

    public void setDispFormat( String dispFormat )
    {
        this.dispFormat = dispFormat;
    }

    public String getMaxLength()
    {
        return maxLength;
    }

    public void setMaxLength( String maxLength )
    {
        this.maxLength = maxLength;
    }

    public String getMinLength()
    {
        return minLength;
    }

    public void setMinLength( String minLength )
    {
        this.minLength = minLength;
    }

    public String getHighVal()
    {
        return highVal;
    }

    public void setHighVal( String highVal )
    {
        this.highVal = highVal;
    }

    public String getLowVal()
    {
        return lowVal;
    }

    public void setLowVal( String lowVal )
    {
        this.lowVal = lowVal;
    }

    public String getCharSet()
    {
        return charSet;
    }

    public void setCharSet( String charSet )
    {
        this.charSet = charSet;
    }

    public String getDecimalPlace()
    {
        return decimalPlace;
    }

    public void setDecimalPlace( String decimalPlace )
    {
        this.decimalPlace = decimalPlace;
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

    public String getVdType()
    {
        return vdType;
    }

    public void setVdType( String vdType )
    {
        this.vdType = vdType;
    }

    public RepresentationModel getRepresentationModel()
    {
        return representationModel;
    }

    public void setRepresentationModel( RepresentationModel representationModel )
    {
        this.representationModel = representationModel;
    }

    public ConceptDerivationRuleModel getConceptDerivationRuleModel()
    {
        return conceptDerivationRuleModel;
    }

    public void setConceptDerivationRuleModel( ConceptDerivationRuleModel conceptDerivationRuleModel )
    {
        this.conceptDerivationRuleModel = conceptDerivationRuleModel;
    }
}
