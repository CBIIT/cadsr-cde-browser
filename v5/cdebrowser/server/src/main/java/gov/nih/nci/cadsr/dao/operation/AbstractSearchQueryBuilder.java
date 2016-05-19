package gov.nih.nci.cadsr.dao.operation;

public abstract class AbstractSearchQueryBuilder
{
    public static int NAME_FIELD = 0;
    public static int PUBLIC_ID_FIELD = 1;

    public static String REPLACE_TOKEN = "SRCSTR";

    public static String wkFlowFrom = " , sbr.ac_status_lov_view asl ";
    public static String workFlowWhere = " AND de.asl_name = asl.asl_name (+)";

    public static String selectClause = "SELECT DISTINCT de.de_idseq "
            + "      ,de.preferred_name de_preferred_name"
            + "      ,de.long_name "
            + "      ,rd.doc_text "
            + "      ,conte.name "
            + "      ,de.asl_name "
            + "      ,to_char(de.cde_id) de_cdeid"
            + "      ,de.version de_version "
            + "      ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby "
            + "      ,de.vd_idseq "
            + "      ,de.dec_idseq "
            + "      ,de.conte_idseq "
            + "      ,de.preferred_definition "
            + "      ,acr.registration_status "
            + "      ,rsl.display_order "
            + "      ,asl.display_order wkflow_order "
            + "      ,de.cde_id cdeid";

////////////////////////////////////
public static String wkFlowDbField = "asl.asl_name";

////////////////////////////////////

	//FIXME remove excludeArr
    //////////////////////////////////////////////////////////////////
	//TODO this array is a placeholder to represent a list of registration statuses for Advanced search
    protected String[] regStatusesWhere = { "ALL" };
	//TODO this array is a placeholder to represent a list of workflow statuses for Advanced search
    protected String[] statusWhere = { "ALL" };
    
    //protected String[] excludeArr = { "Retired" };
    protected String altName = "";
    
    // replaced public static String[] aslNameExcludeList = { "CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "RETIRED ARCHIVED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN" };
    
    //Search in the following field(s) in Advanced search
    protected String[] searchIn = { "ALL" };
    
    //jspValueDomain is in advancedSearch_inc.jsp associated with a field labeled “Search for Value Domains”.  It’s an odd hidden field associated with a “LOV” AND with a text field that is disabled.  There’s a DB table for value_domains.  Value Domain (VD), Value Meaning (VM) AND Permissible Values (PV) all contribute to the data about what can be the answers to questions.
    public String valueDomain = "";
}
