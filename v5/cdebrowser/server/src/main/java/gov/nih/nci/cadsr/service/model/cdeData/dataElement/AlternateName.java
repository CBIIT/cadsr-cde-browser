package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.commons.lang3.StringUtils;

import gov.nih.nci.cadsr.dao.model.DesignationModel;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;

public class AlternateName implements Comparable
{
    private String name;
    private String type;
    private String context;
    private String language;

    public AlternateName()
    {
    }

    public AlternateName( DesignationModel designationModel )
    {
        setName( designationModel.getName() );
        setType( designationModel.getType() );
        setContext( designationModel.getContex().getName() );
        setLanguage( designationModel.getLang() );
    }

    public AlternateName( DesignationModelAlt designationModel )
    {
        setName( designationModel.getName() );
        setType( designationModel.getType() );
        setContext( designationModel.getContextName());
        setLanguage( designationModel.getLang() );
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

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage( String language )
    {
        this.language = language;
    }

	@Override
	public String toString() {
		return "AlternateName [name=" + name + ", type=" + type + ", context=" + context + ", language=" + language
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
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
		AlternateName other = (AlternateName) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
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

	@Override
	public int compareTo(Object other) {
		if (other instanceof AlternateName) {
			AlternateName that = (AlternateName)other;
			//to avoid null pointer for the values which are never null in our DB
			String thisName = (this.name != null) ? name : "";
			String thisType = (this.type != null) ? this.type : "";
			String thisContext = (this.context != null) ? this.context : "";
			//Sorting order: name, type, context
			if (!(thisName.equals(that.name))) {
				return StringUtils.lowerCase(thisName).compareTo(StringUtils.lowerCase(that.name));
			}
			else if (!(thisType.equals(that.type))){
				return thisType.compareTo(that.type);
			}
			else {
				return thisContext.compareTo(that.context);
			}
		}
		else {
			return -1;
		}
	}

}
