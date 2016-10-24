package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import gov.nih.nci.cadsr.dao.model.DataElementModel;

public interface DataElementDAO
{
    List<DataElementModel> getCdeBySearchString( String DataElementSql );
    DataElementModel getCdeByDeIdseq( String deIdseq ) throws EmptyResultDataAccessException;
    List<DataElementModel> getCdeByDeIdseqList(List<String> deIdseqSet) throws EmptyResultDataAccessException;
    List<DataElementModel> getAllCdeByCdeId( Integer cdeId );
    DataElementModel geCdeByCdeIdAndVersion( Integer cdeId, Number version );


    //public void setDataElementSql( String dataElementSql );

}
