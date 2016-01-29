package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class ConceptModel extends BaseModel
{
    private String conceptName;
    private String conceptCode;
    private String publicId;
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

    public String getPublicId()
    {
        return publicId;
    }

    public void setPublicId( String publicId )
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

    @Override
    public String toString()
    {
        return "ConceptModel{" +
                "conceptName='" + conceptName + '\'' +
                ", conceptCode='" + conceptCode + '\'' +
                ", publicId='" + publicId + '\'' +
                ", definitionSource='" + definitionSource + '\'' +
                ", evsSource='" + evsSource + '\'' +
                ", primary='" + primary + '\'' +
                "}";
    }


}
