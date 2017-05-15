package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.commons.lang3.StringUtils;

public class ReferenceDocument implements Comparable 
{
    private String documentName;
    private String documentType;
    private String documentText;
    private String context;
    private String url;

    public ReferenceDocument()
    {
    }

    public String getDocumentName()
    {
        return documentName;
    }

    public void setDocumentName( String documentName )
    {
        this.documentName = documentName;
    }

    public String getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType( String documentType )
    {
        this.documentType = documentType;
    }

    public String getDocumentText()
    {
        return documentText;
    }

    public void setDocumentText( String documentText )
    {
        this.documentText = documentText;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
		result = prime * result + ((documentText == null) ? 0 : documentText.hashCode());
		result = prime * result + ((documentType == null) ? 0 : documentType.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		ReferenceDocument other = (ReferenceDocument) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (documentName == null) {
			if (other.documentName != null)
				return false;
		} else if (!documentName.equals(other.documentName))
			return false;
		if (documentText == null) {
			if (other.documentText != null)
				return false;
		} else if (!documentText.equals(other.documentText))
			return false;
		if (documentType == null) {
			if (other.documentType != null)
				return false;
		} else if (!documentType.equals(other.documentType))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object other) {
		//CDEBROWSER-809 arrange Question Text in a specific order
		if (other instanceof ReferenceDocument) {
			ReferenceDocument that = (ReferenceDocument)other;
			//to avoid null pointer for the values which are never null in our DB
			//String class compare methods through exceptions on null parameter
			String thisName = (this.documentName != null) ? this.documentName : "";
			String thisDocType = (this.documentType != null) ? this.documentType : "";
			String thatName = (that.documentName != null) ? that.documentName : "";
			String thatDocType = (that.documentType != null) ? that.documentType : "";
			//Preferred Question Text goes first, then "Alternate Question Text", everything else is alphabetized
			if (("Preferred Question Text".equals(thisDocType)) && (!("Preferred Question Text".equals(that.getDocumentType())))) {
				return -1;
			}
			else if ((!("Preferred Question Text".equals(thisDocType))) && ("Preferred Question Text".equals(that.getDocumentType()))) {
				return 1;
			}
			else if (("Preferred Question Text".equals(thisDocType)) && ("Preferred Question Text".equals(that.getDocumentType()))) {
				return (thisName.compareToIgnoreCase(thatName));
			}
			else if (("Alternate Question Text".equals(thisDocType)) && (!("Alternate Question Text".equals(that.getDocumentType())))) {
				return -1;
			}
			else if ((!("Alternate Question Text".equals(thisDocType))) && ("Alternate Question Text".equals(that.getDocumentType()))) {
				return 1;
			}
			else if (("Alternate Question Text".equals(thisDocType)) && ("Alternate Question Text".equals(that.getDocumentType()))) {
				return (thisName.compareToIgnoreCase(thatName));
			}
			else {
				if (thisDocType.equals(thatDocType)) {
					return (thisName.compareToIgnoreCase(thatName));
				}
				else {
					return thisDocType.compareToIgnoreCase(thatDocType);
				}
			}
		}
		else {
			return -1;
		}
	}
}
