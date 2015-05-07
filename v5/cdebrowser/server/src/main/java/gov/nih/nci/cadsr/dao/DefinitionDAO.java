package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DefinitionModel;

import java.util.List;

/**
 * Created by lavezzojl on 5/6/15.
 */
public interface DefinitionDAO {
    DefinitionModel getDefinitionByDefinIdseq(String definIdseq);

    List<DefinitionModel> getAllDefinitionsByAcIdseq(String acIdseq);
}
