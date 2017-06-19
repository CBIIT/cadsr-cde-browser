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
	public static final String UNDEFINED_STATUS_FROM_UI = "undefined";//somehow UI sends this status instead of null by times
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
		if ((ALL_WORKFLOW_STATUSES.equals(this.workFlowStatus)) || (UNDEFINED_STATUS_FROM_UI.equals(this.workFlowStatus)))
			this.workFlowStatus = "ALL";
		if ((ALL_REGISRTATION_STATUSES.equals(this.registrationStatus)) || (UNDEFINED_STATUS_FROM_UI.equals(this.registrationStatus)))
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
	
	public static boolean isVdTypeFlagValid(String vdTypeFlag) {
		return ("E".equals(vdTypeFlag) || "N".equals(vdTypeFlag) || StringUtils.isBlank(vdTypeFlag));
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altName == null) ? 0 : altName.hashCode());
		result = prime * result + ((altNameType == null) ? 0 : altNameType.hashCode());
		result = prime * result + ((classification == null) ? 0 : classification.hashCode());
		result = prime * result + ((conceptCode == null) ? 0 : conceptCode.hashCode());
		result = prime * result + ((conceptInput == null) ? 0 : conceptInput.hashCode());
		result = prime * result + ((conceptName == null) ? 0 : conceptName.hashCode());
		result = prime * result + ((conceptQueryType == null) ? 0 : conceptQueryType.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + contextUse;
		result = prime * result + ((csCsiIdSeq == null) ? 0 : csCsiIdSeq.hashCode());
		result = prime * result + ((dataElementConcept == null) ? 0 : dataElementConcept.hashCode());
		result = prime * result + ((derivedDEFlag == null) ? 0 : derivedDEFlag.hashCode());
		result = prime * result + ((filteredinput == null) ? 0 : filteredinput.hashCode());
		result = prime * result + ((formIdSeq == null) ? 0 : formIdSeq.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((objectClass == null) ? 0 : objectClass.hashCode());
		result = prime * result + ((permissibleValue == null) ? 0 : permissibleValue.hashCode());
		result = prime * result + ((programArea == null) ? 0 : programArea.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result + ((publicId == null) ? 0 : publicId.hashCode());
		result = prime * result + publicIdVersion;
		result = prime * result + pvQueryType;
		result = prime * result + queryType;
		result = prime * result + ((registrationStatus == null) ? 0 : registrationStatus.hashCode());
		result = prime * result + ((searchMode == null) ? 0 : searchMode.hashCode());
		result = prime * result + ((valueDomain == null) ? 0 : valueDomain.hashCode());
		result = prime * result + ((vdTypeFlag == null) ? 0 : vdTypeFlag.hashCode());
		result = prime * result + versionType;
		result = prime * result + ((workFlowStatus == null) ? 0 : workFlowStatus.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchCriteria other = (SearchCriteria) obj;
		if (altName == null) {
			if (other.altName != null)
				return false;
		} else if (!altName.equals(other.altName))
			return false;
		if (altNameType == null) {
			if (other.altNameType != null)
				return false;
		} else if (!altNameType.equals(other.altNameType))
			return false;
		if (classification == null) {
			if (other.classification != null)
				return false;
		} else if (!classification.equals(other.classification))
			return false;
		if (conceptCode == null) {
			if (other.conceptCode != null)
				return false;
		} else if (!conceptCode.equals(other.conceptCode))
			return false;
		if (conceptInput == null) {
			if (other.conceptInput != null)
				return false;
		} else if (!conceptInput.equals(other.conceptInput))
			return false;
		if (conceptName == null) {
			if (other.conceptName != null)
				return false;
		} else if (!conceptName.equals(other.conceptName))
			return false;
		if (conceptQueryType == null) {
			if (other.conceptQueryType != null)
				return false;
		} else if (!conceptQueryType.equals(other.conceptQueryType))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (contextUse != other.contextUse)
			return false;
		if (csCsiIdSeq == null) {
			if (other.csCsiIdSeq != null)
				return false;
		} else if (!csCsiIdSeq.equals(other.csCsiIdSeq))
			return false;
		if (dataElementConcept == null) {
			if (other.dataElementConcept != null)
				return false;
		} else if (!dataElementConcept.equals(other.dataElementConcept))
			return false;
		if (derivedDEFlag == null) {
			if (other.derivedDEFlag != null)
				return false;
		} else if (!derivedDEFlag.equals(other.derivedDEFlag))
			return false;
		if (filteredinput == null) {
			if (other.filteredinput != null)
				return false;
		} else if (!filteredinput.equals(other.filteredinput))
			return false;
		if (formIdSeq == null) {
			if (other.formIdSeq != null)
				return false;
		} else if (!formIdSeq.equals(other.formIdSeq))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objectClass == null) {
			if (other.objectClass != null)
				return false;
		} else if (!objectClass.equals(other.objectClass))
			return false;
		if (permissibleValue == null) {
			if (other.permissibleValue != null)
				return false;
		} else if (!permissibleValue.equals(other.permissibleValue))
			return false;
		if (programArea == null) {
			if (other.programArea != null)
				return false;
		} else if (!programArea.equals(other.programArea))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (publicId == null) {
			if (other.publicId != null)
				return false;
		} else if (!publicId.equals(other.publicId))
			return false;
		if (publicIdVersion != other.publicIdVersion)
			return false;
		if (pvQueryType != other.pvQueryType)
			return false;
		if (queryType != other.queryType)
			return false;
		if (registrationStatus == null) {
			if (other.registrationStatus != null)
				return false;
		} else if (!registrationStatus.equals(other.registrationStatus))
			return false;
		if (searchMode == null) {
			if (other.searchMode != null)
				return false;
		} else if (!searchMode.equals(other.searchMode))
			return false;
		if (valueDomain == null) {
			if (other.valueDomain != null)
				return false;
		} else if (!valueDomain.equals(other.valueDomain))
			return false;
		if (vdTypeFlag == null) {
			if (other.vdTypeFlag != null)
				return false;
		} else if (!vdTypeFlag.equals(other.vdTypeFlag))
			return false;
		if (versionType != other.versionType)
			return false;
		if (workFlowStatus == null) {
			if (other.workFlowStatus != null)
				return false;
		} else if (!workFlowStatus.equals(other.workFlowStatus))
			return false;
		return true;
	}

}
