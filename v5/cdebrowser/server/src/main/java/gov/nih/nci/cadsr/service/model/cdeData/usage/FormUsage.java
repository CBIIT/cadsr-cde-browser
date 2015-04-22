package gov.nih.nci.cadsr.service.model.cdeData.usage;

/**
 * Created by lernermh on 4/22/15.
 */
public class FormUsage
{
    private String protocolNumber ;
    private String leadOrg ;
    private String formName ;
    private String questionName ;
    private String formUsageType ;
    private int publicId;
    private float version;

    public String getProtocolNumber()
    {
        return protocolNumber;
    }

    public void setProtocolNumber( String protocolNumber )
    {
        this.protocolNumber = protocolNumber;
    }

    public String getLeadOrg()
    {
        return leadOrg;
    }

    public void setLeadOrg( String leadOrg )
    {
        this.leadOrg = leadOrg;
    }

    public String getFormName()
    {
        return formName;
    }

    public void setFormName( String formName )
    {
        this.formName = formName;
    }

    public String getQuestionName()
    {
        return questionName;
    }

    public void setQuestionName( String questionName )
    {
        this.questionName = questionName;
    }

    public String getFormUsageType()
    {
        return formUsageType;
    }

    public void setFormUsageType( String formUsageType )
    {
        this.formUsageType = formUsageType;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public float getVersion()
    {
        return version;
    }

    public void setVersion( float version )
    {
        this.version = version;
    }
}
