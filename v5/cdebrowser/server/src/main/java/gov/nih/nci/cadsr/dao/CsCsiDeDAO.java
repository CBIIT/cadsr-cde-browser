package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
/**
 * This class is to support DEs' Classifications to Designation and Definitions. See CDEBROWSER-647
 * @author asafievan
 *
 */
public interface CsCsiDeDAO
{
	/**
	 * 
	 * @param deIdseq Value Meaning unique ID
	 * @return List of CsCsiValueMeaningModel related to Value Meaning with ID deIdseq
	 */
	public List<CsCsiDeModel> getCsCsisByDeId(String deIdseq);
	public List<DesignationModelAlt> getCsCsiDeAltNamesById(String deIdseq, List<CsCsiDeModel> csCsiDeModels);
	public List<DefinitionModelAlt> getCsCsiDeDefinitionsById(String deIdseq, List<CsCsiDeModel> csCsiDeModels);
}
