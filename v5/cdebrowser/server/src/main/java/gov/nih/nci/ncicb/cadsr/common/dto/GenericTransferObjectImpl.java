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
import java.util.HashMap;
import java.util.Date;

public class GenericTransferObjectImpl implements GenericTransferObject  
{
  protected HashMap stringMap = null;
  protected HashMap dateMap = null;
  protected HashMap floatMap = null;
  
  public GenericTransferObjectImpl()
  {
  }

  public String getString(String key)
  {
    if(stringMap!=null)
      return (String)stringMap.get(key);
    return null;
  }

  public void setString(String key, String value)
  {
    if(stringMap==null)
      stringMap = new HashMap();
    stringMap.put(key,value);
  }

  public Float getFloat(String key)
  {
    if(floatMap!=null)
      return (Float)floatMap.get(key);
    return null;
  }

  public void setFloat(String key, Float value)
  {
    if(floatMap==null)
      floatMap = new HashMap();
    floatMap.put(key,value);  
  }

  public Date getDate(String key)
  {
    if(dateMap!=null)
      return (Date)dateMap.get(key);
    return null;
  }

  public void setDate(String key, Date value)
  {
    if(dateMap==null)
      dateMap = new HashMap();
    dateMap.put(key,value);    
  }
}