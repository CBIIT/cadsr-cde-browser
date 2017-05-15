/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.CsCsiDeDAO;
import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;
import gov.nih.nci.cadsr.dao.model.CsCsiDeModelList;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateDefinition;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;

/**
 *
 * @author asafievan
 *
 */
public class ControllerUtils {
	private static Logger logger = LogManager.getLogger(ControllerUtils.class.getName());

	/**
	 * This method initializes Search Preferences in HTTP Session.
	 *
	 * @throws NullPointerException
	 * @param httpSession session to add search preferences attribute
	 */
	public static SearchPreferencesServer initSearchPreferencesServerInSession(HttpSession httpSession) {
		SearchPreferencesServer searchPreferencesServer = new SearchPreferencesServer();
		searchPreferencesServer.initPreferences();
		httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferencesServer);
		logger.debug("SearchPreferencesServer adding default to the user HTTP session: " + searchPreferencesServer);
		return searchPreferencesServer;
	}
	/**
	 * This method adds initial Search Preferences to HTTP Session if they are not in the session.
	 *
	 * @throws NullPointerException
	 * @param httpSession session to add search preferences attribute
	 * @returns SearchPreferencesServer object of the session
	 */
	public static SearchPreferencesServer retriveSessionSearchPreferencesServer(HttpSession httpSession) {
		Object obj;
		if (((obj = httpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)) == null) || (!(obj instanceof SearchPreferencesServer))){
			SearchPreferencesServer searchPreferencesServer = new SearchPreferencesServer();
			searchPreferencesServer.initPreferences();//this operation will add all excluded statuses including server exclusion always
			httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferencesServer);
			logger.debug("SearchPreferencesServer not found in the user HTTP session, adding default: " + searchPreferencesServer);
			return searchPreferencesServer;
		}
		else {
			logger.debug("SearchPreferencesServer found in the user HTTP session: " + obj);
			return (SearchPreferencesServer)obj;
		}
	}
	/**
     * This auxilary method creates a list of classified Alt names and Definitions.
     * @return List<CsCsi>
     */
	protected static List<CsCsi> populateCsCsiDeModel(String deIdseq, final CsCsiDeDAO csCsiDeDAO) {
		List<CsCsi> deCsCsis = new ArrayList<>();
		List<CsCsiDeModel> modelList = csCsiDeDAO.getCsCsisByDeId(deIdseq);
		if ((modelList == null) || (modelList.isEmpty())) {
			logger.debug("There is no classified Alt Names/Definitions for DE ID: " + deIdseq);
			return deCsCsis;
		}
		else if (logger.isDebugEnabled()) {
			logger.debug("modelList size: " + modelList.size() + ", modelList: " + modelList);
		}
		// Find all Alt Names related to CsCsiModel
		List<DesignationModelAlt> altNamesList = csCsiDeDAO.getCsCsiDeAltNamesById(deIdseq, modelList);
		logger.debug("altNamesList size=" + altNamesList.size() + altNamesList);
		// Find all definitions related to CsCsiModel
		List<DefinitionModelAlt> definList = csCsiDeDAO.getCsCsiDeDefinitionsById(deIdseq, modelList);
		logger.debug("definList size=" + definList.size() + definList);

		String csCsiIdseq;
		String currId;
		AlternateName alternateName;
		AlternateDefinition definition;
		// Loop through deCsCsis Model list to build the models representations
		for (CsCsiDeModel csCsiDeModel : modelList) {
			csCsiIdseq = csCsiDeModel.getCsCsiIdseq();
			CsCsi csCsi = new CsCsi(csCsiDeModel);//this is a representation class
			ArrayList<AlternateName> classifiedAlternateNames = new ArrayList<>();//representation classes
			// Add all Alt Names based on cs_csi_id ID
			for (DesignationModelAlt designationModelAlt : altNamesList) {
				currId = designationModelAlt.getDesigIdseq();
				if (csCsiIdseq.equals(currId)) {
					alternateName = new AlternateName();
					alternateName.setName(designationModelAlt.getName());
					alternateName.setType(designationModelAlt.getType());
					alternateName.setContext(designationModelAlt.getContextName());
					alternateName.setLanguage(designationModelAlt.getLang());
					classifiedAlternateNames.add(alternateName);
				}
			}
			
			ArrayList<AlternateDefinition> classifiedDefinitions = new ArrayList<>();//representation classes
			//  Add all Definitions based on ID
			for (DefinitionModelAlt definitionModelAlt : definList) {
				currId = definitionModelAlt.getDefinIdseq();
				if (csCsiIdseq.equals(currId)) {
					definition = new AlternateDefinition();
					definition.setName(definitionModelAlt.getDefinition());
					definition.setType(definitionModelAlt.getType());
					definition.setContext(definitionModelAlt.getContextName());
					definition.setLanguage(definitionModelAlt.getLang());
					classifiedDefinitions.add(definition);
				}
			}
			
			if (classifiedAlternateNames.size() > 0 || (classifiedDefinitions.size() > 0)) {
				csCsi.setAlternateNames(classifiedAlternateNames);
				csCsi.setAlternateDefinitions(classifiedDefinitions);
				deCsCsis.add(csCsi);
			}
			else {
				//This CsCsiModel will not be represented since it does not have Alternate Names AKA Designations) nor Definitions
				logger.trace("No Alternate Name AKA Designations nor Definition is found for Classification: " + csCsiDeModel);
			}
		}
		return deCsCsis;
	}
	/**
     * This auxilary method creates a list of unclassified Alt names and Definitions based on pre-populated DataElementModel
     * @return CsCsi with lists of AlternateNames and AlternateDefinitions (not null lists).
     */
	public static CsCsi populateCsCsiDeUnclassified (DataElementModel dataElementModel) {
        CsCsi unclassCsCsi = new CsCsi( dataElementModel.getCsCsiData().get( CsCsiModel.UNCLASSIFIED ) );
        ArrayList<AlternateName> unclassAlternateNames = new ArrayList<>();
        if( dataElementModel.getCsCsiDesignations().get( CsCsiModel.UNCLASSIFIED ) != null )
        {
            for( String designationIdseq : dataElementModel.getCsCsiDesignations().get( CsCsiModel.UNCLASSIFIED ) )
            {
                unclassAlternateNames.add( new AlternateName( dataElementModel.getDesignationModels().get( designationIdseq ) ) );
            }
        }
        unclassCsCsi.setAlternateNames( unclassAlternateNames );
        ArrayList<AlternateDefinition> unclassAlternateDefinitions = new ArrayList<>();
        if( dataElementModel.getCsCsiDefinitions().get( CsCsiModel.UNCLASSIFIED ) != null )
        {
            for( String definitionIdseq : dataElementModel.getCsCsiDefinitions().get( CsCsiModel.UNCLASSIFIED ) )
            {
                unclassAlternateDefinitions.add( new AlternateDefinition( dataElementModel.getDefinitionModels().get( definitionIdseq ) ) );
            }
        }
        unclassCsCsi.setAlternateDefinitions( unclassAlternateDefinitions );
        return(unclassCsCsi);
    }
	/**
	 * 
	 * @param arr
	 * @return true if an array is null or empty
	 */
	public static boolean isArrayEmpty (List<String> arr) {
		return (arr == null) || (arr.isEmpty());
	}
	/**
	 * 
	 * @param arr
	 * @return true if an array is not null and is not empty
	 */
	public static boolean isArrayNotEmpty (List<String> arr) {
		return (arr != null) && (! arr.isEmpty());
	}
}
