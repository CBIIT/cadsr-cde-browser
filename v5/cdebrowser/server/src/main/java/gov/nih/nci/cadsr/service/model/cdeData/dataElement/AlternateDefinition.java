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
        setName( definitionModel.getName() );
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
    
}
