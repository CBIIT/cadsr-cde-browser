package gov.nih.nci.cadsr.service.model.cdeData.classifications;

/**
 * Created by lernermh on 4/22/15.
 */
public class ClassificationsScheneRefernceDocument
{
    private String csLongName;
    private float csVersion;
    private String documentName;
    private String documentType;
    private String documentText;
    private String url;
    private String attachments;

    public String getCsLongName()
    {
        return csLongName;
    }

    public void setCsLongName( String csLongName )
    {
        this.csLongName = csLongName;
    }

    public float getCsVersion()
    {
        return csVersion;
    }

    public void setCsVersion( float csVersion )
    {
        this.csVersion = csVersion;
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

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getAttachments()
    {
        return attachments;
    }

    public void setAttachments( String attachments )
    {
        this.attachments = attachments;
    }
}
