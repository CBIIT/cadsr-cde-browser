package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lernermh on 7/8/15.
 */
public class PropertyConceptModel
{
    private String propIdseq;
    private String condrIdseq;
    private String name;

    public String getPropIdseq()
    {
        return propIdseq;
    }

    public void setPropIdseq( String propIdseq )
    {
        this.propIdseq = propIdseq;
    }

    public String getCondrIdseq()
    {
        return condrIdseq;
    }

    public void setCondrIdseq( String condrIdseq )
    {
        this.condrIdseq = condrIdseq;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
