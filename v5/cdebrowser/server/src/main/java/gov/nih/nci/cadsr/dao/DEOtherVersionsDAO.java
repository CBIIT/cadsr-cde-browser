package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;

import java.util.List;

/**
 * Created by lavezzojl on 5/22/15.
 */
public interface DEOtherVersionsDAO {
    List<DEOtherVersionsModel> getOtherVersions(Integer publicId, String deIdseq);
}
