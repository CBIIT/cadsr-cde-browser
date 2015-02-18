package gov.nih.nci.cadsr.service.model.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicSearchNode
{
    private Logger logger = LogManager.getLogger( BasicSearchNode.class.getName() );
    String longName;
    String preferredQuestionText;
    String ownedBy;
    String publicId;
    String workflowStatus;
    String version;
    String usedByContext;
    String registrationStatus;

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getPreferredQuestionText()
    {
        return preferredQuestionText;
    }

    public void setPreferredQuestionText( String preferredQuestionText )
    {
        this.preferredQuestionText = preferredQuestionText;
    }

    public String getOwnedBy()
    {
        return ownedBy;
    }

    public void setOwnedBy( String ownedBy )
    {
        this.ownedBy = ownedBy;
    }

    public String getPublicId()
    {
        return publicId;
    }

    public void setPublicId( String publicId )
    {
        this.publicId = publicId;
    }

    public String getWorkflowStatus()
    {
        return workflowStatus;
    }

    public void setWorkflowStatus( String workflowStatus )
    {
        this.workflowStatus = workflowStatus;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getUsedByContext()
    {
        return usedByContext;
    }

    public void setUsedByContext( String usedByContext )
    {
        this.usedByContext = usedByContext;
    }

    public String getRegistrationStatus()
    {

        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }
}
