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
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.ReferenceDocument;
import java.util.List;

public class ReferenceDocumentTransferObject extends BaseTransferObject
  implements ReferenceDocument{
  private String docName;
  private String docType;
  private String docText;
  private String docIdSeq;
  private String url;
  private String language;
  private List attachments;
  private Context context;
  private int displayOrder;
  
  public ReferenceDocumentTransferObject() {
  }
  
  public String getDocName()
  {
    return docName;
  }
  
  public String getDocType()
  {
    return docType;
  }
  public String getDocText()
  {
    return docText;
  }
  public String getDocIDSeq()
  {
    return docIdSeq;
  }
  public String getUrl()
  {
    return url;
  }
  public String getLanguage()
  {
    return language;
  }

  public String getDocIdSeq()
  {
    return docIdSeq;
  }

  public void setDocName(String docName)
  {
    this.docName = docName;
  }

  public void setDocText(String docText)
  {
    this.docText = docText;
  }

  public void setDocType(String docType)
  {
    this.docType = docType;
  }

  public void setLanguage(String language)
  {
    this.language = language;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public void setDocIDSeq(String docIdSeq)
  {
    this.docIdSeq = docIdSeq;
  }
  
  public Context getContext()
  {
    return context;
  }
  
  public void setContext(Context newContext)
  {
     context=newContext;
  }  
  public List getAttachments()
  {
    return attachments;
  }
  public void setAttachments(List newAttachments)
  {
    attachments=newAttachments;
  }  


  public void setDisplayOrder(int displayOrder)
  {
    this.displayOrder = displayOrder;
  }


  public int getDisplayOrder()
  {
    return displayOrder;
  }
}
