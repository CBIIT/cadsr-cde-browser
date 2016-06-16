/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModel;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateDefinition;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
/**
 * This class keeps a representation of Classification for a Value Meaning.
 * 
 * @author asafievan
 *
 */
public class ClassificationSchemaAlternate {
	private String csLongName;
	private String csDefinition;
	private String csiName;
	private String csiType;
	private int csiPublicId;
	private String csiVersion;
	List<AlternateName> alternateNames = new ArrayList<>();
	List<AlternateDefinition> definitions = new ArrayList<>();

	public ClassificationSchemaAlternate(CsCsiValueMeaningModel csCsiAttModel) {
		this.setCsDefinition(csCsiAttModel.getCsDefinition());
		this.csiName = csCsiAttModel.getCsiName();
		this.setCsiPublicId(csCsiAttModel.getCsiId());
		this.setCsiType(csCsiAttModel.getCsitlName());
		this.setCsiVersion(Float.toString(csCsiAttModel.getCsiVersion()));
		this.setCsLongName(csCsiAttModel.getCsLongName());
		this.csiName = csCsiAttModel.getCsiName();
	}
	
	public ClassificationSchemaAlternate() {
		super();
	}

	public String getCsLongName() {
		return csLongName;
	}
	public void setCsLongName(String csLongName) {
		this.csLongName = csLongName;
	}
	public String getCsDefinition() {
		return csDefinition;
	}
	public void setCsDefinition(String csDefinition) {
		this.csDefinition = csDefinition;
	}
	public String getCsiType() {
		return csiType;
	}
	public void setCsiType(String csiType) {
		this.csiType = csiType;
	}
	public int getCsiPublicId() {
		return csiPublicId;
	}
	public void setCsiPublicId(int csiPublicId) {
		this.csiPublicId = csiPublicId;
	}
	public String getCsiVersion() {
		return csiVersion;
	}
	public void setCsiVersion(String csiVersion) {
		this.csiVersion = csiVersion;
	}
	public List<AlternateName> getAlternateNames() {
		return alternateNames;
	}
	public void setAlternateNames(List<AlternateName> alternateNames) {
		this.alternateNames = alternateNames;
	}
	public void addAlternateName(AlternateName alternateName) {
		if (alternateName != null)
			this.alternateNames.add(alternateName);
	}
	public List<AlternateDefinition> getDefinitions() {
		return definitions;
	}
	public void setDefinitions(List<AlternateDefinition> definitions) {
		this.definitions = definitions;
	}
	public void addDefinition(AlternateDefinition definition) {
		if (definition != null)
			this.definitions.add(definition);
	}

	public String getCsiName() {
		return csiName;
	}

	public void setCsiName(String csiName) {
		this.csiName = csiName;
	}

	@Override
	public String toString() {
		return "ClassificationSchemaAlternate [csLongName=" + csLongName + ", csDefinition=" + csDefinition
				+ ", csiName=" + csiName + ", csiType=" + csiType + ", csiPublicId=" + csiPublicId + ", csiVersion="
				+ csiVersion + ", alternateNames=" + alternateNames + ", definitions=" + definitions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alternateNames == null) ? 0 : alternateNames.hashCode());
		result = prime * result + ((csDefinition == null) ? 0 : csDefinition.hashCode());
		result = prime * result + ((csLongName == null) ? 0 : csLongName.hashCode());
		result = prime * result + ((csiName == null) ? 0 : csiName.hashCode());
		result = prime * result + csiPublicId;
		result = prime * result + ((csiType == null) ? 0 : csiType.hashCode());
		result = prime * result + ((csiVersion == null) ? 0 : csiVersion.hashCode());
		result = prime * result + ((definitions == null) ? 0 : definitions.hashCode());
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
		ClassificationSchemaAlternate other = (ClassificationSchemaAlternate) obj;
		if (alternateNames == null) {
			if (other.alternateNames != null)
				return false;
		} else if (!alternateNames.equals(other.alternateNames))
			return false;
		if (csDefinition == null) {
			if (other.csDefinition != null)
				return false;
		} else if (!csDefinition.equals(other.csDefinition))
			return false;
		if (csLongName == null) {
			if (other.csLongName != null)
				return false;
		} else if (!csLongName.equals(other.csLongName))
			return false;
		if (csiName == null) {
			if (other.csiName != null)
				return false;
		} else if (!csiName.equals(other.csiName))
			return false;
		if (csiPublicId != other.csiPublicId)
			return false;
		if (csiType == null) {
			if (other.csiType != null)
				return false;
		} else if (!csiType.equals(other.csiType))
			return false;
		if (csiVersion == null) {
			if (other.csiVersion != null)
				return false;
		} else if (!csiVersion.equals(other.csiVersion))
			return false;
		if (definitions == null) {
			if (other.definitions != null)
				return false;
		} else if (!definitions.equals(other.definitions))
			return false;
		return true;
	}

}
