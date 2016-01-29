package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.CSRefDocModel;

import java.util.List;


public interface CSRefDocDAO
{
    List<CSRefDocModel> getCSRefDocsByDEIdseq( String deIdseq );
}
