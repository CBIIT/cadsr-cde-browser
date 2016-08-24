package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

public class ValueMeaningUiModel
{
    private String pvIdseq;
    private String pvMeaning;
    private String vmPublicId;
    private String vmVersion;
    private String vmIdseq;

    private List<AlternateNameUiModel> alternateNames = null;
    private List<AlternateDefinitionUiModel> alternateDefinitions = null;

    public String getPvIdseq()
    {
        return pvIdseq;
    }

    public void setPvIdseq( String pvIdseq )
    {
        this.pvIdseq = pvIdseq;
    }

    public String getPvMeaning()
    {
        return pvMeaning;
    }

    public void setPvMeaning( String pvMeaning )
    {
        this.pvMeaning = pvMeaning;
    }

    public String getVmPublicId()
    {
        return vmPublicId;
    }

    public void setVmPublicId( String vmPublicId )
    {
        this.vmPublicId = vmPublicId;
    }

    public String getVmVersion()
    {
        return vmVersion;
    }

    public void setVmVersion( String vmVersion )
    {
        this.vmVersion = vmVersion;
    }

    public String getVmIdseq()
    {
        return vmIdseq;
    }

    public void setVmIdseq( String vmIdseq )
    {
        this.vmIdseq = vmIdseq;
    }

    public List<AlternateNameUiModel> getAlternateNames()
    {
        return alternateNames;
    }

    public void setAlternateNames( List<AlternateNameUiModel> alternateNames )
    {
        this.alternateNames = alternateNames;
    }

    public List<AlternateDefinitionUiModel> getAlternateDefinitions()
    {
        return alternateDefinitions;
    }

    public void setAlternateDefinitions( List<AlternateDefinitionUiModel> alternateDefinitions )
    {
        this.alternateDefinitions = alternateDefinitions;
    }

    @Override
    public String toString()
    {
        String valueMeaningStr = "ValueMeaningUiModel{" +
                "pvIdseq='" + pvIdseq + '\'' +
                ", pvMeaning='" + pvMeaning + '\'' +
                ", vmPublicId='" + vmPublicId + '\'' +
                ", vmVersion='" + vmVersion + '\'' +
                ", vmIdseq='" + vmIdseq + '\'' + "\n";
        if( alternateNames != null )
        {
            valueMeaningStr += "\n";
            for( AlternateNameUiModel alternateName : this.alternateNames )
            {
                valueMeaningStr += alternateName.toString() + "\n";
            }
        }
        if( alternateDefinitions != null)
        {
            valueMeaningStr += "\n";
            for( AlternateDefinitionUiModel alternateDefinition: this.alternateDefinitions )
            {
                valueMeaningStr += alternateDefinition.toString() + "\n";
            }
        }
        valueMeaningStr += '}';

        return valueMeaningStr;
    }
}
