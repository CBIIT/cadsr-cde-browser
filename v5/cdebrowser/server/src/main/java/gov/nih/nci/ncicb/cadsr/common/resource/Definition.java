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

public interface Definition {
    public Context getContext();
    public String getId() ;
    public String getDefinition();
    public List getCsCsis() ;
    public String getLanguage();

    public void addCsCsi(ClassSchemeItem newCsCsi) ;
    public String getType();

    public void setType(String newType);

    public void setDefinition(String newDefinition);

    public void setContext(Context newContext);

    public void setId(String newId) ;
    
    public void setLanguage(String language);
 

}