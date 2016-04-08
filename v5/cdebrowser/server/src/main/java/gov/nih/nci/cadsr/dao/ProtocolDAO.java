package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProtocolModel;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;

import java.util.List;

public interface ProtocolDAO
{
    public List<ProtocolModel> getAllProtocols();
    public List<ProtocolModel> getProtocolsByContext( String conteId );
    
    public List<Protocol> getAllProtocolsWithProgramAreaAndContext();
}
