package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import java.util.List;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;

/**
 * Created by lavezzojl on 5/8/15.
 */
public class CsCsi {
    private String csLongName;
    private String csDefinition;
    private String csiName;
    private String csiType;
    private String csId;
    private Float csVersion;
    private Integer csiId;
    private Float csiVersion;
    private Boolean hide;

    private List<AlternateName> alternateNames;
    private List<AlternateDefinition> alternateDefinitions;

    public CsCsi() {
    }

    public CsCsi(CsCsiModel csCsiModel) {
        csLongName = csCsiModel.getCsLongName();
        csDefinition = csCsiModel.getCsPreffredDefinition();
        csId = csCsiModel.getCsId();
        csVersion = csCsiModel.getCsVersion();
        csiName = csCsiModel.getCsiName();
        csiType = csCsiModel.getCsitlName();
        csiId = csCsiModel.getCsiId();
        csiVersion = csCsiModel.getCsiVersion();
        if (csiName == CsCsiModel.UNCLASSIFIED) {
            hide = true;
        }
    }

    public String getCsLongName() {
        return csLongName;
    }

    public void setCsLongName(String csLongName) {
        this.csLongName = csLongName;
    }

    public String getCsDefinition() {
        return csDefinition;
    }

    public void setCsDefinition(String csDefinition) {
        this.csDefinition = csDefinition;
    }

    public String getCsiName() {
        return csiName;
    }

    public void setCsiName(String csiName) {
        this.csiName = csiName;
    }

    public String getCsiType() {
        return csiType;
    }

    public void setCsiType(String csiType) {
        this.csiType = csiType;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public Float getCsVersion() {
        return csVersion;
    }

    public void setCsVersion(Float csVersion) {
        this.csVersion = csVersion;
    }

    public Integer getCsiId() {
        return csiId;
    }

    public void setCsiId(Integer csiId) {
        this.csiId = csiId;
    }

    public Float getCsiVersion() {
        return csiVersion;
    }

    public void setCsiVersion(Float csiVersion) {
        this.csiVersion = csiVersion;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public List<AlternateName> getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(List<AlternateName> alternateNames) {
        this.alternateNames = alternateNames;
    }

    public List<AlternateDefinition> getAlternateDefinitions() {
        return alternateDefinitions;
    }

    public void setAlternateDefinitions(List<AlternateDefinition> alternateDefinitions) {
        this.alternateDefinitions = alternateDefinitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CsCsi)) return false;

        CsCsi csCsi = (CsCsi) o;

        if (getCsLongName() != null ? !getCsLongName().equals(csCsi.getCsLongName()) : csCsi.getCsLongName() != null)
            return false;
        if (getCsDefinition() != null ? !getCsDefinition().equals(csCsi.getCsDefinition()) : csCsi.getCsDefinition() != null)
            return false;
        if (getCsiName() != null ? !getCsiName().equals(csCsi.getCsiName()) : csCsi.getCsiName() != null) return false;
        if (getCsiType() != null ? !getCsiType().equals(csCsi.getCsiType()) : csCsi.getCsiType() != null) return false;
        if (getCsId() != null ? !getCsId().equals(csCsi.getCsId()) : csCsi.getCsId() != null) return false;
        if (getCsVersion() != null ? !getCsVersion().equals(csCsi.getCsVersion()) : csCsi.getCsVersion() != null)
            return false;
        if (getCsiId() != null ? !getCsiId().equals(csCsi.getCsiId()) : csCsi.getCsiId() != null) return false;
        if (getCsiVersion() != null ? !getCsiVersion().equals(csCsi.getCsiVersion()) : csCsi.getCsiVersion() != null)
            return false;
        if (getHide() != null ? !getHide().equals(csCsi.getHide()) : csCsi.getHide() != null) return false;
        if (getAlternateNames() != null ? !getAlternateNames().equals(csCsi.getAlternateNames()) : csCsi.getAlternateNames() != null)
            return false;
        return !(getAlternateDefinitions() != null ? !getAlternateDefinitions().equals(csCsi.getAlternateDefinitions()) : csCsi.getAlternateDefinitions() != null);

    }

    @Override
    public int hashCode() {
        int result = getCsLongName() != null ? getCsLongName().hashCode() : 0;
        result = 31 * result + (getCsDefinition() != null ? getCsDefinition().hashCode() : 0);
        result = 31 * result + (getCsiName() != null ? getCsiName().hashCode() : 0);
        result = 31 * result + (getCsiType() != null ? getCsiType().hashCode() : 0);
        result = 31 * result + (getCsId() != null ? getCsId().hashCode() : 0);
        result = 31 * result + (getCsVersion() != null ? getCsVersion().hashCode() : 0);
        result = 31 * result + (getCsiId() != null ? getCsiId().hashCode() : 0);
        result = 31 * result + (getCsiVersion() != null ? getCsiVersion().hashCode() : 0);
        result = 31 * result + (getHide() != null ? getHide().hashCode() : 0);
        result = 31 * result + (getAlternateNames() != null ? getAlternateNames().hashCode() : 0);
        result = 31 * result + (getAlternateDefinitions() != null ? getAlternateDefinitions().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CsCsi{" +
                "csLongName='" + csLongName + '\'' +
                ", csDefinition='" + csDefinition + '\'' +
                ", csiName='" + csiName + '\'' +
                ", csiType='" + csiType + '\'' +
                ", csId='" + csId + '\'' +
                ", csVersion=" + csVersion +
                ", csiId=" + csiId +
                ", csiVersion=" + csiVersion +
                ", hide=" + hide +
                ", alternateNames=" + alternateNames +
                ", alternateDefinitions=" + alternateDefinitions +
                '}';
    }
}
