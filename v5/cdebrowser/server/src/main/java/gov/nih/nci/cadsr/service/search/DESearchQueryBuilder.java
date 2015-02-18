package gov.nih.nci.cadsr.service.search;/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

import gov.nih.nci.cadsr.common.util.SimpleSortableColumnHeader;
import gov.nih.nci.cadsr.common.util.SortableColumnHeader;
import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class will be used to build the sql query for CDE Browser's
 * data element search page. The basis for the resulting query is the user request.
 *
 * @author Ram Chilukuri
 */
public class DESearchQueryBuilder extends Object
{
    private static Logger logger = LogManager.getLogger( DESearchQueryBuilder.class.getName() );

    private final static String REPLACE_TOKEN = "SRCSTR";

    private String searchStr = "";
    private String whereClause = "";
    private String[] strArray = null;
    private StringBuffer workflowList = null;
    private String xmlQueryStmt = "";
    private String vdPrefName = "";
    private String csiName = "";
    private String decPrefName = "";
    private String sqlStmt = "";
    private String treeParamIdSeq = "";
    private String treeParamRegStatus = null;
    private String treeParamType = "";
    private String treeConteIdSeq = "";

    //Does this get used?
    private Object[] queryParams = new Object[]{ "%", "%", "%", "%", "%" };

    private String contextUse = "";
    private String orderBy = " ORDER BY long_name, de_version ";
    private String sqlWithoutOrderBy;
    private SortableColumnHeader sortColumnHeader = null;

    //public gov.nih.nci.cadsr.service.search.DESearchQueryBuilder( HttpServletRequest request,
    public DESearchQueryBuilder( TempTestParameters request,
                                 String treeParamType,
                                 String treeParamIdSeq,
                                 String treeConteIdSeq, DataElementSearchBean searchBean, String query, String searchMode )
    {


        if( treeParamType != null &&
                ( treeParamType.equalsIgnoreCase( "REGCSI" ) ||
                treeParamType.equalsIgnoreCase( "REGCS" ) ) )
        {
            String[] subStr = treeParamIdSeq.split( "," );
            this.treeParamIdSeq = subStr[0];
            this.treeParamRegStatus = subStr[1];
        }
        else
        {
            this.treeParamIdSeq = treeParamIdSeq;
        }


        this.treeParamType = treeParamType;
        this.treeConteIdSeq = treeConteIdSeq;
        strArray = request.getParameterValues( "SEARCH" );
        System.out.println( " strArray: " );

        // Loop over string indexes strArray for-loop.
        for( int i = 0; i < strArray.length; i++ )
        {
            System.out.println( strArray[i] );
        }

        vdPrefName = request.getParameter( "txtValueDomain" );
        System.out.println( " txtValueDomain: " + vdPrefName );


        decPrefName = request.getParameter( "txtDataElementConcept" );
        System.out.println( " txtDataElementConcept: " + decPrefName );


        csiName = request.getParameter( "txtClassSchemeItem" );
        System.out.println( " txtClassSchemeItem: " + csiName );

        String selIndex = null;

        contextUse = request.getParameter( "contextUse" );
        if( contextUse == null )
        {
            contextUse = "";
        }
        System.out.println( " contextUse: " + contextUse );


        String usageWhere = "";

        String searchStr0 = "";
        String searchStr2 = "";
        String searchStr3 = "";
        String searchStr4 = "";
        String searchStr5 = "";
        String searchStr6 = "";
        String searchStr8 = "";
        String latestWhere = "";
        String csiWhere = "";
        String fromClause = "";
        String vdFrom = "";
        String decFrom = "";
        String conceptName = "";
        String conceptCode = "";
        String vdType = "";
        String cdeType = "";
        String deDerivWhere = "";
        String deDerivFrom = "";
        StringBuffer whereBuffer = new StringBuffer();

        // release 3.0 updated to add display order for registration status
        String registrationFrom = " , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl";

        //Added for preferences
        String registrationWhere = " and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) ";

        String registrationExcludeWhere = "";

        if( searchBean != null )
        {

            //FIXME - hard coded for dev time
            //String[] excludeArr = searchBean.getRegStatusExcludeList();
            String[] excludeArr = {"Retired"};

                    System.out.println("excludeArr: [" + Arrays.toString( excludeArr )+"]");

            if( !StringUtils.isArrayWithEmptyStrings( excludeArr ) )
            {
                System.out.println("registrationExcludeWhere: " + registrationExcludeWhere);
                System.out.println(" searchBean.getExcludeWhereCluase( \"nvl(acr.registration_status,'-1')\", excludeArr ): " +  searchBean.getExcludeWhereCluase( "nvl(acr.registration_status,'-1')", excludeArr ));
                registrationExcludeWhere = " and " + searchBean.getExcludeWhereCluase( "nvl(acr.registration_status,'-1')", excludeArr );
            }
        }


        String wkFlowFrom = " , sbr.ac_status_lov_view asl ";
        String workFlowWhere = " and de.asl_name = asl.asl_name (+)";
        //Added for preferences
        String workflowExcludeWhere = "";
        if( searchBean != null )
        {
            System.out.println(" searchBean != null ");

            //FIXME dev time testing
String[] temp =  {"CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "RETIRED ARCHIVED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN"};
searchBean.setAslNameExcludeList( temp );

            String[] excludeArr = searchBean.getAslNameExcludeList();
            System.out.println("excludeArr: " + Arrays.toString(excludeArr));
            if( !StringUtils.isArrayWithEmptyStrings( excludeArr ) )
            {
                System.out.println("  !gov.nih.nci.cadsr.common.util.StringUtils.isArrayWithEmptyStrings( excludeArr ) ");
                workflowExcludeWhere = " and " + searchBean.getExcludeWhereCluase( "asl.asl_name", excludeArr );
            }
        }

        String contextExludeWhere = "";

        //String contextExludeToExclude = searchBean.getExcludeContextList();
        //FIXME dev time testing
        String contextExludeToExclude = "\'TEST\', \'Training\'";


        if( !contextExludeToExclude.equals( "" ) )
        {
            contextExludeWhere = " and conte.name NOT IN (" + contextExludeToExclude + " )";
        }
        System.out.println("workflowExcludeWhere: [" + workflowExcludeWhere + "]");
        System.out.println("contextExludeToExclude: [" + contextExludeToExclude+ "]");


        /********************************************************************************************************************************************/
        /********************************************************************************************************************************************/
        /********************************************************************************************************************************************/
        /********************************************************************************************************************************************/
        /********************************************************************************************************************************************/
        /********************************************************************************************************************************************/
        if( strArray == null )
        {
            searchStr = "";
            whereClause = "";
            selIndex = "";

            if( this.treeParamRegStatus != null )
            {
                whereBuffer.append( " and acr.registration_status = '" + this.treeParamRegStatus + "'" );
            }
        }
        else
        {
            searchStr0 = StringUtils.replaceNull( query );
            System.out.println("searchStr0: " + searchStr0);
            String[] searchStr1 = request.getParameterValues( "jspStatus" );
            System.out.println("jspStatus: " + Arrays.toString( searchStr1 ));

            String[] searchStr7 = request.getParameterValues( "regStatus" );
            System.out.println("regStatus: " + Arrays.toString(searchStr7));

            String[] searchStr9 = request.getParameterValues( "altName" );
            System.out.println("altName: " + Arrays.toString(searchStr9));

            String[] searchIn = request.getParameterValues( "jspSearchIn" );
            System.out.println("jspSearchIn: " + Arrays.toString(searchIn));

            String validValue =
                    StringUtils.replaceNull( request.getParameter( "jspValidValue" ) );
            System.out.println("jspValidValue: " + validValue);

            String objectClass =
                    StringUtils.replaceNull( request.getParameter( "jspObjectClass" ) );
            System.out.println("jspObjectClass: " + objectClass);

            String property =
                    StringUtils.replaceNull( request.getParameter( "jspProperty" ) );
            System.out.println("jspProperty: " + property);
            boolean doStatusSearch = false;
            if( searchStr1 != null )
            {
                if( !StringUtils.containsKey( searchStr1, "ALL" ) )
                {
                    doStatusSearch = true;
                }
            }

            boolean doRegStatusSearch = false;
            //check if registration status is selected
            if( searchStr7 != null )
            {
                if( !StringUtils.containsKey( searchStr7, "ALL" ) )
                {
                    doRegStatusSearch = true;
                }
            }

            searchStr2 =
                    StringUtils.replaceNull( request.getParameter( "jspValueDomain" ) );
            System.out.println("jspValueDomain: " + searchStr2);

            searchStr3 = StringUtils.replaceNull( request.getParameter( "jspCdeId" ) );
            System.out.println("jspCdeId: " + searchStr3);

            searchStr4 =
                    StringUtils.replaceNull( request.getParameter( "jspDataElementConcept" ) );
            System.out.println("jspDataElementConcept: " + searchStr4);

            searchStr5 =
                    StringUtils.replaceNull( request.getParameter( "jspClassification" ) );
            System.out.println("jspDataElementConcept: " + searchStr5);

            searchStr6 =
                    StringUtils.replaceNull( request.getParameter( "jspLatestVersion" ) );
            System.out.println("jspLatestVersion: " + searchStr6);

            searchStr8 =
                    StringUtils.replaceNull( request.getParameter( "jspAltName" ) );
            System.out.println("jspAltName: " + searchStr8);

            conceptName =
                    StringUtils.replaceNull( request.getParameter( "jspConceptName" ) );
            System.out.println("jspConceptName: " + conceptName);

            conceptCode =
                    StringUtils.replaceNull( request.getParameter( "jspConceptCode" ) );
            System.out.println("jspConceptCode: " + conceptCode);

            vdType =
                    StringUtils.replaceNull( request.getParameter( "jspVDType" ) );
            System.out.println("jspVDType: " + vdType );

            cdeType =
                    StringUtils.replaceNull( request.getParameter( "jspCDEType" ) );
            System.out.println("jspCDEType: " + cdeType );

            /********************************************************************************************************************************************/
            /********************************************************************************************************************************************/
            /********************************************************************************************************************************************/
            /********************************************************************************************************************************************/
            /********************************************************************************************************************************************/
            /********************************************************************************************************************************************/


//      if (searchStr6.equals("Yes")) {
//        latestWhere = " and de.latest_version_ind = 'Yes' ";
//      }
//      else {
//        latestWhere = "";
//      }
//
//      if((treeParamType!=null)&&(treeParamType.equals("CRF")||treeParamType.equals("TEMPLATE")))
//      {
//        if(!searchStr6.equals("Yes")||searchStr6.equals(""))
//          latestWhere = "";
//      }




            //set filter on "version"
            if( searchStr6.equals( "No" ) ) //*********************************************
            {
                latestWhere = ""; // advance search, return all version if user choose so
            }
            else if( ( treeParamType != null ) && ( treeParamType.equals( "CRF" ) || treeParamType.equals( "TEMPLATE" )
                    || treeParamType.equals( "CSI" ) || treeParamType.equals( "CLASSIFICATION" ) ) )
            {
                latestWhere = ""; //Tree expanded search, return all version
            }
            else
            {
                latestWhere = " and de.latest_version_ind = 'Yes' "; //basic search, only return the latest version as default
            }

            //search within results
            //remove "version" filtering and return all object within previously found data
//HttpSession session = request.getSession( false );
/*

            HttpSession session = null;
            if( session != null )
            {
                //FIXME move to consts
                String searchScope = (String) session.getAttribute("browserSearchScope" );
                if( searchScope != null && searchScope.equalsIgnoreCase( "browserSearchScopeSearchResults" ) )
                {
                    latestWhere = "";
                }
            }

*/
            if( searchStr5.equals( "" ) )
            {
                csiWhere = "";
                fromClause = "";
            }
            else
            {
                csiWhere = getCSItemWhereClause( searchStr5 );
                fromClause = " ,sbr.ac_csi_view acs ";
            }

            String wkFlowWhere = "";
            String cdeIdWhere = "";
            String vdWhere = "";
            String decWhere = "";
            String docWhere = "";
            String vvWhere = "";
            String regStatusWhere = "";
            String altNameWhere = "";

            if( doStatusSearch )
            {
                wkFlowWhere = this.buildStatusWhereClause( searchStr1 );
            }
            if( doRegStatusSearch )
            {
                regStatusWhere = this.buildRegStatusWhereClause( searchStr7 );
            }
            //if (!getSearchStr(3).equals("")){
            if( !searchStr3.equals( "" ) )
            {
                String newCdeStr = StringReplace.strReplace( searchStr3, "*", "%" );
                cdeIdWhere = " and " + buildSearchString( "to_char(de.cde_id) like 'SRCSTR'",
                        newCdeStr, searchMode );
            }


            //if (!getSearchStr(2).equals("")){
            if( !searchStr2.equals( "" ) )
            {
                //vdWhere = " and vd.vd_idseq = '"+searchStr2+"'";
                vdWhere = " and vd.vd_idseq = '" + searchStr2 + "'"
                        + " and vd.vd_idseq = de.vd_idseq ";
                vdFrom = " ,sbr.value_domains_view vd ";
                //queryParams[1] = searchStr2;
            }
            else if( !vdType.equals( "" ) && !vdType.equals( ProcessConstants.VD_TYPE_BOTH ) )
            {
                String type = "E";
                if( vdType.equals( ProcessConstants.VD_TYPE_NON_ENUMERATED ) )
                {
                    type = "N";
                }
                vdWhere = " and vd.VD_TYPE_FLAG = '" + type + "'"
                        + " and vd.vd_idseq = de.vd_idseq ";
                vdFrom = " ,sbr.value_domains_view vd ";
            }
            //if (!getSearchStr(4).equals("")){
            if( !searchStr4.equals( "" ) )
            {
                decWhere = " and dec.dec_idseq = '" + searchStr4 + "'"
                        + " and de.dec_idseq = dec.dec_idseq ";
                decFrom = " ,sbr.data_element_concepts_view dec ";
                //queryParams[2] = searchStr4;
            }
            if( !searchStr0.equals( "" ) )
            {
                docWhere = this.buildSearchTextWhere( searchStr0, searchIn, searchMode );
            }
            if( !searchStr8.equals( "" ) )
            {
                altNameWhere = this.buildAltNamesWhere( searchStr8, searchStr9 );
            }
            if( !validValue.equals( "" ) )
            {
                vvWhere = this.buildValidValueWhere( validValue, searchBean.getPvSearchMode() );
            }
            // Check to see if a WHERE clause for concepts needs to be added
            String conceptWhere = this.buildConceptWhere( conceptName, conceptCode );

            // release 3.0, TT1235, 1236
            // check to see if a Where clause for object class and property needs to be added
            String deConceptWhere = this.buildDEConceptWhere( objectClass, property );

            if( !cdeType.equals( "" ) )
            {
                deDerivWhere = " and comp_de.P_DE_IDSEQ = de.de_idseq";
                deDerivFrom = ", sbr.COMPLEX_DATA_ELEMENTS_VIEW comp_de ";
            }

            whereBuffer.append( wkFlowWhere );
            whereBuffer.append( regStatusWhere );
            whereBuffer.append( cdeIdWhere );
            whereBuffer.append( decWhere );
            whereBuffer.append( vdWhere );
            whereBuffer.append( latestWhere );
            whereBuffer.append( docWhere );
            whereBuffer.append( usageWhere );
            whereBuffer.append( vvWhere );
            whereBuffer.append( altNameWhere );
            whereBuffer.append( conceptWhere );
            whereBuffer.append( deConceptWhere );
            whereBuffer.append( deDerivWhere );
        }

        if( treeConteIdSeq != null )
        {
            usageWhere = this.getUsageWhereClause();
            whereBuffer.append( usageWhere );
        }


        whereClause = whereBuffer.toString();
        String fromWhere = "";
        if( treeParamType == null || treeParamType.equals( "P_PARAM_TYPE" ) )
        {
            fromWhere =  " from sbr.data_elements_view de , "+
                    "sbr.reference_documents_view rd , "+
                    "sbr.contexts_view conte "+
                    //"sbrext.de_cde_id_view dc " +
                    //"sbr.value_domains vd, "+
                    //"sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause+
                    registrationFrom+
                    wkFlowFrom+
                    deDerivFrom+
                    //" where de.deleted_ind = 'No' "+  [don't need to use this since we are using view)
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) "+
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    csiWhere + whereClause + registrationWhere + workFlowWhere + deDerivWhere;
/*

            System.out.println("\nvdFrom: " + vdFrom + "\n\n");
            System.out.println("\ndecFrom: " + decFrom + "\n\n");
            System.out.println("\nfromClause: " + fromClause + "\n\n");
            System.out.println("\nregistrationFrom: " + registrationFrom + "\n\n");
            System.out.println("\nwkFlowFrom: " + wkFlowFrom + "\n\n");
            System.out.println("\ndeDerivFrom: " + deDerivFrom + "\n\n");
            System.out.println("\nregistrationExcludeWhere: " + registrationExcludeWhere + "\n\n");
            System.out.println("\nworkflowExcludeWhere: " + workflowExcludeWhere + "\n\n");
            System.out.println("\ncontextExludeWhere: " + contextExludeWhere + "\n\n");
            System.out.println("\ncsiWhere: " + csiWhere + "\n\n");
            System.out.println("\nwhereClause: " + whereClause + "\n\n");
            System.out.println("\nregistrationWhere: " + registrationWhere + "\n\n");
            System.out.println("\nworkFlowWhere: " + workFlowWhere + "\n\n");
            System.out.println("\ndeDerivWhere: " + deDerivWhere + "\n\n");
            System.out.println("\nfromWhere: " + fromWhere + "\n\n");

*/
        }
        else if( treeParamType.equals( "CONTEXT" ) )
        {
            fromWhere = " from sbr.data_elements_view de , " +
                    "sbr.reference_documents_view rd , " +
                    "sbr.contexts_view conte " +
                    //"sbrext.de_cde_id_view dc " +
                    //"sbr.value_domains vd, "+
                    //"sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No' "+  [don't need to use this since we are using view)
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) " +
                    //" and conte.conte_idseq = '"+treeParamIdSeq+"'" +
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    //usageWhere +
                    csiWhere + whereClause + registrationWhere + workFlowWhere;

        }
        else if( treeParamType.equals( "PROTOCOL" ) )
        {
            fromWhere = " from  sbr.data_elements_view de , " +
                    " sbr.reference_documents_view rd , " +
                    " sbr.contexts_view conte, " +
                    //" sbrext.de_cde_id_view dc, " +
                    " sbrext.quest_contents_view_ext frm ," +
                    " sbrext.protocol_qc_ext ptfrm ," +
                    " sbrext.protocols_view_ext pt ," +
                    " sbrext.quest_contents_view_ext qc " +
                    //"sbr.value_domains vd, "+
                    //"sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No' "+  [don't need to use this since we are using view)
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) " +
                    " and pt.proto_idseq = ptfrm.proto_idseq " +
                    " and frm.qc_idseq = ptfrm.qc_idseq " +
                    " and frm.latest_version_ind = 'Yes' " +  //GF 19701
                    " and frm.qtl_name = 'CRF' " +
                    " and qc.dn_crf_idseq = frm.qc_idseq " +
                    " and qc.qtl_name = 'QUESTION' " +
                    " and qc.de_idseq = de.de_idseq " +
                    " and pt.proto_idseq = '" + treeParamIdSeq + "'" +
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    csiWhere + whereClause + registrationWhere + workFlowWhere;
        }
        //Published Change Order
        else if( treeParamType.equals( "PUBLISHING_PROTOCOL" ) )
        {
            fromWhere = " from  sbr.data_elements_view de , " +
                    " sbr.reference_documents_view rd , " +
                    " sbr.contexts_view conte, " +
                    //" sbrext.de_cde_id_view dc, " +
                    " sbrext.quest_contents_view_ext frm ," +
                    " sbrext.protocols_view_ext pt ," +
                    " sbrext.quest_contents_view_ext qc , " +
                    " sbrext.published_forms_view published " +
                    //"sbr.value_domains vd, "+
                    //"sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No' "+  [don't need to use this since we are using view)
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) " +
                    " and pt.proto_idseq = frm.proto_idseq " +
                    " and frm.qtl_name = 'CRF' " +
                    " and qc.dn_crf_idseq = frm.qc_idseq " +
                    " and qc.qtl_name = 'QUESTION' " +
                    " and qc.de_idseq = de.de_idseq " +
                    " and frm.qc_idseq = published.qc_idseq " +
                    " and pt.proto_idseq = '" + treeParamIdSeq + "'" +
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    csiWhere + whereClause + registrationWhere + workFlowWhere;
        }
        else if( treeParamType.equals( "CRF" ) || treeParamType.equals( "TEMPLATE" ) )
        {
            fromWhere = " from  sbr.data_elements_view de , " +
                    " sbr.reference_documents_view rd , " +
                    " sbr.contexts_view conte, " +
                    //" sbrext.de_cde_id_view dc, " +
                    " sbrext.quest_contents_view_ext qc " +
                    //" sbr.value_domains vd, "+
                    //" sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No'  "+
                    //Commented for TT 1511
                    // registrationExcludeWhere + workflowExcludeWhere+contextExludeWhere +
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) " +
                    " and qc.dn_crf_idseq = '" + treeParamIdSeq + "'" +
                    " and qc.qtl_name = 'QUESTION' " +
                    " and qc.de_idseq = de.de_idseq " +
                    // " and vd.vd_idseq = de.vd_idseq " +
                    // " and dec.dec_idseq = de.dec_idseq " +

                    csiWhere + whereClause + registrationWhere + workFlowWhere;

        }
        else if( treeParamType.equals( "CSI" ) || treeParamType.equals( "REGCSI" ) )
        {
            if( searchStr5.equals( "" ) )
            {
                csiWhere = " and acs.cs_csi_idseq = '" + this.treeParamIdSeq + "'";
            }
            else
            {
                csiWhere = " and acs.cs_csi_idseq IN ('" + this.treeParamIdSeq + "','" + searchStr5 + "')";
            }
            fromWhere = " from  sbr.data_elements_view de , " +
                    " sbr.reference_documents_view rd , " +
                    " sbr.contexts_view conte, " +
                    //" sbrext.de_cde_id_view dc, " +
                    " sbr.ac_csi_view acs " +
                    //" sbr.value_domains vd, "+
                    //" sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No' "+
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) " +
                    csiWhere +
                    " and acs.ac_idseq = de.de_idseq " +
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    whereClause + registrationWhere + workFlowWhere;

        }
        else if( treeParamType.equals( "CLASSIFICATION" )
                || treeParamType.equals( "REGCS" )
                || treeParamType.equals( "CSCONTAINER" ) )
        {
            if( searchStr5.equals( "" ) )
            {
                csiWhere = "";
            }
            else
            {
                csiWhere = " and acs.cs_csi_idseq = '" + searchStr5 + "'";
            }

            String csWhere = "";
            if( treeParamType.equals( "CSCONTAINER" ) )
            {
                csWhere = getCSContainerWhere( this.treeParamIdSeq );
            }
            else
            {
                csWhere = this.getCSWhere( this.treeParamIdSeq );
            }


            fromWhere = " from  sbr.data_elements_view de , " +
                    " sbr.reference_documents_view rd , " +
                    " sbr.contexts_view conte " +
                    //" sbr.ac_csi acs, " +
                    //" sbr.cs_csi csc " +
                    vdFrom +
                    decFrom +
                    registrationFrom +
                    wkFlowFrom +
                    fromClause +
                    // " where de.deleted_ind = 'No' "+
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and csc.cs_idseq = '"+treeParamIdSeq+"'" +
                    //" and csc.cs_csi_idseq = acs.cs_csi_idseq " +
                    //" and acs.ac_idseq = de.de_idseq " +
                    csiWhere + whereClause + registrationWhere + workFlowWhere + csWhere;

        }
        else if( treeParamType.equals( "CORE" ) )
        {
            fromWhere = " from sbr.data_elements_view de , " +
                    "sbr.reference_documents_view rd , " +
                    "sbr.contexts_view conte " +
                    //"sbrext.de_cde_id_view dc " +
                    //"sbr.value_domains vd, "+
                    //"sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No' "+
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    contextExludeWhere + registrationExcludeWhere + workflowExcludeWhere +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) "+
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    " and de.de_idseq in ( select de_idseq " +
                    " from   sbrext.core_noncore_de_view " +
                    " where csi_idseq = '" + treeParamIdSeq + "'" +
                    " and de_group = 'CORE') " +
                    csiWhere + whereClause + workFlowWhere + registrationWhere;
        }
        else if( treeParamType.equals( "NON-CORE" ) )
        {
            fromWhere = " from sbr.data_elements_view de , " +
                    "sbr.reference_documents_view rd , " +
                    "sbr.contexts_view conte " +
                    //"sbrext.de_cde_id_view dc " +
                    //"sbr.value_domains vd, "+
                    //"sbr.data_element_concepts dec " +
                    vdFrom +
                    decFrom +
                    fromClause +
                    registrationFrom +
                    wkFlowFrom +
                    //" where de.deleted_ind = 'No' "+
                    " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                    contextExludeWhere + registrationExcludeWhere + workflowExcludeWhere +
                    //" and de.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " +
                    " and de.asl_name != 'RETIRED DELETED' " +
                    " and conte.conte_idseq = de.conte_idseq " +
                    //" and de.de_idseq = dc.ac_idseq (+) "+
                    //" and vd.vd_idseq = de.vd_idseq " +
                    //" and dec.dec_idseq = de.dec_idseq " +
                    " and de.de_idseq in ( select de_idseq " +
                    " from   sbrext.core_noncore_de_view " +
                    " where csi_idseq = '" + treeParamIdSeq + "'" +
                    " and de_group = 'NON-CORE') " +
                    csiWhere + whereClause + workFlowWhere + registrationWhere;
        }
        //String orderBy = " order by de.preferred_name, de.version ";
        StringBuffer finalSqlStmt = new StringBuffer();

//release 3.0, added display_order of registration status
// Added distinct due to duplicates
        String selectClause = "SELECT distinct de.de_idseq "
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
        finalSqlStmt.append( selectClause );
        finalSqlStmt.append( fromWhere );
        sqlWithoutOrderBy = finalSqlStmt.toString();

//  CHECKME MHL finalSqlStmt.append( orderBy );

        sqlStmt = finalSqlStmt.toString();
        xmlQueryStmt = "select de.de_idseq " + fromWhere;
        //buildWorkflowList(getSearchStr(1),dbUtil);

        //release 3.0, sort search result by column
        sortColumnHeader = new SimpleSortableColumnHeader();
        sortColumnHeader.setPrimary( "display_order" );
        sortColumnHeader.setSecondary( "wkflow_order" );
        sortColumnHeader.setTertiary( "long_name" );
        sortColumnHeader.setDefaultOrder( true );
        sortColumnHeader.setOrder( SimpleSortableColumnHeader.ASCENDING );
        System.out.println("MHL sqlStmt: " + sqlStmt );
    }

    protected String getCSItemWhereClause( String searchStr5 )
    {
        String csiWhere;
        csiWhere = " and de.de_idseq = acs.ac_idseq " +
                " and acs.cs_csi_idseq = '" + searchStr5 + "'";
        return csiWhere;
    }

    public String getSearchStr( int arrayIndex )
    {
        if( strArray != null )
        {
            return strArray[arrayIndex];
        }
        else
        {
            return "";
        }
    }

    public String getXMLQueryStmt()
    {
        return xmlQueryStmt;
    }

    public String getQueryStmt()
    {
        return sqlStmt;
    }

    public String getVDPrefName()
    {
        if( vdPrefName == null )
        {
            return "";
        }
        return vdPrefName;
    }

    public String getDECPrefName()
    {
        if( decPrefName == null )
        {
            return "";
        }
        return decPrefName;
    }

    public String getCSIName()
    {
        if( csiName == null )
        {
            return "";
        }
        return csiName;
    }

    public Object[] getQueryParams()
    {
        return queryParams;
    }

    public String getContextUse()
    {
        return contextUse;
    }

    public String getSQLWithoutOrderBy()
    {
        return sqlWithoutOrderBy;
    }

    public String getOrderBy()
    {
        StringBuffer sb = new StringBuffer();
      /*
    String sortOrder = "";
    if (sortColumnHeader.getOrder() == gov.nih.nci.cadsr.common.util.SortableColumnHeader.DESCENDING)
       sortOrder = " DESC";
    StringBuffer sb = new StringBuffer();
      if( sortColumnHeader.isColumnNumeric( sortColumnHeader.getPrimary() ) )
          sb = sb.append( ( sortColumnHeader.getPrimary() ) + sortOrder );
      else
          sb = sb.append( "upper(" + sortColumnHeader.getPrimary() + ")" + sortOrder );
      if( sortColumnHeader.getSecondary() != null && !sortColumnHeader.getSecondary().equalsIgnoreCase( "" ) )
          if( sortColumnHeader.isColumnNumeric( sortColumnHeader.getSecondary() ) )
              sb.append( "," + sortColumnHeader.getSecondary() + sortOrder );
          else
              sb.append( "," + "upper(" + sortColumnHeader.getSecondary() + ")" + sortOrder );

      if( sortColumnHeader.getTertiary() != null && !sortColumnHeader.getTertiary().equalsIgnoreCase( "" ) )
          if( sortColumnHeader.isColumnNumeric( sortColumnHeader.getTertiary() ) )
              sb.append( "," + sortColumnHeader.getTertiary() + sortOrder );
          else
              sb.append( "," + "upper(" + sortColumnHeader.getTertiary() + ")" + sortOrder );
              */
        return sb.toString();
    }

    protected String getUsageWhereClause()
    {
        String usageWhere = "";
        if( "used_by".equals( contextUse ) )
        {
            usageWhere =
                    " and de.de_idseq IN (select ac_idseq " +
                            "                     from   sbr.designations_view des " +
                            "                     where  des.conte_idseq = '" + treeConteIdSeq + "'" +
                            "                     and    des.DETL_NAME = 'USED_BY')  ";
        }
        //else if ("owned_by".equals(contextUse) || "".equals(contextUse)) {
        else if( "owned_by".equals( contextUse ) )
        {
            usageWhere = " and conte.conte_idseq = '" + treeConteIdSeq + "' ";
        }
        else if( "both".equals( contextUse ) || "".equals( contextUse ) )
        {
            if( "CONTEXT".equals( treeParamType ) )
            {
                usageWhere =
                        " and de.de_idseq IN (select ac_idseq " +
                                "                     from   sbr.designations_view des " +
                                "                     where  des.conte_idseq = '" + treeConteIdSeq + "'" +
                                "                     and    des.DETL_NAME = 'USED_BY' " +
                                "                     UNION " +
                                "                     select de_idseq " +
                                "                     from   sbr.data_elements_view de1 " +
                                "                     where  de1.conte_idseq ='" + treeConteIdSeq + "') ";
            }
        }

        return usageWhere;
    }

    private String buildStatusWhereClause( String[] statusList )
    {
        String wkFlowWhere = "";
        String wkFlow = "";
        if( statusList.length == 1 )
        {
            wkFlow = statusList[0];
            wkFlowWhere = " and de.asl_name = '" + wkFlow + "'";
        }
        else
        {
            for( int i = 0; i < statusList.length; i++ )
            {
                if( i == 0 )
                {
                    wkFlow = "'" + statusList[0] + "'";
                }
                else
                {
                    wkFlow = wkFlow + "," + "'" + statusList[i] + "'";
                }
            }
            wkFlowWhere = " and de.asl_name IN (" + wkFlow + ")";

        }

        return wkFlowWhere;
    }

    private String buildRegStatusWhereClause( String[] regStatusList )
    {
        String regStatWhere = "";
        String regStatus = "";
        if( regStatusList.length == 1 )
        {
            regStatus = regStatusList[0];
            regStatWhere = " and acr.registration_status = '" + regStatus + "'";
        }
        else
        {
            for( int i = 0; i < regStatusList.length; i++ )
            {
                if( i == 0 )
                {
                    regStatus = "'" + regStatusList[0] + "'";
                }
                else
                {
                    regStatus = regStatus + "," + "'" + regStatusList[i] + "'";
                }
            }
            regStatWhere = " and acr.registration_status IN (" + regStatus + ")";

        }

        return regStatWhere;
    }

    private String buildSearchTextWhere( String text, String[] searchDomain, String searchMode )
    {
        String docWhere = null;
        String newSearchStr = "";
        String searchWhere = null;
        String longNameWhere = null;
        String shortNameWhere = null;
        String docTextSearchWhere = null;
        String docTextTypeWhere = null;
        String umlAltNameWhere = null;


        newSearchStr = StringReplace.strReplace( text, "*", "%" );
        newSearchStr = StringReplace.strReplace( newSearchStr, "'", "''" );

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "Long Name" ) )
        {
            longNameWhere = buildSearchString( "upper (de1.long_name) like upper ( 'SRCSTR') ", newSearchStr, searchMode );
        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "Short Name" ) )
        {
            shortNameWhere = buildSearchString( "upper (de1.preferred_name) like upper ( 'SRCSTR') ", newSearchStr, searchMode );
        }
        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "Doc Text" ) ||
                StringUtils.containsKey( searchDomain, "Hist" ) )
        {
            docTextSearchWhere =
                    buildSearchString( "upper (nvl(rd1.doc_text,'%')) like upper ('SRCSTR') ", newSearchStr, searchMode );
        }

        // compose the search for data elements table
        searchWhere = longNameWhere;

        if( searchWhere == null )
        {
            searchWhere = shortNameWhere;
        }
        else if( shortNameWhere != null )
        {
            searchWhere = searchWhere + " OR " + shortNameWhere;
        }

        if( searchWhere == null && docTextSearchWhere != null )
        {
            searchWhere = " and " + docTextSearchWhere;
        }
        else if( docTextSearchWhere != null )
        {
            searchWhere = searchWhere + " OR " + docTextSearchWhere;
            searchWhere = " and (" + searchWhere + ") ";
        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                ( StringUtils.containsKey( searchDomain, "Doc Text" ) &&
                        StringUtils.containsKey( searchDomain, "Hist" ) ) )
        {
            docWhere = "(select de_idseq "
                    + " from sbr.reference_documents_view rd1, sbr.data_elements_view de1 "
                    + " where  de1.de_idseq  = rd1.ac_idseq (+) "
                    + " and    rd1.dctl_name (+) = 'Preferred Question Text' "
                    + searchWhere
                    + " union "
                    + " select de_idseq "
                    + " from sbr.reference_documents_view rd2,sbr.data_elements_view de2 "
                    + " where  de2.de_idseq  = rd2.ac_idseq (+) "
                    + " and    rd2.dctl_name (+) = 'Alternate Question Text' "
                    + " and    " + buildSearchString( "upper (nvl(rd2.doc_text,'%')) like upper ('SRCSTR') ", newSearchStr, searchMode ) + ") ";
        }
        else if( StringUtils.containsKey( searchDomain, "Doc Text" ) )
        {
            docTextTypeWhere = "rd1.dctl_name (+) = 'Preferred Question Text'";
        }
        else if( StringUtils.containsKey( searchDomain, "Hist" ) )
        {
            docTextTypeWhere = "rd1.dctl_name (+) = 'Alternate Question Text'";
        }

        if( docTextSearchWhere == null && searchWhere != null )
        {
            //this is a search not involving reference documents
            docWhere = "(select de_idseq "
                    + " from sbr.data_elements_view de1 "
                    + " where  " + searchWhere + " ) ";

        }
        else if( docWhere == null && docTextTypeWhere != null )
        {
            docWhere = "(select de_idseq "
                    + " from sbr.reference_documents_view rd1, sbr.data_elements_view de1 "
                    + " where  de1.de_idseq  = rd1.ac_idseq (+) "
                    + " and  " + docTextTypeWhere
                    + searchWhere + " ) ";

        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "UML ALT Name" ) )
        {
            umlAltNameWhere =
                    " (select de_idseq  from sbr.designations_view dsn, sbr.data_elements_view de1  "
                            + "where  de1.de_idseq  = dsn.ac_idseq (+)  "
                            + "and dsn.detl_name = 'UML Class:UML Attr'  and "
                            + buildSearchString( "upper (nvl(dsn.name,'%')) like upper ('SRCSTR')", newSearchStr, searchMode )
                            + " )";

            if( docWhere == null )
            {
                return " and de.de_idseq IN " + umlAltNameWhere;
            }
            else
            {
                String nameWhere = " and de.de_idseq IN (" + umlAltNameWhere
                        + " union " + docWhere + ") ";
                return nameWhere;
            }
        }

        return " and de.de_idseq IN " + docWhere;
    }


    private String buildSearchString( String whereTemplate, String searchPhrase, String searchMode )
    {
        Pattern p = Pattern.compile( REPLACE_TOKEN );
        Matcher matcher = p.matcher( whereTemplate );

        if( searchMode.equals( ProcessConstants.DE_SEARCH_MODE_EXACT ) )
        {
            return matcher.replaceAll( searchPhrase );
        }

        String oper = null;
        if( searchMode.equals( ProcessConstants.DE_SEARCH_MODE_ANY ) )
        {
            oper = " or ";
        }
        else
        {
            oper = " and ";
        }

        String[] words = searchPhrase.split( " " );
        String whereClause = "(";
        for( int i = 0; i < words.length; i++ )
        {
            if( whereClause.length() > 2 )
            {
                whereClause += oper;
            }
            whereClause += buildWordMatch( matcher, words[i] );

        }

        whereClause += ")";

        return whereClause;
    }

    /**
     * This method returns the query for whole word matching the input param word
     *
     * @param matcher
     * @param word
     * @return
     */
    private String buildWordMatch( Matcher matcher, String word )
    {
        return "(" + matcher.replaceAll( "% " + word + " %" )
                + " or " + matcher.replaceAll( word + " %" )
                + " or " + matcher.replaceAll( word )
                + " or " + matcher.replaceAll( "% " + word ) + ")";
    }

    private String buildValidValueWhere( String value, String searchMode )
    {
        String newSearchStr = StringReplace.strReplace( value, "*", "%" );
        newSearchStr = StringReplace.strReplace( newSearchStr, "'", "''" );
        String whereClause = "select vd.vd_idseq from sbr.value_domains_view vd"
                + " , sbr.vd_pvs_view vp, sbr.permissible_values_view pv  "
                + " where  vd.vd_idseq = vp.vd_idseq  "
                + "and    pv.pv_idseq = vp.pv_idseq and ";

        Pattern p = Pattern.compile( REPLACE_TOKEN );
        Matcher matcher = p.matcher( "upper (pv.value) like upper ('SRCSTR') " );

        if( searchMode.equals( ProcessConstants.DE_SEARCH_MODE_EXACT ) )
        {
            return ( " and de.vd_idseq IN (" + whereClause
                    + matcher.replaceAll( newSearchStr ) + ")" );
        }

        String oper = null;
        if( searchMode.equals( ProcessConstants.DE_SEARCH_MODE_ANY ) )
        {
            oper = " UNION ";
        }
        else
        {
            oper = " INTERSECT ";
        }

        String[] words = newSearchStr.split( " " );
        String vvWhere = " and de.vd_idseq IN (";
        for( int i = 0; i < words.length; i++ )
        {
            if( i > 0 )
            {
                vvWhere += oper;
            }
            vvWhere += ( whereClause + buildWordMatch( matcher, words[i] ) );

        }

        vvWhere += ")";

        return vvWhere;

    }

    private String buildDEConceptWhere( String objectClass, String property )
    {

        if( objectClass.equals( "" ) && property.equals( "" ) )
        {
            return "";
        }

        String objectClassWhere = "";
        String objectClassFrom = "";
        String propertyFrom = "";
        String propertyWhere = "";

        if( !"".equals( objectClass ) )
        {
            String newObjClass = StringReplace.strReplace( objectClass, "*", "%" );
            newObjClass = StringReplace.strReplace( newObjClass, "'", "''" );
            objectClassFrom = ", sbrext.object_classes_view_ext oc ";
            objectClassWhere = "oc.oc_idseq = dec.oc_idseq " +
                    " and upper(oc.LONG_NAME) like upper('" + newObjClass + "')";
        }

        if( !"".equals( property ) )
        {
            String newSearchStr = StringReplace.strReplace( property, "*", "%" );
            newSearchStr = StringReplace.strReplace( newSearchStr, "'", "''" );
            propertyFrom = " , sbrext.properties_view_ext pc";
            propertyWhere = " pc.PROP_IDSEQ = dec.PROP_IDSEQ " +
                    "and upper(pc.LONG_NAME) like upper('" + newSearchStr + "')";

            if( !"".equals( objectClassWhere ) )
            {
                propertyWhere = " and " + propertyWhere;
            }
        }

        String deConceptWhere = "and  de.de_idseq IN ("
                + "select de_idseq "
                + "from   sbr.data_elements_view "
                + "where  dec_idseq IN (select dec.dec_idseq "
                + "from   sbr.data_element_concepts_view dec "
                + objectClassFrom
                + propertyFrom
                + " where  "
                + objectClassWhere
                + propertyWhere + "))";
        return deConceptWhere;

    }

    private String buildAltNamesWhere( String text, String[] altNameTypes )
    {
        String altWhere = "";
        String newSearchStr = "";
        String typeWhere = "";
        String altTypeStr = "";
        String searchWhere = "";

        newSearchStr = StringReplace.strReplace( text, "*", "%" );
        newSearchStr = StringReplace.strReplace( newSearchStr, "'", "''" );
        if( altNameTypes == null ||
                StringUtils.containsKey( altNameTypes, "ALL" ) )
        {
            typeWhere = "";
        }
        else if( altNameTypes.length == 1 )
        {
            altTypeStr = altNameTypes[0];
            typeWhere = " and dsn.detl_name = '" + altTypeStr + "'";
        }
        else
        {
            for( int i = 0; i < altNameTypes.length; i++ )
            {
                if( i == 0 )
                {
                    altTypeStr = "'" + altNameTypes[0] + "'";
                }
                else
                {
                    altTypeStr = altTypeStr + "," + "'" + altNameTypes[i] + "'";
                }
            }
            typeWhere = " and dsn.detl_name IN (" + altTypeStr + ")";

        }

        searchWhere = " and upper (nvl(dsn.name,'%')) like upper ('" + newSearchStr + "') ";

        altWhere = " and de.de_idseq IN "
                + "(select de_idseq "
                + " from sbr.designations_view dsn, sbr.data_elements_view de1 "
                + " where  de1.de_idseq  = dsn.ac_idseq (+) "
                + typeWhere
                + searchWhere + " ) ";
        return altWhere;
    }

    private String buildConceptWhere( String conceptName, String conceptCode )
    {
        String conceptWhere = "";
        String conceptCodeWhere = "";
        String conceptNameWhere = "";
        if( !"".equals( conceptName ) )
        {
            String newConceptName = StringReplace.strReplace( conceptName, "*", "%" );
            conceptNameWhere = " where upper(long_name) like upper('" + newConceptName + "')";
        }
        if( !"".equals( conceptCode ) )
        {
            String newConceptCode = StringReplace.strReplace( conceptCode, "*", "%" );
            if( !"".equals( conceptName ) )
            {
                conceptCodeWhere = " and upper(preferred_name) like upper('" + newConceptCode + "')";
            }
            else
            {
                conceptCodeWhere = " where upper(preferred_name) like upper('" + newConceptCode + "')";
            }
        }
        if( ( !"".equals( conceptName ) ) || ( !"".equals( conceptCode ) ) )
        {
            conceptWhere = "and    de.de_idseq IN ("
                    + "select de_idseq "
                    + "from   sbr.data_elements_view "
                    + "where  dec_idseq IN (select dec.dec_idseq "
                    + "from   sbr.data_element_concepts_view dec, "
                    + "       sbrext.object_classes_view_ext oc "
                    + "where  oc.oc_idseq = dec.oc_idseq "
                    + "and    oc.condr_idseq in(select cdr.condr_idseq "
                    + "from   sbrext.con_derivation_rules_view_ext cdr, "
                    + "       sbrext.component_concepts_view_ext cc "
                    + "where  cdr.condr_idseq = cc.condr_idseq "
                    + "and    cc.con_idseq in (select con_idseq "
                    + "from   sbrext.concepts_view_ext "
                    + conceptNameWhere + conceptCodeWhere + ")) "
                    + "UNION "
                    + "select dec.dec_idseq "
                    + "from   sbr.data_element_concepts_view dec, sbrext.properties_view_ext pc "
                    + "where  pc.prop_idseq = dec.prop_idseq "
                    + "and    pc.condr_idseq in(select cdr.condr_idseq "
                    + "from   sbrext.con_derivation_rules_view_ext cdr, "
                    + "       sbrext.component_concepts_view_ext cc "
                    + "where  cdr.condr_idseq = cc.condr_idseq "
                    + "and    cc.con_idseq in (select con_idseq "
                    + "from   sbrext.concepts_view_ext "
                    + conceptNameWhere + conceptCodeWhere + "))) "
                    + "UNION "
                    + "select de_idseq "
                    + "from   sbr.data_elements_view "
                    + "where  vd_idseq IN (select vd.vd_idseq "
                    + "from   sbr.value_domains_view vd, "
                    + "       sbr.vd_pvs_view vp, "
                    + "       sbr.permissible_values_view pv, "
                    + "       sbr.value_meanings_view vm "
                    + "where  vd.vd_idseq = vp.vd_idseq "
                    + "and    pv.pv_idseq = vp.pv_idseq "
                    + "and    vm.vm_idseq = pv.vm_idseq "
                    + "and     vm.condr_idseq in(select cdr.condr_idseq "
                    + "from   sbrext.con_derivation_rules_view_ext cdr, "
                    + "       sbrext.component_concepts_view_ext cc "
                    + "where  cdr.condr_idseq = cc.condr_idseq "
                    + "and    cc.con_idseq in (select con_idseq "
                    + "from   sbrext.concepts_view_ext "
                    + conceptNameWhere + conceptCodeWhere + ")))) ";

        }
        return conceptWhere;
    }


    public SortableColumnHeader getSortColumnHeader()
    {
        return sortColumnHeader;
    }

    protected String getCSWhere( String csId )
    {
        String csWhere = " and de.de_idseq IN ( " +
                " select de_idseq " +
                " from  sbr.data_elements_view de , " +
                "       sbr.ac_csi_view acs, " +
                "       sbr.cs_csi_view csc " +
                " where csc.cs_idseq = '" + csId + "'" +
                " and   csc.cs_csi_idseq = acs.cs_csi_idseq " +
                " and   acs.ac_idseq = de_idseq ) ";
        return csWhere;

    }

    private String getCSContainerWhere( String csId )
    {
        String csWhere = " and de.de_idseq IN ( " +
                " select de_idseq " +
                " from  sbr.data_elements_view de , " +
                "       sbr.ac_csi_view acs, " +
                "       sbr.cs_csi_view csc " +
                " where csc.cs_idseq IN ( " +
                "       select unique(cs.cs_idseq)" +
                "       from   sbr.classification_schemes_view cs" +
                "       where  cs.asl_name = 'RELEASED'" +
                "       and    cs.cstl_name != 'Container'" +
                "       and    cs.cs_idseq in (" +
                "         select c_cs_idseq " +
                "         from sbrext.cs_recs_hasa_view " +
                "         start with p_cs_idseq = '" + csId + "'" +
                "         connect by Prior c_cs_idseq = p_cs_idseq))" +
                " and   csc.cs_csi_idseq = acs.cs_csi_idseq " +
                " and   acs.ac_idseq = de_idseq ) ";
        return csWhere;

    }


    protected String getTreeParamType()
    {
        return treeParamType;
    }

    protected String getTreeConteIdSeq()
    {
        return treeConteIdSeq;
    }

    public DESearchQueryBuilder()
    {
        System.out.println( "In constructor" );

    }


    public static void main( String[] args )
    {

        /*

        gov.nih.nci.cadsr.service.search.DESearchQueryBuilder( HttpServletRequest request,
                String treeParamType,
                String treeParamIdSeq,
                String treeConteIdSeq, gov.nih.nci.cadsr.service.search.DataElementSearchBean searchBean

         */
        TempTestParameters request = new TempTestParameters();
//        String treeParamType = "REGCSI";
        String treeParamType = null;
        String treeParamIdSeq = null; //"99BA9DC8-2094-4E69-E034-080020C9C0E0,Standard";
        String treeConteIdSeq =  null; //"99BA9DC8-2094-4E69-E034-080020C9C0E0";
        DataElementSearchBean searchBean = new DataElementSearchBean();


        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request,treeParamType,treeParamIdSeq , treeConteIdSeq, searchBean, "tissue", "0");
        System.out.println( "dESearchQueryBuilder: " + dESearchQueryBuilder.getQueryStmt().replaceAll( "  *", " " ));


       // System.out.println( "dESearchQueryBuilder: " + dESearchQueryBuilder.buildSearchString( "a=b", "Phrase", gov.nih.nci.cadsr.service.search.ProcessConstants.DE_SEARCH_MODE_EXACT ) );

    }
}
