package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.service.model.context.ContextNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CDEDataController
{

    private Logger logger = LogManager.getLogger( CDEDataController.class.getName() );

    @RequestMapping(value = "/CDEData")
    @ResponseBody
    public DataElementModel CDEDataController(@RequestParam("deIdseq") String deIdseq)
    {
        logger.debug( "Received rest call \"CDEData\": " + deIdseq );

        DataElementModel dataElementModel = new DataElementModel();

        return dataElementModel;
    }

}
