package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cadsr.dao.ProgramAreaDAO;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;

@Component("restControllerCommon")
public class RestControllerCommon
{
    private Logger logger = LogManager.getLogger( RestControllerCommon.class.getName() );

    @Autowired
    private ProgramAreaDAO programAreaDAO;

    public RestControllerCommon()
    {

    }

    public ProgramAreaDAO getProgramAreaDAO()
    {
        return programAreaDAO;
    }

    public void setProgramAreaDAO( ProgramAreaDAO programAreaDAO )
    {
        this.programAreaDAO = programAreaDAO;
    }


    protected List<ProgramAreaModel> getProgramAreaList()
    {
        List<ProgramAreaModel> programAreaModelList = programAreaDAO.getAllProgramAreas();//this call does retry on a DB error, or on empty DB return
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
