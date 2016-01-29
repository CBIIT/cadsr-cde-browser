package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;

import java.util.List;

interface ReferenceDocDAO
{
    List<ReferenceDocModel> getRefDocsByRdIdseq( String rdIdseq );
    List<ReferenceDocModel> getRefDocsByAcIdseq( String acIdseq );
}
