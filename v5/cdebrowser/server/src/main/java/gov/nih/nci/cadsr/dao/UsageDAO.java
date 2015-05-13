package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.UsageModel;

import java.util.List;

/**
 * Created by lavezzojl on 5/13/15.
 */
public interface UsageDAO {
    List<UsageModel> getUsagesByDeIdseq(String deIdseq);
}
