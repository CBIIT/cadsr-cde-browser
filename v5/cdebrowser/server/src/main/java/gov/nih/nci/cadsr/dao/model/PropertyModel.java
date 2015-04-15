package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lavezzojl on 4/6/15.
 */
public class PropertyModel extends BaseModel {
    private String preferredName;
    private String longName;
    private Float version;
    private ContextModel context;
    private int publicId;
    private String name;
    private String qualifier;

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
    }

    public ContextModel getContext()
    {
        return context;
    }

    public void setContext( ContextModel context )
    {
        this.context = context;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getQualifier()
    {
        return qualifier;
    }

    public void setQualifier( String qualifier )
    {
        this.qualifier = qualifier;
    }
}
