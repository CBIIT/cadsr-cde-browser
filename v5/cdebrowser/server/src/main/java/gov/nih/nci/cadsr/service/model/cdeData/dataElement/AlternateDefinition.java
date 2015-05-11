package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import gov.nih.nci.cadsr.dao.model.DefinitionModel;

public class AlternateDefinition
{
    private String name;
    private String type;
    private String context;

    public AlternateDefinition() {
    }

    public AlternateDefinition (DefinitionModel definitionModel) {
        setName(definitionModel.getDefinition());
        setContext(definitionModel.getContext().getName());
        setType(definitionModel.getDeflName());
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
}
