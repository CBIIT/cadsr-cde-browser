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
import gov.nih.nci.ncicb.cadsr.common.resource.Form;
import gov.nih.nci.ncicb.cadsr.common.resource.FormElement;
import gov.nih.nci.ncicb.cadsr.common.resource.Module;
import gov.nih.nci.ncicb.cadsr.common.resource.Protocol;
import gov.nih.nci.ncicb.cadsr.common.resource.TriggerAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TriggerActionTransferObject implements TriggerAction
{
    private FormElement actionSource;

    private FormElement actionTarget;

    private List<Protocol> protocols;

    private List<ClassSchemeItem> ClassSchemeItems;

    private String instruction;

    private String idSeq;

    public TriggerActionTransferObject()
    {
    }

    public void setActionSource(FormElement actionSource)
    {
        this.actionSource = actionSource;
    }

    public FormElement getActionSource()
    {
        return actionSource;
    }

    public void setActionTarget(FormElement actionTarget)
    {
        this.actionTarget = actionTarget;
    }

    public FormElement getActionTarget()
    {
        return actionTarget;
    }

    public void setProtocols(List<Protocol> protocols)
    {
        this.protocols = protocols;
    }

    public List<Protocol> getProtocols()
    {
        return protocols;
    }

    public void setClassSchemeItems(List<ClassSchemeItem> classSchemeItems)
    {
        this.ClassSchemeItems = classSchemeItems;
    }

    public List<ClassSchemeItem> getClassSchemeItems()
    {
        return ClassSchemeItems;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setIdSeq(String idSeq)
    {
        this.idSeq = idSeq;
    }

    public String getIdSeq()
    {
        return idSeq;
    }

    public Object clone() throws CloneNotSupportedException
    {
        TriggerAction copy = null;
        copy = (TriggerAction)super.clone();
        // make the copy a little deeper
        if (getClassSchemeItems() != null)
        {
            List<ClassSchemeItem> csiCopy = new ArrayList<ClassSchemeItem>();
            ListIterator<ClassSchemeItem> csiit =
                getClassSchemeItems().listIterator();
            while (csiit.hasNext())
            {
                ClassSchemeItem csi = csiit.next();
                ClassSchemeItem csiClone = (ClassSchemeItem)csi.clone();
                csiCopy.add(csiClone);
            }
            copy.setClassSchemeItems(csiCopy);
        }
        if (getProtocols() != null)
        {
            List<Protocol> protoCopy = new ArrayList<Protocol>();
            ListIterator<Protocol> protoit = getProtocols().listIterator();
            while (protoit.hasNext())
            {
                Protocol protocol = protoit.next();
                Protocol protoClone = (Protocol)protocol.clone();
                protoCopy.add(protoClone);
            }

            copy.setProtocols(protoCopy);

        }
        return copy;
    }
   
}
