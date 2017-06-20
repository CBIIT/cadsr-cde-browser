package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;

import java.util.List;

public interface DataElementDerivationDAO
{
    DataElementDerivationModel getDataElementDerivationByCdeId( int cdeId );
    List<DataElementDerivationComponentModel> getDataElementDerivationComponentsByCdeId( int cdeId );
	DataElementDerivationModel getDataElementDerivationByCdeIdseq(String deIdseq);
    List<DataElementDerivationComponentModel> getDataElementDerivationComponentsByCdeIdseq(String deIdseq);
    List<String> getDataElementDerivationIdseqList(List<String> deIdseq);//CDEBROWSER-280 OC shall have Derived from DE IDs
}
