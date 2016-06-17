package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.io.Serializable;
import java.util.List;
import java.util.Set;
/**
 * This class is for DB model of Alternate Section of Value Domain tab
 * @author asafievan
 *
 */
public class DesignationModelAlt implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String desigIdseq;
	private String name;
	private String type;
	private String contextName;
	private String lang;

	public DesignationModelAlt() {
	}

	public String getDesigIdseq() {
		return desigIdseq;
	}

	public void setDesigIdseq(String desigIdseq) {
		this.desigIdseq = desigIdseq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public String toString() {
		return "DesignationModelAlt [desigIdseq=" + desigIdseq + ", name=" + name + ", type=" + type + ", contextName="
				+ contextName + ", lang=" + lang + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
		result = prime * result + ((desigIdseq == null) ? 0 : desigIdseq.hashCode());
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		DesignationModelAlt other = (DesignationModelAlt) obj;
		if (contextName == null) {
			if (other.contextName != null)
				return false;
		} else if (!contextName.equals(other.contextName))
			return false;
		if (desigIdseq == null) {
			if (other.desigIdseq != null)
				return false;
		} else if (!desigIdseq.equals(other.desigIdseq))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


}
