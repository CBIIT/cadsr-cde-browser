package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.dao.ProgramAreaDAOImpl;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lernermh
 * Date: 3/19/15
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class RestControllerCommon
{
    private ProgramAreaDAOImpl programAreaDAO;

    public RestControllerCommon()
    {

    }
    public ProgramAreaDAOImpl getProgramAreaDAO()
    {
        return programAreaDAO;
    }

    public void setProgramAreaDAO( ProgramAreaDAOImpl programAreaDAO )
    {
        this.programAreaDAO = programAreaDAO;
    }

    protected List<ProgramAreaModel> getProgramAreaList()
    {
        List<ProgramAreaModel> programAreaModelList = programAreaDAO.getAllProgramAreas();
        Collections.sort( programAreaModelList, new ProgramAreaComparator() );
        return programAreaModelList;
    }

    private class ProgramAreaComparator implements Comparator<ProgramAreaModel>
    {
        @Override
        public int compare( ProgramAreaModel a, ProgramAreaModel b )
        {
            return a.getPalName().toUpperCase().compareTo( b.getPalName().toUpperCase() );

        }
    }
}
