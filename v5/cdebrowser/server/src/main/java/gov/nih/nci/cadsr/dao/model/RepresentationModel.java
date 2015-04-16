package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lavezzojl on 4/6/15.
 */
public class RepresentationModel extends BaseModel {
    protected String preferredName;
    protected String longName;
    protected Float version;
    protected ContextModel context;
    protected int publicId;
    protected String idseq;
    private ConceptDerivationRuleModel conceptDerivationRuleModel;

    public String getPreferredName()
    {
        return preferredName;
    }

    public RepresentationModel() {
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

    public String getIdseq()
    {
        return idseq;
    }

    public void setIdseq( String idseq )
    {
        this.idseq = idseq;
    }

    public ConceptDerivationRuleModel getConceptDerivationRuleModel()
    {
        return conceptDerivationRuleModel;
    }

    public void setConceptDerivationRuleModel( ConceptDerivationRuleModel conceptDerivationRuleModel )
    {
        this.conceptDerivationRuleModel = conceptDerivationRuleModel;
    }
}
