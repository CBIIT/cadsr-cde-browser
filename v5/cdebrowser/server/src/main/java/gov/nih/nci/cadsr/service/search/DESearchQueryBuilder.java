package gov.nih.nci.cadsr.service.search;
/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DESearchQueryBuilder extends AbstractSearchQueryBuilder
{
    private static Logger logger = LogManager.getLogger( DESearchQueryBuilder.class.getName() );

    private String whereClause = "";
    private String[] strArray = null;
    private String sqlStmt = "";

    protected String query = "";
    private int clientSearchField = -1;
    private String clientSearchMode = "";

    //Will be refactored away soon
    private TempTestParameters request;
    //Will be refactored away soon
    private DataElementSearchBean searchBean;


    public DESearchQueryBuilder(
            TempTestParameters request,
            DataElementSearchBean searchBean, String clientQuery, String clientSearchMode, int clientSearchField )
    {
        this.query = clientQuery;
        this.clientSearchMode = clientSearchMode;
        this.clientSearchField = clientSearchField;
        this.request = request;
        this.searchBean = searchBean;

        buildSql();
    }

    protected void buildSql()
    {

        if( query.isEmpty())
        {
            logger.warn( "Search builder received no query." );
            sqlStmt = null;
            return;
        }

        if( ( clientSearchField != NAME_FIELD ) && ( clientSearchField != PUBLIC_ID_FIELD ) ) //Unknown Search Field
        {
            logger.warn( "Search builder received Unknown Search Field: [" + clientSearchField + "]" );
            sqlStmt = null;
            return;
        }

        String latestWhere = "";
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

        String registrationExcludeWhere = "";
        //excludeArr will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( excludeArr ) )
        {
            registrationExcludeWhere = " and " + searchBean.getExcludeWhereCluase( "nvl(acr.registration_status,'-1')", excludeArr );
        }

        String workflowExcludeWhere = "";
        //aslNameExcludeList will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( aslNameExcludeList ) )
        {
            workflowExcludeWhere = " and " + searchBean.getExcludeWhereCluase( "asl.asl_name", aslNameExcludeList );
        }

        String contextExludeWhere = "";
        //CONTEXT_EXCLUDES will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !CONTEXT_EXCLUDES.equals( "" ) )
        {
            contextExludeWhere = " and conte.name NOT IN (" + CONTEXT_EXCLUDES + " )";
        }

            /*
            jspValidValue  is in advancedSearch_inc.jsp associated with a field labeled “Permissible Value”. There’s a whole table called Permissible_values
            jspObjectClass is in advancedSearch_inc.jsp associated with a field labeled “Object Class”.  I haven’t seen this in the DB
            jspProperty is in advancedSearch_inc.jsp associated with a field labeled “Property”.  I haven’t seen this in the DB
            jspValueDomain is in advancedSearch_inc.jsp associated with a field labeled “Search for Value Domains”.  It’s an odd hidden field associated with a “LOV” and with a text field that is disabled.  There’s a DB table for value_domains.  Value Domain (VD), Value Meaning (VM) and Permissible Values (PV) all contribute to the data about what can be the answers to questions.
            jspCdeId is in advancedSearch_inc.jsp associated with a field labeled “Public ID”. It appears to be used for storing the public id for a CDE. Now, since “a CDE” is the combination of VD, VM and PV’s AND a few other fields, I am not sure right now how there can be one ID for a CDE.
            jspDataElementConcept is in advancedSearch_inc.jsp associated with a field labeled “Data Element Concept”. There’s something in the db called “Concept"
            jspClassification is in advancedSearch_inc.jsp associated with a field labeled "Search for Classification Scheme Items".
            jspLatestVersion is in advancedSearch_inc.jsp associated with a boolean checkbox field that isn’t labeled but I would assume that this field ties into the data’s version field and would choose the max value of
            */

         String[] searchIn = request.getParameterValues( "jspSearchIn" );
/*

        String validValue = StringUtils.replaceNull( request.getParameter( "jspValidValue" ) );
        String objectClass = StringUtils.replaceNull( request.getParameter( "jspObjectClass" ) );
        String property = StringUtils.replaceNull( request.getParameter( "jspProperty" ) );
*/


        logger.debug( "  query: [" + query + "]" );

        conceptName = StringUtils.replaceNull( request.getParameter( "jspConceptName" ) );
        conceptCode = StringUtils.replaceNull( request.getParameter( "jspConceptCode" ) );
        vdType = StringUtils.replaceNull( request.getParameter( "jspVDType" ) );
        cdeType = StringUtils.replaceNull( request.getParameter( "jspCDEType" ) );


        logger.debug( "  statusWhere (jspStatus): " + StringUtils.stringArrayToString( statusWhere ) );
        logger.debug( "  regStatusesWhere (regStatus): " + StringUtils.stringArrayToString( regStatusesWhere ) );
        //logger.debug( "  searchStr9 (altName): " + StringUtils.stringArrayToString( searchStr9 ) );
        logger.debug( "  searchIn (jspSearchIn): " + StringUtils.stringArrayToString( searchIn ) );
 /*       logger.debug( "  validValue (jspValidValue): " + validValue );
        logger.debug( "  objectClass (jspObjectClass): " + objectClass );
        logger.debug( "  property (jspProperty): " + property );
*/
        logger.debug( "  altName / jspAltName: " + altName );
        logger.debug( "  conceptName / jspConceptName: " + conceptName );
        logger.debug( "  conceptCode / jspConceptCode: " + conceptCode );
        logger.debug( "  vdType / jspVDType: " + vdType );
        logger.debug( "  cdeType / jspCDEType: " + cdeType );


        //set filter on "version"
        latestWhere = " and de.latest_version_ind = 'Yes' "; //basic search, only return the latest version as default

        String wkFlowWhere = "";
        String cdeIdWhere = "";
        String vdWhere = "";
        String decWhere = "";
        String docWhere = "";
        String vvWhere = "";
        String regStatus = "";
        String altNameWhere = "";

        wkFlowWhere = this.buildStatusWhereClause( statusWhere );

        //check if registration status is selected
        regStatus = this.buildRegStatusWhereClause( regStatusesWhere );

        if( clientSearchField == PUBLIC_ID_FIELD )
        {
            String newCdeStr = StringReplace.strReplace( query, "*", "%" );
            cdeIdWhere = " and " + buildSearchString( "to_char(de.cde_id) like 'SRCSTR'",
                    newCdeStr, clientSearchMode );
        }

        if( !valueDomain.equals( "" ) )
        {
            vdWhere = " and vd.vd_idseq = '" + valueDomain + "'"
                    + " and vd.vd_idseq = de.vd_idseq ";
            vdFrom = " ,sbr.value_domains_view vd ";

        } else if( !vdType.equals( "" ) && !vdType.equals( ProcessConstants.VD_TYPE_BOTH ) )
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

        if( clientSearchField == NAME_FIELD )
        {
            docWhere = this.buildSearchTextWhere( query, searchIn, clientSearchMode );
        }

        /*
         searchStr9 in the previous version mapped to altName, we are not using this currently,
         but, should be researched to see if it is a parameter from the client we will want to implement.
         This was the init in the  previous version:  String[] searchStr9 = request.getParameterValues( "altName" );

         if( !altName.equals( "" ) )
         {
             altNameWhere = this.buildAltNamesWhere( altName, searchStr9 );
         }
        */

        if( !cdeType.equals( "" ) )
        {
            deDerivWhere = " and comp_de.P_DE_IDSEQ = de.de_idseq";
            deDerivFrom = ", sbr.COMPLEX_DATA_ELEMENTS_VIEW comp_de ";
        }

        whereBuffer.append( wkFlowWhere );
        whereBuffer.append( regStatus );
        whereBuffer.append( cdeIdWhere );
        whereBuffer.append( decWhere );
        whereBuffer.append( vdWhere );
        whereBuffer.append( latestWhere );
        whereBuffer.append( docWhere );
        whereBuffer.append( vvWhere );
        whereBuffer.append( deDerivWhere );

/*
             logger.debug("wkFlowWhere: " + wkFlowWhere);
             logger.debug("cdeIdWhere: " + cdeIdWhere);
             logger.debug("vdWhere: " + vdWhere);
             logger.debug("decWhere: " + decWhere);
             logger.debug("docWhere: " + docWhere);
             logger.debug("vvWhere: " + vvWhere);
             logger.debug("regStatus: " + regStatus);
             logger.debug("altNameWhere: " + altNameWhere);
             logger.debug("whereBuffer: " + whereBuffer.toString())
*/


        whereClause = whereBuffer.toString();
        String fromWhere = " from sbr.data_elements_view de , " +
                "sbr.reference_documents_view rd , " +
                "sbr.contexts_view conte " +
                vdFrom +
                decFrom +
                fromClause +
                registrationFrom +
                wkFlowFrom +
                deDerivFrom +
                " where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                " and de.asl_name != 'RETIRED DELETED' " +
                " and conte.conte_idseq = de.conte_idseq " +
                whereClause + registrationWhere + workFlowWhere + deDerivWhere;

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

        sqlStmt = finalSqlStmt.toString();
    }

     public String getQueryStmt()
    {
        return sqlStmt;
    }


    protected String buildStatusWhereClause( String[] statusList )
    {
        if( ( statusList == null ) || ( StringUtils.containsKey( statusList, "ALL" ) || ( statusList[0].isEmpty() ) ) )
        {
            return "";
        }

        String wkFlowWhere = "";
        String wkFlow = "";
        if( statusList.length == 1 )
        {
            wkFlow = statusList[0];
            wkFlowWhere = " and de.asl_name = '" + wkFlow + "'";
        } else
        {
            for( int i = 0; i < statusList.length; i++ )
            {
                if( i == 0 )
                {
                    wkFlow = "'" + statusList[0] + "'";
                } else
                {
                    wkFlow = wkFlow + "," + "'" + statusList[i] + "'";
                }
            }
            wkFlowWhere = " and de.asl_name IN (" + wkFlow + ")";

        }

        return wkFlowWhere;
    }

    protected String buildRegStatusWhereClause( String[] regStatusList )
    {

        if( ( regStatusList == null ) || ( StringUtils.containsKey( regStatusList, "ALL" ) || ( regStatusList[0].isEmpty() ) ) )
        {
            return "";
        }

        String regStatWhere = "";
        String regStatus = "";
        if( regStatusList.length == 1 )
        {
            regStatus = regStatusList[0];
            regStatWhere = " and acr.registration_status = '" + regStatus + "'";
        } else
        {
            for( int i = 0; i < regStatusList.length; i++ )
            {
                if( i == 0 )
                {
                    regStatus = "'" + regStatusList[0] + "'";
                } else
                {
                    regStatus = regStatus + "," + "'" + regStatusList[i] + "'";
                }
            }
            regStatWhere = " and acr.registration_status IN (" + regStatus + ")";

        }
        logger.debug( "regStatWhere: " + regStatWhere );
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
            longNameWhere = buildSearchString( "upper (de1.long_name) like upper ('SRCSTR') ", newSearchStr, searchMode );
        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "Short Name" ) )
        {

            shortNameWhere = buildSearchString( "upper (de1.preferred_name) like upper ('SRCSTR') ", newSearchStr, searchMode );
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
        } else if( shortNameWhere != null )
        {
            searchWhere = searchWhere + " OR " + shortNameWhere;
        }

        if( searchWhere == null && docTextSearchWhere != null )
        {
            searchWhere = " and " + docTextSearchWhere;
        } else if( docTextSearchWhere != null )
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


        } else if( StringUtils.containsKey( searchDomain, "Doc Text" ) )
        {
            docTextTypeWhere = "rd1.dctl_name (+) = 'Preferred Question Text'";
        } else if( StringUtils.containsKey( searchDomain, "Hist" ) )
        {
            docTextTypeWhere = "rd1.dctl_name (+) = 'Alternate Question Text'";
        }


        if( docTextSearchWhere == null && searchWhere != null )
        {
            //this is a search not involving reference documents
            docWhere = "(select de_idseq "
                    + " from sbr.data_elements_view de1 "
                    + " where  " + searchWhere + " ) ";

        } else if( docWhere == null && docTextTypeWhere != null )
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
                return " and de.de_idseq IN " + umlAltNameWhere;
            else
            {
                String nameWhere = " and de.de_idseq IN (" + umlAltNameWhere
                        + " union " + docWhere + ") ";
                return nameWhere;
            }
        }
        logger.debug( "  buildSearchTextWhere - docWhere: " + docWhere );
        logger.debug( "  buildSearchTextWhere - docTextTypeWhere: " + docTextTypeWhere );

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
        } else
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



}