package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

/**
 * Created by lerner on 4/20/15.
 */
public class AlternateDefinition
{
    private String name;
    private String type;
    private String context;

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
