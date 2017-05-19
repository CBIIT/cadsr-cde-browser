package gov.nih.nci.cadsr.service.model.search;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SearchCriteria implements Serializable
{
	private static final long serialVersionUID = -4732600582872432160L;
	private static Logger logger = LogManager.getLogger(SearchCriteria.class.getName());
	
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
	private int publicIdVersion;

	public static final String ALL_REGISRTATION_STATUSES = "ALL Registration Statuses";
	public static final String ALL_WORKFLOW_STATUSES = "ALL Workflow Statuses";
	public static final String ALL_ALTNAME_TYPES = "ALL Alternate Name Types";
	public static final String ALL_FIELDS = "ALL Fields";
	public static final String delimiter= ":::";//this is a separator used by the client part.
	/**
	 * This method takes care of client values received to be adjusted to server component expectations.
	 *
	 */
	public void preprocessCriteria() {
		if (ALL_FIELDS.equals(this.filteredinput))
			this.filteredinput = "ALL";
		if (altNameType != null) {
			if (altNameType.startsWith(ALL_ALTNAME_TYPES)) //use this one since delimiter separator can be added by client
				this.altNameType = "ALL";
		}
		if (ALL_WORKFLOW_STATUSES.equals(this.workFlowStatus))
			this.workFlowStatus = "ALL";
		if (ALL_REGISRTATION_STATUSES.equals(this.registrationStatus))
			this.registrationStatus = "ALL";
		
		//if this is public ID search then we shall use version type selected on public ID search view
		if (StringUtils.isNotEmpty(this.publicId)) {
			int versionTypePublicId = this.publicIdVersion;
			this.versionType = versionTypePublicId;//this is to avoid using this parameter to override publicId selection
		}
		preprocessSearchContext();//CDEBROWSER-801 When selecting the from the navigation tree search versus the drop down does, search results shall be the same: not using Context not PA
	}
	/**
	 * Exclude from search parameters 'context' and 'programArea' 
	 * if a request is received from Search widget with parameters 'classification' or 'protocol' or 'csCsiIdSeq'.
	 * See JIRA CDEBROWSER-801, CDEBROWSER-683.
	 */
	protected void preprocessSearchContext() {
		//CDEBROWSER-801 
		String classificationSearch = this.getClassification();
		String protocolSearch = this.getProtocol();
		String csCsiIdSeq = this.getCsCsiIdSeq();
		String formIdSeq = this.getFormIdSeq();
		
		if ((StringUtils.isNotEmpty(classificationSearch)) || (StringUtils.isNotEmpty(protocolSearch)) 
				|| (StringUtils.isNotEmpty(csCsiIdSeq)) || (StringUtils.isNotEmpty(formIdSeq))) {
			logger.debug("We ignore programArea parameter: " + getProgramArea() + " and context parameter: " + getContext() +" because we search either classification: " + 
							classificationSearch +  ", or protocol: " + protocolSearch + ", or csCsiIdSeq: " + csCsiIdSeq + ", or formIdSeq: " + formIdSeq);
			this.setProgramArea(null);
			this.setContext(null);
		}
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

    public int getPublicIdVersion() {
		return publicIdVersion;
	}
	public void setPublicIdVersion(int publicIdVersion) {
		this.publicIdVersion = publicIdVersion;
	}
	
	@Override
	public String toString() {
		return "SearchCriteria [name=" + name + ", searchMode=" + searchMode + ", publicId=" + publicId + ", queryType="
				+ queryType + ", programArea=" + programArea + ", context=" + context + ", classification="
				+ classification + ", csCsiIdSeq=" + csCsiIdSeq + ", protocol=" + protocol + ", formIdSeq=" + formIdSeq
				+ ", workFlowStatus=" + workFlowStatus + ", registrationStatus=" + registrationStatus + ", conceptName="
				+ conceptName + ", conceptCode=" + conceptCode + ", conceptInput=" + conceptInput
				+ ", conceptQueryType=" + conceptQueryType + ", dataElementConcept=" + dataElementConcept
				+ ", permissibleValue=" + permissibleValue + ", pvQueryType=" + pvQueryType + ", objectClass="
				+ objectClass + ", contextUse=" + contextUse + ", versionType=" + versionType + ", altName=" + altName
				+ ", altNameType=" + altNameType + ", vdTypeFlag=" + vdTypeFlag + ", valueDomain=" + valueDomain
				+ ", filteredinput=" + filteredinput + ", property=" + property + ", derivedDEFlag=" + derivedDEFlag
				+ ", publicIdVersion=" + publicIdVersion + "]";
	}
	
	public String toLogString()
    {
        StringBuilder logBuilderString = new StringBuilder( "SearchCriteria{" );
        if( ( name != null ) && ( !name.isEmpty() ) )
        {
            logBuilderString.append( "name='" + name + "\'," );
        }
        if( ( searchMode != null ) && ( !searchMode.isEmpty() ) )
        {
            logBuilderString.append( "searchMode='" + searchMode + "\'," );
        }
        if( ( publicId != null ) && ( !publicId.isEmpty() ) )
        {
            logBuilderString.append( "publicId='" + publicId + "\'," );
        }
        logBuilderString.append( "publicIdVersion='" + publicIdVersion + "\'," );
        logBuilderString.append( "queryType='" + queryType + "\'," );
        if( ( programArea != null ) && ( !programArea.isEmpty() ) )
        {
            logBuilderString.append( "programArea='" + programArea + "\'," );
        }
        if( ( context != null ) && ( !context.isEmpty() ) )
        {
            logBuilderString.append( "context='" + context + "\'," );
        }
        if( ( classification != null ) && ( !classification.isEmpty() ) )
        {
            logBuilderString.append( "classification='" + classification + "\'," );
        }
        if( ( csCsiIdSeq != null ) && ( !csCsiIdSeq.isEmpty() ) )
        {
            logBuilderString.append( "csCsiIdSeq='" + csCsiIdSeq + "\'," );
        }
        if( ( protocol != null ) && ( !protocol.isEmpty() ) )
        {
            logBuilderString.append( "protocol='" + protocol + "\'," );
        }
        if( ( formIdSeq != null ) && ( !formIdSeq.isEmpty() ) )
        {
            logBuilderString.append( "formIdSeq='" + formIdSeq + "\'," );
        }
        if( ( workFlowStatus != null ) && ( !workFlowStatus.isEmpty() ) )
        {
            logBuilderString.append( "workFlowStatus='" + workFlowStatus + "\'," );
        }
        if( ( registrationStatus != null ) && ( !registrationStatus.isEmpty() ) )
        {
            logBuilderString.append( "registrationStatus='" + registrationStatus + "\'," );
        }
        if( ( conceptName != null ) && ( !conceptName.isEmpty() ) )
        {
            logBuilderString.append( "conceptName='" + conceptName + "\'," );
        }
        if( ( conceptCode != null ) && ( !conceptCode.isEmpty() ) )
        {
            logBuilderString.append( "conceptCode='" + conceptCode + "\'," );
        }
        if( ( conceptInput != null ) && ( !conceptInput.isEmpty() ) )
        {
            logBuilderString.append( "conceptInput='" + conceptInput + "\'," );
        }
        if( ( conceptQueryType != null ) && ( !conceptQueryType.isEmpty() ) )
        {
            logBuilderString.append( "conceptQueryType='" + conceptQueryType + "\'," );
        }
        if( ( dataElementConcept != null ) && ( !dataElementConcept.isEmpty() ) )
        {
            logBuilderString.append( "dataElementConcept='" + dataElementConcept + "\'," );
        }
        if( ( permissibleValue != null ) && ( !permissibleValue.isEmpty() ) )
        {
            logBuilderString.append( "permissibleValue='" + permissibleValue + "\'," );
        }
        logBuilderString.append( "pvQueryType='" + pvQueryType + "\'," );
        if( ( objectClass != null ) && ( !objectClass.isEmpty() ) )
        {
            logBuilderString.append( "objectClass='" + objectClass + "\'," );
        }
        logBuilderString.append( "contextUse='" + contextUse + "\'," );
        logBuilderString.append( "versionType='" + versionType + "\'," );
        if( ( altName != null ) && ( !altName.isEmpty() ) )
        {
            logBuilderString.append( "altName='" + altName + "\'," );
        }
        if( ( altNameType != null ) && ( !altNameType.isEmpty() ) )
        {
            logBuilderString.append( "altNameType='" + altNameType + "\'," );
        }
        if( ( vdTypeFlag != null ) && ( !vdTypeFlag.isEmpty() ) )
        {
            logBuilderString.append( "vdTypeFlag='" + vdTypeFlag + "\'," );
        }
        if( ( valueDomain != null ) && ( !valueDomain.isEmpty() ) )
        {
            logBuilderString.append( "valueDomain='" + valueDomain + "\'," );
        }
        if( ( filteredinput != null ) && ( !filteredinput.isEmpty() ) )
        {
            logBuilderString.append( "filteredinput='" + filteredinput + "\'," );
        }
        if( ( property != null ) && ( !property.isEmpty() ) )
        {
            logBuilderString.append( "property='" + property + "\'," );
        }
        if( ( derivedDEFlag != null ) && ( !derivedDEFlag.isEmpty() ) )
        {
            logBuilderString.append( "derivedDEFlag='" + derivedDEFlag + "\'," );
        }
        return  logBuilderString.toString().replaceFirst( ",$", "}" );
    }

}
