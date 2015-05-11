/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.model;

public class CsCsiModel extends BaseModel
{
    public static final String UNCLASSIFIED = "UNCLASSIFIED";
    private String csiName; // Goes in CSI* Name
    private String csiType; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.
    private String csitlName; // Goes in CSI* Type
    private String csiIdseq; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.
    private String csCsiIdseq; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.
    private String csiDescription;
    private String parentCsiIdseq;
    private String csIdseq;
    private String csPreffredDefinition; //Spelled wrong to match database. Goes in CS* Definition
    private String csLongName; // goes in CS* Long Name
    private String csPrefName; // this field is not in the view. CS_PREFFERED_NAME is.
    private String csConteIdseq;
    private String acCsiIdseq; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.
    private String cstlName;

    private String csID; //Unused  this field is not in the view.  since it's marked unused, should be removed from class.
    private Float csVersion; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.
    private Integer csiId; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.
    private Float csiVersion; // this field is not in the view.  Needs to be set with a rowmapper or deleted if not needed.

    public CsCsiModel() {
    }

    public CsCsiModel(String csiIdseq, String csPrefName, String csPreffredDefinition, String csiName, String csitlName ) {
        this.csiName = csiName;
        this.csitlName = csitlName;
        this.csPreffredDefinition = csPreffredDefinition;
        this.csPrefName = csPrefName;
        this.csiIdseq = csiIdseq;
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

    public String getCsitlName() {
        return csitlName;
    }

    public void setCsitlName(String csitlName) {
        this.csitlName = csitlName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CsCsiModel)) return false;

        CsCsiModel that = (CsCsiModel) o;

        if (getCsiName() != null ? !getCsiName().equals(that.getCsiName()) : that.getCsiName() != null) return false;
        if (getCsiType() != null ? !getCsiType().equals(that.getCsiType()) : that.getCsiType() != null) return false;
        if (getCsitlName() != null ? !getCsitlName().equals(that.getCsitlName()) : that.getCsitlName() != null)
            return false;
        if (getCsiIdseq() != null ? !getCsiIdseq().equals(that.getCsiIdseq()) : that.getCsiIdseq() != null)
            return false;
        if (getCsCsiIdseq() != null ? !getCsCsiIdseq().equals(that.getCsCsiIdseq()) : that.getCsCsiIdseq() != null)
            return false;
        if (getCsiDescription() != null ? !getCsiDescription().equals(that.getCsiDescription()) : that.getCsiDescription() != null)
            return false;
        if (getParentCsiIdseq() != null ? !getParentCsiIdseq().equals(that.getParentCsiIdseq()) : that.getParentCsiIdseq() != null)
            return false;
        if (getCsIdseq() != null ? !getCsIdseq().equals(that.getCsIdseq()) : that.getCsIdseq() != null) return false;
        if (getCsPreffredDefinition() != null ? !getCsPreffredDefinition().equals(that.getCsPreffredDefinition()) : that.getCsPreffredDefinition() != null)
            return false;
        if (getCsLongName() != null ? !getCsLongName().equals(that.getCsLongName()) : that.getCsLongName() != null)
            return false;
        if (getCsPrefName() != null ? !getCsPrefName().equals(that.getCsPrefName()) : that.getCsPrefName() != null)
            return false;
        if (getCsConteIdseq() != null ? !getCsConteIdseq().equals(that.getCsConteIdseq()) : that.getCsConteIdseq() != null)
            return false;
        if (getAcCsiIdseq() != null ? !getAcCsiIdseq().equals(that.getAcCsiIdseq()) : that.getAcCsiIdseq() != null)
            return false;
        if (getCstlName() != null ? !getCstlName().equals(that.getCstlName()) : that.getCstlName() != null)
            return false;
        if (getCsID() != null ? !getCsID().equals(that.getCsID()) : that.getCsID() != null) return false;
        if (getCsVersion() != null ? !getCsVersion().equals(that.getCsVersion()) : that.getCsVersion() != null)
            return false;
        if (getCsiId() != null ? !getCsiId().equals(that.getCsiId()) : that.getCsiId() != null) return false;
        return !(getCsiVersion() != null ? !getCsiVersion().equals(that.getCsiVersion()) : that.getCsiVersion() != null);

    }

    @Override
    public int hashCode() {
        int result = getCsiName() != null ? getCsiName().hashCode() : 0;
        result = 31 * result + (getCsiType() != null ? getCsiType().hashCode() : 0);
        result = 31 * result + (getCsitlName() != null ? getCsitlName().hashCode() : 0);
        result = 31 * result + (getCsiIdseq() != null ? getCsiIdseq().hashCode() : 0);
        result = 31 * result + (getCsCsiIdseq() != null ? getCsCsiIdseq().hashCode() : 0);
        result = 31 * result + (getCsiDescription() != null ? getCsiDescription().hashCode() : 0);
        result = 31 * result + (getParentCsiIdseq() != null ? getParentCsiIdseq().hashCode() : 0);
        result = 31 * result + (getCsIdseq() != null ? getCsIdseq().hashCode() : 0);
        result = 31 * result + (getCsPreffredDefinition() != null ? getCsPreffredDefinition().hashCode() : 0);
        result = 31 * result + (getCsLongName() != null ? getCsLongName().hashCode() : 0);
        result = 31 * result + (getCsPrefName() != null ? getCsPrefName().hashCode() : 0);
        result = 31 * result + (getCsConteIdseq() != null ? getCsConteIdseq().hashCode() : 0);
        result = 31 * result + (getAcCsiIdseq() != null ? getAcCsiIdseq().hashCode() : 0);
        result = 31 * result + (getCstlName() != null ? getCstlName().hashCode() : 0);
        result = 31 * result + (getCsID() != null ? getCsID().hashCode() : 0);
        result = 31 * result + (getCsVersion() != null ? getCsVersion().hashCode() : 0);
        result = 31 * result + (getCsiId() != null ? getCsiId().hashCode() : 0);
        result = 31 * result + (getCsiVersion() != null ? getCsiVersion().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CsCsiModel{" +
                "csiName='" + csiName + '\'' +
                ", csiType='" + csiType + '\'' +
                ", csitlName='" + csitlName + '\'' +
                ", csiIdseq='" + csiIdseq + '\'' +
                ", csCsiIdseq='" + csCsiIdseq + '\'' +
                ", csiDescription='" + csiDescription + '\'' +
                ", parentCsiIdseq='" + parentCsiIdseq + '\'' +
                ", csIdseq='" + csIdseq + '\'' +
                ", csPreffredDefinition='" + csPreffredDefinition + '\'' +
                ", csLongName='" + csLongName + '\'' +
                ", csPrefName='" + csPrefName + '\'' +
                ", csConteIdseq='" + csConteIdseq + '\'' +
                ", acCsiIdseq='" + acCsiIdseq + '\'' +
                ", cstlName='" + cstlName + '\'' +
                ", csID='" + csID + '\'' +
                ", csVersion=" + csVersion +
                ", csiId=" + csiId +
                ", csiVersion=" + csiVersion +
                '}';
    }
}
