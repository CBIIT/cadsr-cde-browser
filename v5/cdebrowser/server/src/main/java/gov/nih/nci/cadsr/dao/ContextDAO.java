/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;

import java.util.Collection;
import java.util.List;

public interface ContextDAO
{
    public static final String CTEP = "CTEP";

    public List<ContextModel> getAllContexts();
    public List<ContextModel> getContextsByName( String name );
    public ContextModel getContextById( String id );
    public Collection getContexts( String username, String businessRole );

    /**
     * Gets all the contexts excluding the excludeList
     */
    public List getAllContexts( String excludeList );
}