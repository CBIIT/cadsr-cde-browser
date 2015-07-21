package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

/**
 * Created by lernermh on 4/22/15.
 */
public class PermissibleValue
{
    private int pv;
    private String pvMeaning;
    private String pvMeaningConceptCode;
    private String pvMeaningDescription;
    private String pvBeginDate;
    private String pvEndDate;
    private int vmPublicId ;
    private float vmVersion;

    public int getPv()
    {
        return pv;
    }

    public void setPv( int pv )
    {
        this.pv = pv;
    }

    public String getPvMeaning()
    {
        return pvMeaning;
    }

    public void setPvMeaning( String pvMeaning )
    {
        this.pvMeaning = pvMeaning;
    }

    public String getPvMeaningConceptCode()
    {
        return pvMeaningConceptCode;
    }

    public void setPvMeaningConceptCode( String pvMeaningConceptCode )
    {
        this.pvMeaningConceptCode = pvMeaningConceptCode;
    }

    public String getPvMeaningDescription()
    {
        return pvMeaningDescription;
    }

    public void setPvMeaningDescription( String pvMeaningDescription )
    {
        this.pvMeaningDescription = pvMeaningDescription;
    }

    public String getPvBeginDate()
    {
        return pvBeginDate;
    }

    public void setPvBeginDate( String pvBeginDate )
    {
        this.pvBeginDate = pvBeginDate;
    }

    public String getPvEndDate()
    {
        return pvEndDate;
    }

    public void setPvEndDate( String pvEndDate )
    {
        this.pvEndDate = pvEndDate;
    }

    public int getVmPublicId()
    {
        return vmPublicId;
    }

    public void setVmPublicId( int vmPublicId )
    {
        this.vmPublicId = vmPublicId;
    }

    public float getVmVersion()
    {
        return vmVersion;
    }

    public void setVmVersion( float vmVersion )
    {
        this.vmVersion = vmVersion;
    }
}
