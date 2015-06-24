package gov.nih.nci.cadsr.dao.model;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is just a test class used in the early stages of populating CDE Details of the client, it will deleted soon.
 */

public class DataElementModel extends BaseModel
{

    private String preferredQuestionText;
    private String contextName; // do we really need this to be a separate field instead of context.getName()?
    private String usingContexts; // not in table. Filled from designationModels.contexts.name where designationModels.detlName = 'USED_BY'
    private List<ReferenceDocModel> refDocs;// from ReferenceDocumentsView.ac_idseq = data_elements.de_idseq
    /**
     * Hashmap of DesignationModels indexed by designationIdseq
     */
    private HashMap<String,DesignationModel> designationModels; // from DesignationsView.ac_idseq = data_elements.de_idseq
    /**
     * Hashmap of DefinitionModels indexed by definitionIdseq
     */
    private HashMap<String,DefinitionModel> definitionModels;
    private Integer publicId; // this is a duplicate of cdeId. do we really need this?
    private String idseq;
    private String registrationStatus; // not in table. Filled from SBR.AC_RESISTRATIONS.REGISTRATION_STATUS see DAO row mapper
    private ValueDomainModel valueDomainModel; // from vd_idseq
    private DataElementConceptModel dec; // from dec_idseq
    private ContextModel context;
    private String deIdseq; // primary key
    private Float version;
    private String conteIdseq; // this field can't possibly be needed since we have a whole context model object
    private String preferredName;
    private String vdIdseq; // this field can't possibly be needed since we have a whole value domain model object
    private String decIdseq;
    private String preferredDefinition;
    private String aslName; // workflow status
    private String longName;
    private String latestVerInd;
    private String deletedInd;
    private Timestamp beginDate;
    private Timestamp endDate;
    private String origin;
    private Integer cdeId;
    private String question;
    /**
     * Hashmap of CsCsiModels indexed by csiIdseq. csiIdseq may be the value "UNCLASSIFIED"
     */
    private HashMap<String,CsCsiModel> csCsiData;
    /**
     * Hashmap of Lists of designationIdseqs indexed by csiIdseq. csiIdseq may be the value "UNCLASSIFIED"
     * (each entry is a list of the designationIdseqs that are classified into the indexed csiIdseq)
     */
    private HashMap<String,List<String>> csCsiDesignations;
    /**
     * Hashmap of Lists of definitionIdseqs indexed by csiIdseq. csiIdseq may be the value "UNCLASSIFIED"
     * (each entry is a list of the definitionIdseqs that are classified into the indexed csiIdseq)
     */
    private HashMap<String,List<String>> csCsiDefinitions;
    private List<UsageModel> usageModels;
    private List<DEOtherVersionsModel> deOtherVersionsModels;
    private List<CsCsiModel> classifications;
    private List<CSRefDocModel> csRefDocModels;
    private List<CSIRefDocModel> csiRefDocModels;


    public DataElementModel() {
    }

    public String getPreferredQuestionText() {
        return preferredQuestionText;
    }

    public void setPreferredQuestionText(String preferredQuestionText) {
        this.preferredQuestionText = preferredQuestionText;
    }

    /**
     * populate the preferred question text (longCDEName) field out of the
     * reference doc where DCTL_NAME is "Preferred Question Text"
     */
    public void fillPreferredQuestionText() {
        if (getRefDocs() != null) {
            for (ReferenceDocModel referenceDocModel : getRefDocs()) {
                if (referenceDocModel.getDctlName() != null && referenceDocModel.getDctlName().equals("Preferred Question Text")
                        && referenceDocModel.getDocText() != null) {
                    setPreferredQuestionText(referenceDocModel.getDocText());
                    return;
                }
            }
        }
    }

    /**
     * populate the usingContexts field by concatenating the designationModels' contexts' names
     * where
     */
    public void fillUsingContexts() {
        ArrayList<String> usingContexts = new ArrayList<>();
        if (getDesignationModels() != null) {
            for (DesignationModel designationModel : getDesignationModels().values()) {
                if (designationModel.getDetlName() != null
                        && designationModel.getDetlName().equals("USED_BY")
                        && designationModel.getContex().getName() != null) {
                    usingContexts.add(designationModel.getContex().getName());
                }
            }
        }
        setUsingContexts(StringUtils.join(usingContexts, ", "));
    }

    public void fillCsCsiData(List<CsCsiModel> csCsiModels) {
        // initialize csCsiData
        csCsiData = new HashMap<String,CsCsiModel>();
        // Prepare a CsCsiModel for any definitions and designations that are unclassified
        csCsiData.put(CsCsiModel.UNCLASSIFIED, new CsCsiModel(CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED));
        // copy over the rest of the models, using the hashmap to remove duplicates
        for (CsCsiModel csCsiModel : csCsiModels) {
            csCsiData.put(csCsiModel.getCsIdseq(), csCsiModel);
        }
    }

    public void fillCsCsiDesignations () {
        if (getCsCsiDesignations() == null) {
            setCsCsiDesignations(new HashMap<String, List<String>>());
        }
        for (DesignationModel designationModel : getDesignationModels().values()) {
            if (designationModel.getCsiIdseqs() != null && designationModel.getCsiIdseqs().size() > 0) {
                for (String csiIdseq : designationModel.getCsiIdseqs()) {
                    if (getCsCsiDesignations().get(csiIdseq) == null) {
                        getCsCsiDesignations().put(csiIdseq, new ArrayList<String>());
                    }
                    getCsCsiDesignations().get(csiIdseq).add(designationModel.getDesigIDSeq());
                }
            }
        }

    }

    public void fillCsCsiDefinitions () {
        if (getCsCsiDefinitions() == null) {
            setCsCsiDefinitions(new HashMap<String, List<String>>());
        }
        for (DefinitionModel definitionModel : getDefinitionModels().values()) {
            if (definitionModel.getCsiIdseqs() != null && definitionModel.getCsiIdseqs().size() > 0) {
                for (String csiIdseq : definitionModel.getCsiIdseqs()) {
                    if (getCsCsiDefinitions().get(csiIdseq) == null) {
                        getCsCsiDefinitions().put(csiIdseq, new ArrayList<String>());
                    }
                    getCsCsiDefinitions().get(csiIdseq).add(definitionModel.getDefinIdseq());
                }
            }
        }

    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getUsingContexts() {
        return usingContexts;
    }

    public void setUsingContexts(String usingContexts) {
        this.usingContexts = usingContexts;
    }

    public List<ReferenceDocModel> getRefDocs() {
        return refDocs;
    }

    public void setRefDocs(List<ReferenceDocModel> refDocs) {
        this.refDocs = refDocs;
    }

    public Integer getPublicId() {
        return publicId;
    }

    public void setPublicId(Integer publicId) {
        this.publicId = publicId;
    }

    public String getIdseq() {
        return idseq;
    }

    public void setIdseq(String idseq) {
        this.idseq = idseq;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public ValueDomainModel getValueDomainModel() {
        return valueDomainModel;
    }

    public void setValueDomainModel(ValueDomainModel valueDomainModel) {
        this.valueDomainModel = valueDomainModel;
    }

    public DataElementConceptModel getDec() {
        return dec;
    }

    public void setDec(DataElementConceptModel dec) {
        this.dec = dec;
    }

    public ContextModel getContext() {
        return context;
    }

    public void setContext(ContextModel context) {
        this.context = context;
    }

    public String getDeIdseq()
    {
        return deIdseq;
    }

    public void setDeIdseq(String deIdseq)
    {
        this.deIdseq = deIdseq;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion(Float version)
    {
        this.version = version;
        setFormattedVersion(  version );
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq(String conteIdseq)
    {
        this.conteIdseq = conteIdseq;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName(String preferredName)
    {
        this.preferredName = preferredName;
    }

    public String getVdIdseq()
    {
        return vdIdseq;
    }

    public void setVdIdseq(String vdIdseq)
    {
        this.vdIdseq = vdIdseq;
    }

    public String getDecIdseq()
    {
        return decIdseq;
    }

    public void setDecIdseq(String decIdseq)
    {
        this.decIdseq = decIdseq;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition(String preferredDefinition)
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName(String aslName)
    {
        this.aslName = aslName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName(String longName)
    {
        this.longName = longName;
    }

    public String getLatestVerInd()
    {
        return latestVerInd;
    }

    public void setLatestVerInd(String latestVerInd)
    {
        this.latestVerInd = latestVerInd;
    }

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd(String deletedInd)
    {
        this.deletedInd = deletedInd;
    }

    public Timestamp getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate)
    {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Timestamp endDate)
    {
        this.endDate = endDate;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public Integer getCdeId()
    {
        return cdeId;
    }

    public void setCdeId(Integer cdeId)
    {
        this.cdeId = cdeId;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
        setPreferredQuestionText( this.question );
    }

    public HashMap<String, CsCsiModel> getCsCsiData() {
        return csCsiData;
    }

    public void setCsCsiData(HashMap<String, CsCsiModel> csCsiData) {
        this.csCsiData = csCsiData;
    }

    public HashMap<String, List<String>> getCsCsiDesignations() {
        return csCsiDesignations;
    }

    public void setCsCsiDesignations(HashMap<String, List<String>> csCsiDesignations) {
        this.csCsiDesignations = csCsiDesignations;
    }

    public HashMap<String, List<String>> getCsCsiDefinitions() {
        return csCsiDefinitions;
    }

    public void setCsCsiDefinitions(HashMap<String, List<String>> csCsiDefinitions) {
        this.csCsiDefinitions = csCsiDefinitions;
    }

    public HashMap<String, DesignationModel> getDesignationModels() {
        return designationModels;
    }

    public void setDesignationModels(HashMap<String, DesignationModel> designationModels) {
        this.designationModels = designationModels;
    }

    public void setDesignationModels(List<DesignationModel> designationModels) {
        setDesignationModels(new HashMap<String, DesignationModel>());
        for (DesignationModel designationModel : designationModels) {
            getDesignationModels().put(designationModel.getDesigIDSeq(), designationModel);
        }
    }

    public HashMap<String, DefinitionModel> getDefinitionModels() {
        return definitionModels;
    }

    public void setDefinitionModels(HashMap<String, DefinitionModel> definitionModels) {
        this.definitionModels = definitionModels;
    }

    public void setDefinitionModels(List<DefinitionModel> definitionModels) {
        setDefinitionModels(new HashMap<String,DefinitionModel>());
        for (DefinitionModel definitionModel : definitionModels) {
            getDefinitionModels().put(definitionModel.getDefinIdseq(), definitionModel);
        }
    }

    public List<UsageModel> getUsageModels() {
        return usageModels;
    }

    public void setUsageModels(List<UsageModel> usageModels) {
        this.usageModels = usageModels;
    }

    public List<DEOtherVersionsModel> getDeOtherVersionsModels() {
        return deOtherVersionsModels;
    }

    public void setDeOtherVersionsModels(List<DEOtherVersionsModel> deOtherVersionsModels) {
        this.deOtherVersionsModels = deOtherVersionsModels;
    }

    public List<CsCsiModel> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<CsCsiModel> classifications) {
        this.classifications = classifications;
    }

    public List<CSRefDocModel> getCsRefDocModels() {
        return csRefDocModels;
    }

    public void setCsRefDocModels(List<CSRefDocModel> csRefDocModels) {
        this.csRefDocModels = csRefDocModels;
    }

    public List<CSIRefDocModel> getCsiRefDocModels() {
        return csiRefDocModels;
    }

    public void setCsiRefDocModels(List<CSIRefDocModel> csiRefDocModels) {
        this.csiRefDocModels = csiRefDocModels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataElementModel)) return false;

        DataElementModel that = (DataElementModel) o;

        if (getPreferredQuestionText() != null ? !getPreferredQuestionText().equals(that.getPreferredQuestionText()) : that.getPreferredQuestionText() != null)
            return false;
        if (getContextName() != null ? !getContextName().equals(that.getContextName()) : that.getContextName() != null)
            return false;
        if (getUsingContexts() != null ? !getUsingContexts().equals(that.getUsingContexts()) : that.getUsingContexts() != null)
            return false;
        if (getRefDocs() != null ? !getRefDocs().equals(that.getRefDocs()) : that.getRefDocs() != null) return false;
        if (getDesignationModels() != null ? !getDesignationModels().equals(that.getDesignationModels()) : that.getDesignationModels() != null)
            return false;
        if (getDefinitionModels() != null ? !getDefinitionModels().equals(that.getDefinitionModels()) : that.getDefinitionModels() != null)
            return false;
        if (getPublicId() != null ? !getPublicId().equals(that.getPublicId()) : that.getPublicId() != null)
            return false;
        if (getIdseq() != null ? !getIdseq().equals(that.getIdseq()) : that.getIdseq() != null) return false;
        if (getRegistrationStatus() != null ? !getRegistrationStatus().equals(that.getRegistrationStatus()) : that.getRegistrationStatus() != null)
            return false;
        if (getValueDomainModel() != null ? !getValueDomainModel().equals(that.getValueDomainModel()) : that.getValueDomainModel() != null)
            return false;
        if (getDec() != null ? !getDec().equals(that.getDec()) : that.getDec() != null) return false;
        if (getContext() != null ? !getContext().equals(that.getContext()) : that.getContext() != null) return false;
        if (getDeIdseq() != null ? !getDeIdseq().equals(that.getDeIdseq()) : that.getDeIdseq() != null) return false;
        if (getVersion() != null ? !getVersion().equals(that.getVersion()) : that.getVersion() != null) return false;
        if (getConteIdseq() != null ? !getConteIdseq().equals(that.getConteIdseq()) : that.getConteIdseq() != null)
            return false;
        if (getPreferredName() != null ? !getPreferredName().equals(that.getPreferredName()) : that.getPreferredName() != null)
            return false;
        if (getVdIdseq() != null ? !getVdIdseq().equals(that.getVdIdseq()) : that.getVdIdseq() != null) return false;
        if (getDecIdseq() != null ? !getDecIdseq().equals(that.getDecIdseq()) : that.getDecIdseq() != null)
            return false;
        if (getPreferredDefinition() != null ? !getPreferredDefinition().equals(that.getPreferredDefinition()) : that.getPreferredDefinition() != null)
            return false;
        if (getAslName() != null ? !getAslName().equals(that.getAslName()) : that.getAslName() != null) return false;
        if (getLongName() != null ? !getLongName().equals(that.getLongName()) : that.getLongName() != null)
            return false;
        if (getLatestVerInd() != null ? !getLatestVerInd().equals(that.getLatestVerInd()) : that.getLatestVerInd() != null)
            return false;
        if (getDeletedInd() != null ? !getDeletedInd().equals(that.getDeletedInd()) : that.getDeletedInd() != null)
            return false;
        if (getBeginDate() != null ? !getBeginDate().equals(that.getBeginDate()) : that.getBeginDate() != null)
            return false;
        if (getEndDate() != null ? !getEndDate().equals(that.getEndDate()) : that.getEndDate() != null) return false;
        if (getOrigin() != null ? !getOrigin().equals(that.getOrigin()) : that.getOrigin() != null) return false;
        if (getCdeId() != null ? !getCdeId().equals(that.getCdeId()) : that.getCdeId() != null) return false;
        if (getQuestion() != null ? !getQuestion().equals(that.getQuestion()) : that.getQuestion() != null)
            return false;
        if (getCsCsiData() != null ? !getCsCsiData().equals(that.getCsCsiData()) : that.getCsCsiData() != null)
            return false;
        if (getCsCsiDesignations() != null ? !getCsCsiDesignations().equals(that.getCsCsiDesignations()) : that.getCsCsiDesignations() != null)
            return false;
        if (getCsCsiDefinitions() != null ? !getCsCsiDefinitions().equals(that.getCsCsiDefinitions()) : that.getCsCsiDefinitions() != null)
            return false;
        if (getUsageModels() != null ? !getUsageModels().equals(that.getUsageModels()) : that.getUsageModels() != null)
            return false;
        if (getDeOtherVersionsModels() != null ? !getDeOtherVersionsModels().equals(that.getDeOtherVersionsModels()) : that.getDeOtherVersionsModels() != null)
            return false;
        if (getClassifications() != null ? !getClassifications().equals(that.getClassifications()) : that.getClassifications() != null)
            return false;
        if (getCsRefDocModels() != null ? !getCsRefDocModels().equals(that.getCsRefDocModels()) : that.getCsRefDocModels() != null)
            return false;
        return !(getCsiRefDocModels() != null ? !getCsiRefDocModels().equals(that.getCsiRefDocModels()) : that.getCsiRefDocModels() != null);

    }

    @Override
    public int hashCode() {
        int result = getPreferredQuestionText() != null ? getPreferredQuestionText().hashCode() : 0;
        result = 31 * result + (getContextName() != null ? getContextName().hashCode() : 0);
        result = 31 * result + (getUsingContexts() != null ? getUsingContexts().hashCode() : 0);
        result = 31 * result + (getRefDocs() != null ? getRefDocs().hashCode() : 0);
        result = 31 * result + (getDesignationModels() != null ? getDesignationModels().hashCode() : 0);
        result = 31 * result + (getDefinitionModels() != null ? getDefinitionModels().hashCode() : 0);
        result = 31 * result + (getPublicId() != null ? getPublicId().hashCode() : 0);
        result = 31 * result + (getIdseq() != null ? getIdseq().hashCode() : 0);
        result = 31 * result + (getRegistrationStatus() != null ? getRegistrationStatus().hashCode() : 0);
        result = 31 * result + (getValueDomainModel() != null ? getValueDomainModel().hashCode() : 0);
        result = 31 * result + (getDec() != null ? getDec().hashCode() : 0);
        result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
        result = 31 * result + (getDeIdseq() != null ? getDeIdseq().hashCode() : 0);
        result = 31 * result + (getVersion() != null ? getVersion().hashCode() : 0);
        result = 31 * result + (getConteIdseq() != null ? getConteIdseq().hashCode() : 0);
        result = 31 * result + (getPreferredName() != null ? getPreferredName().hashCode() : 0);
        result = 31 * result + (getVdIdseq() != null ? getVdIdseq().hashCode() : 0);
        result = 31 * result + (getDecIdseq() != null ? getDecIdseq().hashCode() : 0);
        result = 31 * result + (getPreferredDefinition() != null ? getPreferredDefinition().hashCode() : 0);
        result = 31 * result + (getAslName() != null ? getAslName().hashCode() : 0);
        result = 31 * result + (getLongName() != null ? getLongName().hashCode() : 0);
        result = 31 * result + (getLatestVerInd() != null ? getLatestVerInd().hashCode() : 0);
        result = 31 * result + (getDeletedInd() != null ? getDeletedInd().hashCode() : 0);
        result = 31 * result + (getBeginDate() != null ? getBeginDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        result = 31 * result + (getOrigin() != null ? getOrigin().hashCode() : 0);
        result = 31 * result + (getCdeId() != null ? getCdeId().hashCode() : 0);
        result = 31 * result + (getQuestion() != null ? getQuestion().hashCode() : 0);
        result = 31 * result + (getCsCsiData() != null ? getCsCsiData().hashCode() : 0);
        result = 31 * result + (getCsCsiDesignations() != null ? getCsCsiDesignations().hashCode() : 0);
        result = 31 * result + (getCsCsiDefinitions() != null ? getCsCsiDefinitions().hashCode() : 0);
        result = 31 * result + (getUsageModels() != null ? getUsageModels().hashCode() : 0);
        result = 31 * result + (getDeOtherVersionsModels() != null ? getDeOtherVersionsModels().hashCode() : 0);
        result = 31 * result + (getClassifications() != null ? getClassifications().hashCode() : 0);
        result = 31 * result + (getCsRefDocModels() != null ? getCsRefDocModels().hashCode() : 0);
        result = 31 * result + (getCsiRefDocModels() != null ? getCsiRefDocModels().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DataElementModel{" +
                "preferredQuestionText='" + preferredQuestionText + '\'' +
                ", contextName='" + contextName + '\'' +
                ", usingContexts='" + usingContexts + '\'' +
                ", refDocs=" + refDocs +
                ", designationModels=" + designationModels +
                ", definitionModels=" + definitionModels +
                ", publicId=" + publicId +
                ", idseq='" + idseq + '\'' +
                ", registrationStatus='" + registrationStatus + '\'' +
                ", valueDomainModel=" + valueDomainModel +
                ", dec=" + dec +
                ", context=" + context +
                ", deIdseq='" + deIdseq + '\'' +
                ", version=" + version +
                ", conteIdseq='" + conteIdseq + '\'' +
                ", preferredName='" + preferredName + '\'' +
                ", vdIdseq='" + vdIdseq + '\'' +
                ", decIdseq='" + decIdseq + '\'' +
                ", preferredDefinition='" + preferredDefinition + '\'' +
                ", aslName='" + aslName + '\'' +
                ", longName='" + longName + '\'' +
                ", latestVerInd='" + latestVerInd + '\'' +
                ", deletedInd='" + deletedInd + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", origin='" + origin + '\'' +
                ", cdeId=" + cdeId +
                ", question='" + question + '\'' +
                ", csCsiData=" + csCsiData +
                ", csCsiDesignations=" + csCsiDesignations +
                ", csCsiDefinitions=" + csCsiDefinitions +
                ", usageModels=" + usageModels +
                ", deOtherVersionsModels=" + deOtherVersionsModels +
                ", classifications=" + classifications +
                ", csRefDocModels=" + csRefDocModels +
                ", csiRefDocModels=" + csiRefDocModels +
                '}';
    }
}
