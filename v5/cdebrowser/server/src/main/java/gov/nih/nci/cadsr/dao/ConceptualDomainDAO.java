package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptualDomainModel;

/**
 * Created by lavezzojl on 4/22/15.
 */
public interface ConceptualDomainDAO {
    ConceptualDomainModel getConceptualDomainByIdseq(String cdIdseq);
}
