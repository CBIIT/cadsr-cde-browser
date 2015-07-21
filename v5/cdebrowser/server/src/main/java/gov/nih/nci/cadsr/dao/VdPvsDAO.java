package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.VdPvsModel;

import java.util.List;

/**
 * Created by lernermh on 6/3/15.
 */
public interface VdPvsDAO
{
    List<VdPvsModel> getVdPvs( String vdIdseq);
}
