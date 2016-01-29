package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.CSIRefDocModel;

import java.util.List;

public interface CSIRefDocDAO
{
    List<CSIRefDocModel> getCSIRefDocsByDEIdseq( String deIdseq );
}
