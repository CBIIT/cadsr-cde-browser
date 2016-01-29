package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.UsageModel;

import java.util.List;

public interface UsageDAO
{
    List<UsageModel> getUsagesByDeIdseq( String deIdseq );
}
