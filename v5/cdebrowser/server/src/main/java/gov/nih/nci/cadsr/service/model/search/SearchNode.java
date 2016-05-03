package gov.nih.nci.cadsr.service.model.search;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchNode
{
    private static final Logger logger = LogManager.getLogger( SearchNode.class.getName() );
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
    // Status will tell the client if there was an error, if so message will be in text field.- this may no longer be true.
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
        this.publicId = Integer.valueOf( publicId );
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

	@Override
	public String toString() {
		return "SearchNode [logger=" + logger + ", longName=" + longName + ", preferredQuestionText="
				+ preferredQuestionText + ", ownedBy=" + ownedBy + ", publicId=" + publicId + ", workflowStatus="
				+ workflowStatus + ", version=" + version + ", usedByContext=" + usedByContext + ", registrationStatus="
				+ registrationStatus + ", href=" + href + ", deIdseq=" + deIdseq + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deIdseq == null) ? 0 : deIdseq.hashCode());
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((ownedBy == null) ? 0 : ownedBy.hashCode());
		result = prime * result + ((preferredQuestionText == null) ? 0 : preferredQuestionText.hashCode());
		result = prime * result + ((publicId == null) ? 0 : publicId.hashCode());
		result = prime * result + ((registrationStatus == null) ? 0 : registrationStatus.hashCode());
		result = prime * result + status;
		result = prime * result + ((usedByContext == null) ? 0 : usedByContext.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((workflowStatus == null) ? 0 : workflowStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchNode other = (SearchNode) obj;
		if (deIdseq == null) {
			if (other.deIdseq != null)
				return false;
		} else if (!deIdseq.equals(other.deIdseq))
			return false;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		if (longName == null) {
			if (other.longName != null)
				return false;
		} else if (!longName.equals(other.longName))
			return false;
		if (ownedBy == null) {
			if (other.ownedBy != null)
				return false;
		} else if (!ownedBy.equals(other.ownedBy))
			return false;
		if (preferredQuestionText == null) {
			if (other.preferredQuestionText != null)
				return false;
		} else if (!preferredQuestionText.equals(other.preferredQuestionText))
			return false;
		if (publicId == null) {
			if (other.publicId != null)
				return false;
		} else if (!publicId.equals(other.publicId))
			return false;
		if (registrationStatus == null) {
			if (other.registrationStatus != null)
				return false;
		} else if (!registrationStatus.equals(other.registrationStatus))
			return false;
		if (status != other.status)
			return false;
		if (usedByContext == null) {
			if (other.usedByContext != null)
				return false;
		} else if (!usedByContext.equals(other.usedByContext))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (workflowStatus == null) {
			if (other.workflowStatus != null)
				return false;
		} else if (!workflowStatus.equals(other.workflowStatus))
			return false;
		return true;
	}

}
