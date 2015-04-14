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
}
