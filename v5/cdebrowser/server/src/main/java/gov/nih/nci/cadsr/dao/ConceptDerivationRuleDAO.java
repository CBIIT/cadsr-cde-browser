package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;

/**
 * Created by lavezzojl on 4/16/15.
 */
public interface ConceptDerivationRuleDAO {
    ConceptDerivationRuleModel getCDRByIdseq(String condrIdseq);
    ConceptDerivationRuleModel getCDRByRepId( String repId );

    }
