package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.PropertyModel;

public interface PropertyDAO
{
    PropertyModel getPropertyByIdseq( String propIdseq );
}
