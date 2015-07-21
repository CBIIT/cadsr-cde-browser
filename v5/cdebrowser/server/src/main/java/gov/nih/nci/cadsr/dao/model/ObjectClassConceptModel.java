package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lernermh on 7/6/15.
 */
public class ObjectClassConceptModel extends BaseModel
{
    private String ocIdseq;
    private String decIdseq;
    private String condrIdseq;
    private String name;
    private String primary;

    public String getOcIdseq()
    {
        return ocIdseq;
    }

    public void setOcIdseq( String ocIdseq )
    {
        this.ocIdseq = ocIdseq;
    }

    public String getDecIdseq()
    {
        return decIdseq;
    }

    public void setDecIdseq( String decIdseq )
    {
        this.decIdseq = decIdseq;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getCondrIdseq()
    {
        return condrIdseq;
    }

    public void setCondrIdseq( String condrIdseq )
    {
        this.condrIdseq = condrIdseq;
    }

    public String getPrimary()
    {
        return primary;
    }

    public void setPrimary( String primary )
    {
        this.primary = primary;
    }
}
