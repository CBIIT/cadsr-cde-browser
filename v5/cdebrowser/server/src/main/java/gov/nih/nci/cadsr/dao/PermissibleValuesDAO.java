package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;

import java.util.List;

public interface PermissibleValuesDAO
{
    List<PermissibleValuesModel> getPermissibleValuesByPvIdseq( String pvIdseq );
    List<PermissibleValuesModel> getPermissibleValuesByVdIdseq( String vdIdseq );
}
