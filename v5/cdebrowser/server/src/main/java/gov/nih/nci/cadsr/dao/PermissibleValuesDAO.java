package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;

import java.util.List;

/**
 * Created by lernermh on 6/3/15.
 */
public interface PermissibleValuesDAO
{
    List<PermissibleValuesModel> getPermissibleValuesByPvIdseq( String pvIdseq);
    List<PermissibleValuesModel> getPermissibleValuesByVdIdseq( String vdIdseq);
}
