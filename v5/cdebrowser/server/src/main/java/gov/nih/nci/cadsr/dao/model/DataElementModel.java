package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataElementModel extends BaseDesignationDefinitionModel
{
	private static final long serialVersionUID = 1L;
	
	private String preferredQuestionText;
    private String contextName; // do we really need this to be a separate field instead of context.getName()?
    private String usingContexts; // not in table. Filled from designationModels.contexts.name where designationModels.detlName = 'USED_BY'
    private List<ReferenceDocModel> refDocs;// from ReferenceDocumentsView.ac_idseq = data_elements.de_idseq
    private Integer publicId; // this is a duplicate of cdeId. do we really need this?
    private String idseq;
    private String registrationStatus; // not in table. Filled from SBR.AC_RESISTRATIONS.REGISTRATION_STATUS see DAO row mapper
    private ValueDomainModel valueDomainModel; // from vd_idseq
    private DataElementConceptModel dec; // from dec_idseq
    private ContextModel context;
    private String deIdseq; // primary key
    private Float version;
    private String conteIdseq; // this field can't possibly be needed since we have a whole context model object
    private String preferredName;
    private String vdIdseq; // this field can't possibly be needed since we have a whole value domain model object
    private String decIdseq;
    private String preferredDefinition;
    private String aslName; // workflow status
    private String longName;
    private String latestVerInd;
    private String deletedInd;
    private Timestamp beginDate;
    private Timestamp endDate;
    private String origin;
    private Integer cdeId;
    private String question;
    private List<UsageModel> usageModels;
    private List<DEOtherVersionsModel> deOtherVersionsModels;
    private List<CsCsiModel> classifications;
    private List<CSRefDocModel> csRefDocModels;
    private List<CSIRefDocModel> csiRefDocModels;
    private String changeNote;

    public DataElementModel()
    {
    }

    public String getPreferredQuestionText()
    {
        return preferredQuestionText;
    }

    public void setPreferredQuestionText( String preferredQuestionText )
    {
        this.preferredQuestionText = preferredQuestionText;
    }

    /**
     * populate the preferred question text (longCDEName) field out of the
     * reference doc where DCTL_NAME is "Preferred Question Text"
     */
    public void fillPreferredQuestionText()
    {
        if( getRefDocs() != null )
        {
            for( ReferenceDocModel referenceDocModel : getRefDocs() )
            {
                if( referenceDocModel.getDctlName() != null && referenceDocModel.getDctlName().equals( "Preferred Question Text" )
                        && referenceDocModel.getDocText() != null )
                {
                    setPreferredQuestionText( referenceDocModel.getDocText() );
                    return;
                }
            }
        }
    }

    /**
     * populate the usingContexts field by concatenating the designationModels' contexts' names
     * where
     */
    public void fillUsingContexts()
    {
        ArrayList<String> usingContexts = new ArrayList<>();
        if( getDesignationModels() != null )
        {
            for( DesignationModel designationModel : getDesignationModels().values() )
            {
                if( designationModel.getDetlName() != null
                        && designationModel.getDetlName().equals( "USED_BY" )
                        && designationModel.getContex().getName() != null )
                {
                    usingContexts.add( designationModel.getContex().getName() );
                }
            }
        }
        setUsingContexts( StringUtils.join( usingContexts, ", " ) );
    }

    public String getContextName()
    {
        return contextName;
    }

    public void setContextName( String contextName )
    {
        this.contextName = contextName;
    }

    public String getUsingContexts()
    {
        return usingContexts;
    }

    public void setUsingContexts( String usingContexts )
    {
        this.usingContexts = usingContexts;
    }

    public List<ReferenceDocModel> getRefDocs()
    {
        return refDocs;
    }

    public void setRefDocs( List<ReferenceDocModel> refDocs )
    {
        this.refDocs = refDocs;
    }

    public Integer getPublicId()
    {
        return publicId;
    }

    public void setPublicId( Integer publicId )
    {
        this.publicId = publicId;
    }

    public String getIdseq()
    {
        return idseq;
    }

    public void setIdseq( String idseq )
    {
        this.idseq = idseq;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public ValueDomainModel getValueDomainModel()
    {
        return valueDomainModel;
    }

    public void setValueDomainModel( ValueDomainModel valueDomainModel )
    {
        this.valueDomainModel = valueDomainModel;
    }

    public DataElementConceptModel getDec()
    {
        return dec;
    }

    public void setDec( DataElementConceptModel dec )
    {
        this.dec = dec;
    }

    public ContextModel getContext()
    {
        return context;
    }

    public void setContext( ContextModel context )
    {
        this.context = context;
    }

    public String getDeIdseq()
    {
        return deIdseq;
    }

    public void setDeIdseq( String deIdseq )
    {
        this.deIdseq = deIdseq;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
        setFormattedVersion( version );
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getVdIdseq()
    {
        return vdIdseq;
    }

    public void setVdIdseq( String vdIdseq )
    {
        this.vdIdseq = vdIdseq;
    }

    public String getDecIdseq()
    {
        return decIdseq;
    }

    public void setDecIdseq( String decIdseq )
    {
        this.decIdseq = decIdseq;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getLatestVerInd()
    {
        return latestVerInd;
    }

    public void setLatestVerInd( String latestVerInd )
    {
        this.latestVerInd = latestVerInd;
    }

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd( String deletedInd )
    {
        this.deletedInd = deletedInd;
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

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public Integer getCdeId()
    {
        return cdeId;
    }

    public void setCdeId( Integer cdeId )
    {
        this.cdeId = cdeId;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion( String question )
    {
        this.question = question;
        setPreferredQuestionText( this.question );
    }

    public List<UsageModel> getUsageModels()
    {
        return usageModels;
    }

    public void setUsageModels( List<UsageModel> usageModels )
    {
        this.usageModels = usageModels;
    }

    public List<DEOtherVersionsModel> getDeOtherVersionsModels()
    {
        return deOtherVersionsModels;
    }

    public void setDeOtherVersionsModels( List<DEOtherVersionsModel> deOtherVersionsModels )
    {
        this.deOtherVersionsModels = deOtherVersionsModels;
    }

    public List<CsCsiModel> getClassifications()
    {
        return classifications;
    }

    public void setClassifications( List<CsCsiModel> classifications )
    {
        this.classifications = classifications;
    }

    public List<CSRefDocModel> getCsRefDocModels()
    {
        return csRefDocModels;
    }

    public void setCsRefDocModels( List<CSRefDocModel> csRefDocModels )
    {
        this.csRefDocModels = csRefDocModels;
    }

    public List<CSIRefDocModel> getCsiRefDocModels()
    {
        return csiRefDocModels;
    }

    public void setCsiRefDocModels( List<CSIRefDocModel> csiRefDocModels )
    {
        this.csiRefDocModels = csiRefDocModels;
    }
    
	public String getChangeNote() {
		return changeNote;
	}

	public void setChangeNote(String changeNote) {
		this.changeNote = changeNote;
	}
    

	@Override
	public String toString() {
		return "DataElementModel [preferredQuestionText=" + preferredQuestionText + ", contextName=" + contextName
				+ ", usingContexts=" + usingContexts + ", refDocs=" + refDocs + ", publicId=" + publicId + ", idseq="
				+ idseq + ", registrationStatus=" + registrationStatus + ", valueDomainModel=" + valueDomainModel
				+ ", dec=" + dec + ", context=" + context + ", deIdseq=" + deIdseq + ", version=" + version
				+ ", conteIdseq=" + conteIdseq + ", preferredName=" + preferredName + ", vdIdseq=" + vdIdseq
				+ ", decIdseq=" + decIdseq + ", preferredDefinition=" + preferredDefinition + ", aslName=" + aslName
				+ ", longName=" + longName + ", latestVerInd=" + latestVerInd + ", deletedInd=" + deletedInd
				+ ", beginDate=" + beginDate + ", endDate=" + endDate + ", origin=" + origin + ", cdeId=" + cdeId
				+ ", question=" + question + ", csCsiData=" + csCsiData + ", csCsiDesignations=" + csCsiDesignations
				+ ", csCsiDefinitions=" + csCsiDefinitions + ", usageModels=" + usageModels + ", deOtherVersionsModels="
				+ deOtherVersionsModels + ", classifications=" + classifications + ", csRefDocModels=" + csRefDocModels
				+ ", csiRefDocModels=" + csiRefDocModels + ", designationModels=" + designationModels
				+ ", definitionModels=" + definitionModels + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", formattedVersion="
				+ formattedVersion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aslName == null) ? 0 : aslName.hashCode());
		result = prime * result + ((beginDate == null) ? 0 : beginDate.hashCode());
		result = prime * result + ((cdeId == null) ? 0 : cdeId.hashCode());
		result = prime * result + ((classifications == null) ? 0 : classifications.hashCode());
		result = prime * result + ((conteIdseq == null) ? 0 : conteIdseq.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
		result = prime * result + ((csCsiData == null) ? 0 : csCsiData.hashCode());
		result = prime * result + ((csCsiDefinitions == null) ? 0 : csCsiDefinitions.hashCode());
		result = prime * result + ((csCsiDesignations == null) ? 0 : csCsiDesignations.hashCode());
		result = prime * result + ((csRefDocModels == null) ? 0 : csRefDocModels.hashCode());
		result = prime * result + ((csiRefDocModels == null) ? 0 : csiRefDocModels.hashCode());
		result = prime * result + ((deIdseq == null) ? 0 : deIdseq.hashCode());
		result = prime * result + ((deOtherVersionsModels == null) ? 0 : deOtherVersionsModels.hashCode());
		result = prime * result + ((dec == null) ? 0 : dec.hashCode());
		result = prime * result + ((decIdseq == null) ? 0 : decIdseq.hashCode());
		result = prime * result + ((deletedInd == null) ? 0 : deletedInd.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((idseq == null) ? 0 : idseq.hashCode());
		result = prime * result + ((latestVerInd == null) ? 0 : latestVerInd.hashCode());
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((preferredDefinition == null) ? 0 : preferredDefinition.hashCode());
		result = prime * result + ((preferredName == null) ? 0 : preferredName.hashCode());
		result = prime * result + ((preferredQuestionText == null) ? 0 : preferredQuestionText.hashCode());
		result = prime * result + ((publicId == null) ? 0 : publicId.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((refDocs == null) ? 0 : refDocs.hashCode());
		result = prime * result + ((registrationStatus == null) ? 0 : registrationStatus.hashCode());
		result = prime * result + ((usageModels == null) ? 0 : usageModels.hashCode());
		result = prime * result + ((usingContexts == null) ? 0 : usingContexts.hashCode());
		result = prime * result + ((valueDomainModel == null) ? 0 : valueDomainModel.hashCode());
		result = prime * result + ((vdIdseq == null) ? 0 : vdIdseq.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataElementModel other = (DataElementModel) obj;
		if (aslName == null) {
			if (other.aslName != null)
				return false;
		} else if (!aslName.equals(other.aslName))
			return false;
		if (beginDate == null) {
			if (other.beginDate != null)
				return false;
		} else if (!beginDate.equals(other.beginDate))
			return false;
		if (cdeId == null) {
			if (other.cdeId != null)
				return false;
		} else if (!cdeId.equals(other.cdeId))
			return false;
		if (classifications == null) {
			if (other.classifications != null)
				return false;
		} else if (!classifications.equals(other.classifications))
			return false;
		if (conteIdseq == null) {
			if (other.conteIdseq != null)
				return false;
		} else if (!conteIdseq.equals(other.conteIdseq))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (contextName == null) {
			if (other.contextName != null)
				return false;
		} else if (!contextName.equals(other.contextName))
			return false;
		if (csCsiData == null) {
			if (other.csCsiData != null)
				return false;
		} else if (!csCsiData.equals(other.csCsiData))
			return false;
		if (csCsiDefinitions == null) {
			if (other.csCsiDefinitions != null)
				return false;
		} else if (!csCsiDefinitions.equals(other.csCsiDefinitions))
			return false;
		if (csCsiDesignations == null) {
			if (other.csCsiDesignations != null)
				return false;
		} else if (!csCsiDesignations.equals(other.csCsiDesignations))
			return false;
		if (csRefDocModels == null) {
			if (other.csRefDocModels != null)
				return false;
		} else if (!csRefDocModels.equals(other.csRefDocModels))
			return false;
		if (csiRefDocModels == null) {
			if (other.csiRefDocModels != null)
				return false;
		} else if (!csiRefDocModels.equals(other.csiRefDocModels))
			return false;
		if (deIdseq == null) {
			if (other.deIdseq != null)
				return false;
		} else if (!deIdseq.equals(other.deIdseq))
			return false;
		if (deOtherVersionsModels == null) {
			if (other.deOtherVersionsModels != null)
				return false;
		} else if (!deOtherVersionsModels.equals(other.deOtherVersionsModels))
			return false;
		if (dec == null) {
			if (other.dec != null)
				return false;
		} else if (!dec.equals(other.dec))
			return false;
		if (decIdseq == null) {
			if (other.decIdseq != null)
				return false;
		} else if (!decIdseq.equals(other.decIdseq))
			return false;
		if (deletedInd == null) {
			if (other.deletedInd != null)
				return false;
		} else if (!deletedInd.equals(other.deletedInd))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (idseq == null) {
			if (other.idseq != null)
				return false;
		} else if (!idseq.equals(other.idseq))
			return false;
		if (latestVerInd == null) {
			if (other.latestVerInd != null)
				return false;
		} else if (!latestVerInd.equals(other.latestVerInd))
			return false;
		if (longName == null) {
			if (other.longName != null)
				return false;
		} else if (!longName.equals(other.longName))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (preferredDefinition == null) {
			if (other.preferredDefinition != null)
				return false;
		} else if (!preferredDefinition.equals(other.preferredDefinition))
			return false;
		if (preferredName == null) {
			if (other.preferredName != null)
				return false;
		} else if (!preferredName.equals(other.preferredName))
			return false;
		if (preferredQuestionText == null) {
			if (other.preferredQuestionText != null)
				return false;
		} else if (!preferredQuestionText.equals(other.preferredQuestionText))
			return false;
		if (publicId == null) {
			if (other.publicId != null)
				return false;
		} else if (!publicId.equals(other.publicId))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (refDocs == null) {
			if (other.refDocs != null)
				return false;
		} else if (!refDocs.equals(other.refDocs))
			return false;
		if (registrationStatus == null) {
			if (other.registrationStatus != null)
				return false;
		} else if (!registrationStatus.equals(other.registrationStatus))
			return false;
		if (usageModels == null) {
			if (other.usageModels != null)
				return false;
		} else if (!usageModels.equals(other.usageModels))
			return false;
		if (usingContexts == null) {
			if (other.usingContexts != null)
				return false;
		} else if (!usingContexts.equals(other.usingContexts))
			return false;
		if (valueDomainModel == null) {
			if (other.valueDomainModel != null)
				return false;
		} else if (!valueDomainModel.equals(other.valueDomainModel))
			return false;
		if (vdIdseq == null) {
			if (other.vdIdseq != null)
				return false;
		} else if (!vdIdseq.equals(other.vdIdseq))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	
}
