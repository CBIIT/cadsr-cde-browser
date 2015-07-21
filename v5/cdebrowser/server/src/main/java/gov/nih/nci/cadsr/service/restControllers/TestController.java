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
public class TestController
{

    //This rest service is just for testing for now.
    @RequestMapping("/test")
    public String getTestData()
    {

        StringBuffer testPage = new StringBuffer();
        testPage.append( "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Test Page\n" +
                "</body>\n" +
                "</html>" );
        return testPage.toString();
    }

}