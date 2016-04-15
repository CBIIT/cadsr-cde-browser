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

/**
 * Overloads the appends method of StringBuffer  
 * 
 */
public class DebugStringBuffer 
{
  private StringBuffer buffer = new StringBuffer();
  
  public DebugStringBuffer()
  {
  }
  public DebugStringBuffer(String str)
  {
    buffer= new StringBuffer(str);
  }

  public DebugStringBuffer append(String str)
  {
      buffer.append(str);
      return this;
  }
  
  public DebugStringBuffer append(String str,String tester)
  {
    if(tester!=null&& !tester.equals(""))
    {
      buffer.append(str);
      return this;
    }
    else
      return this;
  }

  public DebugStringBuffer append(String str,Float tester)
  {
    if(tester!=null)
    {
      buffer.append(str);
      return this;
    }
    else
      return this;
  }  
  public DebugStringBuffer append(DebugStringBuffer strBuffer,String tester)
  {
    // TODO:  Override this java.lang.StringBuffer method
    if(tester!=null&& !tester.equals(""))
    {
      buffer.append(strBuffer);
      return this;
    }
    else
      return this;
  }
  public String toString()
  {
    if(buffer!=null)
      return buffer.toString();
    else
      return null;
  }
  
}