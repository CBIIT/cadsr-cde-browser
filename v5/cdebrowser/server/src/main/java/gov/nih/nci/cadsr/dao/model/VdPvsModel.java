package gov.nih.nci.cadsr.dao.model;

import java.sql.Timestamp;

/**
 * Created by lernermh on 6/3/15.
 */
public class VdPvsModel  extends BaseModel
{
    private String  vpIdseq;
    private String  vdIdseq;
    private String  pvIdseq;
    private String  conteIdseq;
    private String   origin;
    private String conIdesq;
    private Timestamp beginDate;
    private Timestamp endDate;

    public String getVpIdseq()
    {
        return vpIdseq;
    }

    public void setVpIdseq( String vpIdseq )
    {
        this.vpIdseq = vpIdseq;
    }

    public String getVdIdseq()
    {
        return vdIdseq;
    }

    public void setVdIdseq( String vdIdseq )
    {
        this.vdIdseq = vdIdseq;
    }

    public String getPvIdseq()
    {
        return pvIdseq;
    }

    public void setPvIdseq( String pvIdseq )
    {
        this.pvIdseq = pvIdseq;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getConIdesq()
    {
        return conIdesq;
    }

    public void setConIdesq( String conIdesq )
    {
        this.conIdesq = conIdesq;
    }

    public Timestamp getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate( Timestamp beginDate )
    {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Timestamp endDate )
    {
        this.endDate = endDate;
    }
}
