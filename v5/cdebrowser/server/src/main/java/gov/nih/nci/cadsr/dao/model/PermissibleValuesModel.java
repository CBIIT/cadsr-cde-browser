package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.CaDSRConstants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class PermissibleValuesModel extends BaseModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
    private String[] conceptCodeArray;
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
        if (conceptCode!= null) {
        	setConceptCodeArray(conceptCode.split(":"));
        }
    }

    public String getVmVersion()
    {
        return vmVersion;
    }

    public void setVmVersion( String vmVersion )
    {
        this.vmVersion = vmVersion;
        this.formattedVersion = Float.toString( Float.valueOf( vmVersion ) );
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
        if( ( beginDate != null ) )
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
        if( ( endDate != null ) )
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

	public String[] getConceptCodeArray() {
		return conceptCodeArray;
	}

	public void setConceptCodeArray(String[] conceptCodeArray) {
		this.conceptCodeArray = conceptCodeArray;
	}

	@Override
	public String toString() {
		return "PermissibleValuesModel [pvIdseq=" + pvIdseq + ", value=" + value + ", shortMeaning=" + shortMeaning
				+ ", meaningDescription=" + meaningDescription + ", beginDate=" + beginDate + ", beginDateString="
				+ beginDateString + ", endDate=" + endDate + ", endDateString=" + endDateString + ", highValueNum="
				+ highValueNum + ", lowValueNum=" + lowValueNum + ", vmIdseq=" + vmIdseq + ", conceptCode="
				+ conceptCode + ", vmDescription=" + vmDescription + ", vmId=" + vmId + ", vmVersion=" + vmVersion
				+ ", getCreatedBy()=" + getCreatedBy() + ", getDateCreated()=" + getDateCreated() + ", getModifiedBy()="
				+ getModifiedBy() + ", getDateModified()=" + getDateModified() + "]";
	}
    
}
