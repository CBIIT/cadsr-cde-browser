package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DesignationModel;

public class AlternateName
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
    
}
