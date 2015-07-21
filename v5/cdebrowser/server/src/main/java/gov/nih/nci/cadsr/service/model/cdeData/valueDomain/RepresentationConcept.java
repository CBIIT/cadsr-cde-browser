package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

/**
 * Created by lernermh on 4/22/15.
 * This is not used - see ConceptModel
 *
 */
public class RepresentationConcept
{
    private String conceptName;
    private String conceptCode;
    private int publicId;
    private String definitionSource;
    private String evsSource;
    private String primary;

    public String getConceptName()
    {
        return conceptName;
    }

    public void setConceptName( String conceptName )
    {
        this.conceptName = conceptName;
    }

    public String getConceptCode()
    {
        return conceptCode;
    }

    public void setConceptCode( String conceptCode )
    {
        this.conceptCode = conceptCode;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public String getDefinitionSource()
    {
        return definitionSource;
    }

    public void setDefinitionSource( String definitionSource )
    {
        this.definitionSource = definitionSource;
    }

    public String getEvsSource()
    {
        return evsSource;
    }

    public void setEvsSource( String evsSource )
    {
        this.evsSource = evsSource;
    }

    public String getPrimary()
    {
        return primary;
    }

    public void setPrimary( String primary )
    {
        this.primary = primary;
    }
}
