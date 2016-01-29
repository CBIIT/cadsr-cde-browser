package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


/**
 * Represents the data found by joining a data element
 * to a classification scheme (via the ac_csi table to the cs_csi table)
 * and then taking the cs_idseq from cs_csi and finding it in the
 * reference_documents table's ac_idseq field
 * <p/>
 * Only a few of a data element's classification schemes will have
 * reference doc entries for them.
 * <p/>
 * Because only a few of the related classification scheme and reference document fields
 * are displayed in CDE Browser, the members of this class are only a subset of the possible fields
 * <p/>
 * Do not confuse this with the CSIRefDocModel for classification scheme items' reference documents
 */
public class CSRefDocModel extends BaseModel
{
    public String csLongName;
    public Float csVersion;
    private String formattedCsVersion;
    public String documentName;
    public String documentType;
    public String DocumentText;
    public String url;
    public String attachments;

    public CSRefDocModel()
    {
    }

    public String getCsLongName()
    {
        return csLongName;
    }

    public void setCsLongName( String csLongName )
    {
        this.csLongName = csLongName;
    }

    public Float getCsVersion()
    {
        return csVersion;
    }

    public void setCsVersion( Float csVersion )
    {
        this.csVersion = csVersion;
        this.formattedCsVersion = Float.toString( Float.valueOf( csVersion ) );
    }

    public String getFormattedCsVersion()
    {
        return formattedCsVersion;
    }

    public void setFormattedCsVersion( String formattedCsVersion )
    {
        this.formattedCsVersion = formattedCsVersion;
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
        return DocumentText;
    }

    public void setDocumentText( String documentText )
    {
        DocumentText = documentText;
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
        if( !( o instanceof CSRefDocModel ) ) return false;

        CSRefDocModel that = ( CSRefDocModel ) o;

        if( getCsLongName() != null ? !getCsLongName().equals( that.getCsLongName() ) : that.getCsLongName() != null )
            return false;
        if( getCsVersion() != null ? !getCsVersion().equals( that.getCsVersion() ) : that.getCsVersion() != null )
            return false;
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
        int result = getCsLongName() != null ? getCsLongName().hashCode() : 0;
        result = 31 * result + ( getCsVersion() != null ? getCsVersion().hashCode() : 0 );
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
        return "CSRefDocModel{" +
                "csLongName='" + csLongName + '\'' +
                ", csVersion=" + csVersion +
                ", documentName='" + documentName + '\'' +
                ", documentType='" + documentType + '\'' +
                ", DocumentText='" + DocumentText + '\'' +
                ", url='" + url + '\'' +
                ", attachments='" + attachments + '\'' +
                '}';
    }
}
