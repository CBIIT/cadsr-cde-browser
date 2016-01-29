package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AcRegistrationsModel;

public interface AcRegistrationsDAO
{
    AcRegistrationsModel getAcRegistrationByAcIdseq( String acIdseq );
}
