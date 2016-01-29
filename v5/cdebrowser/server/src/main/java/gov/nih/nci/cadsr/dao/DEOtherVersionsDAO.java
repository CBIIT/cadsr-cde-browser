package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;

import java.util.List;

public interface DEOtherVersionsDAO
{
    List<DEOtherVersionsModel> getOtherVersions( Integer publicId, String deIdseq );
}
