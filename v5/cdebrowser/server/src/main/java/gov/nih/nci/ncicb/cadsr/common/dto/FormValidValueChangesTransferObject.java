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

package gov.nih.nci.ncicb.cadsr.common.dto;
import java.util.List;
import gov.nih.nci.ncicb.cadsr.common.resource.FormValidValueChanges;

public class FormValidValueChangesTransferObject implements FormValidValueChanges 
{

  private List newValidValues = null;
  private List updatedValidValues = null;
  private List deletedValidValues = null;
  private String questionId = null;
  
  
  public FormValidValueChangesTransferObject()
  {
  }

  public String getQuestionId()
  {
    return questionId;
  }
  public void setQuestionId(String questionId)
  {
    this.questionId = questionId;
  }
  
  public List getUpdatedValidValues()
  {
    return updatedValidValues;
  }

  public void setUpdatedValidValues(List fvvs)
  {
    updatedValidValues=fvvs;
  }

  public List getNewValidValues()
  {
    return newValidValues;
  }

  public void setNewValidValues(List fvvs)
  {
    newValidValues=fvvs;
  }

  public List getDeletedValidValues()
  {
    return deletedValidValues;
  }

  public void setDeletedValidValues(List fvvs)
  {
    deletedValidValues=fvvs;
  }

  public boolean isEmpty()
  {
    if(deletedValidValues==null&&updatedValidValues==null&&newValidValues==null)
    {
      return true;
    }
    boolean result = true;
    if(deletedValidValues!=null&&!deletedValidValues.isEmpty())
      result =false;
    if(updatedValidValues!=null&&!updatedValidValues.isEmpty())
      result =false;
    if(newValidValues!=null&&!newValidValues.isEmpty())
      result =false;   
    return result;
  }
}