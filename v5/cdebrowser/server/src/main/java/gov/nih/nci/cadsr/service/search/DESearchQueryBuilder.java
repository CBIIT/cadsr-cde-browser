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
    private String programArea = "";

    public DESearchQueryBuilder()
    {
    }

    public DESearchQueryBuilder( String clientQuery, String clientSearchMode, int clientSearchField, String programArea )
    {
        logger.debug( "DESearchQueryBuilder:  clientQuery[" + clientQuery + "]   clientSearchMode[" + clientSearchMode + "]" +
                "  clientSearchField[" + clientSearchField + "]   programArea[" + programArea + "]" );
        this.query = clientQuery;
        this.clientSearchMode = clientSearchMode;
        this.clientSearchField = clientSearchField;
        this.programArea = programArea;

        buildSql();
    }

    public DESearchQueryBuilder( String clientQuery, String clientSearchMode, int clientSearchField )
    {
        this( clientQuery, clientSearchMode, clientSearchField, "" );
    }

    public void setProgramArea( String programArea )
    {
        this.programArea = programArea;
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
            registrationExcludeWhere = " AND " + getExcludeWhereClause( "nvl(acr.registration_status,'-1')", excludeArr );
        }

        String workflowExcludeWhere = "";
        //aslNameExcludeList will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtils.isArrayWithEmptyStrings( aslNameExcludeList ) )
        {
            //workflowExcludeWhere = " AND " + searchBean.getExcludeWhereClause( "asl.asl_name", aslNameExcludeList );
            workflowExcludeWhere = " AND " + getExcludeWhereClause( "asl.asl_name", aslNameExcludeList );
        }

        String contextExludeWhere = "";
        //CONTEXT_EXCLUDES will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !CONTEXT_EXCLUDES.equals( "" ) )
        {
            contextExludeWhere = " AND conte.name NOT IN (" + CONTEXT_EXCLUDES + " )";
        }

        /*
        Will not be needed until "Search only Derived DEs" is implemented
        cdeType = StringUtils.replaceNull( request.getParameter( "jspCDEType" ) );
        */

        //set filter on "version"
        latestWhere = " AND de.latest_version_ind = 'Yes' "; //basic search, only return the latest version as default

        String programAreaWhere = "";
        String wkFlowWhere = "";
        String cdeIdWhere = "";
        String vdWhere = "";
        String decWhere = "";
        String docWhere = "";
        String vvWhere = "";
        String regStatus = "";

        if( !programArea.isEmpty() )
        {
            programAreaWhere = " conte.pal_name = '" + programArea + "' AND ";
        }

        wkFlowWhere = this.buildStatusWhereClause( statusWhere );

        //check if registration status is selected
        regStatus = this.buildRegStatusWhereClause( regStatusesWhere );

        if( clientSearchField == PUBLIC_ID_FIELD )
        {
            String newCdeStr = StringReplace.strReplace( query, "*", "%" );
            cdeIdWhere = " AND " + buildSearchString( "to_char(de.cde_id) LIKE 'SRCSTR'",
                    newCdeStr, clientSearchMode );
        }

        if( !valueDomain.equals( "" ) )
        {
            vdWhere = " AND vd.vd_idseq = '" + valueDomain + "'"
                    + " AND vd.vd_idseq = de.vd_idseq ";
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
            deDerivWhere = " AND comp_de.P_DE_IDSEQ = de.de_idseq";
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
        String fromWhere = " FROM sbr.data_elements_view de , " +
                "sbr.reference_documents_view rd , " +
                "sbr.contexts_view conte " +
                vdFrom +
                fromClause +
                registrationFrom +
                wkFlowFrom +
                deDerivFrom +
                " WHERE " +
                programAreaWhere +
                " de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text'" +
                registrationExcludeWhere + workflowExcludeWhere + contextExludeWhere +
                " AND de.asl_name != 'RETIRED DELETED' " +
                " AND conte.conte_idseq = de.conte_idseq " +
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
            wkFlowWhere = " AND de.asl_name = '" + wkFlow + "'";
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
            wkFlowWhere = " AND de.asl_name IN (" + wkFlow + ")";
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
            regStatWhere = " AND acr.registration_status = '" + regStatus + "'";
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
            regStatWhere = " AND acr.registration_status IN (" + regStatus + ")";

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
            longNameWhere = buildSearchString( "UPPER (de1.long_name) LIKE UPPER ('SRCSTR') ", newSearchStr, searchMode );
        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "Short Name" ) )
        {

            shortNameWhere = buildSearchString( "UPPER (de1.preferred_name) LIKE UPPER ('SRCSTR') ", newSearchStr, searchMode );
        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "Doc Text" ) ||
                StringUtils.containsKey( searchDomain, "Hist" ) )
        {

            docTextSearchWhere =
                    buildSearchString( "UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('SRCSTR') ", newSearchStr, searchMode );
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
            searchWhere = " AND " + docTextSearchWhere;
        }
        else if( docTextSearchWhere != null )
        {
            searchWhere = searchWhere + " OR " + docTextSearchWhere;
            searchWhere = " AND (" + searchWhere + ") ";
        }

        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                ( StringUtils.containsKey( searchDomain, "Doc Text" ) &&
                        StringUtils.containsKey( searchDomain, "Hist" ) ) )
        {
            docWhere = "(SELECT de_idseq "
                    + " FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 "
                    + " WHERE  de1.de_idseq  = rd1.ac_idseq (+) "
                    + " AND    rd1.dctl_name (+) = 'Preferred Question Text' "
                    + searchWhere
                    + " UNION "
                    + " SELECT de_idseq "
                    + " FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 "
                    + " WHERE  de2.de_idseq  = rd2.ac_idseq (+) "
                    + " AND    rd2.dctl_name (+) = 'Alternate Question Text' "
                    + " AND    " + buildSearchString( "UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('SRCSTR') ", newSearchStr, searchMode ) + ") ";


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
            docWhere = "(SELECT de_idseq "
                    + " FROM sbr.data_elements_view de1 "
                    + " WHERE  " + searchWhere + " ) ";

        }
        else if( docWhere == null && docTextTypeWhere != null )
        {
            docWhere = "(SELECT de_idseq "
                    + " FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 "
                    + " WHERE  de1.de_idseq  = rd1.ac_idseq (+) "
                    + " AND  " + docTextTypeWhere
                    + searchWhere + " ) ";

        }


        if( StringUtils.containsKey( searchDomain, "ALL" ) ||
                StringUtils.containsKey( searchDomain, "UML ALT Name" ) )
        {
            umlAltNameWhere =
                    " (SELECT de_idseq  FROM sbr.designations_view dsn, sbr.data_elements_view de1  "
                            + "WHERE  de1.de_idseq  = dsn.ac_idseq (+)  "
                            + "AND dsn.detl_name = 'UML Class:UML Attr'  AND "
                            + buildSearchString( "UPPER (nvl(dsn.name,'%')) LIKE UPPER ('SRCSTR')", newSearchStr, searchMode )
                            + " )";

            if( docWhere == null )
            {
                return " AND de.de_idseq IN " + umlAltNameWhere;
            }
            else
            {
                String nameWhere = " AND de.de_idseq IN (" + umlAltNameWhere
                        + " UNION " + docWhere + ") ";
                return nameWhere;
            }
        }
        logger.debug( "  buildSearchTextWhere - docWhere: " + docWhere );
        logger.debug( "  buildSearchTextWhere - docTextTypeWhere: " + docTextTypeWhere );

        return " AND de.de_idseq IN " + docWhere;
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
            oper = " OR ";
        }
        else
        {
            oper = " AND ";
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
            }
            else
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
                + " OR " + matcher.replaceAll( word + " %" )
                + " OR " + matcher.replaceAll( word )
                + " OR " + matcher.replaceAll( "% " + word ) + ")";
    }


    public String getQueryCdeByProtocolForm( String id )
    {
        return "SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbrext.quest_contents_view_ext qc ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             qc.dn_crf_idseq = '" + id + "'\n" +
                "AND             qc.qtl_name = 'QUESTION'\n" +
                "AND             qc.de_idseq = de.de_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)";
    }

    public String getQueryCdeByProtocol( String protocolId )
    {
        return "SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbrext.quest_contents_view_ext frm ,\n" +
                "                sbrext.protocol_qc_ext ptfrm ,\n" +
                "                sbrext.protocols_view_ext pt ,\n" +
                "                sbrext.quest_contents_view_ext qc ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' )\n" +
                "AND             asl.asl_name NOT                      IN ( 'CMTE APPROVED' ,\n" +
                "                                                          'CMTE SUBMTD' ,\n" +
                "                                                          'CMTE SUBMTD USED' ,\n" +
                "                                                          'RETIRED ARCHIVED' ,\n" +
                "                                                          'RETIRED PHASED OUT' ,\n" +
                "                                                          'RETIRED WITHDRAWN' )\n" +
                "AND             conte.NAME NOT IN ( 'TEST',\n" +
                "                                   'Training' )\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             pt.proto_idseq = ptfrm.proto_idseq\n" +
                "AND             frm.qc_idseq = ptfrm.qc_idseq\n" +
                "AND             frm.latest_version_ind = 'Yes'\n" +
                "AND             frm.qtl_name = 'CRF'\n" +
                "AND             qc.dn_crf_idseq = frm.qc_idseq\n" +
                "AND             qc.qtl_name = 'QUESTION'\n" +
                "AND             qc.de_idseq = de.de_idseq\n" +
                "AND             pt.proto_idseq = '" + protocolId + "'\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)";
    }

    public String getQueryCdeByContextClassificationSchemeItem( String classificationSchemeItemId )
    {
        return " SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbr.ac_csi_view acs ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' )\n" +
                "AND             asl.asl_name NOT                      IN ( 'CMTE APPROVED' ,\n" +
                "                                                          'CMTE SUBMTD' ,\n" +
                "                                                          'CMTE SUBMTD USED' ,\n" +
                "                                                          'RETIRED ARCHIVED' ,\n" +
                "                                                          'RETIRED PHASED OUT' ,\n" +
                "                                                          'RETIRED WITHDRAWN' )\n" +
                "AND             conte.NAME NOT IN ( 'TEST',\n" +
                "                                   'Training' )\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             acs.cs_csi_idseq = '" + classificationSchemeItemId + "'\n" +
                "AND             acs.ac_idseq = de.de_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)";
    }

    public String getQueryCdeByContextClassificationScheme( String classificationSchemeId )
    {
        return " SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' )\n" +
                "AND             asl.asl_name NOT                      IN ( 'CMTE APPROVED' ,\n" +
                "                                                          'CMTE SUBMTD' ,\n" +
                "                                                          'CMTE SUBMTD USED' ,\n" +
                "                                                          'RETIRED ARCHIVED' ,\n" +
                "                                                          'RETIRED PHASED OUT' ,\n" +
                "                                                          'RETIRED WITHDRAWN' )\n" +
                "AND             conte.NAME NOT IN ( 'TEST',\n" +
                "                                   'Training' )\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)\n" +
                "AND             de.de_idseq IN\n" +
                "                (\n" +
                "                       SELECT de_idseq\n" +
                "                       FROM   sbr.data_elements_view de ,\n" +
                "                              sbr.ac_csi_view acs,\n" +
                "                              sbr.cs_csi_view csc\n" +
                "                       WHERE  csc.cs_idseq = '" + classificationSchemeId + "'\n" +
                "                       AND    csc.cs_csi_idseq = acs.cs_csi_idseq\n" +
                "                       AND    acs.ac_idseq = de_idseq )";
    }

    public String getQueryCDEsOwnedAndUsedByContext( String conteId )
    {
        return " SELECT DISTINCT de.de_idseq,\n" +
                "                de.preferred_name                        de_preferred_name,\n" +
                "                de.long_name,\n" +
                "                rd.doc_text,\n" +
                "                conte.name,\n" +
                "                de.asl_name,\n" +
                "                To_char(de.cde_id)                       de_cdeid,\n" +
                "                de.version                               de_version,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby,\n" +
                "                de.vd_idseq,\n" +
                "                de.dec_idseq,\n" +
                "                de.conte_idseq,\n" +
                "                de.preferred_definition,\n" +
                "                acr.registration_status,\n" +
                "                rsl.display_order,\n" +
                "                asl.display_order                        wkflow_order,\n" +
                "                de.cde_id                                cdeid\n" +
                "FROM   sbr.data_elements_view de,\n" +
                "       sbr.reference_documents_view rd,\n" +
                "       sbr.contexts_view conte,\n" +
                "       sbr.ac_registrations_view acr,\n" +
                "       sbr.reg_status_lov_view rsl,\n" +
                "       sbr.ac_status_lov_view asl\n" +
                "WHERE  de.de_idseq = rd.ac_idseq (+)\n" +
                "       AND rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "       AND Nvl(acr.registration_status, '-1') NOT IN ( 'Retired' )\n" +
                "       AND asl.asl_name NOT IN ( 'CMTE APPROVED', 'CMTE SUBMTD',\n" +
                "                                 'CMTE SUBMTD USED',\n" +
                "                                 'RETIRED ARCHIVED',\n" +
                "                                 'RETIRED PHASED OUT', 'RETIRED WITHDRAWN' )\n" +
                "       AND conte.name NOT IN ( 'TEST', 'Training' )\n" +
                "       AND de.asl_name != 'RETIRED DELETED'\n" +
                "       AND conte.conte_idseq = de.conte_idseq\n" +
                "       AND de.de_idseq IN (SELECT ac_idseq\n" +
                "                           FROM   sbr.designations_view des\n" +
                "                           WHERE\n" +
                "           des.conte_idseq = '" + conteId + "'\n" +
                "           AND des.detl_name = 'USED_BY'\n" +
                "                           UNION\n" +
                "                           SELECT de_idseq\n" +
                "                           FROM   sbr.data_elements_view de1\n" +
                "                           WHERE\n" +
                "           de1.conte_idseq = '" + conteId + "')\n" +
                "       AND de.de_idseq = acr.ac_idseq (+)\n" +
                "       AND acr.registration_status = rsl.registration_status (+)\n" +
                "       AND de.asl_name = asl.asl_name (+)  ";
    }


}
