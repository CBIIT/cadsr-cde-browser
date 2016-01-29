package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

/**
 * Represents the data found by joining a data element
 * to a CLASSIFICATION SCHEME ITEM (via the ac_csi table to the cs_csi table)
 * and then taking the csi_idseq from cs_csi and finding it in the
 * reference_documents table's ac_idseq field
 * <p/>
 * Only a few of a data element's classification scheme items will have
 * reference doc entries for them.
 * <p/>
 * Because only a few of the related classification scheme item and reference document fields
 * are displayed in CDE Browser, the members of this class are only a subset of the possible fields
 * <p/>
 * Do not confuse this with the CSRefDocModel for classification schemes' reference documents
 */
public class CSIRefDocModel extends BaseModel
{
    public String csiName;
    public String documentName;
    public String documentType;
    public String documentText;
    public String url;
    public String attachments;

    public CSIRefDocModel()
    {
    }

    public String getCsiName()
    {
        return csiName;
    }

    public void setCsiName( String csiName )
    {
        this.csiName = csiName;
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

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof CSIRefDocModel ) ) return false;

        CSIRefDocModel that = ( CSIRefDocModel ) o;

        if( getCsiName() != null ? !getCsiName().equals( that.getCsiName() ) : that.getCsiName() != null ) return false;
        if( getDocumentName() != null ? !getDocumentName().equals( that.getDocumentName() ) : that.getDocumentName() != null )
            return false;
        if( getDocumentType() != null ? !getDocumentType().equals( that.getDocumentType() ) : that.getDocumentType() != null )
            return false;
        if( getDocumentText() != null ? !getDocumentText().equals( that.getDocumentText() ) : that.getDocumentText() != null )
            return false;
        if( getUrl() != null ? !getUrl().equals( that.getUrl() ) : that.getUrl() != null ) return false;
        return !( getAttachments() != null ? !getAttachments().equals( that.getAttachments() ) : that.getAttachments() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getCsiName() != null ? getCsiName().hashCode() : 0;
        result = 31 * result + ( getDocumentName() != null ? getDocumentName().hashCode() : 0 );
        result = 31 * result + ( getDocumentType() != null ? getDocumentType().hashCode() : 0 );
        result = 31 * result + ( getDocumentText() != null ? getDocumentText().hashCode() : 0 );
        result = 31 * result + ( getUrl() != null ? getUrl().hashCode() : 0 );
        result = 31 * result + ( getAttachments() != null ? getAttachments().hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "CSIRefDocModel{" +
                "csiName='" + csiName + '\'' +
                ", documentName='" + documentName + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentText='" + documentText + '\'' +
                ", url='" + url + '\'' +
                ", attachments='" + attachments + '\'' +
                '}';
    }
}
