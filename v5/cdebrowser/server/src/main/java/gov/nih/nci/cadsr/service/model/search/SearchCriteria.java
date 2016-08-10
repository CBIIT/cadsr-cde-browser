package gov.nih.nci.cadsr.service.model.search;

import java.io.Serializable;

public class SearchCriteria implements Serializable
{
	private static final long serialVersionUID = -4732600582872432160L;

	private String name;
	private String searchMode;
	private String publicId;
	private int queryType; 			// 2 = "At least one of the words"
	private String programArea; 	// 0 = All (Ignore Program area)
	private String context;
	private String classification;
	private String csCsiIdSeq;
	private String protocol;
	private String formIdSeq;
	private String workFlowStatus;
	private String registrationStatus;

	//FIXME we do not use in 5.2 conceptName and conceptCode. If this version stay consider to remove these two fields.
	private String conceptName;
	private String conceptCode;

	private String conceptInput;//this is either Concept long name or preferred name AKA Concept Code
	//FIXME in 5.3 we remove conceptQueryType and make the search as in CT in both name values
	private String conceptQueryType;

	private String dataElementConcept;
	private String permissibleValue;
	private int pvQueryType;
	private String objectClass;
	private int contextUse;
	private int versionType;//0 - latest, 1 - All
	private String altName;
	private String altNameType;
	private String vdTypeFlag;
	private String valueDomain;
	private String filteredinput;
	private String property;
	private String derivedDEFlag;


	public static final String ALL_REGISRTATION_STATUSES = "ALL Registration Statuses";
	public static final String ALL_WORKFLOW_STATUSES = "ALL Workflow Statuses";
	public static final String ALL_ALTNAME_TYPES = "ALL Alternate Name Types";
	public static final String ALL_FIELDS = "ALL Fields";
	/**
	 * This method takes care of client values received to be adjusted to server component expectations.
	 *
	 */
	public void preprocessCriteria() {
		if (ALL_FIELDS.equals(this.filteredinput))
			this.filteredinput = "ALL";
		if (ALL_ALTNAME_TYPES.equals(this.altNameType))
			this.altNameType = "ALL";
		if (ALL_WORKFLOW_STATUSES.equals(this.workFlowStatus))
			this.workFlowStatus = "ALL";
		if (ALL_REGISRTATION_STATUSES.equals(this.registrationStatus))
			this.registrationStatus = "ALL";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public String getProgramArea() {
		return programArea;
	}

	public void setProgramArea(String programArea) {
		this.programArea = programArea;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getWorkFlowStatus() {
		return workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public String getCsCsiIdSeq() {
		return csCsiIdSeq;
	}

	public void setCsCsiIdSeq(String csCsiIdSeq) {
		this.csCsiIdSeq = csCsiIdSeq;
	}

	public String getFormIdSeq() {
		return formIdSeq;
	}

	public void setFormIdSeq(String formIdSeq) {
		this.formIdSeq = formIdSeq;
	}

	public String getPermissibleValue()
	{
		return permissibleValue;
	}

	public void setPermissibleValue( String permissibleValue )
	{
		this.permissibleValue = permissibleValue;
	}

	public int getPvQueryType()
	{
		return pvQueryType;
	}

	public void setPvQueryType( int pvQueryType )
	{
		this.pvQueryType = pvQueryType;
	}

	public String getDataElementConcept()
	{
		return dataElementConcept;
	}

	public void setDataElementConcept( String dataElementConcept )
	{
		this.dataElementConcept = dataElementConcept;
	}

	public String getObjectClass()
	{
		return objectClass;
	}

	public void setObjectClass( String objectClass )
	{
		this.objectClass = objectClass;
	}

	public int getContextUse()
	{
		return contextUse;
	}

	public void setContextUse( int contextUse )
	{
		this.contextUse = contextUse;
	}

	public int getVersionType() {
		return versionType;
	}

	public void setVersionType(int versionType) {
		this.versionType = versionType;
	}

	public String getAltName() {
		return altName;
	}

	public void setAltName(String altName) {
		this.altName = altName;
	}

	public String getAltNameType() {
		return altNameType;
	}

	public void setAltNameType(String altNameType) {
		this.altNameType = altNameType;
	}

	public String getFilteredinput()
	{
		return filteredinput;
	}

	public void setFilteredinput( String filteredinput )
	{
		this.filteredinput = filteredinput;
	}

	public String getVdTypeFlag() {
		return vdTypeFlag;
	}

	public void setVdTypeFlag(String vdTypeFlag) {
		if (vdTypeFlag.equals("0")) {
			this.vdTypeFlag = "E";
		} else if (vdTypeFlag.equals("1")) {
			this.vdTypeFlag = "N";
		} else {
			this.vdTypeFlag = "";
		}

	}

	public String getValueDomain() {
		return valueDomain;
	}

	public void setValueDomain(String valueDomain) {
		this.valueDomain = valueDomain;
	}

	public String getConceptInput() {
		return conceptInput;
	}

	public void setConceptInput(String conceptInput) {
		this.conceptInput = conceptInput;
	}

	public String getConceptQueryType() {
		return conceptQueryType;
	}

	public void setConceptQueryType(String conceptQueryType) {
		this.conceptQueryType = conceptQueryType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDerivedDEFlag() {
		return derivedDEFlag;
	}

	public void setDerivedDEFlag(String derivedDEFlag) {
		this.derivedDEFlag = derivedDEFlag;
	}

	@Override
	public String toString() {
		return "SearchCriteria [name=" + name + ", searchMode=" + searchMode + ", publicId=" + publicId + ", queryType="
				+ queryType + ", programArea=" + programArea + ", context=" + context + ", classification="
				+ classification + ", csCsiIdSeq=" + csCsiIdSeq + ", protocol=" + protocol + ", formIdSeq=" + formIdSeq
				+ ", workFlowStatus=" + workFlowStatus + ", registrationStatus=" + registrationStatus + ", conceptName="
				+ conceptName + ", conceptCode=" + conceptCode + ", dataElementConcept=" + dataElementConcept
				+ ", permissibleValue=" + permissibleValue + ", pvQueryType=" + pvQueryType + ", objectClass="
				+ objectClass + ", contextUse=" + contextUse + ", versionType=" + versionType + ", altName=" + altName
				+ ", altNameType=" + altNameType + ", vdTypeFlag=" + vdTypeFlag + ", valueDomain=" + valueDomain
				+ ", filteredinput=" + filteredinput + ", conceptInput=" + conceptInput + ", conceptType=" + conceptQueryType
				+ ", property=" + property + ", derivedDEFlag=" + derivedDEFlag
				+ "]";
	}

}
