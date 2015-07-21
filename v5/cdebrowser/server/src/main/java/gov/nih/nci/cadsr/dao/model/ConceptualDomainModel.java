package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lavezzojl on 4/22/15.
 */
public class ConceptualDomainModel extends BaseModel {


    // these are all the fields used for now
    private String preferredName;
    private Float version;
    private int cdId; //aka public id
    private ContextModel contextModel;
    private String conteIdseq;

    public ConceptualDomainModel() {
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public Float getVersion() {
        return version;
    }

    public void setVersion(Float version) {
        this.version = version;
        setFormattedVersion(  this.version );
    }

    public int getCdId() {
        return cdId;
    }

    public void setCdId(int cdId) {
        this.cdId = cdId;
    }

    public ContextModel getContextModel() {
        return contextModel;
    }

    public void setContextModel(ContextModel contextModel) {
        this.contextModel = contextModel;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    @Override
    public String toString() {
        return "ConceptualDomainModel{" +
                "preferredName='" + preferredName + '\'' +
                ", version=" + version +
                ", cdId=" + cdId +
                ", contextModel=" + contextModel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptualDomainModel)) return false;

        ConceptualDomainModel that = (ConceptualDomainModel) o;

        if (getCdId() != that.getCdId()) return false;
        if (getPreferredName() != null ? !getPreferredName().equals(that.getPreferredName()) : that.getPreferredName() != null)
            return false;
        if (getVersion() != null ? !getVersion().equals(that.getVersion()) : that.getVersion() != null) return false;
        return !(getContextModel() != null ? !getContextModel().equals(that.getContextModel()) : that.getContextModel() != null);

    }

    @Override
    public int hashCode() {
        int result = getPreferredName() != null ? getPreferredName().hashCode() : 0;
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + getCdId();
        result = 31 * result + (getContextModel() != null ? getContextModel().hashCode() : 0);
        return result;
    }
}
