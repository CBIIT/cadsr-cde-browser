package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.VdPvsModel;
import gov.nih.nci.ncicb.cadsr.common.dto.ValidValueTransferObject;

import java.util.List;

public interface VdPvsDAO
{
    List<VdPvsModel> getVdPvs( String vdIdseq );
}
