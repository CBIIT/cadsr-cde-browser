package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.nih.nci.cadsr.service.restControllers.ControllerUtils;

/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */
/**
 * 
 * @author asafievan
 *
 */
public class AlternateDefinitionCsCsi implements Comparable
{
    private String name;
    private String type;
    private String context;
    private String language;
    private List<String> csCsi;//could be null
   //CDEBROWSER-809
    public AlternateDefinitionCsCsi()
    {
    }
    
    public AlternateDefinitionCsCsi(AlternateDefinition definitionModel)
    {
        setName( definitionModel.getName() );
        setContext( definitionModel.getContext());
        setType( definitionModel.getType());
        setLanguage(definitionModel.getLanguage());
    }
    
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getCsCsi() {
		return csCsi;
	}

	public void setCsCsi(List<String> csCsi) {
		this.csCsi = csCsi;
	}

	@Override
	public int compareTo(Object other) {
		if (other instanceof AlternateDefinitionCsCsi) {
			AlternateDefinitionCsCsi that = (AlternateDefinitionCsCsi)other;
			//to avoid null pointer for the values which are never null in our DB
			String thisName = (this.name != null) ? name : "";
			String thisType = (this.type != null) ? this.type : "";
			String thisContext = (this.context != null) ? this.context : "";
			String thatName = (that.name != null) ? that.name : "";
			String thatType = (that.type != null) ? that.type : "";
			String thatContext = (that.context != null) ? that.context : "";
			//Sorting order: empty csCsi, name, type, context
			if ((ControllerUtils.isArrayEmpty(this.csCsi)) && (ControllerUtils.isArrayNotEmpty(that.csCsi))) {
				return -1;
			}
			else if ((ControllerUtils.isArrayNotEmpty(this.csCsi)) && (ControllerUtils.isArrayEmpty(that.csCsi))) {
				return 1;
			}	
			else if (!(thisName.equals(thatName))) {
				return thisName.compareToIgnoreCase(thatName);
			}
			else if (!(thisType.equals(thatType))){
				return thisType.compareTo(thatType);
			}
			else {
				return thisContext.compareTo(thatContext);
			}
		}
		else if (other instanceof AlternateDefinition) {
			return 1;
		}
		else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		return "AlternateDefinitionCsCsi [name=" + name + ", type=" + type + ", context=" + context + ", language="
				+ language + ", csCsi=" + csCsi + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((csCsi == null) ? 0 : csCsi.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
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
		AlternateDefinitionCsCsi other = (AlternateDefinitionCsCsi) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (csCsi == null) {
			if (other.csCsi != null)
				return false;
		} else if (!csCsi.equals(other.csCsi))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
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
