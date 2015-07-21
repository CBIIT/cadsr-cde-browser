package gov.nih.nci.cadsr.dao.model;

import gov.nih.nci.cadsr.common.CaDSRConstants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by lernermh on 6/3/15.
 */
public class PermissibleValuesModel extends BaseModel
{
    private String pvIdseq;
    private String value;
    private String shortMeaning;
    private String meaningDescription;
    private Timestamp beginDate;
    private String beginDateString;
    private Timestamp endDate;
    private String endDateString;
    private String highValueNum;
    private String lowValueNum;
    private String vmIdseq;
    private String conceptCode;
    private String vmDescription;
    private String vmId;
    private String vmVersion;

    public String getConceptCode()
    {
        return conceptCode;
    }

    public void setConceptCode( String conceptCode )
    {
        this.conceptCode = conceptCode;
    }

    public String getVmVersion()
    {
        return vmVersion;
    }

    public void setVmVersion( String vmVersion )
    {
        this.vmVersion = vmVersion;
    }

    public String getVmDescription()
    {
        return vmDescription;
    }

    public void setVmDescription( String vmDescription )
    {
        this.vmDescription = vmDescription;
    }

    public String getVmId()
    {
        return vmId;
    }

    public void setVmId( String vmId )
    {
        this.vmId = vmId;
    }

    public String getPvIdseq()
    {
        return pvIdseq;
    }

    public void setPvIdseq( String pvIdseq )
    {
        this.pvIdseq = pvIdseq;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    public String getShortMeaning()
    {
        return shortMeaning;
    }

    public void setShortMeaning( String shortMeaning )
    {
        this.shortMeaning = shortMeaning;
    }

    public String getMeaningDescription()
    {
        return meaningDescription;
    }

    public void setMeaningDescription( String meaningDescription )
    {
        this.meaningDescription = meaningDescription;
    }

    public Timestamp getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate( Timestamp beginDate )
    {
        this.beginDate = beginDate;
        if( (beginDate != null) )
        {
            this.beginDateString = new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format( beginDate );
        }
        else
        {
            this.beginDateString = "";
        }
    }

    public String getBeginDateString()
    {
        return beginDateString;
    }

    public void setBeginDateString( String beginDateString )
    {
        this.beginDateString = beginDateString;
    }

    public Timestamp getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Timestamp endDate )
    {
        this.endDate = endDate;
        if( (endDate != null) )
        {
            this.endDateString = new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format( endDate );
        }
        else
        {
            this.endDateString = "";
        }

    }

    public String getEndDateString()
    {
        return endDateString;
    }

    public void setEndDateString( String endDateString )
    {
        this.endDateString = endDateString;
    }

    public String getHighValueNum()
    {
        return highValueNum;
    }

    public void setHighValueNum( String highValueNum )
    {
        this.highValueNum = highValueNum;
    }

    public String getLowValueNum()
    {
        return lowValueNum;
    }

    public void setLowValueNum( String lowValueNum )
    {
        this.lowValueNum = lowValueNum;
    }

    public String getVmIdseq()
    {
        return vmIdseq;
    }

    public void setVmIdseq( String vmIdseq )
    {
        this.vmIdseq = vmIdseq;
    }
}
