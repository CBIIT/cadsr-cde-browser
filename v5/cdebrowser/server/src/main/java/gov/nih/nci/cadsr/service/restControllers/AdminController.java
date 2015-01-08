/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.dao.ClassificationSchemeDAOImpl;
import gov.nih.nci.cadsr.dao.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController
{
    private String status = "";
    private ClassificationSchemeDAOImpl classificationSchemeDAO ;
    @Value("${version}") String version;

    public void setClassificationSchemeDAO( ClassificationSchemeDAOImpl classificationSchemeDAO )
    {
        this.classificationSchemeDAO = classificationSchemeDAO;
    }

    //This rest service is just for testing for now.
    @RequestMapping("/admin")
    public String getAdminData()
    {

        return version;
        /*
        List<ClassificationSchemeModel>  classificationSchemeModesl = this.classificationSchemeDAO.getClassificationSchemes( "A89C651A-17CA-1166-E040-BB89AD43308E" );

        return classificationSchemeModesl.toString();
        */
    }


}
