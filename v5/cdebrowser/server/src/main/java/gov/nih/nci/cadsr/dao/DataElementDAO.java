package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementModel;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface DataElementDAO
{
    List<DataElementModel> getCdeBySearchString(String DataElementSql);
    DataElementModel getCdeByDeIdseq(String deIdseq) throws EmptyResultDataAccessException;
    List<DataElementModel> getAllCdeByCdeId(Integer cdeId);
    DataElementModel geCdeByCdeIdAndVersion(Integer cdeId, Integer version);



        //public void setDataElementSql( String dataElementSql );

}
