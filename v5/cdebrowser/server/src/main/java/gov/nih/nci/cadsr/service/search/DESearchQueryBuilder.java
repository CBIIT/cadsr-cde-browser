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

    private String sqlStmt = "";
    protected String query = "";
    private int clientSearchField = -1;
    private String clientSearchMode = "";


    public DESearchQueryBuilder( String clientQuery, String clientSearchMode, int clientSearchField )
    {
        this.query = clientQuery;
        this.clientSearchMode = clientSearchMode;
        this.clientSearchField = clientSearchField;
        buildSql();

        //Test with program area constraint
        //buildSql("NCI");
    }

    /**
     * FIXME - This just a test of search within program area - still need to combine with other buildSql.
     * @param palName  PAL_NAME from Program area
     */
    protected void buildSql(String palName)
    {

        if( query.isEmpty() )
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

        String vdFrom = "";
        String latestWhere = "";
        String fromClause = "";
        String deDerivWhere = "";
        String deDerivFrom = "";
        String whereClause = "";
        StringBuilder whereBuffer = new StringBuilder();


        String registrationExcludeWhere = "";
        //excludeArr will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( excludeArr ) )
        {
            registrationExcludeWhere = " and " + getExcludeWhereClause( "nvl(acr.registration_status,'-1')", excludeArr );
        }

        String workflowExcludeWhere = "";
        //aslNameExcludeList will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( aslNameExcludeList ) )
        {
            //workflowExcludeWhere = " and " + searchBean.getExcludeWhereClause( "asl.asl_name", aslNameExcludeList );
            workflowExcludeWhere = " and " + getExcludeWhereClause( "asl.asl_name", aslNameExcludeList );
        }

        String contextExludeWhere = "";
        //CONTEXT_EXCLUDES will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !CONTEXT_EXCLUDES.equals( "" ) )
        {
            contextExludeWhere = " and conte.name NOT IN (" + CONTEXT_EXCLUDES + " )";
        }

        /*
        Will not be needed until "Search only Derived DEs" is implemented
        cdeType = StringUtils.replaceNull( request.getParameter( "jspCDEType" ) );
        */

        //set filter on "version"
        latestWhere = " and de.latest_version_ind = 'Yes' "; //basic search, only return the latest version as default

        String wkFlowWhere = "";
        String cdeIdWhere = "";
        String vdWhere = "";
        String decWhere = "";
        String docWhere = "";
        String vvWhere = "";
        String regStatus = "";

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

        /*
        Will not be needed until "Search only Derived DEs" is implemented
        if( !cdeType.equals( "" ) )
        {
            deDerivWhere = " and comp_de.P_DE_IDSEQ = de.de_idseq";
            deDerivFrom = ", sbr.COMPLEX_DATA_ELEMENTS_VIEW comp_de ";
        }
        */

        whereBuffer.append( wkFlowWhere );
        whereBuffer.append( regStatus );
        whereBuffer.append( cdeIdWhere );
        whereBuffer.append( decWhere );
        whereBuffer.append( vdWhere );
        whereBuffer.append( latestWhere );
        whereBuffer.append( docWhere );
        whereBuffer.append( vvWhere );
        whereBuffer.append( deDerivWhere );


        whereClause = whereBuffer.toString();
        String fromWhere = " from sbr.data_elements_view de , " +
                "sbr.reference_documents_view rd , " +
                "sbr.contexts_view conte " +
                vdFrom +
                fromClause +
                registrationFrom +
                wkFlowFrom +
                deDerivFrom +
                " where " +
                " conte.pal_name = '" + palName + "' and " +
                " de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text'" +
                registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                " and de.asl_name != 'RETIRED DELETED' " +
                " and conte.conte_idseq = de.conte_idseq " +
                whereClause + registrationWhere + workFlowWhere + deDerivWhere;

        StringBuffer finalSqlStmt = new StringBuffer();

        finalSqlStmt.append( selectClause );
        finalSqlStmt.append( fromWhere );

        sqlStmt = finalSqlStmt.toString();
    }
    protected void buildSql()
    {

        if( query.isEmpty() )
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

        String vdFrom = "";
        String latestWhere = "";
        String fromClause = "";
        String deDerivWhere = "";
        String deDerivFrom = "";
        String whereClause = "";
        StringBuilder whereBuffer = new StringBuilder();


        String registrationExcludeWhere = "";
        //excludeArr will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( excludeArr ) )
        {
            registrationExcludeWhere = " and " + getExcludeWhereClause( "nvl(acr.registration_status,'-1')", excludeArr );
        }

        String workflowExcludeWhere = "";
        //aslNameExcludeList will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( aslNameExcludeList ) )
        {
            //workflowExcludeWhere = " and " + searchBean.getExcludeWhereClause( "asl.asl_name", aslNameExcludeList );
            workflowExcludeWhere = " and " + getExcludeWhereClause( "asl.asl_name", aslNameExcludeList );
        }

        String contextExludeWhere = "";
        //CONTEXT_EXCLUDES will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !CONTEXT_EXCLUDES.equals( "" ) )
        {
            contextExludeWhere = " and conte.name NOT IN (" + CONTEXT_EXCLUDES + " )";
        }

        /*
        Will not be needed until "Search only Derived DEs" is implemented
        cdeType = StringUtils.replaceNull( request.getParameter( "jspCDEType" ) );
        */

        //set filter on "version"
        latestWhere = " and de.latest_version_ind = 'Yes' "; //basic search, only return the latest version as default

        String wkFlowWhere = "";
        String cdeIdWhere = "";
        String vdWhere = "";
        String decWhere = "";
        String docWhere = "";
        String vvWhere = "";
        String regStatus = "";

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

        /*
        Will not be needed until "Search only Derived DEs" is implemented
        if( !cdeType.equals( "" ) )
        {
            deDerivWhere = " and comp_de.P_DE_IDSEQ = de.de_idseq";
            deDerivFrom = ", sbr.COMPLEX_DATA_ELEMENTS_VIEW comp_de ";
        }
        */

        whereBuffer.append( wkFlowWhere );
        whereBuffer.append( regStatus );
        whereBuffer.append( cdeIdWhere );
        whereBuffer.append( decWhere );
        whereBuffer.append( vdWhere );
        whereBuffer.append( latestWhere );
        whereBuffer.append( docWhere );
        whereBuffer.append( vvWhere );
        whereBuffer.append( deDerivWhere );


        whereClause = whereBuffer.toString();
        String fromWhere = " from sbr.data_elements_view de , " +
                "sbr.reference_documents_view rd , " +
                "sbr.contexts_view conte " +
                vdFrom +
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

    protected String buildSearchTextWhere( String text, String[] searchDomain, String searchMode )
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
            {
                return " and de.de_idseq IN " + umlAltNameWhere;
            } else
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


    public String getExcludeWhereClause( String colName, String[] excludeArr )
    {
        String whereClauseStr = null;
        if( ( excludeArr == null ) || ( excludeArr.length < 1 ) )
        {
            return null;
        }

        for( int i = 0; i < excludeArr.length; i++ )
        {
            if( whereClauseStr == null )
            {
                whereClauseStr = " " + colName + " NOT IN ('" + excludeArr[i] + "'";
            } else
            {
                whereClauseStr = whereClauseStr + " , '" + excludeArr[i] + "'";
            }
        }
        whereClauseStr = whereClauseStr + " ) ";
        return whereClauseStr;
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