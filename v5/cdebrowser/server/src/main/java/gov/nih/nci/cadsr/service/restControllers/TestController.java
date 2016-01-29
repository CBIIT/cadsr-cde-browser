package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.TestDaoImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController
{
    private TestDaoImpl testDao;
    private List<String> tableList = new ArrayList<>();

    public TestController()
    {
        initListAlpha();
    }

    //This rest service is just for testing for now.
    @RequestMapping( "/test" )
    public List<String> getTestDataAlpha()
    {
        List<String> results = new ArrayList<>();

        for( String table : tableList )
        {

            testDao.getRowsByTableCol( table, results );
        }
        return results;
    }

    public List<String> getTestData()
    {
        List<String> results = new ArrayList<>();

        for( String tableCol : tableList )
        {

            testDao.getRowsByTableCol( tableCol, results );
        }
        return results;
    }

    public TestDaoImpl getTestDao()
    {
        return testDao;
    }

    public void setTestDao( TestDaoImpl testDao )
    {
        this.testDao = testDao;
    }

    private void initListAlpha()
    {
        tableList.add( "SBR.AC_CI_BU,AC_IDSEQ" );
        tableList.add( "SBR.AC_CI_BU,CS_CSI_IDSEQ" );
        tableList.add( "SBR.AC_CSI,AC_IDSEQ" );
        tableList.add( "SBR.AC_HISTORIES,ACH_IDSEQ" );
        tableList.add( "SBR.AC_HISTORIES,AC_IDSEQ" );
        tableList.add( "SBR.AC_HISTORIES,SOURCE_AC_IDSEQ" );
        tableList.add( "SBR.AC_REGISTRATIONS,AC_IDSEQ" );
        tableList.add( "SBR.AC_REGISTRATIONS,AR_IDSEQ" );
        tableList.add( "SBR.ADMINISTERED_COMPONENTS,PREFERRED_NAME" );
        tableList.add( "SBR.CLASSIFICATION_SCHEMES,CS_IDSEQ" );
        tableList.add( "SBR.COMPLEX_DATA_ELEMENTS,P_DE_IDSEQ" );
        tableList.add( "SBR.COMPLEX_DATA_ELEMENTS,RULE" );
        tableList.add( "SBR.COMPLEX_DE_RELATIONSHIPS,CDR_IDSEQ" );
        tableList.add( "SBR.COMPLEX_DE_RELATIONSHIPS,C_DE_IDSEQ" );
        tableList.add( "SBR.COMPLEX_DE_RELATIONSHIPS,P_DE_IDSEQ" );
        tableList.add( "SBR.CONCEPTUAL_DOMAINS,CD_IDSEQ" );
        tableList.add( "SBR.CONCEPTUAL_DOMAINS,PREFERRED_NAME" );
        tableList.add( "SBR.CS_CSI,CS_CSI_IDSEQ" );
        tableList.add( "SBR.CS_CSI,CS_IDSEQ" );
        tableList.add( "SBR.CS_CSI,LABEL" );
        tableList.add( "SBR.CS_ITEMS,COMMENTS" );
        tableList.add( "SBR.CS_ITEMS,CSI_IDSEQ" );
        tableList.add( "SBR.CS_ITEMS,CSI_NAME" );
        tableList.add( "SBR.CS_ITEMS,PREFERRED_NAME" );
        tableList.add( "SBR.DATA_ELEMENTS,VD_IDSEQ" );
        tableList.add( "SBR.DATA_ELEMENT_CONCEPTS,PROP_IDSEQ" );
        tableList.add( "SBR.DESIGNATIONS,AC_IDSEQ" );
        tableList.add( "SBR.DESIGNATIONS,DESIG_IDSEQ" );
        tableList.add( "SBR.DESIGNATIONS,NAME" );
        tableList.add( "SBR.ORGANIZATIONS,ORG_IDSEQ" );
        tableList.add( "SBR.UI_ITEMS,UII_IDSEQ" );
        tableList.add( "SBR.UI_ITEM_GENERATORS,UIG_IDSEQ" );
        tableList.add( "SBR.UI_ITEM_HIERARCHIES,UIIH_IDSEQ" );
        tableList.add( "SBR.USER_ACCOUNTS,ORG_IDSEQ" );
        tableList.add( "SBR.VALUE_DOMAINS,CD_IDSEQ" );
        tableList.add( "SBR.VALUE_DOMAINS,PREFERRED_DEFINITION" );
        tableList.add( "SBR.VALUE_DOMAINS,REP_IDSEQ" );
        tableList.add( "SBR.VALUE_DOMAINS,VD_IDSEQ" );
        tableList.add( "SBR.VALUE_MEANINGS,COMMENTS" );
        tableList.add( "SBR.VALUE_MEANINGS,DESCRIPTION" );
        tableList.add( "SBR.VD_PVS,PV_IDSEQ" );
        tableList.add( "SBREXT.AC_CHANGE_HISTORY_EXT,AC_IDSEQ" );
        tableList.add( "SBREXT.AC_CHANGE_HISTORY_EXT,CHANGED_TABLE_IDSEQ" );
        tableList.add( "SBREXT.CONCEPTS_EXT,PREFERRED_NAME" );
        tableList.add( "SBREXT.CON_DERIVATION_RULES_EXT,NAME" );
        tableList.add( "SBREXT.CRF_TOOL_PARAMETER_EXT,PROTO_IDSEQ" );
        tableList.add( "SBREXT.CRF_TOOL_PARAMETER_EXT,QC_IDSEQ" );
        tableList.add( "SBREXT.GS_COMPOSITE,AC_IDSEQ" );
        tableList.add( "SBREXT.GS_TOKENS,AC_IDSEQ" );
        tableList.add( "SBREXT.MATCH_RESULTS_EXT,QC_MATCH_IDSEQ" );
        tableList.add( "SBREXT.MATCH_RESULTS_EXT,RD_MATCH_IDSEQ" );
        tableList.add( "SBREXT.OBJECT_CLASSES_EXT,OC_IDSEQ" );
        tableList.add( "SBREXT.OBJECT_CLASSES_EXT,PREFERRED_DEFINITION" );
        tableList.add( "SBREXT.OBJECT_CLASSES_EXT,PREFERRED_NAME" );
        tableList.add( "SBREXT.PROPERTIES_EXT,PREFERRED_NAME" );
        tableList.add( "SBREXT.PROPERTIES_EXT,PROP_IDSEQ" );

        tableList.add( "SBREXT.PROTOCOLS_EXT,PREFERRED_NAME" );
        tableList.add( "SBREXT.PROTOCOLS_EXT,PROTO_IDSEQ" );
        tableList.add( "SBREXT.PROTOCOL_QC_EXT,PQ_IDSEQ" );
        tableList.add( "SBREXT.PROTOCOL_QC_EXT,PROTO_IDSEQ" );
        tableList.add( "SBREXT.PROTOCOL_QC_EXT,QC_IDSEQ" );
        tableList.add( "SBREXT.QUALIFIER_LOV_EXT,COMMENTS" );
        tableList.add( "SBREXT.QUEST_ATTRIBUTES_EXT,DEFAULT_VALUE" );
        tableList.add( "SBREXT.QUEST_CONTENTS_EXT,DE_IDSEQ" );
        tableList.add( "SBREXT.QUEST_CONTENTS_EXT,PROTO_IDSEQ" );
        tableList.add( "SBREXT.QUEST_CONTENTS_EXT,VP_IDSEQ" );

        tableList.add( "SBREXT.QUEST_VV_EXT,QV_IDSEQ" );

        tableList.add( "SBREXT.REPRESENTATIONS_EXT,PREFERRED_NAME" );
        tableList.add( "SBREXT.REPRESENTATIONS_EXT,REP_IDSEQ" );
        tableList.add( "SBREXT.TOOL_OPTIONS_EXT,VALUE" );
        tableList.add( "SBREXT.TRIGGERED_ACTIONS_EXT,TA_IDSEQ" );
        tableList.add( "SBREXT.UP_SEMANTIC_METADATA_MVW,CONCEPT_CODE" );
        tableList.add( "SBREXT.UP_TYPE_ENUMERATION_MVW,VP_IDSEQ" );
        tableList.add( "SBREXT.VALID_VALUES_ATT_EXT,QC_IDSEQ" );
    }

    private void initTableList()
    {
        tableList.add( "SBR.AC_ACTIONS_MATRIX" );
        tableList.add( "SBR.AC_CI_BU" );
        tableList.add( "SBR.AC_CONTACTS" );
        tableList.add( "SBR.AC_CSI" );
        tableList.add( "SBR.AC_CSI_DISEASE" );
        tableList.add( "SBR.AC_HISTORIES" );
        tableList.add( "SBR.AC_RECS" );
        tableList.add( "SBR.AC_REGISTRATIONS" );
        tableList.add( "SBR.AC_STATUS_LOV" );
        tableList.add( "SBR.AC_SUBJECTS" );
        tableList.add( "SBR.AC_TYPES_LOV" );
        tableList.add( "SBR.AC_WF_BUSINESS_ROLES" );
        tableList.add( "SBR.AC_WF_RULES" );
        tableList.add( "SBR.ACTIONS_LOV" );
        tableList.add( "SBR.ADDR_TYPES_LOV" );
        tableList.add( "SBR.ADMINISTERED_COMPONENTS" );
        tableList.add( "SBR.ADVANCE_RPT_LOV" );
        tableList.add( "SBR.APP_COMPONENT_TYPES_LOV" );
        tableList.add( "SBR.APP_GRANTS" );
        tableList.add( "SBR.APP_OBJECTS" );
        tableList.add( "SBR.APP_OBJECTS_LOV" );
        tableList.add( "SBR.APP_PRIV_LOV" );
        tableList.add( "SBR.APP_ROLES_LOV" );
        tableList.add( "SBR.APP_VERSIONS" );
        tableList.add( "SBR.BUSINESS_ROLES_LOV" );
        tableList.add( "SBR.CD_VMS" );
        tableList.add( "SBR.CHARACTER_SET_LOV" );
        tableList.add( "SBR.CLASSIFICATION_SCHEMES" );
        tableList.add( "SBR.CM_STATES_LOV" );
        tableList.add( "SBR.COMM_TYPES_LOV" );
        tableList.add( "SBR.COMPLEX_DATA_ELEMENTS" );
        tableList.add( "SBR.COMPLEX_DE_RELATIONSHIPS" );
        tableList.add( "SBR.COMPLEX_REP_TYPE_LOV" );
        tableList.add( "SBR.CONCEPTUAL_DOMAINS" );
        tableList.add( "SBR.CONTACT_ADDRESSES" );
        tableList.add( "SBR.CONTACT_COMMS" );
        tableList.add( "SBR.CONTEXTS" );
        tableList.add( "SBR.CS_CSI" );
        tableList.add( "SBR.CS_ITEMS" );
        tableList.add( "SBR.CS_RECS" );
        tableList.add( "SBR.CS_TYPES_LOV" );
        tableList.add( "SBR.CSI_RECS" );
        tableList.add( "SBR.CSI_TYPES_LOV" );
        tableList.add( "SBR.DATA_ELEMENT_CONCEPTS" );
        //tableList.add("SBR.DATA_ELEMENTS" );
        tableList.add( "SBR.DATATYPES_LOV" );
        tableList.add( "SBR.DE_RECS" );
        tableList.add( "SBR.DEC_RECS" );
        tableList.add( "SBR.DEFINITIONS" );
        tableList.add( "SBR.DESIGNATION_TYPES_LOV" );
        tableList.add( "SBR.DESIGNATIONS" );
        tableList.add( "SBR.DOCUMENT_TYPES_LOV" );
        tableList.add( "SBR.FAILED_LOG" );
        tableList.add( "SBR.FORMATS_LOV" );
        tableList.add( "SBR.GROUP_RECS" );
        tableList.add( "SBR.GROUPS" );
        tableList.add( "SBR.GRP_BUSINESS_ROLES" );
        tableList.add( "SBR.LANGUAGES_LOV" );
        tableList.add( "SBR.LIFECYCLES_LOV" );
        tableList.add( "SBR.LOOKUP_LOV" );
        tableList.add( "SBR.META_TEXT" );
        tableList.add( "SBR.META_UTIL_STATUSES" );
        tableList.add( "SBR.OBJECT_CLASSES_LOV" );
        tableList.add( "SBR.OC_CADSR" );
        tableList.add( "SBR.OC_COMPRESULT" );
        tableList.add( "SBR.OC_VD" );
        tableList.add( "SBR.ORGANIZATIONS" );
        tableList.add( "SBR.PERMISSIBLE_VALUES" );
        tableList.add( "SBR.PERSONS" );
        tableList.add( "SBR.PROGRAM_AREAS_LOV" );
        tableList.add( "SBR.PROGRAMS" );
        tableList.add( "SBR.PROPERTIES_LOV" );
        tableList.add( "SBR.REFERENCE_DOCUMENTS" );
        tableList.add( "SBR.REFERENCE_FORMATS_LOV" );
        tableList.add( "SBR.REG_STATUS_LOV" );
        tableList.add( "SBR.REGISTRARS" );
        tableList.add( "SBR.REL_USAGE_LOV" );
        tableList.add( "SBR.RELATIONSHIPS_LOV" );
        tableList.add( "SBR.RL_RUL" );
        tableList.add( "SBR.RULES_LOV" );
        tableList.add( "SBR.S_AC_STANDARDS" );
        tableList.add( "SBR.S_AC_STD_APPLICABILITIES" );
        tableList.add( "SBR.S_CMM_SA_MAP" );
        tableList.add( "SBR.S_CMR_META_MODELS" );
        tableList.add( "SBR.S_COMPLIANCE_STATUS_LOV" );
        tableList.add( "SBR.S_MANDATORY_TYPES_LOV" );
        tableList.add( "SBR.S_STANDARD_ATTRIBUTES" );
        tableList.add( "SBR.S_STANDARDS_LOV" );
        tableList.add( "SBR.SC_CONTEXTS" );
        tableList.add( "SBR.SC_GROUPS" );
        tableList.add( "SBR.SC_USER_ACCOUNTS" );
        tableList.add( "SBR.SECURITY_CONTEXTS_LOV" );
        tableList.add( "SBR.STEWARDS" );
        tableList.add( "SBR.SUBJECTS" );
        tableList.add( "SBR.SUBMITTERS" );
        tableList.add( "SBR.UA_BUSINESS_ROLES" );
        tableList.add( "SBR.UI_AC_TYPES_LOV" );
        tableList.add( "SBR.UI_ACTIVITIES_LOV" );
        tableList.add( "SBR.UI_CONSTRAINTS" );
        tableList.add( "SBR.UI_ELEMENTS" );
        tableList.add( "SBR.UI_ELEMENTS_ITEMS" );
        tableList.add( "SBR.UI_FRAMESETS" );
        tableList.add( "SBR.UI_HIER_LINK_RECS" );
        tableList.add( "SBR.UI_HIERARCHIES" );
        tableList.add( "SBR.UI_IMAGE_TYPES_LOV" );
        tableList.add( "SBR.UI_IMAGES" );
        tableList.add( "SBR.UI_ITEM_GENERATORS" );
        tableList.add( "SBR.UI_ITEM_HIERARCHIES" );
        tableList.add( "SBR.UI_ITEM_IMAGES" );
        tableList.add( "SBR.UI_ITEM_LINK_RECS" );
        tableList.add( "SBR.UI_ITEMS" );
        tableList.add( "SBR.UI_LINK_FRAMESET_RECS" );
        tableList.add( "SBR.UI_LINK_LINK_RECS" );
        tableList.add( "SBR.UI_LINK_PARAMS" );
        tableList.add( "SBR.UI_LINKS" );
        tableList.add( "SBR.UI_METADATA" );
        tableList.add( "SBR.UI_REFERENCE" );
        tableList.add( "SBR.UI_TYPES_LOV" );
        tableList.add( "SBR.UNIT_OF_MEASURES_LOV" );
        tableList.add( "SBR.USER_ACCOUNTS" );
        tableList.add( "SBR.USER_GROUPS" );
        tableList.add( "SBR.VALUE_DOMAINS" );
        tableList.add( "SBR.VALUE_MEANINGS" );
        tableList.add( "SBR.VD_PV_RECS" );
        tableList.add( "SBR.VD_PVS" );
        tableList.add( "SBR.VD_RECS" );
        tableList.add( "SBR.WSGSR_SESSIONS" );
        tableList.add( "SBR.WSGSR_USERDATA" );
        tableList.add( "SBREXT.AC_ATT_CSCSI_EXT" );
        tableList.add( "SBREXT.AC_ATT_TYPES_LOV_EXT" );
        tableList.add( "SBREXT.AC_CHANGE_HISTORY_EXT" );
        tableList.add( "SBREXT.AC_SOURCES_EXT" );
        tableList.add( "SBREXT.AC_SOURCES_HST" );
        tableList.add( "SBREXT.ADMINISTERED_COMPONENTS_HST" );
        tableList.add( "SBREXT.ASL_ACTL_EXT" );
        //tableList.add("SBREXT.CART" );
        //tableList.add("SBREXT.CART_OBJECT" );
        //tableList.add("SBREXT.CDE_CART_ITEMS" );
        tableList.add( "SBREXT.COMPONENT_CONCEPTS_EXT" );
        tableList.add( "SBREXT.COMPONENT_LEVELS_EXT" );
        tableList.add( "SBREXT.CON_DERIVATION_RULES_EXT" );
        tableList.add( "SBREXT.CONCEPT_SOURCES_LOV_EXT" );
        tableList.add( "SBREXT.CONCEPTS_EXT" );
        tableList.add( "SBREXT.CONDITION_COMPONENTS_EXT" );
        tableList.add( "SBREXT.CONDITION_MESSAGE_EXT" );
        tableList.add( "SBREXT.CONTACT_ROLES_EXT" );
        tableList.add( "SBREXT.CRF_TOOL_PARAMETER_EXT" );
        tableList.add( "SBREXT.CUSTOM_DOWNLOAD_TYPES" );
        tableList.add( "SBREXT.DATA_ELEMENTS_HST" );
        tableList.add( "SBREXT.DEC_RELATIONSHIPS" );
        tableList.add( "SBREXT.DEFINITION_TYPES_LOV_EXT" );
        tableList.add( "SBREXT.EUL4_ACCESS_PRIVS" );
        tableList.add( "SBREXT.EUL4_APP_PARAMS" );
        tableList.add( "SBREXT.EUL4_ASM_POLICIES" );
        tableList.add( "SBREXT.EUL4_ASMP_CONS" );
        tableList.add( "SBREXT.EUL4_ASMP_LOGS" );
        tableList.add( "SBREXT.EUL4_BA_OBJ_LINKS" );
        tableList.add( "SBREXT.EUL4_BAS" );
        tableList.add( "SBREXT.EUL4_BATCH_PARAMS" );
        tableList.add( "SBREXT.EUL4_BATCH_QUERIES" );
        tableList.add( "SBREXT.EUL4_BATCH_REPORTS" );
        tableList.add( "SBREXT.EUL4_BATCH_SHEETS" );
        tableList.add( "SBREXT.EUL4_BQ_DEPS" );
        tableList.add( "SBREXT.EUL4_BQ_TABLES" );
        tableList.add( "SBREXT.EUL4_BR_RUNS" );
        tableList.add( "SBREXT.EUL4_DBH_NODES" );
        tableList.add( "SBREXT.EUL4_DOCUMENTS" );
        tableList.add( "SBREXT.EUL4_DOMAINS" );
        tableList.add( "SBREXT.EUL4_ELEM_XREFS" );
        tableList.add( "SBREXT.EUL4_EUL_USERS" );
        tableList.add( "SBREXT.EUL4_EXP_DEPS" );
        tableList.add( "SBREXT.EUL4_EXPRESSIONS" );
        tableList.add( "SBREXT.EUL4_FREQ_UNITS" );
        tableList.add( "SBREXT.EUL4_FUN_ARGUMENTS" );
        tableList.add( "SBREXT.EUL4_FUN_CTGS" );
        tableList.add( "SBREXT.EUL4_FUN_FC_LINKS" );
        tableList.add( "SBREXT.EUL4_FUNCTIONS" );
        tableList.add( "SBREXT.EUL4_GATEWAYS" );
        tableList.add( "SBREXT.EUL4_HI_NODES" );
        tableList.add( "SBREXT.EUL4_HI_SEGMENTS" );
        tableList.add( "SBREXT.EUL4_HIERARCHIES" );
        tableList.add( "SBREXT.EUL4_IG_EXP_LINKS" );
        tableList.add( "SBREXT.EUL4_IHS_FK_LINKS" );
        tableList.add( "SBREXT.EUL4_KEY_CONS" );
        tableList.add( "SBREXT.EUL4_OBJ_DEPS" );
        tableList.add( "SBREXT.EUL4_OBJ_JOIN_USGS" );
        tableList.add( "SBREXT.EUL4_OBJS" );
        tableList.add( "SBREXT.EUL4_PLAN_TABLE" );
        tableList.add( "SBREXT.EUL4_QPP_STATS" );
        tableList.add( "SBREXT.EUL4_SEGMENTS" );
        tableList.add( "SBREXT.EUL4_SEQUENCES" );
        tableList.add( "SBREXT.EUL4_SQ_CRRLTNS" );
        tableList.add( "SBREXT.EUL4_SUB_QUERIES" );
        tableList.add( "SBREXT.EUL4_SUM_BITMAPS" );
        tableList.add( "SBREXT.EUL4_SUM_RFSH_SETS" );
        tableList.add( "SBREXT.EUL4_SUMMARY_OBJS" );
        tableList.add( "SBREXT.EUL4_SUMO_EXP_USGS" );
        tableList.add( "SBREXT.EUL4_VERSIONS" );
        tableList.add( "SBREXT.GS_COMPOSITE" );
        tableList.add( "SBREXT.GS_TABLES_LOV" );
        tableList.add( "SBREXT.GS_TOKENS" );
        //tableList.add("SBREXT.GUEST_LOG" );
        //tableList.add("SBREXT.ICD" );
        tableList.add( "SBREXT.LOADER_DEFAULTS" );
        tableList.add( "SBREXT.MATCH_RESULTS_EXT" );
        tableList.add( "SBREXT.MESSAGE_TYPES_EXT" );
        tableList.add( "SBREXT.OBJECT_CLASSES_EXT" );
        tableList.add( "SBREXT.OC_RECS_EXT" );
        //tableList.add("SBREXT.PASSWORD_NOTIFICATION" );
        //tableList.add("SBREXT.PCOLL_CONTROL" );
        tableList.add( "SBREXT.PERMISSIBLE_VALUES_HST" );
        tableList.add( "SBREXT.PLAN_TABLE" );
        tableList.add( "SBREXT.PROPERTIES_EXT" );
        tableList.add( "SBREXT.PROTOCOL_QC_EXT" );
        tableList.add( "SBREXT.PROTOCOLS_EXT" );
        //tableList.add("SBREXT.PS_TXN" );
        tableList.add( "SBREXT.QC_DISPLAY_LOV_EXT" );
        tableList.add( "SBREXT.QC_DISPLAY_LOV_EXT" );
        tableList.add( "SBREXT.QC_RECS_HST" );
        tableList.add( "SBREXT.QC_TYPE_LOV_EXT" );
        //tableList.add("SBREXT.QUAL_MAP" );
        tableList.add( "SBREXT.QUALIFIER_LOV_EXT" );
        tableList.add( "SBREXT.QUEST_ATTRIBUTES_EXT" );
        tableList.add( "SBREXT.QUEST_CONTENTS_EXT" );
        tableList.add( "SBREXT.QUEST_VV_EXT" );
        tableList.add( "SBREXT.QUESTION_CONDITIONS_EXT" );
        tableList.add( "SBREXT.REPRESENTATION_LOV_EXT" );
        tableList.add( "SBREXT.REPRESENTATIONS_EXT" );
        tableList.add( "SBREXT.REVIEWER_FEEDBACK_LOV_EXT" );
        tableList.add( "SBREXT.RULE_FUNCTIONS_EXT" );
        //tableList.add("SBREXT.SN_ALERT_EXT" );
        //tableList.add("SBREXT.SN_QUERY_EXT" );
        //tableList.add("SBREXT.SN_RECIPIENT_EXT" );
        //tableList.add("SBREXT.SN_REP_CONTENTS_EXT" );
        //tableList.add("SBREXT.SN_REPORT_EXT" );
        tableList.add( "SBREXT.SOURCE_DATA_LOADS" );
        tableList.add( "SBREXT.SOURCES_EXT" );
        tableList.add( "SBREXT.SUBSTITUTIONS_EXT" );
        tableList.add( "SBREXT.TA_PROTO_CSI_EXT" );
        tableList.add( "SBREXT.TEXT_STRINGS_EXT" );
        tableList.add( "SBREXT.TOOL_OPTIONS_EXT" );
        tableList.add( "SBREXT.TOOL_PROPERTIES_EXT" );
        tableList.add( "SBREXT.TRIGGERED_ACTIONS_EXT" );
        tableList.add( "SBREXT.TS_TYPE_LOV_EXT" );
        tableList.add( "SBREXT.UI_MENU_TREE_EXT" );
        tableList.add( "SBREXT.UML_LOADER_DEFAULTS" );
        tableList.add( "SBREXT.UP_ASSOCIATIONS_METADATA_MVW" );
        tableList.add( "SBREXT.UP_ATTRIBUTE_METADATA_MVW" );
        tableList.add( "SBREXT.UP_ATTRIBUTE_TYPE_METADATA_MVW" );
        tableList.add( "SBREXT.UP_CADSR_PROJECT_MVW" );
        tableList.add( "SBREXT.UP_CLASS_METADATA_MVW" );
        tableList.add( "SBREXT.UP_PACKAGES_MVW" );
        tableList.add( "SBREXT.UP_SEMANTIC_METADATA_MVW" );
        tableList.add( "SBREXT.UP_SUB_PROJECTS_MVW" );
        tableList.add( "SBREXT.UP_TYPE_ENUMERATION_MVW" );
        //tableList.add("SBREXT.USER_SECURITY_QUESTIONS" );
        //tableList.add("SBREXT.USERS_LOCKOUT" );
        tableList.add( "SBREXT.VALID_VALUES_ATT_EXT" );
        //tableList.add("SBREXT.VALUE_DOMAINS_HST" );
        //tableList.add("SBREXT.VD_PVS_HST" );
        //tableList.add("SBREXT.VD_PVS_SOURCES_EXT" );
        //tableList.add("SBREXT.VD_PVS_SOURCES_HST" );
    }
}
