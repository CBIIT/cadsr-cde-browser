package gov.nih.nci.cadsr.service.model.cdeData.usage;

import gov.nih.nci.cadsr.dao.model.UsageModel;

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
    private String formattedVersion;
    private String url;

    public FormUsage(UsageModel usageModel) {
        this.protocolNumber = usageModel.getProtocolNumber();
        this.leadOrg = usageModel.getLeadOrg();
        this.formName = usageModel.getFormName();
        this.questionName = usageModel.getQuestionName();
        this.formUsageType = usageModel.getFormUsageType();
        this.publicId = usageModel.getPublicId();
        this.version = usageModel.getVersion();
        this.formattedVersion = usageModel.getFormattedVersion();
        //FIXME: compose URL from properties/DB Values
        this.url = "https://formbuilder-dev.nci.nih.gov/FormBuilder/formDetailsAction.do?method=getFormDetails&formIdSeq=" + usageModel.getFormIdseq();
    }

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

    public String getFormattedVersion()
    {
        return formattedVersion;
    }

    public void setFormattedVersion( String formattedVersion )
    {
        this.formattedVersion = formattedVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
