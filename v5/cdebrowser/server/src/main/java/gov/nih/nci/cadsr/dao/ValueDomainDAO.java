package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ValueDomainModel;

public interface ValueDomainDAO
{
    ValueDomainModel getValueDomainByIdseq( String vdIdseq );
}
