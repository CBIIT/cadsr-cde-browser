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

package gov.nih.nci.ncicb.cadsr.common.lov;

/**
 * A Bean class.
 * <P>
 * @author Oracle Corporation
 */
import gov.nih.nci.ncicb.cadsr.common.util.CommonLOVBean;
import gov.nih.nci.ncicb.cadsr.common.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.common.util.StringReplace;
import gov.nih.nci.ncicb.cadsr.common.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.common.util.logging.LogFactory;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;

public class DataElementConceptsLOVBean extends Object {
  private static Log log = LogFactory.getLog(DataElementConceptsLOVBean.class.getName());

  private String[] searchName;
  private String[] displayName;
  private String[] jspParm;
  private String[] sqlStmtParm;
  private CommonLOVBean clb;
  private String targetJsp = "valueDomainsLOV.jsp";
  private String whereClause = "";
  private String searchStr = "";
  private boolean isContextSpecific = false;
  private boolean isEscape = false;

  public DataElementConceptsLOVBean(HttpServletRequest request
                                   ,DBUtil dbUtil
                                   ,String additionalWhere
                                   ){

    try {
      searchStr = request.getParameter("SEARCH");
      if (searchStr ==null) searchStr ="";

      String searchWhere = "";
      String newSearchStr = "";
      if (!searchStr.equals("")) {
        /*if ((searchStr.indexOf("%") != -1)){
          isEscape = true;
          newSearchStr = StringReplace.strReplace(searchStr,"%","\\%");
          //newSearchStr = StringReplace.strReplace(newSearchStr,"_","\\_");
          newSearchStr = StringReplace.strReplace(newSearchStr,"*","%");
        }
        else {*/
          newSearchStr = StringReplace.strReplace(searchStr,"*","%");
          //Release 3.0, TT#1178
          newSearchStr = StringReplace.strReplace(newSearchStr,"'","''");
        //}
        searchWhere = " and   (upper (dec.long_name) like upper ( '"+newSearchStr+"') " +
                      " OR upper (dec.preferred_name) like upper ( '"+newSearchStr+"')) "
                      ;
      }
      if (request.getParameter("chkContext") == null){
        /*whereClause = " and   (upper (nvl(dec.long_name,'%')) like upper ( '%"+searchStr+"%') " +
                      " OR upper (nvl(dec.preferred_name,'%')) like upper ( '%"+searchStr+"%')) "
                      ;*/
          whereClause = searchWhere;
      }
      else {
        /*whereClause = " and   (upper (nvl(dec.long_name,'%')) like upper ( '%"+searchStr+"%') " +
                      " OR upper (nvl(dec.preferred_name,'%')) like upper ( '%"+searchStr+"%')) "+
                      additionalWhere;*/
        whereClause = searchWhere+additionalWhere;
        isContextSpecific = true;
      }
      if (isEscape) {
        whereClause = whereClause + "ESCAPE '\\'";
      }
      // pass the following parameters to CommonListCntrlBean
      String[] searchParm ={"dec.long_name","Keyword"};
      String[] jspLinkParm={ "dec.dec_idseq","P_ID"};
      String[] displayParm={"dec.preferred_name","Short Name" ,
                            "dec.long_name","Long Name",
                            "dec_conte.name","Context",
                            "dec.asl_name","Workflow Status",
                            "dec.preferred_definition","Definition",
                            "dec.version", "Version"};
      String[] sqlStmtParm = new String[2];
      sqlStmtParm[0] = " from sbr.data_element_concepts_view dec, sbr.contexts_view dec_conte " +
                           " where dec.conte_idseq = dec_conte.conte_idseq " +
                           //" and dec.deleted_ind = 'No' " +  //using view
                           " and dec.asl_name not in ('RETIRED PHASED OUT','RETIRED DELETED') " + whereClause;
      sqlStmtParm[1] = " order by dec.preferred_name ";
      int[] lovPassbackCols = {0};

      clb = new CommonLOVBean(
        request,
        dbUtil,
        searchParm,
        jspLinkParm,
        displayParm,
        sqlStmtParm,
        false,
        lovPassbackCols );

      clb.setCompressFlag(false); // set compress flag
      clb.setLinkCol(0);          // set detail page link column, 0-> first; 1->second
      clb.setDetailReq_Type("dec"); //set req_type for detail page
      clb.setShowRowNum(40);
      //clb.setPerformQueryToFalse();
      //clb.setJsId(StringEscapeUtils.escapeHtml(request.getParameter("idVar")));
      clb.setJsId("jspDataElementConcept");
      //clb.setJsName(StringEscapeUtils.escapeHtml(request.getParameter("nameVar")));
      clb.setJsName("txtDataElementConcept");
      if (isContextSpecific)
        clb.setExtraURLInfo("&performQuery=false&ckhContext=yes");
      else
        clb.setExtraURLInfo("&performQuery=false");

    }
    catch( SQLException e){
      log.error("Exception: ", e);
    }
  }

  public CommonLOVBean getCommonLOVBean() {
    return this.clb;
  }

  public String getJsp() {
    return targetJsp;
  }

  public boolean getIsContextSpecific(){
    return isContextSpecific;
  }
}
