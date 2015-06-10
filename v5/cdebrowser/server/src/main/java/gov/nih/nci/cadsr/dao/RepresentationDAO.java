package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.RepresentationModel;

/**
 * Created by lavezzojl on 4/16/15.
 */
public interface RepresentationDAO {
    RepresentationModel getRepresentationByIdseq(String representationIdseq);
    RepresentationModel getRepresentationById(String representationId);

}
