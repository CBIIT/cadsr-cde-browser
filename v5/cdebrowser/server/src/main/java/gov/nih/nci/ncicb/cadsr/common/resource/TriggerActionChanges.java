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

public interface TriggerActionChanges
{
    public void setTriggerActionId(String triggerActionId);

    public String getTriggerActionId();

    public void setNewInstruction(String newInstruction);

    public String getNewInstruction();

    public void setAddProtocols(List<String> addProtocols);

    public List<String> getAddProtocols();

    public void setAddCsis(List<String> addCsis);

    public List<String> getAddCsis();

    public void setDeleteProtocols(List<String> deleteProtocols);

    public List<String> getDeleteProtocols();

    public void setDeleteCsis(List<String> deleteCsis);

    public List<String> getDeleteCsis();

    public void setNewTargetId(String newTargetId);

    public String getNewTargetId();
}
