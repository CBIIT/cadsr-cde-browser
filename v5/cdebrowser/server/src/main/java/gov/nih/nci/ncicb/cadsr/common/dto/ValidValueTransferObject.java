/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.common.dto;

import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import java.sql.Timestamp;

public class ValidValueTransferObject
{

	/*
    SHORT_MEANING	        VARCHAR2(255)
    DESCRIPTION		        VARCHAR2(2000)
    COMMENTS			    VARCHAR2(2000)
    BEGIN_DATE	        	DATE
    END_DATE			    DATE
    DATE_CREATED		    DATE
    CREATED_BY		        VARCHAR2(30)
    DATE_MODIFIED	        DATE
    MODIFIED_BY		        VARCHAR2(30)
    CONDR_IDSEQ		        CHAR(36)
    VM_ID			        VARCHAR2(255)
    VM_IDSEQ			    CHAR(36)
    VERSION			        NUMBER
    CONTE_IDSEQ		        CHAR(36)
    PREFERRED_NAME	        VARCHAR2(30)
    PREFERRED_DEFINITION	VARCHAR2(2000)
    ASL_NAME	            VARCHAR2(20)
    LONG_NAME		        VARCHAR2(255)
    LATEST_VERSION_IND	    VARCHAR2(3)	No
    DELETED_IND		        VARCHAR2(3)	No
    CHANGE_NOTE		        VARCHAR2(2000)
    ORIGIN			        VARCHAR2(240)
    PUBLIC_ID		        NUMBER
    WORKFLOW_STATUS_DESC    VARCHAR2(2000)
    UNRESOLVED_ISSUE	    VARCHAR2(200)
    REGISTRATION_STATUS	    VARCHAR2(50)
	 */

    protected String shortMeaning;
    protected String description;
    protected String comments;
    protected Timestamp beginDate;
    protected Timestamp endDate;
    protected Timestamp dateCreated;
    protected String createdBy;
    protected Timestamp dateModified;
    protected String modifiedBy;
    protected String condrIdseq;
    protected String vmId;
    protected String vmIdseq;
    protected String version;  //MHL Changed to match table column name
    protected String conteIdseq;
    protected String preferredName;
    protected String preferredDefinition;
    protected String aslName;
    protected String longName;
    protected String latestVersionInd;
    protected String deletedInd;
    protected String changeNote;
    protected String origin;
    protected String publicId;
    protected String workflowStatusDesc;  //MHL Changed to match table column name
    protected String unresolvedIssue;
    protected String registrationStatus;

    protected ConceptDerivationRule conceptDerivationRule = null;
    protected String context;

    public ValidValueTransferObject()
    {
    }

    public String getDescription()
    {
        return description;
    }

    public String getShortMeaning()
    {
        return shortMeaning;
    }

    public void setShortMeaning( String shortMeaning )
    {
        this.shortMeaning = shortMeaning;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getLatestVersionInd()
    {
        return latestVersionInd;
    }

    public void setLatestVersionInd( String latestVersionInd )
    {
        this.latestVersionInd = latestVersionInd;
    }

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd( String deletedInd )
    {
        this.deletedInd = deletedInd;
    }

    public String getChangeNote()
    {
        return changeNote;
    }

    public void setChangeNote( String changeNote )
    {
        this.changeNote = changeNote;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getPublicId()
    {
        return publicId;
    }

    public void setPublicId( String publicId )
    {
        this.publicId = publicId;
    }

    public String getVmIdseq()
    {
        return vmIdseq;
    }

    public void setVmIdseq( String vmIdseq )
    {
        this.vmIdseq = vmIdseq;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String modifiedBy )
    {
        this.modifiedBy = modifiedBy;
    }

    public String getCondrIdseq()
    {
        return condrIdseq;
    }

    public void setCondrIdseq( String condrIdseq )
    {
        this.condrIdseq = condrIdseq;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getUnresolvedIssue()
    {
        return unresolvedIssue;
    }

    public void setUnresolvedIssue( String unresolvedIssue )
    {
        this.unresolvedIssue = unresolvedIssue;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return null;
    }

    /**
     * FIXME - this needs to be updated
     * This equals method only compares the Idseq to define equals
     *
     * @param obj
     * @return
     */
/*
    public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof ValidValue))
			return false;
		ValidValue vv = (ValidValue)obj;

		if(this.getShortMeaningValue().equalsIgnoreCase(vv.getShortMeaningValue()))
			return true;
		else
			return false;
	}
*/
    public String getVmId()
    {
        return vmId;
    }

    public void setVmId( String vmId )
    {
        this.vmId = vmId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getWorkflowStatusDesc()
    {
        return workflowStatusDesc;
    }

    public void setWorkflowStatusDesc( String workflowStatusDesc )
    {
        this.workflowStatusDesc = workflowStatusDesc;
    }

    public Timestamp getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate( Timestamp beginDate )
    {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Timestamp endDate )
    {
        this.endDate = endDate;
    }

    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( Timestamp dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( Timestamp dateModified )
    {
        this.dateModified = dateModified;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public ConceptDerivationRule getConceptDerivationRule()
    {
        return conceptDerivationRule;
    }

    public void setConceptDerivationRule( ConceptDerivationRule conceptDerivationRule )
    {
        this.conceptDerivationRule = conceptDerivationRule;
    }

    @Override
    public String toString()
    {
        String objStr = "ValidValueTransferObject{" + "\n" +
                "context: '" + context + '\'' + ",\n" +
                "shortMeaning: '" + shortMeaning + '\'' + ",\n" +
                "description: '" + description + '\'' + ",\n" +
                "comments: '" + comments + '\'' + ",\n" +
                "beginDate: '" + beginDate + '\'' + ",\n" +
                "endDate: '" + endDate + '\'' + ",\n" +
                "dateCreated: '" + dateCreated + '\'' + ",\n" +
                "dateModified: '" + dateModified + '\'' + ",\n" +
                "createdBy: '" + createdBy + '\'' + ",\n" +
                "modifiedBy: '" + modifiedBy + '\'' + ",\n" +
                "condrIdseq: '" + condrIdseq + '\'' + ",\n" +
                "vmId: " + vmId + ",\n" +
                "vmIdseq: '" + vmIdseq + '\'' + ",\n" +
                "version: " + version + ",\n" +
                "conteIdseq: '" + conteIdseq + '\'' + ",\n" +
                "preferredName: '" + preferredName + '\'' + ",\n" +
                "preferredDefinition: '" + preferredDefinition + '\'' + ",\n" +
                "aslName: '" + aslName + '\'' + ",\n" +
                "longName: '" + longName + '\'' + ",\n" +
                "latestVersionInd: '" + latestVersionInd + '\'' + ",\n" +
                "deletedInd: '" + deletedInd + '\'' + ",\n" +
                "changeNote: '" + changeNote + '\'' + ",\n" +
                "origin: '" + origin + '\'' + ",\n" +
                "publicId: '" + publicId + '\'' + ",\n" +
                "workflowStatusDesc: '" + workflowStatusDesc + '\'' + ",\n" +
                "unresolvedIssue: '" + unresolvedIssue + '\'' + ",\n" +
                "registrationStatus: '" + registrationStatus + '\'' + "\n";

        if( conceptDerivationRule != null )
        {
            objStr += conceptDerivationRule.toString();
        }
        objStr += '}' + "\n\n\n";
        return objStr;
    }
}
