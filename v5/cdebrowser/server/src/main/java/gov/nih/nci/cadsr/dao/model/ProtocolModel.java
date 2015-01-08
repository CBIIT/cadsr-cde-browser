/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.model;

import gov.nih.nci.cadsr.common.CaDSRConstants;

public class ProtocolModel extends BaseModel
{
    private String protoIdseq;
    private String version;
    private String preferredName;
    private String conteIdseq;
    private String preferredDefinition;
    private String aslName;
    private String longName;
    private String latestVersionInd;
    private String deletedInd;
    private String beginDate;
    private String endDate;
    private String protocolId;
    private String type;
    private String phase;
    private String leadOrg;
    private String changeType;
    private String changeNumber;
    private String reviewedDate;
    private String reviewedBy;
    private String approvedDate;
    private String approvedBy;
    private String changeNote;
    private String origin;
    private String protoId;

    public String getProtoIdseq()
    {
        return protoIdseq;
    }

    public void setProtoIdseq( String protoIdseq )
    {
        this.protoIdseq = protoIdseq;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
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

    public String getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate( String beginDate )
    {
        this.beginDate = beginDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

    public String getProtocolId()
    {
        return protocolId;
    }

    public void setProtocolId( String protocolId )
    {
        this.protocolId = protocolId;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getPhase()
    {
        return phase;
    }

    public void setPhase( String phase )
    {
        this.phase = phase;
    }

    public String getLeadOrg()
    {
        return leadOrg;
    }

    public void setLeadOrg( String leadOrg )
    {
        this.leadOrg = leadOrg;
    }

    public String getChangeType()
    {
        return changeType;
    }

    public void setChangeType( String changeType )
    {
        this.changeType = changeType;
    }

    public String getChangeNumber()
    {
        return changeNumber;
    }

    public void setChangeNumber( String changeNumber )
    {
        this.changeNumber = changeNumber;
    }

    public String getReviewedDate()
    {
        return reviewedDate;
    }

    public void setReviewedDate( String reviewedDate )
    {
        this.reviewedDate = reviewedDate;
    }

    public String getReviewedBy()
    {
        return reviewedBy;
    }

    public void setReviewedBy( String reviewedBy )
    {
        this.reviewedBy = reviewedBy;
    }

    public String getApprovedDate()
    {
        return approvedDate;
    }

    public void setApprovedDate( String approvedDate )
    {
        this.approvedDate = approvedDate;
    }

    public String getApprovedBy()
    {
        return approvedBy;
    }

    public void setApprovedBy( String approvedBy )
    {
        this.approvedBy = approvedBy;
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

    public String getProtoId()
    {
        return protoId;
    }

    public void setProtoId( String protoId )
    {
        this.protoId = protoId;
    }


    public String toString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "protoIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + protoIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "version " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + version + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "preferredName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + preferredName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "conteIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + conteIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "preferredDefinition " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + preferredDefinition + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "aslName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + aslName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "longName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + longName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "latestVersionInd " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + latestVersionInd + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "deletedInd " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + deletedInd + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "beginDate " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + beginDate + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "endDate " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + endDate + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "protocolId " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + protocolId + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "type " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + type + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "phase " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + phase + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "leadOrg " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + leadOrg + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "changeType " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + changeType + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "reviewedDate " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + reviewedDate + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "reviewedBy " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + reviewedBy + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "approvedDate " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + approvedDate + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "approvedBy " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + approvedBy + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "changeNote " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + changeNote + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "origin " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + origin + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "protoId " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + protoId + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        super.toString();

        return stringBuffer.toString();
    }

}
