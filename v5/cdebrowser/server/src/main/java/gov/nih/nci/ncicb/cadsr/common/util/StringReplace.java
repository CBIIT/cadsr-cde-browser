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
 * This utility class is used to implement a string replacement function.  This
 * is used in support of the file pre-processing operation.
 *
 * @author Hyun Soon Kim, Oracle Corporation
 *
 */
public class StringReplace 
{
  public static String strReplace (String sourceStr, String patternStr, String newStr) {
    
    int     bePos = 0;
    int     pos = 0;
    int     patternStrLen = patternStr.length();;
    int     sourceStrLen = sourceStr.length();;
    boolean found = false;
    String  outputStr = new String();
    String  endStr = new String();
    int     endStrIdx = 0;

    while (bePos < sourceStrLen){
      pos = sourceStr.indexOf(patternStr, bePos);
      if (pos != -1) {
        outputStr += sourceStr.substring(bePos, pos) + newStr;
        bePos = pos + patternStrLen;
        endStrIdx = pos + patternStrLen;
        found = true;
      }
      else {
        bePos = sourceStrLen;
      }
    }
    if (found)
      return outputStr + sourceStr.substring(endStrIdx);
    else
      return sourceStr;
  }
}