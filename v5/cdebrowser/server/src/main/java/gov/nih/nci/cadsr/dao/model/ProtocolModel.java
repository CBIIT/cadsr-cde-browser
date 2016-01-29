package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

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

    public ProtocolModel()
    {
    }

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
        setFormattedVersion( version );
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

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof ProtocolModel ) ) return false;

        ProtocolModel that = ( ProtocolModel ) o;

        if( getProtoIdseq() != null ? !getProtoIdseq().equals( that.getProtoIdseq() ) : that.getProtoIdseq() != null )
            return false;
        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getPreferredName() != null ? !getPreferredName().equals( that.getPreferredName() ) : that.getPreferredName() != null )
            return false;
        if( getConteIdseq() != null ? !getConteIdseq().equals( that.getConteIdseq() ) : that.getConteIdseq() != null )
            return false;
        if( getPreferredDefinition() != null ? !getPreferredDefinition().equals( that.getPreferredDefinition() ) : that.getPreferredDefinition() != null )
            return false;
        if( getAslName() != null ? !getAslName().equals( that.getAslName() ) : that.getAslName() != null ) return false;
        if( getLongName() != null ? !getLongName().equals( that.getLongName() ) : that.getLongName() != null )
            return false;
        if( getLatestVersionInd() != null ? !getLatestVersionInd().equals( that.getLatestVersionInd() ) : that.getLatestVersionInd() != null )
            return false;
        if( getDeletedInd() != null ? !getDeletedInd().equals( that.getDeletedInd() ) : that.getDeletedInd() != null )
            return false;
        if( getBeginDate() != null ? !getBeginDate().equals( that.getBeginDate() ) : that.getBeginDate() != null )
            return false;
        if( getEndDate() != null ? !getEndDate().equals( that.getEndDate() ) : that.getEndDate() != null ) return false;
        if( getProtocolId() != null ? !getProtocolId().equals( that.getProtocolId() ) : that.getProtocolId() != null )
            return false;
        if( getType() != null ? !getType().equals( that.getType() ) : that.getType() != null ) return false;
        if( getPhase() != null ? !getPhase().equals( that.getPhase() ) : that.getPhase() != null ) return false;
        if( getLeadOrg() != null ? !getLeadOrg().equals( that.getLeadOrg() ) : that.getLeadOrg() != null ) return false;
        if( getChangeType() != null ? !getChangeType().equals( that.getChangeType() ) : that.getChangeType() != null )
            return false;
        if( getChangeNumber() != null ? !getChangeNumber().equals( that.getChangeNumber() ) : that.getChangeNumber() != null )
            return false;
        if( getReviewedDate() != null ? !getReviewedDate().equals( that.getReviewedDate() ) : that.getReviewedDate() != null )
            return false;
        if( getReviewedBy() != null ? !getReviewedBy().equals( that.getReviewedBy() ) : that.getReviewedBy() != null )
            return false;
        if( getApprovedDate() != null ? !getApprovedDate().equals( that.getApprovedDate() ) : that.getApprovedDate() != null )
            return false;
        if( getApprovedBy() != null ? !getApprovedBy().equals( that.getApprovedBy() ) : that.getApprovedBy() != null )
            return false;
        if( getChangeNote() != null ? !getChangeNote().equals( that.getChangeNote() ) : that.getChangeNote() != null )
            return false;
        if( getOrigin() != null ? !getOrigin().equals( that.getOrigin() ) : that.getOrigin() != null ) return false;
        return !( getProtoId() != null ? !getProtoId().equals( that.getProtoId() ) : that.getProtoId() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getProtoIdseq() != null ? getProtoIdseq().hashCode() : 0;
        result = 31 * result + ( getVersion() != null ? getVersion().hashCode() : 0 );
        result = 31 * result + ( getPreferredName() != null ? getPreferredName().hashCode() : 0 );
        result = 31 * result + ( getConteIdseq() != null ? getConteIdseq().hashCode() : 0 );
        result = 31 * result + ( getPreferredDefinition() != null ? getPreferredDefinition().hashCode() : 0 );
        result = 31 * result + ( getAslName() != null ? getAslName().hashCode() : 0 );
        result = 31 * result + ( getLongName() != null ? getLongName().hashCode() : 0 );
        result = 31 * result + ( getLatestVersionInd() != null ? getLatestVersionInd().hashCode() : 0 );
        result = 31 * result + ( getDeletedInd() != null ? getDeletedInd().hashCode() : 0 );
        result = 31 * result + ( getBeginDate() != null ? getBeginDate().hashCode() : 0 );
        result = 31 * result + ( getEndDate() != null ? getEndDate().hashCode() : 0 );
        result = 31 * result + ( getProtocolId() != null ? getProtocolId().hashCode() : 0 );
        result = 31 * result + ( getType() != null ? getType().hashCode() : 0 );
        result = 31 * result + ( getPhase() != null ? getPhase().hashCode() : 0 );
        result = 31 * result + ( getLeadOrg() != null ? getLeadOrg().hashCode() : 0 );
        result = 31 * result + ( getChangeType() != null ? getChangeType().hashCode() : 0 );
        result = 31 * result + ( getChangeNumber() != null ? getChangeNumber().hashCode() : 0 );
        result = 31 * result + ( getReviewedDate() != null ? getReviewedDate().hashCode() : 0 );
        result = 31 * result + ( getReviewedBy() != null ? getReviewedBy().hashCode() : 0 );
        result = 31 * result + ( getApprovedDate() != null ? getApprovedDate().hashCode() : 0 );
        result = 31 * result + ( getApprovedBy() != null ? getApprovedBy().hashCode() : 0 );
        result = 31 * result + ( getChangeNote() != null ? getChangeNote().hashCode() : 0 );
        result = 31 * result + ( getOrigin() != null ? getOrigin().hashCode() : 0 );
        result = 31 * result + ( getProtoId() != null ? getProtoId().hashCode() : 0 );
        return result;
    }
}
