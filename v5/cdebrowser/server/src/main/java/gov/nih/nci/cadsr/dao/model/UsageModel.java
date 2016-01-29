package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class UsageModel extends BaseModel
{
    private String questionName;
    private Float version;
    private String formName;
    private Integer publicId;
    private String leadOrg;
    private String protocolNumber;
    private String formUsageType;
    private String formIdseq;

    public UsageModel()
    {
    }

    public String getQuestionName()
    {
        return questionName;
    }

    public void setQuestionName( String questionName )
    {
        this.questionName = questionName;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
        setFormattedVersion( version );
    }

    public String getFormName()
    {
        return formName;
    }

    public void setFormName( String formName )
    {
        this.formName = formName;
    }

    public Integer getPublicId()
    {
        return publicId;
    }

    public void setPublicId( Integer publicId )
    {
        this.publicId = publicId;
    }

    public String getLeadOrg()
    {
        return leadOrg;
    }

    public void setLeadOrg( String leadOrg )
    {
        this.leadOrg = leadOrg;
    }

    public String getProtocolNumber()
    {
        return protocolNumber;
    }

    public void setProtocolNumber( String protocolNumber )
    {
        this.protocolNumber = protocolNumber;
    }

    public String getFormUsageType()
    {
        return formUsageType;
    }

    public void setFormUsageType( String formUsageType )
    {
        this.formUsageType = formUsageType;
    }

    public String getFormIdseq()
    {
        return formIdseq;
    }

    public void setFormIdseq( String formIdseq )
    {
        this.formIdseq = formIdseq;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof UsageModel ) ) return false;

        UsageModel that = ( UsageModel ) o;

        if( getQuestionName() != null ? !getQuestionName().equals( that.getQuestionName() ) : that.getQuestionName() != null )
            return false;
        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getFormName() != null ? !getFormName().equals( that.getFormName() ) : that.getFormName() != null )
            return false;
        if( getPublicId() != null ? !getPublicId().equals( that.getPublicId() ) : that.getPublicId() != null )
            return false;
        if( getLeadOrg() != null ? !getLeadOrg().equals( that.getLeadOrg() ) : that.getLeadOrg() != null ) return false;
        if( getProtocolNumber() != null ? !getProtocolNumber().equals( that.getProtocolNumber() ) : that.getProtocolNumber() != null )
            return false;
        if( getFormUsageType() != null ? !getFormUsageType().equals( that.getFormUsageType() ) : that.getFormUsageType() != null )
            return false;
        return !( getFormIdseq() != null ? !getFormIdseq().equals( that.getFormIdseq() ) : that.getFormIdseq() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getQuestionName() != null ? getQuestionName().hashCode() : 0;
        result = 31 * result + ( getVersion() != null ? getVersion().hashCode() : 0 );
        result = 31 * result + ( getFormName() != null ? getFormName().hashCode() : 0 );
        result = 31 * result + ( getPublicId() != null ? getPublicId().hashCode() : 0 );
        result = 31 * result + ( getLeadOrg() != null ? getLeadOrg().hashCode() : 0 );
        result = 31 * result + ( getProtocolNumber() != null ? getProtocolNumber().hashCode() : 0 );
        result = 31 * result + ( getFormUsageType() != null ? getFormUsageType().hashCode() : 0 );
        result = 31 * result + ( getFormIdseq() != null ? getFormIdseq().hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "UsageModel{" +
                "questionName='" + questionName + '\'' +
                ", version=" + version +
                ", formName='" + formName + '\'' +
                ", publicId=" + publicId +
                ", leadOrg='" + leadOrg + '\'' +
                ", protocolNumber='" + protocolNumber + '\'' +
                ", formUsageType='" + formUsageType + '\'' +
                ", formIdseq='" + formIdseq + '\'' +
                '}';
    }
}
