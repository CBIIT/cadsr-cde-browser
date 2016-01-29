package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptualDomainModel;

public interface ConceptualDomainDAO
{
    ConceptualDomainModel getConceptualDomainByIdseq( String cdIdseq );
}
