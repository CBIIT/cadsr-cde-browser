/*L
 * Copyright Oracle Inc, SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 */

package gov.nih.nci.ncicb.cadsr.common.dto;
import gov.nih.nci.ncicb.cadsr.common.resource.TreeParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeProtocolNodesTransferObject {

 private Map formsWithNoProtocol;
 private Map formsWithProtocol;
 
 public TreeProtocolNodesTransferObject() {
  
  }


  public void setFormsWithNoProtocol(Map formsWithNoProtocol)
  {
    this.formsWithNoProtocol = formsWithNoProtocol;
  }


  public Map getFormsWithNoProtocol()
  {
    return formsWithNoProtocol;
  }


  public void setFormsWithProtocol(Map formsWithProtocol)
  {
    this.formsWithProtocol = formsWithProtocol;
  }


  public Map getFormsWithProtocol()
  {
    return formsWithProtocol;
  }


}