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

package gov.nih.nci.ncicb.cadsr.common.util;

import java.io.Serializable;
import java.sql.*;

/**
 * A Bean class.
 * <P>
 * @author Oracle Corporation
 */
public class UserErrorMessage extends Object implements Serializable {

  private String msgOverview = "";
  private String msgText = "";
  private String msgHelp = "";
  private String msgLink = "";
  private String msgTechnical = "";

  /**
   * Constructor
   */
  public UserErrorMessage() {
  }

  public String getMsgOverview() {
    return msgOverview;
  }

  public String getMsgText() {
    return msgText;
  }

  public String getMsgHelp() {
    return msgHelp;
  }

  public String getMsgLink() {
    return msgLink;
  }

  public String getMsgTechnical() {
    return msgTechnical;
  }

  public void setMsgOverview(String value) {
    msgOverview = value;
  }

  public void setMsgText(String value) {
    msgText = value;
  }

  public void setMsgHelp(String value) {
    msgHelp = value;
  }

  public void setMsgLink(String value) {
    msgLink = value;
  }

  public void setMsgTechnical(String value) {
    msgTechnical = value;
  }

  public static UserErrorMessage getUnauthenticatedMsg() {
   UserErrorMessage uem = new UserErrorMessage();
   uem.setMsgOverview("Security Violation");
   uem.setMsgText("You have reached this page because you have not successfully logged into this application.");
   uem.setMsgHelp("Please return to the Home Page and provide a valid username and password.");
   uem.setMsgLink("<a href=\"index.jsp\">Home Page</a>");
   uem.setMsgTechnical(
     "<b>System administrator:</b> This error usually occurs when somebody attempts " +
     "to navigate in the system by changing the URL themselves instead of using " +
     "the system's navigation constructs.");
    return uem;
  }

  public static UserErrorMessage getNoAccessMsg() {
   UserErrorMessage uem = new UserErrorMessage();
   uem.setMsgOverview("Security Violation");
   uem.setMsgText("You are not authorized to access this screen.");
   uem.setMsgHelp("Please contact your administrator if you have additional questions.");
   uem.setMsgTechnical(
     "<b>System administrator:</b> The user's role needs an entry in ROLE_COMPONENT_ASSIGNMENTS.");
    return uem;
  }

  public static UserErrorMessage getDatabaseErrorMsg(SQLException e) {
    UserErrorMessage uem = new UserErrorMessage();
    uem.setMsgOverview("Unexpected Database Error");
    uem.setMsgText("You have reached this page because an unexpected database error has occured.");
    uem.setMsgHelp("Please press the back button and try again.");
    uem.setMsgLink("If this problem persists, please click <a href=\"index.html\">here</a> to try restarting the application.");
    uem.setMsgTechnical(
      "<b>System administrator:</b> The stack trace from the SQLException has been logged. <BR><BR>");
    return uem;
  }

  public static UserErrorMessage getUnexpectedErrorMsg(Exception e) {
    UserErrorMessage uem = new UserErrorMessage();
    uem.setMsgOverview("Unexpected Application Error");
    uem.setMsgText("You have reached this page because an unexpected application error has occured.");
    uem.setMsgHelp("Please press the back button and try again.");
    uem.setMsgLink("If this problem persists, please click <a href=\"index.html\">here</a> to try restarting the application.");
    uem.setMsgTechnical(
      "<b>System administrator:</b> The stack trace from the Exception has been logged.<BR><BR>");
    return uem;
  }
}

