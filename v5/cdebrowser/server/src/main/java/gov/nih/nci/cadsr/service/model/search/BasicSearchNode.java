package gov.nih.nci.cadsr.service.model.search;

import gov.nih.nci.cadsr.service.model.context.BaseNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicSearchNode
{
    private Logger logger = LogManager.getLogger( BasicSearchNode.class.getName() );
    private String longName;
    private String preferredQuestionText;
    private String ownedBy;
    private Integer publicId;
    private String workflowStatus;
    private String version;
    private String usedByContext;
    private String registrationStatus;
    private String href = "";
    private String deIdseq;
    // Status will tell the client if there was an error, if so message will be in text field.
    private int status;

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

    public Integer getPublicId()
    {
        return publicId;
    }

    public void setPublicId( Integer publicId )
    {
        this.publicId = publicId;
    }

    public void setPublicId( String publicId )
    {
        this.publicId = Integer.valueOf(publicId);
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

    public String getHref()
    {
        return href;
    }

    public void setHref( String href )
    {
        this.href = href;
    }

    public String getDeIdseq()
    {
        return deIdseq;
    }

    public void setDeIdseq( String deIdseq )
    {
        this.deIdseq = deIdseq;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus( int status )
    {
        this.status = status;
    }
}
