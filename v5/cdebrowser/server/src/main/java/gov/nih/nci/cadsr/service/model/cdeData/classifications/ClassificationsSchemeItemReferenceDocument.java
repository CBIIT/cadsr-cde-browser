package gov.nih.nci.cadsr.service.model.cdeData.classifications;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.CSIRefDocModel;

public class ClassificationsSchemeItemReferenceDocument
{
    private String csiName;
    private String documentName;
    private String documentType;
    private String documentText;
    private String url;
    private String attachments;

    public ClassificationsSchemeItemReferenceDocument()
    {
    }

    public ClassificationsSchemeItemReferenceDocument( CSIRefDocModel csiRefDocModel )
    {
        this.csiName = csiRefDocModel.getCsiName();
        this.documentName = csiRefDocModel.getDocumentName();
        this.documentType = csiRefDocModel.getDocumentType();
        this.documentText = csiRefDocModel.getDocumentText();
        this.url = csiRefDocModel.getUrl();
//        this.attachments = csiRefDocModel.;
        // todo attachements!
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
}
