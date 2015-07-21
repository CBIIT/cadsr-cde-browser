package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

/**
 * Created by lernermh on 4/22/15.
 */
public class Representation

{
    private int publicId;
    private float version;
    private String formattedVersion;
    private String longName;
    private String shortName;
    private String context;

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public float getVersion()
    {
        return version;
    }

    public void setVersion( float version )
    {
        this.version = version;
        //this will give us at least one digit to the right of the decimal place
        this.formattedVersion = Float.toString( Float.valueOf(version) );

    }

    public String getFormattedVersion()
    {
        return formattedVersion;
    }

    public void setFormattedVersion( String formattedVersion )
    {
        this.formattedVersion = formattedVersion;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
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
