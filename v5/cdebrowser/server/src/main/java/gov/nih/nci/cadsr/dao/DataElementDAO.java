package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementModel;

import java.util.List;

public interface DataElementDAO
{
    List<DataElementModel> getCdeBySearchString(String DataElementSql);
    DataElementModel getCdeByDeIdseq(String deIdseq);
    List<DataElementModel> getAllCdeByCdeId(Integer cdeId);
    DataElementModel geCdeByCdeIdAndVersion(Integer cdeId, Integer version);



        //public void setDataElementSql( String dataElementSql );

}
