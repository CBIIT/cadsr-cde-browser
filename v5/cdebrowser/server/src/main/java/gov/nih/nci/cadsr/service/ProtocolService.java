package gov.nih.nci.cadsr.service;

import java.util.List;

import gov.nih.nci.cadsr.service.model.cdeData.Protocol;

public interface ProtocolService 
{
	public List<Protocol> getProtocolsWithProgramAreaAndContext(String contexIdSeq, String protocolOrForm);

}