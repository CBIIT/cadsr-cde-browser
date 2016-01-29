package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import org.springframework.dao.EmptyResultDataAccessException;

public interface DataElementConceptDAO
{

    DataElementConceptModel getDecByDecIdseq( String decIdseq ) throws EmptyResultDataAccessException;

}
