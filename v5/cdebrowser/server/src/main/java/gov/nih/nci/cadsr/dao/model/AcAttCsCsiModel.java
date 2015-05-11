package gov.nih.nci.cadsr.dao.model;

import java.sql.Timestamp;

/**
 * Created by lavezzojl on 5/8/15.
 */
public class AcAttCsCsiModel extends BaseModel {
    private String acaIdseq;
    private String csCsiIdseq;
    private String attIdseq;
    private String atlName;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    public String getAcaIdseq() {
        return acaIdseq;
    }

    public void setAcaIdseq(String acaIdseq) {
        this.acaIdseq = acaIdseq;
    }

    public String getCsCsiIdseq() {
        return csCsiIdseq;
    }

    public void setCsCsiIdseq(String csCsiIdseq) {
        this.csCsiIdseq = csCsiIdseq;
    }

    public String getAttIdseq() {
        return attIdseq;
    }

    public void setAttIdseq(String attIdseq) {
        this.attIdseq = attIdseq;
    }

    public String getAtlName() {
        return atlName;
    }

    public void setAtlName(String atlName) {
        this.atlName = atlName;
    }

    @Override
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Timestamp getDateModified() {
        return dateModified;
    }

    @Override
    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcAttCsCsiModel)) return false;

        AcAttCsCsiModel that = (AcAttCsCsiModel) o;

        if (getAcaIdseq() != null ? !getAcaIdseq().equals(that.getAcaIdseq()) : that.getAcaIdseq() != null)
            return false;
        if (getCsCsiIdseq() != null ? !getCsCsiIdseq().equals(that.getCsCsiIdseq()) : that.getCsCsiIdseq() != null)
            return false;
        if (getAttIdseq() != null ? !getAttIdseq().equals(that.getAttIdseq()) : that.getAttIdseq() != null)
            return false;
        if (getAtlName() != null ? !getAtlName().equals(that.getAtlName()) : that.getAtlName() != null) return false;
        if (getDateCreated() != null ? !getDateCreated().equals(that.getDateCreated()) : that.getDateCreated() != null)
            return false;
        return !(getDateModified() != null ? !getDateModified().equals(that.getDateModified()) : that.getDateModified() != null);

    }

    @Override
    public int hashCode() {
        int result = getAcaIdseq() != null ? getAcaIdseq().hashCode() : 0;
        result = 31 * result + (getCsCsiIdseq() != null ? getCsCsiIdseq().hashCode() : 0);
        result = 31 * result + (getAttIdseq() != null ? getAttIdseq().hashCode() : 0);
        result = 31 * result + (getAtlName() != null ? getAtlName().hashCode() : 0);
        result = 31 * result + (getDateCreated() != null ? getDateCreated().hashCode() : 0);
        result = 31 * result + (getDateModified() != null ? getDateModified().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AcAttCsCsiModel{" +
                "acaIdseq='" + acaIdseq + '\'' +
                ", csCsiIdseq='" + csCsiIdseq + '\'' +
                ", attIdseq='" + attIdseq + '\'' +
                ", atlName='" + atlName + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';
    }
}
