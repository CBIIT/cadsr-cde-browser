package gov.nih.nci.cadsr.service;

import java.util.List;

import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;

public interface ClassificationSchemeService 
{
	public List<ClassificationScheme> getClassificationSchemesWithProgramAreaAndContext();

}
