package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DefinitionModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;

public class AlternateDefinition
{
    private String name;
    private String type;
    private String context;
    private String language;
    
    public AlternateDefinition()
    {
    }
    
    public AlternateDefinition( DefinitionModel definitionModel )
    {
        setName( definitionModel.getDefinition() );
        setContext( definitionModel.getContext().getName() );
        setType( definitionModel.getDeflName() );
        setLanguage(definitionModel.getLaeName());
    }
    
    public AlternateDefinition( DefinitionModelAlt definitionModel )
    {
        setName( definitionModel.getDefinition() );
        setContext( definitionModel.getContextName() );
        setType( definitionModel.getType() );
        setLanguage(definitionModel.getLang());
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

	@Override
	public String toString() {
		return "AlternateDefinition [name=" + name + ", type=" + type + ", context=" + context + ", language="
				+ language + "]";
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
		AlternateDefinition other = (AlternateDefinition) obj;
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

}
