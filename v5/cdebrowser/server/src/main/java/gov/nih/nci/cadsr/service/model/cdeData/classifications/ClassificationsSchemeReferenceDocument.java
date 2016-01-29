package gov.nih.nci.cadsr.service.model.cdeData.classifications;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.CSRefDocModel;

public class ClassificationsSchemeReferenceDocument
{
    private String csLongName;
    private float csVersion;
    private String formattedCsVersion;
    private String documentName;
    private String documentType;
    private String documentText;
    private String url;
    private String attachments;

    public ClassificationsSchemeReferenceDocument()
    {
    }

    public ClassificationsSchemeReferenceDocument( CSRefDocModel csRefDocModel )
    {
        this.csLongName = csRefDocModel.getCsLongName();
        this.csVersion = csRefDocModel.getCsVersion();
        this.formattedCsVersion = Float.toString( Float.valueOf( csVersion ) );
        this.documentName = csRefDocModel.getDocumentName();
        this.documentType = csRefDocModel.getDocumentType();
        this.documentText = csRefDocModel.getDocumentText();
        this.url = csRefDocModel.getUrl();
//        this.attachments = csRefDocModel.;
        // todo attachements!
    }

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
