package gov.nih.nci.cadsr.service.search;

public abstract class AbstractSearchQueryBuilder
{
    public static int NAME_FIELD = 0;
    public static int PUBLIC_ID_FIELD = 1;

    public static String REPLACE_TOKEN = "SRCSTR";

    public static String wkFlowFrom = " , sbr.ac_status_lov_view asl ";
    public static String workFlowWhere = " and de.asl_name = asl.asl_name (+)";


    //////////////////////////////////////////////////////////////////
    //Will eventually be set as a preference or settings from client.
    public static String CONTEXT_EXCLUDES = "\'TEST\', \'Training\'";
    protected String[] regStatusesWhere = { "ALL" };
    protected String[] statusWhere = { "ALL" };
    protected String[] excludeArr = { "Retired" };
    protected String altName = "";
    public static String[] aslNameExcludeList =  {"CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "RETIRED ARCHIVED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN"};

    // This note was in the source could of the previous version: "release 3.0 updated to add display order for registration status"
    public String registrationFrom = " , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl";
    public String registrationWhere = " and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) ";

    //jspValueDomain is in advancedSearch_inc.jsp associated with a field labeled “Search for Value Domains”.  It’s an odd hidden field associated with a “LOV” and with a text field that is disabled.  There’s a DB table for value_domains.  Value Domain (VD), Value Meaning (VM) and Permissible Values (PV) all contribute to the data about what can be the answers to questions.
    public String valueDomain = "";
}
