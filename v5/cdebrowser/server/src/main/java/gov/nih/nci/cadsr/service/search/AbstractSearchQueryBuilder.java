package gov.nih.nci.cadsr.service.search;

public abstract class AbstractSearchQueryBuilder
{
    public static int NAME_FIELD = 0;
    public static int PUBLIC_ID_FIELD = 1;
    public static String[] aslNameExcludeList =  {"CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "RETIRED ARCHIVED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN"};
    public static String REPLACE_TOKEN = "SRCSTR";

    //Will eventually be set as a preference.
    public static String CONTEXT_EXCLUDES = "\'TEST\', \'Training\'";
}
