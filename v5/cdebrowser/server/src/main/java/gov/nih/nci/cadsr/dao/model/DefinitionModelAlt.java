package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.io.Serializable;
/**
 * This class is for DB model of Alternate Section of Value Domain tab
 * @author asafievan
 *
 */
public class DefinitionModelAlt implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String definIdseq;
	private String definition;
	private String type;
	private String contextName;
	private String lang;

	public DefinitionModelAlt() {
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
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

	public String getDefinIdseq() {
		return definIdseq;
	}

	public void setDefinIdseq(String definIdseq) {
		this.definIdseq = definIdseq;
	}


	@Override
	public String toString() {
		return "DefinitionModelAlt [definIdseq=" + definIdseq + ", definition=" + definition + ", type=" + type
				+ ", contextName=" + contextName + ", lang=" + lang + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contextName == null) ? 0 : contextName.hashCode());
		result = prime * result + ((definIdseq == null) ? 0 : definIdseq.hashCode());
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
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
		DefinitionModelAlt other = (DefinitionModelAlt) obj;
		if (contextName == null) {
			if (other.contextName != null)
				return false;
		} else if (!contextName.equals(other.contextName))
			return false;
		if (definIdseq == null) {
			if (other.definIdseq != null)
				return false;
		} else if (!definIdseq.equals(other.definIdseq))
			return false;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	
}
