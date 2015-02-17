package gov.nih.nci.cadsr.service.restControllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicSearchController
{

    @RequestMapping(value = "/basicSearch")
    @ResponseBody
    public String basicSearch( @RequestParam("query") String query, @RequestParam("field") String field, @RequestParam("queryType") String queryType)
    {

        return "{\"results\":\"" + query + " " + field + " " + queryType +"\"}";
    }
}
