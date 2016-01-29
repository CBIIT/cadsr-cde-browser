/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.model;

import gov.nih.nci.cadsr.common.CaDSRConstants;

import java.sql.Timestamp;

public class ClassificationSchemeModel extends BaseModel
{
    private String aslName;
    private Timestamp beginDate;
    private String changeNote;
    private String cmslName;
    private String conteIdseq; // map this foreign key to a ContextModel?
    private String cstlName;
    private String csId;
    private String csIdseq;
    private String deletedInd; // will always be "Yes" or "No"  why isn't this a bool???
    private Timestamp endDate;
    private String labelTypeFlag;  // one byte char ??!
    private String latestVersionInd; // will always be "Yes" or "No"  why isn't this a bool???
    private String longName;
    private String origin;
    private String preferredDefinition;
    private String preferredName;
    private String registrationStatus; // !@#$%$#@!  not in table!  not used?
    private String unresolvedIssue; // !@#$%$#@!  not in table! not used?
    private String version;
    private String workflowStatusDesc; // !@#$%$#@!  not in table!  not used?
    // do we need to map the condr_idseq to a ConceptDerivationRuleModel?
    // do we need to map the par_cs_idesq to the parent ClassificationSchemeModel??


    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public Timestamp getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate( Timestamp beginDate )
    {
        this.beginDate = beginDate;
    }

    public String getChangeNote()
    {
        return changeNote;
    }

    public void setChangeNote( String changeNote )
    {
        this.changeNote = changeNote;
    }

    public String getCmslName()
    {
        return cmslName;
    }

    public void setCmslName( String cmslName )
    {
        this.cmslName = cmslName;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getCstlName()
    {
        return cstlName;
    }

    public void setCstlName( String cstlName )
    {
        this.cstlName = cstlName;
    }

    public String getCsId()
    {
        return csId;
    }

    public void setCsId( String csId )
    {
        this.csId = csId;
    }

    public String getCsIdseq()
    {
        return csIdseq;
    }

    public void setCsIdseq( String csIdseq )
    {
        this.csIdseq = csIdseq;
    }

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd( String deletedInd )
    {
        this.deletedInd = deletedInd;
    }

    public Timestamp getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Timestamp endDate )
    {
        this.endDate = endDate;
    }

    public String getLabelTypeFlag()
    {
        return labelTypeFlag;
    }

    public void setLabelTypeFlag( String labelTypeFlag )
    {
        this.labelTypeFlag = labelTypeFlag;
    }

    public String getLatestVersionInd()
    {
        return latestVersionInd;
    }

    public void setLatestVersionInd( String latestVersionInd )
    {
        this.latestVersionInd = latestVersionInd;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public String getUnresolvedIssue()
    {
        return unresolvedIssue;
    }

    public void setUnresolvedIssue( String unresolvedIssue )
    {
        this.unresolvedIssue = unresolvedIssue;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
        setFormattedVersion( this.version );
    }

    public String getWorkflowStatusDesc()
    {
        return workflowStatusDesc;
    }

    public void setWorkflowStatusDesc( String workflowStatusDesc )
    {
        this.workflowStatusDesc = workflowStatusDesc;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( CaDSRConstants.OBJ_SEPARATOR_START );
        sb.append( "conteIdSeq=" + getConteIdseq() );
        //csIdseq
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "csIdseq=" + getCsIdseq() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "preferredName=" + getPreferredName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "preferredDefinition=" + getPreferredDefinition() );
        sb.append( super.toString() );
        sb.append( CaDSRConstants.OBJ_SEPARATOR_END );
        sb.toString();


        return sb.toString();
    }
}
