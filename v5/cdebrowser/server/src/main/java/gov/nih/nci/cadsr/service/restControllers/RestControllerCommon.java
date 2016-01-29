package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.ProgramAreaDAOImpl;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.service.model.context.BaseNode;
import gov.nih.nci.cadsr.service.model.context.ContextNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RestControllerCommon
{
    private Logger logger = LogManager.getLogger( RestControllerCommon.class.getName() );

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
