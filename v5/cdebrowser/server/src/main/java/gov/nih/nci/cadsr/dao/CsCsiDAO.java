/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CsCsiModel;

import java.util.List;

public interface CsCsiDAO
{
    public List<CsCsiModel> getCsCsisByParentCsCsi( String parentCsCsi );
    public List<CsCsiModel> getCsCsisById( String csId );
    public List<CsCsiModel> getAllCsCsis( );

}
