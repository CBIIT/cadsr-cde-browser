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
import java.sql.Blob;
import org.apache.struts.upload.FormFile;

public interface Attachment extends Serializable, Audit
{
  public String getName();
  public void setName(String name);
  
  public String getMimeType();
  public void setMimeType(String mimeType);
  
  public long getDocSize();
  public void setDocSize(long docSize);
  
  public Object clone() throws CloneNotSupportedException ;
  
}