package gov.nih.nci.cadsr.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.cadsr.dao.ClassificationSchemeDAO;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;

@Service("classificationSchemeService")
public class ClassificationSchemeServiceImpl implements ClassificationSchemeService
{
	private Logger logger = LogManager.getLogger(ClassificationSchemeServiceImpl.class.getName() );
	
	@Autowired
	private ClassificationSchemeDAO classificationSchemeDAO;

	@Override
	public List<ClassificationScheme> getClassificationSchemesWithProgramAreaAndContext()
	{
		logger.debug("Fetching the list of all classifications schemes with program area and context iddeq.");
		List<ClassificationScheme> csList = classificationSchemeDAO.getAllClassificationSchemeWithProgramAreaAndContext();
		if (csList == null || csList.size() == 0)
		{
			csList = new ArrayList<ClassificationScheme>();
		}
			
		return csList;
	}

}
