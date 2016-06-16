package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModel;
/**
 * This class is to support PVs' VM Classifications to Designation and Definitions. See CDEBROWSER-437
 * @author asafievan
 *
 */
public interface CsCsiValueMeaningDAO
{
	/**
	 * 
	 * @param vmIdseq Value Meaning unique ID
	 * @return List of CsCsiValueMeaningModel related to Value Meaning with ID vmid
	 */
	public List<CsCsiValueMeaningModel> getCsCsisByVmId(String vmIdseq);
	
}
