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
import gov.nih.nci.ncicb.cadsr.common.resource.Attachment;

public class AttachmentTransferObject extends BaseTransferObject 
implements Attachment
{
  private String name;
  private String mimeType;
  private long docSize;
  
  public AttachmentTransferObject()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }


  public void setMimeType(String mimeType)
  {
    this.mimeType = mimeType;
  }


  public String getMimeType()
  {
    return mimeType;
  }


  public void setDocSize(long Docsize)
  {
    this.docSize = Docsize;
  }


  public long getDocSize()
  {
    return docSize;
  }


}