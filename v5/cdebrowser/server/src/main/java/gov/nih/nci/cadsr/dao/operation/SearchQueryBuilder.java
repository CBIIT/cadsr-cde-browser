package gov.nih.nci.cadsr.dao.operation;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.model.SearchPreferences;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import gov.nih.nci.cadsr.service.search.ProcessConstants;

public class SearchQueryBuilder extends AbstractSearchQueryBuilder
{
    private static Logger logger = LogManager.getLogger( SearchQueryBuilder.class.getName() );
    
    public SearchQueryBuilder()
    {

    }

	/**
     * @param clientName        The text the user put in the search text field in the UI.
     * @param clientSearchMode   Exact phrase, All of the words, OR At least one of the words.
     * @param clientSearchField  0 if user select Name field, 1 for Public ID.
     * @param programArea        Empty if All  -  Works
     * @param context            If empty, will not be used   -  Works
     * @param classification     If empty, will not be used   -  Does not work right yet
     * @param protocol           If empty, will not be used  - not implemented yet
     * @param workFlowStatus     sbr.ac_status_lov_view - asl_name   -  Works   If empty will use the exclude list  from common.WorkflowStatusEnum#getExcludList():
     * @param registrationStatus sbr.ac_registrations_view - registration_status     - not implemented yet
     */
    public String initSearchQueryBuilder(SearchCriteria searchCriteria, SearchPreferences searchPreferences)
    {
        logger.debug("Initializing Search query builder with Search Criteria : " + searchCriteria);
        logger.debug("Initializing Search query builder with Search Preferences : " + searchPreferences);
        
        String vdFrom = "";
        String deDerivWhere = "";
        String deDerivFrom = "";
        String whereClause = "";
        StringBuilder whereBuffer = new StringBuilder();
        String contextWhere = "";
        String programAreaWhere = "";
        String wkFlowWhere = "";
        String registrationStatusWhere = "";
        String cdeIdWhere = "";
        String vdWhere = "";
        String decWhere = "";
        String docWhere = "";
        String vvWhere = "";
        String classificationWhere = "";
        String regStatus = "";
        String protocolWhere = "";
        
     // This note was in the source could of the previous version: "release 3.0 updated to add display order for registration status"
        String registrationFrom = " , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl";
        
        String classificationFrom = ", sbr.classification_schemes cls ";
        String protocolFrom = ", sbrext.quest_contents_view_ext frm, sbrext.protocol_qc_ext ptfrm, sbrext.protocols_view_ext pt, sbrext.quest_contents_view_ext qc ";
        String registrationWhere = " AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) ";


        if(StringUtils.isBlank(searchCriteria.getClassification()))
        {
            classificationFrom = "";
        }
        else
        {
            classificationWhere = " AND de.de_idseq IN (SELECT de_idseq FROM   sbr.data_elements_view de , sbr.ac_csi_view acs, sbr.cs_csi_view csc " + 
            					  "WHERE  csc.cs_idseq = '" + searchCriteria.getClassification() + "' " +
            					  "AND    csc.cs_csi_idseq = acs.cs_csi_idseq AND acs.ac_idseq = de_idseq ) ";
        }
        
        if(StringUtils.isBlank(searchCriteria.getProtocol()))
        {
            protocolFrom = "";
        }
        else
        {
            protocolWhere = " AND pt.proto_idseq = ptfrm.proto_idseq AND frm.qc_idseq = ptfrm.qc_idseq AND frm.qtl_name = 'CRF'" +
            				" AND qc.dn_crf_idseq = frm.qc_idseq AND qc.qtl_name = 'QUESTION' AND qc.de_idseq = de.de_idseq" +
            				" AND pt.proto_idseq = '" + searchCriteria.getProtocol() + "' ";
        }

        ////////////////////////////////////////////////////
        // we use selected or excluded Registration Status here
        String registrationExcludeWhere = "";

        List<String> registrationStatusExcluded = searchPreferences.getRegistrationStatusExcluded();
        if (! registrationStatusExcluded.isEmpty()) {
        	String[] excludeRegistrationStatusArr = registrationStatusExcluded.toArray(new String[registrationStatusExcluded.size()]);
            registrationExcludeWhere = " AND " + getExcludeWhereClause( "nvl(acr.registration_status,'-1')", excludeRegistrationStatusArr );
        }
        //FIXME  clean this up - regStatusesWhere is All.  This is where we will plug in a user Registration Status from Advanced Search
        regStatus = this.buildRegStatusWhereClause( regStatusesWhere );
        
        //This is a criteria which comes from drop down box of the basic search
        if (StringUtils.isNotBlank(searchCriteria.getRegistrationStatus()))
        {
        	registrationStatusWhere = " AND acr.registration_status = '" + searchCriteria.getRegistrationStatus() + "' ";
        }

        ////////////////////////////////////////////////////
        // WorkFlowStatus
        // If it is empty, use exclude list in search preferences
        String workflowWhere = "";
        List<String> workflowStatusExcluded = searchPreferences.getWorkflowStatusExcluded();
        if (! workflowStatusExcluded.isEmpty()) {
        	workflowWhere = " AND asl.asl_name NOT IN " + searchPreferences.buildfExcludedWorkflowSql();
        }
        
        if(! StringUtils.isBlank(searchCriteria.getWorkFlowStatus()))
        {
            workflowWhere += " AND asl.asl_name = '" + searchCriteria.getWorkFlowStatus() + "' ";
        }
        //TODO we can consider to simplify this query workflowWhere. If searchCriteria.workFlowStatus is in excluded list there will be no result anyway
        
        //Context excluded
        ////////////////////////////////////////////////////
        // This excludes Search Preferences excluded context.
        String contextExludeWhere = "";
        String excludedContext = searchPreferences.buildContextExclided();
        if(StringUtils.isNotBlank(excludedContext))
        {
            contextExludeWhere = " AND conte.name NOT IN (" + excludedContext + " )";
        }

        ///////////////////////////////////////////////////////
        // Filter for only a specific context is added only if protocol where is not already added otherwise,
        //the left context tree search by protocol is not matching up with the drop down search by context and protocol
        if(StringUtils.isNotBlank(searchCriteria.getContext()) && StringUtils.isBlank(searchCriteria.getProtocol()))
        {
            contextWhere = " de.de_idseq IN (SELECT ac_idseq FROM sbr.designations_view des WHERE des.conte_idseq = '" + searchCriteria.getContext() + "' " +
        				   " AND des.detl_name = 'USED_BY' UNION SELECT de_idseq FROM  sbr.data_elements_view de1 WHERE de1.conte_idseq = '" + searchCriteria.getContext() + "') AND ";
        }

        ///////////////////////////////////////////////////////
        // Filter for only a specific programArea
        if(StringUtils.isNotBlank(searchCriteria.getProgramArea()))
        {
            programAreaWhere = " conte.pal_name = '" + searchCriteria.getProgramArea() + "' AND ";
        }


        if( StringUtils.isNotBlank(searchCriteria.getPublicId()) && ( !searchCriteria.getPublicId().trim().equals( "*" ) ) )
        {
            String newCdeStr = StringReplace.strReplace( searchCriteria.getPublicId(), "*", "%" );
            cdeIdWhere = " AND " + buildSearchString( "to_char(de.cde_id) LIKE 'SRCSTR'", newCdeStr, searchCriteria.getSearchMode() )
            			+ " AND de.latest_version_ind = 'Yes' ";
        }

        if(StringUtils.isNotBlank(valueDomain))
        {
            vdWhere = " AND vd.vd_idseq = '" + valueDomain + "'"
                    + " AND vd.vd_idseq = de.vd_idseq ";
            vdFrom = " ,sbr.value_domains_view vd ";

        }

        if(StringUtils.isNotBlank(searchCriteria.getName()))
        {
        	// AppScan
            String clientName = StringUtilities.sanitizeForSql( searchCriteria.getName() );
            docWhere = this.buildSearchTextWhere( clientName, searchIn, searchCriteria.getSearchMode() );
        }

        whereBuffer.append( wkFlowWhere );
        whereBuffer.append( registrationStatusWhere );
        whereBuffer.append( classificationWhere );
        whereBuffer.append( regStatus );
        whereBuffer.append( cdeIdWhere );
        whereBuffer.append( decWhere );
        whereBuffer.append( vdWhere );
        whereBuffer.append( docWhere );
        whereBuffer.append( vvWhere );
        whereBuffer.append( deDerivWhere ).append(protocolWhere);

        whereClause = whereBuffer.toString();

        String fromWhere = " FROM sbr.data_elements_view de , " +
                "sbr.reference_documents_view rd , " +
                "sbr.contexts_view conte " +
                vdFrom + classificationFrom + protocolFrom +
                registrationFrom +
                wkFlowFrom +
                deDerivFrom +
                " WHERE " +
                contextWhere +
                programAreaWhere +
                " de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text'" +
                registrationExcludeWhere + workflowWhere + contextExludeWhere +
                " AND de.asl_name != 'RETIRED DELETED' " +
                " AND conte.conte_idseq = de.conte_idseq " +
                whereClause + registrationWhere + workFlowWhere + deDerivWhere;

        StringBuffer finalSqlStmt = new StringBuffer();

        finalSqlStmt.append( selectClause );
        finalSqlStmt.append( fromWhere );

        String sqlStmt = finalSqlStmt.toString();
        return sqlStmt;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * This method returns the clientQuery for whole word matching the input param word
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

        if( StringUtilities.containsKey( searchDomain, "ALL" ) ||
                StringUtilities.containsKey( searchDomain, "Long Name" ) )
        {
            longNameWhere = buildSearchString( "UPPER (de1.long_name) LIKE UPPER ('SRCSTR') ", newSearchStr, searchMode );
        }

        if( StringUtilities.containsKey( searchDomain, "ALL" ) ||
                StringUtilities.containsKey( searchDomain, "Short Name" ) )
        {

            shortNameWhere = buildSearchString( "UPPER (de1.preferred_name) LIKE UPPER ('SRCSTR') ", newSearchStr, searchMode );
        }

        if( StringUtilities.containsKey( searchDomain, "ALL" ) ||
                StringUtilities.containsKey( searchDomain, "Doc Text" ) ||
                StringUtilities.containsKey( searchDomain, "Hist" ) )
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

        if( StringUtilities.containsKey( searchDomain, "ALL" ) ||
                ( StringUtilities.containsKey( searchDomain, "Doc Text" ) &&
                        StringUtilities.containsKey( searchDomain, "Hist" ) ) )
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
        else if( StringUtilities.containsKey( searchDomain, "Doc Text" ) )
        {
            docTextTypeWhere = "rd1.dctl_name (+) = 'Preferred Question Text'";
        }
        else if( StringUtilities.containsKey( searchDomain, "Hist" ) )
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


        if( StringUtilities.containsKey( searchDomain, "ALL" ) ||
                StringUtilities.containsKey( searchDomain, "UML ALT Name" ) )
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


    protected String buildRegStatusWhereClause( String[] regStatusList )
    {

        if( ( regStatusList == null ) || ( StringUtilities.containsKey( regStatusList, "ALL" ) || ( regStatusList[0].isEmpty() ) ) )
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

}
