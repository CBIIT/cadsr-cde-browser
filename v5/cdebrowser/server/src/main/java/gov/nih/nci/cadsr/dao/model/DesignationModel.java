package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lavezzojl on 4/6/15.
 */
public class DesignationModel extends BaseModel {
    private String name;
    private String type;
    private String desigIDSeq;
    private ContextModel contex;
    private String lang;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getDesigIDSeq()
    {
        return desigIDSeq;
    }

    public void setDesigIDSeq( String desigIDSeq )
    {
        this.desigIDSeq = desigIDSeq;
    }

    public ContextModel getContex()
    {
        return contex;
    }

    public void setContex( ContextModel contex )
    {
        this.contex = contex;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang( String lang )
    {
        this.lang = lang;
    }
}
