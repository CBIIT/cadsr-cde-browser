package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateDefinition;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;

import java.sql.Timestamp;
import java.util.List;

public class ValueMeaningModel extends BaseModel
{


    private String condrIdseq;
    private String vmIdseq;
    private String preferredDefinition;
    private String longName;
    private String conteIdseq;
    private String aslName;
    private Float version;
    private String vmId;
    private String latestVersionInd;
    private String deletedInd;
    private String origin;
    private String changeNote;
    private String description;
    private String definitionSource;
    private String shortMeaning;
    private String comments;
    private String preferredName;
    private Timestamp beginDate;
    private Timestamp endDate;

    private List<AlternateNameModel> alternateNames = null;
    private List<AlternateDefinitionModel> alternateDefinitions = null;

    public String getCondrIdseq()
    {
        return condrIdseq;
    }

    public void setCondrIdseq( String condrIdseq )
    {
        this.condrIdseq = condrIdseq;
    }

    public String getVmIdseq()
    {
        return vmIdseq;
    }

    public void setVmIdseq( String vmIdseq )
    {
        this.vmIdseq = vmIdseq;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
    }

    public String getVmId()
    {
        return vmId;
    }

    public void setVmId( String vmId )
    {
        this.vmId = vmId;
    }

    public String getLatestVersionInd()
    {
        return latestVersionInd;
    }

    public void setLatestVersionInd( String latestVersionInd )
    {
        this.latestVersionInd = latestVersionInd;
    }

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd( String deletedInd )
    {
        this.deletedInd = deletedInd;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getChangeNote()
    {
        return changeNote;
    }

    public void setChangeNote( String changeNote )
    {
        this.changeNote = changeNote;
    }

    public String getDefinitionSource()
    {
        return definitionSource;
    }

    public void setDefinitionSource( String definitionSource )
    {
        this.definitionSource = definitionSource;
    }

    public String getShortMeaning()
    {
        return shortMeaning;
    }

    public void setShortMeaning( String shortMeaning )
    {
        this.shortMeaning = shortMeaning;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
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

    public List<AlternateNameModel> getAlternateNames()
    {
        return alternateNames;
    }

    public void setAlternateNames( List<AlternateNameModel> alternateNames )
    {
        this.alternateNames = alternateNames;
    }

    public List<AlternateDefinitionModel> getAlternateDefinitions()
    {
        return alternateDefinitions;
    }

    public void setAlternateDefinitions( List<AlternateDefinitionModel> alternateDefinitions )
    {
        this.alternateDefinitions = alternateDefinitions;
    }


    @Override
    public String toString()
    {
        String valueMeaningStr = "ValueMeaningModel{" +
                "condrIdseq='" + condrIdseq + '\'' +
                ", vmIdseq='" + vmIdseq + '\'' +
                ", preferredDefinition='" + preferredDefinition + '\'' +
                ", longName='" + longName + '\'' +
                ", conteIdseq='" + conteIdseq + '\'' +
                ", aslName='" + aslName + '\'' +
                ", version=" + version +
                ", vmId='" + vmId + '\'' +
                ", latestVersionInd='" + latestVersionInd + '\'' +
                ", deletedInd='" + deletedInd + '\'' +
                ", origin='" + origin + '\'' +
                ", changeNote='" + changeNote + '\'' +
                ", definitionSource='" + definitionSource + '\'' +
                ", shortMeaning='" + shortMeaning + '\'' +
                ", description='" + description + '\'' +
                ", comments='" + comments + '\'' +
                ", preferredName='" + preferredName + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate;

        if( alternateNames != null )
        {
            valueMeaningStr += "\n";
            for( AlternateNameModel alternateName : alternateNames )
            {
                valueMeaningStr += alternateName.toString() + "\n";
            }
        }
        if( alternateDefinitions != null)
        {
            valueMeaningStr += "\n";
            for( AlternateDefinitionModel alternateDefinition: alternateDefinitions)
            {
                valueMeaningStr += alternateDefinition.toString() + "\n";
            }
        }

        valueMeaningStr += '}';

        return valueMeaningStr;
    }
}
