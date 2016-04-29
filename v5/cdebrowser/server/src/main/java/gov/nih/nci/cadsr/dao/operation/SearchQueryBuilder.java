package gov.nih.nci.cadsr.dao.operation;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.service.search.ProcessConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQueryBuilder extends AbstractSearchQueryBuilder
{
    private static Logger logger = LogManager.getLogger( SearchQueryBuilder.class.getName() );

    private String sqlStmt = "";

    public SearchQueryBuilder()
    {

    }

    public String getSqlStmt() {
		return sqlStmt;
	}

	public void setSqlStmt(String sqlStmt) {
		this.sqlStmt = sqlStmt;
	}
    
    public SearchQueryBuilder(
            String clientName, String clientSearchMode, String clientPublicId,
            String programArea,
            String context, String classification, String protocol,
            String workFlowStatus, String registrationStatus,
            String conceptName, String conceptCode )
    {
        initSeqrchQueryBuilder( clientName, clientSearchMode, clientPublicId, programArea, context, classification, protocol, workFlowStatus, registrationStatus, conceptName, conceptCode );
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
    public void initSeqrchQueryBuilder(
            String clientName, String clientSearchMode, String clientPublicId,
            String programArea, String context, String classification, String protocol,
            String workFlowStatus, String registrationStatus,
            String conceptName, String conceptCode )
    {
        logger.debug( "initSeqrchQueryBuilder( \"" + clientName + "\", \"" + clientSearchMode + "\", " + clientPublicId + ", \"" + programArea + "\", \"" + context + "\", \"" + classification +
                "\", \"" + protocol + "\", \"" + workFlowStatus + "\", \"" + registrationStatus + "\", \"" + conceptName + "\", \"" + conceptCode + "\" )" );
        
        String vdFrom = "";
        String fromClause = "";
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

        if(StringUtils.isBlank(classification))
        {
            classificationFrom = "";
        }
        else
        {
            classificationWhere = " AND de.de_idseq IN (SELECT de_idseq FROM   sbr.data_elements_view de , sbr.ac_csi_view acs, sbr.cs_csi_view csc " + 
            					  "WHERE  csc.cs_idseq = '" + classification + "' " +
            					  "AND    csc.cs_csi_idseq = acs.cs_csi_idseq AND    acs.ac_idseq = de_idseq )";
        }
        
        if(StringUtils.isBlank(protocol))
        {
            protocolFrom = "";
        }
        else
        {
            protocolWhere = " AND pt.proto_idseq = ptfrm.proto_idseq AND frm.qc_idseq = ptfrm.qc_idseq AND frm.qtl_name = 'CRF'" +
            				" AND qc.dn_crf_idseq = frm.qc_idseq AND qc.qtl_name = 'QUESTION' AND qc.de_idseq = de.de_idseq" +
            				" AND pt.proto_idseq = '" + protocol + "' ";
        }

        ////////////////////////////////////////////////////
        // FIXME - Cleanup/implement use selected or excluded Registration Status here
        String registrationExcludeWhere = "";
        //excludeArr will eventually be set as a preference or settings from client, currently set in the abstract class.
        if( !StringUtilities.isArrayWithEmptyStrings( excludeArr ) )
        {
            registrationExcludeWhere = " AND " + getExcludeWhereClause( "nvl(acr.registration_status,'-1')", excludeArr );
        }
        //FIXME  clean this up - regStatusesWhere is All.  This is where we will plug in a user Registration Status
        regStatus = this.buildRegStatusWhereClause( regStatusesWhere );

        if (StringUtils.isNotBlank(registrationStatus))
        {
        	registrationStatusWhere = " AND acr.registration_status = '" + registrationStatus + "' ";
        }
        
        ////////////////////////////////////////////////////
        // WorkFlowStatus
        // If it is empty, use the default exclude list in WorkflowStatusEnum
        String workflowWhere;
        if(StringUtils.isBlank(workFlowStatus))
        {
            workflowWhere = " AND " + WorkflowStatusEnum.getExcludList();
        }
        else
        {
            workflowWhere = " AND asl.asl_name = '" + workFlowStatus + "'";
        }


        ////////////////////////////////////////////////////
        // This excludes TEST and Training, may become an option later.
        String contextExludeWhere = "";
        if(StringUtils.isNotBlank(CONTEXT_EXCLUDES))
        {
            contextExludeWhere = " AND conte.name NOT IN (" + CONTEXT_EXCLUDES + " )";
        }

        ///////////////////////////////////////////////////////
        // Filter for only a specific context is added only if protocol where is not already added otherwise,
        //the left context tree search by protocol is not matching up with the drop down search by context and protocol
        if(StringUtils.isNotBlank(context) && StringUtils.isBlank(protocol))
        {
            contextWhere = " de.de_idseq IN (SELECT ac_idseq FROM sbr.designations_view des WHERE des.conte_idseq = '" +  context + "' " +
        				   " AND des.detl_name = 'USED_BY' UNION SELECT de_idseq FROM  sbr.data_elements_view de1 WHERE de1.conte_idseq = '" + context + "') AND ";
        }

        ///////////////////////////////////////////////////////
        // Filter for only a specific programArea
        if(StringUtils.isNotBlank(programArea))
        {
            programAreaWhere = " conte.pal_name = '" + programArea + "' AND ";
        }


        if( StringUtils.isNotBlank(clientPublicId) && ( !clientPublicId.trim().equals( "*" ) ) )
        {
            String newCdeStr = StringReplace.strReplace( clientPublicId, "*", "%" );
            cdeIdWhere = " AND " + buildSearchString( "to_char(de.cde_id) LIKE 'SRCSTR'", newCdeStr, clientSearchMode )
            			+ " AND de.latest_version_ind = 'Yes' ";
        }

        if(StringUtils.isNotBlank(valueDomain))
        {
            vdWhere = " AND vd.vd_idseq = '" + valueDomain + "'"
                    + " AND vd.vd_idseq = de.vd_idseq ";
            vdFrom = " ,sbr.value_domains_view vd ";

        }

        if(StringUtils.isNotBlank(clientName))
        {
        	// AppScan
            clientName = StringUtilities.sanitizeForSql( clientName );
            docWhere = this.buildSearchTextWhere( clientName, searchIn, clientSearchMode );
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
                vdFrom +
                fromClause +
                registrationFrom +
                wkFlowFrom +
                deDerivFrom +
                classificationFrom + protocolFrom +
                " WHERE        " +
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

        sqlStmt = finalSqlStmt.toString();
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
