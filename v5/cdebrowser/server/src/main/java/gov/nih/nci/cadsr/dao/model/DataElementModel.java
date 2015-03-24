package gov.nih.nci.cadsr.dao.model;

/**
 * Created with IntelliJ IDEA.
 * User: lerner
 * Date: 3/23/15
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataElementModel extends BaseModel
{
    private String DE_IDSEQ;
    private String VERSION;
    private String CONTE_IDSEQ;
    private String PREFERRED_NAME;
    private String VD_IDSEQ;
    private String DEC_IDSEQ;
    private String PREFERRED_DEFINITION;
    private String ASL_NAME;
    private String LONG_NAME;
    private String LATEST_VERSION_IND;
    private String DELETED_IND;
    private String BEGIN_DATE;
    private String END_DATE;
    private String MODIFIED_BY;
    private String ORIGIN;
    private String CDE_ID;
    private String QUESTION;

    public String getDE_IDSEQ()
    {
        return DE_IDSEQ;
    }

    public void setDE_IDSEQ( String DE_IDSEQ )
    {
        this.DE_IDSEQ = DE_IDSEQ;
    }

    public String getVERSION()
    {
        return VERSION;
    }

    public void setVERSION( String VERSION )
    {
        this.VERSION = VERSION;
    }

    public String getCONTE_IDSEQ()
    {
        return CONTE_IDSEQ;
    }

    public void setCONTE_IDSEQ( String CONTE_IDSEQ )
    {
        this.CONTE_IDSEQ = CONTE_IDSEQ;
    }

    public String getPREFERRED_NAME()
    {
        return PREFERRED_NAME;
    }

    public void setPREFERRED_NAME( String PREFERRED_NAME )
    {
        this.PREFERRED_NAME = PREFERRED_NAME;
    }

    public String getVD_IDSEQ()
    {
        return VD_IDSEQ;
    }

    public void setVD_IDSEQ( String VD_IDSEQ )
    {
        this.VD_IDSEQ = VD_IDSEQ;
    }

    public String getDEC_IDSEQ()
    {
        return DEC_IDSEQ;
    }

    public void setDEC_IDSEQ( String DEC_IDSEQ )
    {
        this.DEC_IDSEQ = DEC_IDSEQ;
    }

    public String getPREFERRED_DEFINITION()
    {
        return PREFERRED_DEFINITION;
    }

    public void setPREFERRED_DEFINITION( String PREFERRED_DEFINITION )
    {
        this.PREFERRED_DEFINITION = PREFERRED_DEFINITION;
    }

    public String getASL_NAME()
    {
        return ASL_NAME;
    }

    public void setASL_NAME( String ASL_NAME )
    {
        this.ASL_NAME = ASL_NAME;
    }

    public String getLONG_NAME()
    {
        return LONG_NAME;
    }

    public void setLONG_NAME( String LONG_NAME )
    {
        this.LONG_NAME = LONG_NAME;
    }

    public String getLATEST_VERSION_IND()
    {
        return LATEST_VERSION_IND;
    }

    public void setLATEST_VERSION_IND( String LATEST_VERSION_IND )
    {
        this.LATEST_VERSION_IND = LATEST_VERSION_IND;
    }

    public String getDELETED_IND()
    {
        return DELETED_IND;
    }

    public void setDELETED_IND( String DELETED_IND )
    {
        this.DELETED_IND = DELETED_IND;
    }

    public String getBEGIN_DATE()
    {
        return BEGIN_DATE;
    }

    public void setBEGIN_DATE( String BEGIN_DATE )
    {
        this.BEGIN_DATE = BEGIN_DATE;
    }

    public String getEND_DATE()
    {
        return END_DATE;
    }

    public void setEND_DATE( String END_DATE )
    {
        this.END_DATE = END_DATE;
    }

    public String getMODIFIED_BY()
    {
        return MODIFIED_BY;
    }

    public void setMODIFIED_BY( String MODIFIED_BY )
    {
        this.MODIFIED_BY = MODIFIED_BY;
    }

    public String getORIGIN()
    {
        return ORIGIN;
    }

    public void setORIGIN( String ORIGIN )
    {
        this.ORIGIN = ORIGIN;
    }

    public String getCDE_ID()
    {
        return CDE_ID;
    }

    public void setCDE_ID( String CDE_ID )
    {
        this.CDE_ID = CDE_ID;
    }

    public String getQUESTION()
    {
        return QUESTION;
    }

    public void setQUESTION( String QUESTION )
    {
        this.QUESTION = QUESTION;
    }
}
