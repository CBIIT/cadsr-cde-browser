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
import java.io.Serializable;
import java.util.Date;

public interface GenericTransferObject extends Serializable
{
  public String getString(String key);
  public void setString(String key, String value);
  
  public Float getFloat(String key);
  public void setFloat(String key, Float value);
  
  public Date getDate(String key);
  public void setDate(String key, Date value);
  
}