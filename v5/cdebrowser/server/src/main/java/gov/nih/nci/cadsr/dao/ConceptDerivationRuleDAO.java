package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;

public interface ConceptDerivationRuleDAO
{
    ConceptDerivationRuleModel getCDRByIdseq( String condrIdseq );
    ConceptDerivationRuleModel getCDRByRepId( String repId );

}
