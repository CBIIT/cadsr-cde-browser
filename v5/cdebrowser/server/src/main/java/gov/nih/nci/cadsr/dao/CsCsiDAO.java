/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CsCsiModel;

import java.util.List;

public interface CsCsiDAO
{
     List<CsCsiModel> getCsCsisByParentCsCsi( String parentCsCsi );
     List<CsCsiModel> getCsCsisById( String csId );
     List<CsCsiModel> getAllCsCsis( );

    List<CsCsiModel> getCsCsisByAcIdseq(String acIdseq);

    List<CsCsiModel> getAltNamesAndDefsByDataElement(String deIdseq);
}
