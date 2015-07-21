/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ProtocolModel;

import java.util.List;

public interface ProtocolDAO
{
    public List<ProtocolModel> getAllProtocols();
    public List<ProtocolModel> getProtocolsByContext( String conteId );
}
