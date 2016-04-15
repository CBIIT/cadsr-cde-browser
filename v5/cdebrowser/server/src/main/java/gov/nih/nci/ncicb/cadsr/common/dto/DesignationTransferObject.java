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

import gov.nih.nci.ncicb.cadsr.common.resource.ClassSchemeItem;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.Designation;

import java.util.ArrayList;
import java.util.List;

public class DesignationTransferObject implements Designation
{
    private String type;
    private String name;
    private Context context;
    private String desigIDSeq;
    private String language;
    public List <ClassSchemeItem> csCsis;

    public DesignationTransferObject() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }


    public void setCsCsis(List<ClassSchemeItem> csCsis) {
        this.csCsis = csCsis;
    }

    public List<ClassSchemeItem> getCsCsis() {
        return csCsis;
    }

    public void setDesigIDSeq(String desigIDSeq) {
        this.desigIDSeq = desigIDSeq;
    }

    public String getDesigIDSeq() {
        return desigIDSeq;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
    
    public void addCscsi(ClassSchemeItem csi){
        if (csCsis==null){
            csCsis = new ArrayList();            
        }
        csCsis.add(csi);
    }
}
