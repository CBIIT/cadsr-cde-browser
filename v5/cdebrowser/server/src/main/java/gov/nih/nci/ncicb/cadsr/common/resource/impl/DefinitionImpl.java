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

package gov.nih.nci.ncicb.cadsr.common.resource.impl;

import gov.nih.nci.ncicb.cadsr.common.resource.ClassSchemeItem;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.Definition;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jane Jiang (jane.jiang@oracle.com)
 * @version $Id:
 */
public class DefinitionImpl implements Definition {
    private String id;
    private Context context;
    private String definition;
    private List csCsis;
    private String type;
    private String language;

    public DefinitionImpl() {
    }


   public void setId(String anId) {
      this.id = anId;
   }

   public String getId() {
      return id;
   }

   public void setDefinition(String aDefinition) {
      this.definition = aDefinition;
   }

   public String getDefinition() {
      return definition;
   }

   public void setContext(Context newContext) {
      this.context = newContext;
   }

   public Context getContext() {
      return context;
   }


   public void setType(String _type) {
      this.type = _type;
   }

   public String getType() {
      return type;
   }

   public void setCsCsis(List csCsis) {
      this.csCsis = csCsis;
   }

   public List getCsCsis() {
      return csCsis;
   }
   
   public void addCsCsi(ClassSchemeItem newCsCsi){
      if (csCsis == null )
         csCsis = new ArrayList();
      
      csCsis.add(newCsCsi);
   
   }
   
   public String getLanguage() {
       return language;
   }
   
   public void setLanguage(String newLanguage) {
       language = newLanguage;
   }
}
