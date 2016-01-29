package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProtocolModel;

import java.util.List;

public interface ProtocolDAO
{
    public List<ProtocolModel> getAllProtocols();
    public List<ProtocolModel> getProtocolsByContext( String conteId );
}
