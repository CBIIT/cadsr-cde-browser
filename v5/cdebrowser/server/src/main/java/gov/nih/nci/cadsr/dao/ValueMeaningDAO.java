package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ValueMeaningModel;
import gov.nih.nci.cadsr.dao.model.ValueMeaningUiModel;

import java.util.List;

public interface ValueMeaningDAO
{
    List<ValueMeaningModel> getValueMeaningsByPvIdseq( String pvIdseq);
    List<ValueMeaningModel> getValueMeaningsByIdAndVersion( String id, String version);
    List<ValueMeaningModel> getValueMeaningsByCdeIdAndVersion( String cdeId, String version );
    List<ValueMeaningModel> getValueMeaningsByCdeIdAndVersion( int cdeId, float version );
    List<ValueMeaningUiModel> getUiValueMeanings( int cdeId, float version );

}
