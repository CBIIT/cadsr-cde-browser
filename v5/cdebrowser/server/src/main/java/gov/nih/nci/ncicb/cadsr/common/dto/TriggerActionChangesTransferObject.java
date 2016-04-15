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

import gov.nih.nci.ncicb.cadsr.common.resource.TriggerActionChanges;

import java.util.List;

public class TriggerActionChangesTransferObject implements TriggerActionChanges
{
   private String triggerActionId;
   private String newInstruction;
   private List<String> addProtocols;
   private List<String> addCsis;
   private List<String> deleteProtocols;
   private List<String> deleteCsis;
   private String newTargetId;
    
   
    public TriggerActionChangesTransferObject()
    {
    }

    public void setTriggerActionId(String triggerActionId)
    {
        this.triggerActionId = triggerActionId;
    }

    public String getTriggerActionId()
    {
        return triggerActionId;
    }

    public void setNewInstruction(String newInstruction)
    {
        this.newInstruction = newInstruction;
    }

    public String getNewInstruction()
    {
        return newInstruction;
    }

    public void setAddProtocols(List<String> addProtocols)
    {
        this.addProtocols = addProtocols;
    }

    public List<String> getAddProtocols()
    {
        return addProtocols;
    }

    public void setAddCsis(List<String> addCsis)
    {
        this.addCsis = addCsis;
    }

    public List<String> getAddCsis()
    {
        return addCsis;
    }

    public void setDeleteProtocols(List<String> deleteProtocols)
    {
        this.deleteProtocols = deleteProtocols;
    }

    public List<String> getDeleteProtocols()
    {
        return deleteProtocols;
    }

    public void setDeleteCsis(List<String> deleteCsis)
    {
        this.deleteCsis = deleteCsis;
    }

    public List<String> getDeleteCsis()
    {
        return deleteCsis;
    }

    public void setNewTargetId(String newTargetId)
    {
        this.newTargetId = newTargetId;
    }

    public String getNewTargetId()
    {
        return newTargetId;
    }
}
