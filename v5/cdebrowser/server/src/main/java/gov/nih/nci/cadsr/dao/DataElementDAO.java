package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementModel;

import java.util.List;

public interface DataElementDAO
{
    List<DataElementModel> getCdeBySearchString(String DataElementSql);
//    DataElementModel getCdeByCdeIdseq(String CdeIdseq);


    //public void setDataElementSql( String dataElementSql );

}
