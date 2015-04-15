package gov.nih.nci.cadsr.dao.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lerner
 * Date: 3/23/15
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataElementModel extends BaseModel
{

    private String longCDEName;
    private String contextName;
    private String usingContexts;
    private List<ReferenceDocModel> refDocs;
    private List<DesignationModel> designationModels;
    private Integer publicId;
    private String idseq;
    private String registrationStatus;
    private ValueDomainModel valueDomainModel;
    private DataElementConceptModel dec;
    private ContextModel context; // Jeff is working on this one
    private String deIdseq;
    private String version; // needs to be a Float!
    private String conteIdseq;
    private String preferredName;
    private String vdIdseq;
    private String decIdseq;
    private String preferredDefinition;
    private String aslName;
    private String longName;
    private String latestVerInd;
    private String deletedInd;
    private String beginDate;
    private String endDate;
    private String origin;
    private String cdeId;
    private String question;
    private String vdName;

    public DataElementModel() {
    }

    public String getLongCDEName() {
        return longCDEName;
    }

    public void setLongCDEName(String longCDEName) {
        this.longCDEName = longCDEName;
    }

    public String getVdName() {
        return vdName;
    }

    public void setVdName(String vdName) {
        this.vdName = vdName;
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

    public List<DesignationModel> getDesignationModels() {
        return designationModels;
    }

    public void setDesignationModels(List<DesignationModel> designationModels) {
        this.designationModels = designationModels;
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

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
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

    public String getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    //  commented out because it's in the superclass
//    public String getMODIFIED_BY()
//    {
//        return MODIFIED_BY;
//    }
//
//    public void setMODIFIED_BY( String MODIFIED_BY )
//    {
//        this.MODIFIED_BY = MODIFIED_BY;
//    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public String getCdeId()
    {
        return cdeId;
    }

    public void setCdeId(String cdeId)
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
    }
}
