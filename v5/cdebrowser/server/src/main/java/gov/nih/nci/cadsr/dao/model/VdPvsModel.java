package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.sql.Timestamp;

public class VdPvsModel extends BaseModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String vpIdseq;
    private String vdIdseq;
    private String pvIdseq;
    private String conteIdseq;
    private String origin;
    private String conIdseq;
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

    public String getConIdseq()
    {
        return conIdseq;
    }

    public void setConIdseq( String conIdesq )
    {
        this.conIdseq = conIdesq;
        
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
