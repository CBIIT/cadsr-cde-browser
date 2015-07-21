package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;

import java.util.List;

/**
 * Created by lernermh on 6/17/15.
 */
public interface DataElementDerivationDAO
{
   DataElementDerivationModel getDataElementDerivationByCdeId( int cdeId );
   List<DataElementDerivationComponentModel> getDataElementDerivationComponentsByCdeId( int cdeId );
}
