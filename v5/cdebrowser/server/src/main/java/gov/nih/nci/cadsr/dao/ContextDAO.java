package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ContextModel;

import java.util.Collection;
import java.util.List;

public interface ContextDAO
{
    public static final String CTEP = "CTEP";

    public List<ContextModel> getAllContexts();
    public List<ContextModel> getContextsByName( String name );
    public ContextModel getContextByIdseq( String contextIdseq );
    public Collection getContexts( String username, String businessRole );

    /**
     * Gets all the contexts excluding the excludeList
     */
    public List getAllContexts( String excludeList );
}
