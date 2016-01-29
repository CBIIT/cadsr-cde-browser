package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class ReferenceDocModel extends BaseModel
{
    private String docName;
    private String docType; // DCTL_NAME
    private String docIDSeq;
    private String docText;
    private String lang;
    private String url;
    private ContextModel context;
    private String dctlName;

    public ReferenceDocModel()
    {
    }

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

    public String getContextName()
    {
        return context.getName();
    }

    public void setContext( ContextModel context )
    {
        this.context = context;
    }

    public String getDctlName()
    {
        return dctlName;
    }

    public void setDctlName( String dctlName )
    {
        this.dctlName = dctlName;
    }

    @Override
    public String toString()
    {
        return "ReferenceDocModel{" +
                "docName='" + docName + '\'' +
                ", docType='" + docType + '\'' +
                ", docIDSeq='" + docIDSeq + '\'' +
                ", docText='" + docText + '\'' +
                ", lang='" + lang + '\'' +
                ", url='" + url + '\'' +
                ", context=" + context +
                ", dctlName='" + dctlName + '\'' +
                '}';
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof ReferenceDocModel ) ) return false;

        ReferenceDocModel that = ( ReferenceDocModel ) o;

        if( getDocName() != null ? !getDocName().equals( that.getDocName() ) : that.getDocName() != null ) return false;
        if( getDocType() != null ? !getDocType().equals( that.getDocType() ) : that.getDocType() != null ) return false;
        if( getDocIDSeq() != null ? !getDocIDSeq().equals( that.getDocIDSeq() ) : that.getDocIDSeq() != null )
            return false;
        if( getDocText() != null ? !getDocText().equals( that.getDocText() ) : that.getDocText() != null ) return false;
        if( getLang() != null ? !getLang().equals( that.getLang() ) : that.getLang() != null ) return false;
        if( getUrl() != null ? !getUrl().equals( that.getUrl() ) : that.getUrl() != null ) return false;
        if( getContext() != null ? !getContext().equals( that.getContext() ) : that.getContext() != null ) return false;
        return !( getDctlName() != null ? !getDctlName().equals( that.getDctlName() ) : that.getDctlName() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getDocName() != null ? getDocName().hashCode() : 0;
        result = 31 * result + ( getDocType() != null ? getDocType().hashCode() : 0 );
        result = 31 * result + ( getDocIDSeq() != null ? getDocIDSeq().hashCode() : 0 );
        result = 31 * result + ( getDocText() != null ? getDocText().hashCode() : 0 );
        result = 31 * result + ( getLang() != null ? getLang().hashCode() : 0 );
        result = 31 * result + ( getUrl() != null ? getUrl().hashCode() : 0 );
        result = 31 * result + ( getContext() != null ? getContext().hashCode() : 0 );
        result = 31 * result + ( getDctlName() != null ? getDctlName().hashCode() : 0 );
        return result;
    }
}
