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
	private String conceptName;
	private String conceptCode;
	
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

	@Override
	public String toString() {
		return "SearchCriteria [name=" + name + ", searchMode=" + searchMode + ", publicId=" + publicId + ", queryType=" + queryType + ", programArea="
				+ programArea + ", context=" + context + ", classification=" + classification + ", csCsiIdSeq = " + csCsiIdSeq + ", protocol=" + protocol
				+ ", formIdSeq = " + formIdSeq + ", workFlowStatus=" + workFlowStatus + ", registrationStatus=" + registrationStatus + ", conceptName="
				+ conceptName + ", conceptCode=" + conceptCode + "]";
	}	

}
