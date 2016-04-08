package gov.nih.nci.cadsr.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.cadsr.dao.ProtocolDAO;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;

@Service("protocolService")
public class ProtocolServiceImpl implements ProtocolService
{
	private Logger logger = LogManager.getLogger(ProtocolServiceImpl.class.getName() );
	
	@Autowired
	ProtocolDAO protocolDAO;

	@Override
	public List<Protocol> getProtocolsWithProgramAreaAndContext()
	{
		logger.debug("Fetching the list of all protocols with program area and context iddeq.");
		List<Protocol> protoList = protocolDAO.getAllProtocolsWithProgramAreaAndContext();
		if (protoList == null || protoList.size() == 0)
		{
			protoList = new ArrayList<Protocol>();
		}
			
		return protoList;
	}

}
