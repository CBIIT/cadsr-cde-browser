package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */
/**
 * 
 * @author asafievan
 *
 */
public class AlternateDefinitionCsCsi
{
    private String name;
    private String type;
    private String context;
    private String language;
    private String csCsi;
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

	public String getCsCsi() {
		return csCsi;
	}

	public void setCsCsi(String csCsi) {
		this.csCsi = csCsi;
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
