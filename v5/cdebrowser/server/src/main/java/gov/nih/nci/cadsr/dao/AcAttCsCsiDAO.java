package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AcAttCsCsiModel;

import java.util.List;

public interface AcAttCsCsiDAO
{
    List<AcAttCsCsiModel> getAllAcAttCsCsiByAttIdseq( String attIdseq );
}
