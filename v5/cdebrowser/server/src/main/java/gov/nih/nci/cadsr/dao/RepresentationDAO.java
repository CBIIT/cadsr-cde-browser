package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.RepresentationModel;

public interface RepresentationDAO
{
    RepresentationModel getRepresentationByIdseq( String representationIdseq );
    RepresentationModel getRepresentationById( String representationId );

}
