package gov.nih.nci.cadsr.dao.operation;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nih.nci.cadsr.dao.DataElementConceptDAO;
import gov.nih.nci.cadsr.dao.RegistrationStatusDAO;
import gov.nih.nci.cadsr.dao.WorkflowStatusDAO;
import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import gov.nih.nci.cadsr.service.search.ProcessConstants;

public class SearchQueryBuilder extends AbstractSearchQueryBuilder
{
    private static Logger logger = LogManager.getLogger( SearchQueryBuilder.class.getName() );

    private DataElementConceptDAO dataElementConceptDAO;

    public SearchQueryBuilder()
    {

    }

	/**
     * @param searchCriteria
     * @param searchPreferences
     * @return String SQL
     */
    public String initSearchQueryBuilder( SearchCriteria searchCriteria, SearchPreferencesServer searchPreferences, 
    		List<String> allowedWorkflowStatuses, List<String> allowedRegStatuses)
    {
        logger.debug( "Initializing Search query builder with Search Preferences : " + searchPreferences );
        logger.debug( "Initializing Search query builder with Search Criteria : " + searchCriteria );
        
        String vdFrom = "";
        String deDerivWhere = "";
        String deDerivFrom = "";
        String whereClause = "";
        StringBuilder whereBuffer = new StringBuilder();
        String contextWhere = "";
        String programAreaWhere = "";
        String registrationStatusWhere = "";
        String cdeIdWhere = "";
        String vdWhere = "";
        String decWhere = "";
        String docWhere = "";
        String vvWhere = "";
        String classificationWhere = "";
        String csiWhere = "";
        String regStatus = "";
        String protocolWhere = "";
        String formWhere = "";
        String dataElementConceptWhere = "";
        String permissibleValueWhere = "";
        String objectClassWhere = "";
        String versionIndWhere = "";
        String propertyWhere = "";
        String derivedDEWhere = "";

        // This note was in the source code of the previous version: "release 3.0 updated to add display order for registration status"
        String registrationFrom = " , sbr.ac_registrations_view acr";

        String csiFrom = ", sbr.ac_csi_view acs ";
        String protocolFrom = ", sbrext.quest_contents_view_ext frm, sbrext.protocol_qc_ext ptfrm, sbrext.protocols_view_ext pt, sbrext.quest_contents_view_ext qc ";
        String formFrom = ", sbrext.quest_contents_view_ext qc ";
        String registrationWhere = " AND de.de_idseq = acr.ac_idseq (+)";

        if( StringUtils.isBlank( searchCriteria.getClassification() ) )
        {

        }
        else
        {
            classificationWhere = " AND de.de_idseq IN (SELECT de_idseq FROM   sbr.data_elements_view de , sbr.ac_csi_view acs, sbr.cs_csi_view csc " +
                    "WHERE  csc.cs_idseq = '" + searchCriteria.getClassification() + "' " +
                    "AND    csc.cs_csi_idseq = acs.cs_csi_idseq AND acs.ac_idseq = de_idseq ) ";
        }

        if( StringUtils.isBlank( searchCriteria.getCsCsiIdSeq() ) )
        {
            csiFrom = "";
        }
        else
        {
            csiWhere = " AND acs.cs_csi_idseq = '" + searchCriteria.getCsCsiIdSeq() + "' AND acs.ac_idseq = de.de_idseq";
        }

        if( StringUtils.isBlank( searchCriteria.getProtocol() ) )
        {
            protocolFrom = "";
        }
        else
        {
            protocolWhere = " AND pt.proto_idseq = ptfrm.proto_idseq AND frm.qc_idseq = ptfrm.qc_idseq AND frm.qtl_name = 'CRF'" +
                    " AND qc.dn_crf_idseq = frm.qc_idseq AND qc.qtl_name = 'QUESTION' AND qc.de_idseq = de.de_idseq" +
                    " AND pt.proto_idseq = '" + searchCriteria.getProtocol() + "' ";
        }

        if( StringUtils.isBlank( searchCriteria.getFormIdSeq() ) )
        {
            formFrom = "";
        }
        else
        {
            formWhere = " AND qc.dn_crf_idseq = '" + searchCriteria.getFormIdSeq() + "' AND qc.qtl_name = 'QUESTION' AND qc.de_idseq = de.de_idseq";
        }

        if( StringUtils.isBlank( searchCriteria.getPermissibleValue() ) )
        {
            permissibleValueWhere = "";
        }
        else
        {
            permissibleValueWhere = buildPermissibleValueWhere( searchCriteria.getPermissibleValue(), searchCriteria.getPvQueryType() );
        }

        if( StringUtils.isBlank( searchCriteria.getObjectClass() ) )
        {
            objectClassWhere = "";
        }
        else
        {
            objectClassWhere = buildObjectClassWhere( searchCriteria.getObjectClass().replace( "*", "%" ) );
        }
        if( StringUtils.isBlank( searchCriteria.getProperty() ) )
        {
        	propertyWhere = "";
        }
        else
        {
        	propertyWhere = buildPropertyWhere( searchCriteria.getProperty().replace( "*", "%" ) );
        }
        if (searchCriteria.getDerivedDEFlag() != null) {
	        if (searchCriteria.getDerivedDEFlag().equalsIgnoreCase("false")) {
	        	derivedDEWhere = "";
	        } else {
	        	derivedDEWhere = " AND de.de_idseq IN (SELECT data_elements.de_idseq FROM complex_data_elements , data_elements WHERE data_elements.de_idseq = complex_data_elements.p_de_idseq) ";
	        }
        }
        if( StringUtils.isBlank( searchCriteria.getDataElementConcept() ) )
        {
            dataElementConceptWhere = "";
        }
        else
        {
            // Get the DEC_IDSEQ values of any/all of the Data Element Concepts, that matched wild card input in getDataElementConcept
        	String newSearchStr = StringReplace.strReplace(searchCriteria.getDataElementConcept(), "'", "''");//Escape SQL single quote
        	List<DataElementConceptModel> dataElementConceptModels = dataElementConceptDAO.getDecByLongNameWildCard(newSearchStr);
            // If there are no matches for the dataElementConcept, than this query can never return results, so we return null right no
            if( dataElementConceptModels.isEmpty() )
            {
                return null;
            }
            else
            {
                if( dataElementConceptModels.size() == 1 )
                {
                    dataElementConceptWhere = " AND dec_idseq = '" + dataElementConceptModels.get( 0 ).getDecIdseq() + "' ";
                }
                else
                {
                    for( int i = 0; i < dataElementConceptModels.size(); i++ )
                    {
                        if( i == 0 )
                        {
                            dataElementConceptWhere = " AND (";
                        }

                        dataElementConceptWhere += " dec_idseq = '" + dataElementConceptModels.get( i ).getDecIdseq() + "' ";
                        if( i < ( dataElementConceptModels.size() - 1 ) )
                        {
                            dataElementConceptWhere += " OR ";
                        }

                    }
                    dataElementConceptWhere += " )";
                }
            }
        }
        //this is for search Concept Name and Concept type
        String conceptInputWhere = SearchQueryBuilderUtils.buildConceptWhere( searchCriteria );
        ////////////////////////////////////////////////////
        // we use selected or excluded Registration Status here
        String registrationExcludeWhere = "";
        //CDEBROWSER-703 Statuses taken from DB
        List<String> registrationStatusExcluded = searchPreferences.getRegistrationStatusExcluded();
        if( !registrationStatusExcluded.isEmpty() )
        {
            String[] excludeRegistrationStatusArr = registrationStatusExcluded.toArray( new String[registrationStatusExcluded.size()] );
            //CDEBROWSER-703 excluded received from search preferences excluded list not from UI
            registrationExcludeWhere = " AND " + getExcludeWhereClause( "nvl(acr.registration_status,'-1')", excludeRegistrationStatusArr);
        }

        //This is a criteria which comes from drop down box of the basic search
        if( StringUtils.isNotBlank( searchCriteria.getRegistrationStatus() ) )
        {
            //CDEBROWSER-703 Statuses taken from DB
            registrationStatusWhere = SearchQueryBuilderUtils.buildRegistrationWhere( searchCriteria.getRegistrationStatus(), "acr.registration_status" , allowedRegStatuses);
        }

        ////////////////////////////////////////////////////
        // WorkFlowStatus
        //use exclude list from session search preferences
        String workflowWhere = "";
        List<String> workflowStatusExcluded = searchPreferences.getWorkflowStatusExcluded();
        if( !workflowStatusExcluded.isEmpty() )
        {
            workflowWhere = " AND de.asl_name NOT IN " + searchPreferences.buildExcludedWorkflowSql();
        }

        if( !StringUtils.isBlank( searchCriteria.getWorkFlowStatus() ) )
        {
            workflowWhere += SearchQueryBuilderUtils.buildWorkflowWhere( searchCriteria.getWorkFlowStatus(), "de.asl_name", allowedWorkflowStatuses);
        }
        //TODO we can consider to simplify this query workflowWhere. If searchCriteria.workFlowStatus is in excluded list there will be no result anyway

        //Context excluded
        ////////////////////////////////////////////////////
        // This excludes Search Preferences excluded context.
        String contextExludeWhere = "";
        String excludedContext = searchPreferences.buildContextExclided();
        if( StringUtils.isNotBlank( excludedContext ) )
        {
            contextExludeWhere = " AND conte.name NOT IN (" + excludedContext + " )";
        }

        ///////////////////////////////////////////////////////
        // Filter for only a specific context is added only if protocol where is not already added otherwise,
        //the left context tree search by protocol is not matching up with the drop down search by context and protocol
        /*
        // This is the original version, I'm keeping it here for reference as I add Used By, Owned By, or Owned By/Used By
        if( StringUtils.isNotBlank( searchCriteria.getContext() ) && StringUtils.isBlank( searchCriteria.getProtocol() ) )
        {
            contextWhere = " de.de_idseq IN (SELECT ac_idseq FROM sbr.designations_view des WHERE des.conte_idseq = '" + searchCriteria.getContext() + "' " +
                    " AND des.detl_name = 'USED_BY' UNION SELECT de_idseq FROM  sbr.data_elements_view de1 WHERE de1.conte_idseq = '" + searchCriteria.getContext() + "') AND ";
        }
        */

        if( StringUtils.isNotBlank( searchCriteria.getContext() ) && StringUtils.isBlank( searchCriteria.getProtocol() ) )
        {
            switch( searchCriteria.getContextUse() )
            {
                case 0:
                    contextWhere = " AND conte.conte_idseq = '" + searchCriteria.getContext() + "' ";
                    break;
                case 1:
                    contextWhere = " AND de.de_idseq IN (select ac_idseq from sbr.designations_view des where des.conte_idseq = '" + searchCriteria.getContext() + "' and des.DETL_NAME = 'USED_BY')  ";
                    break;
                case 2:
                    contextWhere = " AND de.de_idseq IN (SELECT ac_idseq FROM sbr.designations_view des WHERE des.conte_idseq = '" + searchCriteria.getContext() + "' " +
                            " AND des.detl_name = 'USED_BY' UNION SELECT de_idseq FROM  sbr.data_elements_view de1 WHERE de1.conte_idseq = '" + searchCriteria.getContext() + "')  ";
            }

        }

        ///////////////////////////////////////////////////////
        // Filter for only a specific programArea
        String strProgramArea = searchCriteria.getProgramArea();
        if( StringUtils.isNotBlank(strProgramArea) && (StringUtils.isBlank( searchCriteria.getContext())))
        {
        	strProgramArea = strProgramArea.replaceAll("'", "''");//Escape SQL single quote preventing SQL injection
        	//We might consider to retrieve all PA from DB
            programAreaWhere = " conte.pal_name = '" + strProgramArea + "' AND ";
        }


        if( StringUtils.isNotBlank( searchCriteria.getPublicId() ))
        {
            String cdeStrPublicIdSqlFragment = SearchQueryBuilderUtils.buildSearchByPublicId(searchCriteria.getPublicId(), "de.cde_id");
            cdeIdWhere = cdeStrPublicIdSqlFragment;
            //search by Public ID follows now (since v.5.2.2.) similar version type rule that the other search type type do; see Ln 320 versionIndWhere String build based on searchCriteria";
            //publicIdVersion 0 - the latest, 1 - all the versions of DEs
        }

        if ((StringUtils.isNotBlank(searchCriteria.getValueDomain())) || (StringUtils.isNotBlank(searchCriteria.getVdTypeFlag())))
        {
        	vdWhere = SearchQueryBuilderUtils.buildValueDomainWhere(searchCriteria);
            vdFrom = " ,sbr.value_domains_view vd ";
        }

        if( StringUtils.isNotBlank( searchCriteria.getName() ) )
        {
            // AppScan
            String clientName = StringUtilities.sanitizeForSql( searchCriteria.getName() );

/*
            docWhere = this.buildSearchTextWhere( clientName, searchIn, searchCriteria.getSearchMode() );
*/
            docWhere = this.buildSearchTextWhere( clientName, searchCriteria.getFilteredinput().split( "\\s*,\\s*" ), searchCriteria.getSearchMode() );
        }

        whereBuffer.append( registrationStatusWhere );
        whereBuffer.append( classificationWhere );
        whereBuffer.append( csiWhere );
        whereBuffer.append( regStatus );
        whereBuffer.append( cdeIdWhere );
        whereBuffer.append( decWhere );
        whereBuffer.append( objectClassWhere );
        whereBuffer.append( propertyWhere );
        whereBuffer.append( derivedDEWhere );
        whereBuffer.append( vdWhere );
        whereBuffer.append( docWhere );
        whereBuffer.append( vvWhere );
        whereBuffer.append( dataElementConceptWhere );
        whereBuffer.append( conceptInputWhere );
        whereBuffer.append( permissibleValueWhere );
        whereBuffer.append( deDerivWhere ).append( protocolWhere ).append( formWhere );

        String altNamesWhere = SearchQueryBuilderUtils.buildAltNamesWhere( searchCriteria.getAltName(), searchCriteria.getAltNameType() );
        whereBuffer.append( altNamesWhere );

        whereClause = whereBuffer.toString();
        if(
        	((StringUtils.isEmpty(searchCriteria.getPublicId())) && (searchCriteria.getVersionType() == 0 )) || 
        	((StringUtils.isNotEmpty(searchCriteria.getPublicId())) && (searchCriteria.getPublicIdVersion() == 0))
        	)
        {
            versionIndWhere = " AND de.latest_version_ind = 'Yes' ";
        }
        //we do not check for No for the latest_version_ind because we have no UI for previous versions only

        String fromWhere = " FROM sbr.data_elements_view de , " +
                "sbr.reference_documents_view rd , " +
                "sbr.contexts_view conte " +
                vdFrom + csiFrom + protocolFrom + formFrom +
                registrationFrom +
                deDerivFrom +
                " WHERE " +

                programAreaWhere +
                " de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text'" +
                versionIndWhere + registrationExcludeWhere + workflowWhere + contextExludeWhere +
                //" AND de.asl_name != 'RETIRED DELETED' " + //removing this condition from SQL statement. This status is controlled by Search Preferences Server as of release 5.2
                " AND conte.conte_idseq = de.conte_idseq " +
                whereClause + registrationWhere +
                contextWhere +
                deDerivWhere;

        StringBuffer finalSqlStmt = new StringBuffer();

        finalSqlStmt.append( selectClause );
        finalSqlStmt.append( fromWhere );

        String sqlStmt = finalSqlStmt.toString();
        logger.debug( "initSearchQueryBuilder DE search sqlStmt: " + sqlStmt );
        return sqlStmt;
    }


    public String buildObjectClassWhere( String objecClass )
    {
    	objecClass = StringReplace.strReplace(objecClass, "'", "''");//Escape SQL single quote
    	String where = "and  de.de_idseq IN " +
                "(select de_idseq from   sbr.data_elements_view where  " +
                "    dec_idseq IN " +
                "    (" +
                "        select dec.dec_idseq " +
                "            from" +
                "                sbr.data_element_concepts_view dec," +
                "                sbrext.object_classes_view_ext oc  " +
                "            where" +
                "                oc.oc_idseq = dec.oc_idseq  " +
                "                and upper(oc.long_name) like upper('" + objecClass + "')" +
                ")" +
                "    )";
        return where;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public String buildPropertyWhere( String property )
    {
    	property = StringReplace.strReplace(property, "'", "''");//Escape SQL single quote
    	String where = "and  de.de_idseq IN " +
                "(select de_idseq from   sbr.data_elements_view where  " +
                "    dec_idseq IN " +
                "    (" +
                "        select dec.dec_idseq " +
                "            from" +
                "                sbr.data_element_concepts_view dec," +
                "                sbrext.properties_view_ext prop " +
                "            where" +
                "                prop.prop_idseq = dec.prop_idseq  " +
                "                and upper(prop.long_name) like upper('" + property + "')" +
                ")" +
                "    )";
        return where;
    }


    public String buildPermissibleValueWhere( String query, int queryType )
    {
        query = query.replaceAll( "\\*", "%" );
        String where = "and de.vd_idseq \n" +
                "IN (\n";

        //Exact phrase
        if( queryType == 0 )
        {
        	query = StringReplace.strReplace(query, "'", "''");//Escape SQL single quote
        	where += "    select vd.vd_idseq \n" +
                    "    from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv   \n" +
                    "    where  vd.vd_idseq = vp.vd_idseq  and    pv.pv_idseq = vp.pv_idseq and upper (pv.value) like upper ('" + query + "') \n";
        }

        else if( ( queryType == 1 ) || ( queryType == 2 ) )
        {
            String[] parts = query.split( " " );
            boolean firstFlag = true;
            for( String curr : parts )
            {
            	String term = StringReplace.strReplace(curr, "'", "''");
            	if( firstFlag )
                {
                    firstFlag = false;
                }
                else if( queryType == 1 )
                {
                    where += "INTERSECT\n";
                }
                else if( queryType == 2 )
                {
                    where += "UNION\n";
                }

                where += "    select vd.vd_idseq \n" +
                        "    from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv \n" +
                        "    where  vd.vd_idseq = vp.vd_idseq and pv.pv_idseq = vp.pv_idseq and (upper (pv.value) " +
                        "like upper ('% " + term + " %')  or upper (pv.value) like upper ('" + term + " %')  or upper (pv.value) like upper ('" + term + "')  or upper (pv.value) like upper ('% " + term + "') )\n";
            }
        }
        where += ")";
        return where;
    }

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


    public String buildSearchTextWhere( String text, String[] searchDomain, String searchMode )
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
                StringUtilities.containsKey( searchDomain, "Preferred Question Text" ) ||
                StringUtilities.containsKey( searchDomain, "Alternate Question Text" ) )
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
                ( StringUtilities.containsKey( searchDomain, "Preferred Question Text" ) &&
                        StringUtilities.containsKey( searchDomain, "Alternate Question Text" ) ) )
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
        else if( StringUtilities.containsKey( searchDomain, "Preferred Question Text" ) )
        {
            docTextTypeWhere = "rd1.dctl_name (+) = 'Preferred Question Text'";
        }
        else if( StringUtilities.containsKey( searchDomain, "Alternate Question Text" ) )
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
                StringUtilities.containsKey( searchDomain, "UML Class: UML Attr Alternate Name" ) )
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
                String nameWhere = " AND de.de_idseq IN (" + umlAltNameWhere + " UNION " + docWhere + ") ";
                return nameWhere;
            }
        }
        if (StringUtils.isNotBlank(docWhere))
        	return " AND de.de_idseq IN " + docWhere;
        else {
        	logger.error("buildSearchTextWhere error on invalid searchDomain input");
        	return "";
        }
    }

    /**
     * 
     * @param colName
     * @param excludeArr list of statuses excluded by SearchPreferences object
     * @return String SQL fragment
     */
    
    protected String getExcludeWhereClause( String colName, String[] excludeArr )
    {
        String whereClauseStr = null;
        if( ( excludeArr == null ) || ( excludeArr.length < 1 ) )
        {
            return null;
        }

        for( int i = 0; i < excludeArr.length; i++ )
        {
            String strStatus = excludeArr[i];
    		strStatus = strStatus.replaceAll("'", "''");//CDEBROWSER-703 this is for SQL single quote escape
        	if( whereClauseStr == null )
            {
                whereClauseStr = " " + colName + " NOT IN ('" + strStatus + "'";
            }
            else
            {
                whereClauseStr = whereClauseStr + " , '" + strStatus + "'";
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

        if( ( regStatusList == null ) || ( regStatusList.length == 0 ) ||
                ( StringUtilities.containsKey( regStatusList, "ALL" ) || ( regStatusList[0].isEmpty() ) ) )
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
                String strStatus = regStatusList[i];
            	if( i == 0 )
                {
                    regStatus = "'" + strStatus.replaceAll("'", "''") + "'";
                }
                else
                {
                    regStatus = regStatus + "," + "'" + strStatus.replaceAll("'", "''") + "'";
                }
            }
            regStatWhere = " AND acr.registration_status IN (" + regStatus + ")";

        }
        logger.debug( "regStatWhere: " + regStatWhere );
        return regStatWhere;
    }

    public void setDataElementConceptDAO( DataElementConceptDAO dataElementConceptDAO )
    {
        this.dataElementConceptDAO = dataElementConceptDAO;
    }

    public DataElementConceptDAO getDataElementConceptDAO()
    {
        return dataElementConceptDAO;
    }
}
