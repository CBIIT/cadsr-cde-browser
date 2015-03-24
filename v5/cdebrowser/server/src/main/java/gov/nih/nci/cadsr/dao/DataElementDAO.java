package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementModel;

import java.util.List;

public interface DataElementDAO
{
    public List<DataElementModel> getCdeByContextId();
    public void setDataElementSql( String dataElementSql );

}
