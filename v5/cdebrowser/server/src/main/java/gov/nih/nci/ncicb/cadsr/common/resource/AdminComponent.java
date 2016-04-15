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

import java.util.List;


public interface AdminComponent extends Audit {
  
  
  public String getPreferredName();

  public void setPreferredName(String pPreferredName);

  public String getLongName();

  public void setLongName(String pLongName);

  public Float getVersion();

  public void setVersion(Float pVersion);

  public String getPreferredDefinition();

  public void setPreferredDefinition(String pPreferredDefinition);

  public String getAslName();

  public void setAslName(String pAslName);

  public String getLatestVersionInd();

  public void setLatestVersionInd(String pLatestVersionInd);

  public String getDeletedInd();

  public void setDeletedInd(String pDeletedInd);

  public String getConteIdseq();

  public void setConteIdseq(String pConteIdseq);

  public Context getContext();

  public void setContext(Context pContext);

  public List getRefereceDocs();
  
  public void setReferenceDocs(List refDocs);

  public List getDesignations();
  public void setDesignations(List Designations);

  public int getPublicId();

  public void setPublicId(int id);

  public String getOrigin();

  public void setOrigin(String source);
  //Added in 2.1 - Ram C
  public String getIdseq();

  public void setIdseq(String idseq);

  public String getRegistrationStatus();

  public void setRegistrationStatus(String regStatus);

  //Publish Change Order - Shaji
  public boolean getIsPublished();
  
  public void setPublished(boolean published);  
  
  //release 3.1 added definitions
  public List<Definition> getDefinitions();
  public void setDefinitions(List<Definition> definitions);
  public List<Contact> getContacts();

  public void setContacts(List<Contact> contacts);

}
