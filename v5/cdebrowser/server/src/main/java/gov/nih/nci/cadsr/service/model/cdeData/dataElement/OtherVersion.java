package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

public class OtherVersion
{
    private float version;
    private String longName;
    private String workFlowStatus;
    private String registrationStatus;
    private String context;

    public float getVersion()
    {
        return version;
    }

    public void setVersion( float version )
    {
        this.version = version;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getWorkFlowStatus()
    {
        return workFlowStatus;
    }

    public void setWorkFlowStatus( String workFlowStatus )
    {
        this.workFlowStatus = workFlowStatus;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }
}
