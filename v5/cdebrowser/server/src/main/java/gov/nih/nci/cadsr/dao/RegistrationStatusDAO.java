package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.RegistrationStatusModel;

public interface RegistrationStatusDAO
{
	List<RegistrationStatusModel> getAllRegnStatuses(  );
	List<String> getRegnStatusesAsList();
}
