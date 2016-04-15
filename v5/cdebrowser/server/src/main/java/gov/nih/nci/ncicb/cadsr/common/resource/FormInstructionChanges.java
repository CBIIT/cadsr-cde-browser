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

package gov.nih.nci.ncicb.cadsr.common.resource;
import java.io.Serializable;
import java.util.Map;

public interface FormInstructionChanges extends Serializable
{

  public static final String DELETED_INSTRUCTIONS="deletedInstructions";
  public static final String NEW_INSTRUCTION_MAP="newInstructionMap";
  public static final String UPDATED_INSTRUCTIONS="updatedInstructions";  
  
  public void setFormHeaderInstructionChanges(Map formHeaderInstructionChanges);


  public Map getFormHeaderInstructionChanges();


  public void setFormFooterInstructionChanges(Map formFooterInstructionChanges);


  public Map getFormFooterInstructionChanges();
  
  public boolean isEmpty();
}