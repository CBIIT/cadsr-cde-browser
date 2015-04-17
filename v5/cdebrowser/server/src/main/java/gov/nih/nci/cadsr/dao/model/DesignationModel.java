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
    private String detlName;

    public DesignationModel() {
    }

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

    public String getDetlName() {
        return detlName;
    }

    public void setDetlName(String detlName) {
        this.detlName = detlName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DesignationModel)) return false;

        DesignationModel that = (DesignationModel) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getDesigIDSeq() != null ? !getDesigIDSeq().equals(that.getDesigIDSeq()) : that.getDesigIDSeq() != null)
            return false;
        if (getContex() != null ? !getContex().equals(that.getContex()) : that.getContex() != null) return false;
        if (getLang() != null ? !getLang().equals(that.getLang()) : that.getLang() != null) return false;
        return !(detlName != null ? !detlName.equals(that.detlName) : that.detlName != null);

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getDesigIDSeq() != null ? getDesigIDSeq().hashCode() : 0);
        result = 31 * result + (getContex() != null ? getContex().hashCode() : 0);
        result = 31 * result + (getLang() != null ? getLang().hashCode() : 0);
        result = 31 * result + (detlName != null ? detlName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DesignationModel{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desigIDSeq='" + desigIDSeq + '\'' +
                ", contex=" + contex +
                ", lang='" + lang + '\'' +
                ", detlName='" + detlName + '\'' +
                '}';
    }
}
