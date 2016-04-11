package gov.nih.nci.cadsr.model;

public class SearchForm 
{
	private String query; 				//The Name field text of the users search input
	private int field;	  				//0=Name 1=PublicId
	private int queryType; 				//0="Exact phrase" 1="All of the words" 2="At least one of the words
	private int programArea;  			// 0 = All (Ignore Program area)
    private String context;
    private String classification;
    private String protocol;
    private String workFlowStatus;
    private String registrationStatus;
    private String conceptName;
    private String conceptCode;
	
    public String getQuery() {
		return query;
	}
    
	public void setQuery(String query) {
		this.query = query;
	}
	
	public int getField() {
		return field;
	}
	
	public void setField(int field) {
		this.field = field;
	}
	
	public int getQueryType() {
		return queryType;
	}
	
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	
	public int getProgramArea() {
		return programArea;
	}
	
	public void setProgramArea(int programArea) {
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

	@Override
	public String toString() {
		return "SearchForm [query=" + query + ", field=" + field + ", queryType=" + queryType + ", programArea="
				+ programArea + ", context=" + context + ", classification=" + classification + ", protocol=" + protocol
				+ ", workFlowStatus=" + workFlowStatus + ", registrationStatus=" + registrationStatus + ", conceptName="
				+ conceptName + ", conceptCode=" + conceptCode + "]";
	}
    
	
    
}
