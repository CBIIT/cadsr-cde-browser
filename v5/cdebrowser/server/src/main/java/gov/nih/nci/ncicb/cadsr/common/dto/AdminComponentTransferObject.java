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

import gov.nih.nci.ncicb.cadsr.common.resource.AdminComponent;
import gov.nih.nci.ncicb.cadsr.common.resource.Contact;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.Definition;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;

import java.util.List;

public class AdminComponentTransferObject extends BaseTransferObject
  implements AdminComponent {
  protected String preferredName;
  protected String preferredDefinition;
  protected String longName;
  protected String aslName;
  protected Float version;
  protected String deletedInd;
  protected String latestVerInd;
  protected String conteIdseq;
  protected Context context;
  protected List refDocs;
  protected List designations;
  protected int publicId;
  protected String origin;
  protected String idseq;
  protected String registrationStatus;
  //Publish Change Order
  protected boolean published;  
  protected List<Definition> definitions;
  protected List<Contact> contacts;


  public AdminComponentTransferObject() {
  }

  public String getPreferredName() {
    return preferredName;
  }

  public String getLongName() {
    return longName;
  }

  public Float getVersion() {
    return version;
  }

  public String getPreferredDefinition() {
    return preferredDefinition;
  }

  public String getAslName() {
    return aslName;
  }

  public String getLatestVersionInd() {
    return latestVerInd;
  }

  public String getDeletedInd() {
    return deletedInd;
  }

  public String getConteIdseq() {
    return conteIdseq;
  }

  public Context getContext() {
    return context;
  }

  public int getPublicId() {
    return publicId;
  }

  public String getOrigin() {
    return origin;
  }

  //setter methods
  public void setPreferredName(String pPreferredName) {
    preferredName = pPreferredName;
  }

  public void setLongName(String pLongName) {
    longName = pLongName;
  }

  public void setVersion(Float pVersion) {
    version = pVersion;
  }

  public void setPreferredDefinition(String pPreferredDefinition) {
    preferredDefinition = pPreferredDefinition;
  }

  public void setAslName(String pAslName) {
    aslName = pAslName;
  }

  public void setLatestVersionInd(String pLatestVersionInd) {
    latestVerInd = pLatestVersionInd;
  }

  public void setDeletedInd(String pDeletedInd) {
    deletedInd = pDeletedInd;
  }

  public void setConteIdseq(String pContIdseq) {
    conteIdseq = pContIdseq;
  }

  public void setContext(Context pContext) {
    context = pContext;
    if(pContext!=null)
      conteIdseq = pContext.getConteIdseq();
  }

  public List getRefereceDocs() {
    return refDocs;
  }

  public List getDesignations() {
    return designations;
  }

  public void setReferenceDocs(List lRefDocs) {
    refDocs = lRefDocs;
  }

  public void setDesignations(List lDes) {
    designations = lDes;
  }

  public void setPublicId(int id) {
    publicId = id;
  }

  public void setOrigin(String source) {
    origin = source;
  }

  public String getRegistrationStatus() {
    return registrationStatus;
  }

  public void setRegistrationStatus(String regStatus) {
    registrationStatus = regStatus;
  }

//Publish Change Order
  public boolean getIsPublished()
  {
    return published;
  }

  public void setPublished(boolean published)
  {
    this.published = published;
  }
    
  /**
   * Clone the AdminComponent
   * Does a deep Copy of the Context
   * @return 
   */
  public Object clone()throws CloneNotSupportedException {
    AdminComponent copy = null;
    try {
      copy = (AdminComponent)super.clone();
      // make the copy a little deeper
      if(this.getContext()!=null)
      {
        Context contextCopy = getContext();
        copy.setContext((Context)contextCopy.clone());
        //refDocs=null;
        designations=null;
      }
      return copy;
    }
    catch(CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return copy;
  }
  public String toString() {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(ATTR_SEPARATOR + "preferredName=" + getPreferredName(),getPreferredName());
    sb.append(ATTR_SEPARATOR + "longName=" + getLongName(),getLongName());
    sb.append(ATTR_SEPARATOR + "version=" + getVersion(),getVersion());
    sb.append(ATTR_SEPARATOR + "preferredDefinition=" + getPreferredDefinition(),getPreferredDefinition());
    sb.append(ATTR_SEPARATOR + "aslName=" + getAslName(),getAslName());
    sb.append(ATTR_SEPARATOR + "latestVersionInd=" + getLatestVersionInd(),getLatestVersionInd());
    sb.append(ATTR_SEPARATOR + "deletedInd=" + getDeletedInd(),getDeletedInd());
    sb.append(ATTR_SEPARATOR + "publicId=" + getPublicId());
    sb.append(ATTR_SEPARATOR + "origin=" + getOrigin(),getOrigin());

    Context context = getContext();

    if (context != null) {
      sb.append(ATTR_SEPARATOR + "Context=" + context.toString());
    }
    else {
      sb.append(ATTR_SEPARATOR + "Context=null");
    }
    sb.append(super.toString());

    return sb.toString();
  }

  public String getIdseq() {
    return idseq;
  }

  public void setIdseq(String idseq) {
    this.idseq = idseq;
  }

  public List<Definition> getDefinitions() {
   return definitions;
  }
   public void setDefinitions(List<Definition> newDefinitions){
     this.definitions = newDefinitions;
  }
  
  public List<Contact> getContacts() {
     return contacts;
  }
  
  public void setContacts(List<Contact> newContacts) {
     this.contacts = newContacts;
  }

}
