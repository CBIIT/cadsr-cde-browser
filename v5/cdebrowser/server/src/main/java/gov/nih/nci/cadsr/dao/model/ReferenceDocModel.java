package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lavezzojl on 4/6/15.
 */
public class ReferenceDocModel extends BaseModel {
    private String docName;
    private String docType;
    private String docIDSeq;
    private String docText;
    private String lang;
    private String url;
    private ContextModel context;

    public String getDocName()
    {
        return docName;
    }

    public void setDocName( String docName )
    {
        this.docName = docName;
    }

    public String getDocType()
    {
        return docType;
    }

    public void setDocType( String docType )
    {
        this.docType = docType;
    }

    public String getDocIDSeq()
    {
        return docIDSeq;
    }

    public void setDocIDSeq( String docIDSeq )
    {
        this.docIDSeq = docIDSeq;
    }

    public String getDocText()
    {
        return docText;
    }

    public void setDocText( String docText )
    {
        this.docText = docText;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang( String lang )
    {
        this.lang = lang;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public ContextModel getContext()
    {
        return context;
    }

    public void setContext( ContextModel context )
    {
        this.context = context;
    }
}
