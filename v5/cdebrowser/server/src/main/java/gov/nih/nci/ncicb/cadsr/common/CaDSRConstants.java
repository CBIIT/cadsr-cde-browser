/*L
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

package gov.nih.nci.ncicb.cadsr.common;

public interface CaDSRConstants {
  public static final String USER_KEY = "nciUser";
  public static final String CDE_MANAGER = "CDE MANAGER";  
  //Publish Change Order Oct 24/04
  public static final String CONTEXT_ADMIN = "CONTEXT ADMIN";
  public static final String DEFAULT_CONTEXT = "NCIP";	//"caBIG";	//GF32649
  public static final String CONTEXT_TEST = "TEST";
  public static final String CONTEXT_TRAINING = "TRAINING";

  public static final String SESSION_SCOPE = "sessionScope";
  public static final String PAGE_SCOPE = "pageScope";
  public static final String ATTR_SEPARATOR = ",";
  public static final String OBJ_SEPARATOR_START = "{";
  public static final String OBJ_SEPARATOR_END = "}";
  public static final String CDE_CART = "cdeCart";
  public static final String CDE_CARTSCHEME = "CDE Cart Classification";
  public static final String USER_CONTEXTS = "userContexts";
  public static final String GLOBAL_SESSION_KEYS="globalSessionKeys";
  public static final String GLOBAL_SESSION_MAP="globalSessionMap";
  public static final String PREVIOUS_SESSION_ID="previousSessionId";
  public static final String YES="Yes";
  public static final String NO="No";
  public static final String CDEBROWSER_SERVICE_LOCATOR_CLASSNAME = "gov.nih.nci.ncicb.cadsr.common.servicelocator.ejb.ServiceLocatorImpl";
  public static final String TREE_REFRESH_INDICATOR="treeRefreshIndicator";
  public static final String ANCHOR="anchor";
  
  public static final String CDEBROWSER = "CDEBrowser";
  public static final String FORMBUILDER = "FormBuilder";
  public static final String cadsrToolName = "caDSR";
  
  public static final String ERROR_FATAL="FB911";
  
  public static final String CLEAR_SESSION_KEYS="clearSessionKeys";
  
  public static final String ALL_CONTEXTS = "allContexts";
  
  public static final String PAGEINDEX = "pageIndex";

  public static final String FORM_CS_TYPE="FOLDER";
  public static final String FORM_CSI_TYPE="Form Type";
  public static final String TEMPLATE_CS_TYPE="FOLDER";
  public static final String TEMPLATE_CSI_TYPE="Template Type";
  
  public static final String EXCLUDE_TEST_CONTEXT="excludeTestContext";
  public static final String EXCLUDE_TRAINING_CONTEXT="excludeTrainingContext";
  public static final String SEARCH_PREFERENCES="searchpreferences";  
  
  public static final String REFERENCE_DOC_ATTACHMENT_NAME = "refDocAttName";
  public static final String REFDOC_ATTACHMENT_MAP = "refDocAttachmentMap";
  
  public static final String VALUE_MEANING_OBJ = "ValueMeaningObj";
  public static final String PROTOCOLS_LOV_TAB_BEAN = "protocolLOVTabBean";
  public static final String PROTOCOLS_LOV_BEAN = "protocolLOVBean";
  
  public static final String MODULE_INDEX = "moduleIndex";
  public static final String QUESTION_INDEX = "questionIndex";
  
}
