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
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.resource.ValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueMeaning;

import java.util.Collection;
import java.sql.Date;

public class ValidValueTransferObject implements ValidValue {

    protected String vdIdseq;
    protected String vpIdseq;
    protected String shortMeaning;
    protected String shortMeaningDescription;
    protected String shortMeaningValue;
    protected String description;
    protected Integer vmId;
    protected Float vmVersion;
    protected String beginDate;
    protected String endDate;
    protected String context;
    protected String workflowstatus;
    protected Collection instructions = null;
    protected ConceptDerivationRule conceptDerivationRule = null;
    protected ValueMeaning valueMeaning = null;

    public ValidValueTransferObject() {
    }


    public String getVdIdseq() {
        return vdIdseq;
    }

    public void setVdIdseq(String aVdIdseq) {
        this.vdIdseq = vdIdseq;
    }

    public String getShortMeaning() {
        return shortMeaning;
    }

    public void setShortMeaning(String shortMeaning) {
        this.shortMeaning = shortMeaning;
    }

    public String getShortMeaningDescription() {
        return shortMeaningDescription;
    }

    public void setShortMeaningDescription(String aShortMeaningDescription) {
        this.shortMeaningDescription = shortMeaningDescription;
    }

    public String getShortMeaningValue() {
        return shortMeaningValue;
    }

    public void setShortMeaningValue(String shortMeaningValue) {
        this.shortMeaningValue = shortMeaningValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection getInstructions()
    {
        return instructions;
    }
    public void setInstructions(Collection newInstructions)
    {
        instructions=newInstructions;
    }
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    /**
     * This equals method only compares the Idseq to define equals
     * @param obj
     * @return
     */
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(!(obj instanceof ValidValue))
            return false;
        ValidValue vv = (ValidValue)obj;

        if(this.getShortMeaningValue().equalsIgnoreCase(vv.getShortMeaningValue()))
            return true;
        else
            return false;
    }

    public String getVpIdseq() {
        return vpIdseq;
    }

    public void setVpIdseq(String aVpIdseq) {
        vpIdseq = aVpIdseq;
    }
    public ConceptDerivationRule getConceptDerivationRule()
    {
        return conceptDerivationRule;
    }
    public void setConceptDerivationRule(ConceptDerivationRule rule)
    {
        conceptDerivationRule = rule;
    }

    public void setValueMeaning(ValueMeaning vm){
        valueMeaning = vm;
    }

    public ValueMeaning getValueMeaning() {
        return valueMeaning;
    }

    public String getBeginDate(){
        return beginDate;
    }

    public void setBeginDate(String beginDate){
        this.beginDate = beginDate;
    }

    public String getEndDate(){
        return beginDate;
    }

    public void setEndDate(String beginDate){
        this.beginDate = beginDate;
    }

    public Integer getVmId(){
        return  vmId;
    }

    public void setVmId(Integer vmId){
        this.vmId = vmId;
    }
    public Float getVmVersion(){
        return vmVersion;
    }

    public void setVmVersion(Float vmVersion){
        this.vmVersion = vmVersion;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getWorkflowstatus() {
        return workflowstatus;
    }

    public void setWorkflowstatus(String workflowstatus) {
        this.workflowstatus = workflowstatus;
    }

}
