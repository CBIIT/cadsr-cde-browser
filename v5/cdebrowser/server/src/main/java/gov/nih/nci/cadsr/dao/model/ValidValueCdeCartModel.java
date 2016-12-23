/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.model;
/**
 * 
 * @author asafievan
 * This class encapsulates required non-empty data to populate in ValueDomainTransferObject of CDE Cart.
 */
public class ValidValueCdeCartModel {
	private String context;
	private int vmId;//not empty ever
	private String shortMeaningValue;
	private String workflowstatus;
	private String shortMeaning;
	private Float vmVersion; //not empty ever
	private String conceptDerivationRuleIdseq;
	private String description;
	private String vdIdseq; //non-empty expected
	private String vpIdseq; //non-empty expected
	
	public String getVdIdseq() {
		return vdIdseq;
	}
	public void setVdIdseq(String vdIdseq) {
		this.vdIdseq = vdIdseq;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public int getVmId() {
		return vmId;
	}
	public void setVmId(int vmId) {
		this.vmId = vmId;
	}
	public String getShortMeaningValue() {
		return shortMeaningValue;
	}
	public void setShortMeaningValue(String shortMeaningValue) {
		this.shortMeaningValue = shortMeaningValue;
	}
	public String getWorkflowstatus() {
		return workflowstatus;
	}
	public void setWorkflowstatus(String workflowstatus) {
		this.workflowstatus = workflowstatus;
	}
	public String getShortMeaning() {
		return shortMeaning;
	}
	public void setShortMeaning(String shortMeaning) {
		this.shortMeaning = shortMeaning;
	}
	public Float getVmVersion() {
		return vmVersion;
	}
	public void setVmVersion(Float vmVersion) {
		this.vmVersion = vmVersion;
	}
	public String getConceptDerivationRuleIdseq() {
		return conceptDerivationRuleIdseq;
	}
	public void setConceptDerivationRuleIdseq(String conceptDerivationRuleIdseq) {
		this.conceptDerivationRuleIdseq = conceptDerivationRuleIdseq;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getVpIdseq() {
		return vpIdseq;
	}
	public void setVpIdseq(String vpIdseq) {
		this.vpIdseq = vpIdseq;
	}
	
	@Override
	public String toString() {
		return "ValidValueCdeCartModel [context=" + context + ", vmId=" + vmId + ", shortMeaningValue="
				+ shortMeaningValue + ", workflowstatus=" + workflowstatus + ", shortMeaning=" + shortMeaning
				+ ", vmVersion=" + vmVersion + ", conceptDerivationRuleIdseq=" + conceptDerivationRuleIdseq
				+ ", description=" + description + ", vdIdseq=" + vdIdseq + ", vpIdseq=" + vpIdseq + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conceptDerivationRuleIdseq == null) ? 0 : conceptDerivationRuleIdseq.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((shortMeaning == null) ? 0 : shortMeaning.hashCode());
		result = prime * result + ((shortMeaningValue == null) ? 0 : shortMeaningValue.hashCode());
		result = prime * result + ((vdIdseq == null) ? 0 : vdIdseq.hashCode());
		result = prime * result + vmId;
		result = prime * result + ((vmVersion == null) ? 0 : vmVersion.hashCode());
		result = prime * result + ((vpIdseq == null) ? 0 : vpIdseq.hashCode());
		result = prime * result + ((workflowstatus == null) ? 0 : workflowstatus.hashCode());
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
		ValidValueCdeCartModel other = (ValidValueCdeCartModel) obj;
		if (conceptDerivationRuleIdseq == null) {
			if (other.conceptDerivationRuleIdseq != null)
				return false;
		} else if (!conceptDerivationRuleIdseq.equals(other.conceptDerivationRuleIdseq))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (shortMeaning == null) {
			if (other.shortMeaning != null)
				return false;
		} else if (!shortMeaning.equals(other.shortMeaning))
			return false;
		if (shortMeaningValue == null) {
			if (other.shortMeaningValue != null)
				return false;
		} else if (!shortMeaningValue.equals(other.shortMeaningValue))
			return false;
		if (vdIdseq == null) {
			if (other.vdIdseq != null)
				return false;
		} else if (!vdIdseq.equals(other.vdIdseq))
			return false;
		if (vmId != other.vmId)
			return false;
		if (vmVersion == null) {
			if (other.vmVersion != null)
				return false;
		} else if (!vmVersion.equals(other.vmVersion))
			return false;
		if (vpIdseq == null) {
			if (other.vpIdseq != null)
				return false;
		} else if (!vpIdseq.equals(other.vpIdseq))
			return false;
		if (workflowstatus == null) {
			if (other.workflowstatus != null)
				return false;
		} else if (!workflowstatus.equals(other.workflowstatus))
			return false;
		return true;
	}

}
