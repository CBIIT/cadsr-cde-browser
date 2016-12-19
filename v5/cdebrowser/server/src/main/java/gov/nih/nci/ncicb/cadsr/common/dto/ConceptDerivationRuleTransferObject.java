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

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.ncicb.cadsr.common.resource.ComponentConcept;
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ConceptDerivationRuleTransferObject
        implements ConceptDerivationRule, Serializable {
    private String idseq;
    private String name;
    private String type;
    private String rule;
    private String methods;
    private String concatenationChar = null;
    private List componentConcepts = null;

    public ConceptDerivationRuleTransferObject() {
    }

    /**
     * Make a ConceptDerivationRuleTransferObject from a ConceptDerivationRuleModel
     * @param conceptDerivationRuleModel
     */
    public ConceptDerivationRuleTransferObject( ConceptDerivationRuleModel conceptDerivationRuleModel) {
        this.idseq = conceptDerivationRuleModel.getCondrIdseq();
        this.name = conceptDerivationRuleModel.getName();
        this.rule = conceptDerivationRuleModel.getRule();
        this.methods = conceptDerivationRuleModel.getMethods();
        this.concatenationChar = conceptDerivationRuleModel.getConcatChar();
    }

    /**
     * Get the Id value.
     *
     * @return the Id value.
     */
    public String getIdseq() {
        return idseq;
    }

    public String getName() {
        if (name == null)
            return "";
        else
            return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    /**
     * Get the Type value.
     *
     * @return the Type value.
     */
    public String getType() {
        if (type == null)
            return "";
        else
            return type;
    }

    /**
     * Set the Type value.
     *
     * @param newType The new Type value.
     */
    public void setType(String newType) {
        type = newType;
    }

    /**
     * Set the Id value.
     *
     * @param newId The new Id value.
     */
    public void setIdseq(String newId) {
        idseq = newId;
    }

    public void setComponentConcepts(List componentConcepts) {
        this.componentConcepts = componentConcepts;
    }

    public List getComponentConcepts() {
        return componentConcepts;
    }

    public void addComponentConcept(ComponentConcept comp) {
        if (componentConcepts == null) {
            componentConcepts = new ArrayList();
        }

        componentConcepts.add(comp);
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        if (rule == null)
            return "";
        else
            return rule;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getMethods() {
        if (methods == null)
            return "";
        else
            return methods;
    }

    public void setConcatenationChar(String concatenationChar) {
        this.concatenationChar = concatenationChar;
    }

    public String getConcatenationChar() {
        if (concatenationChar == null)
            return "";
        else
            return concatenationChar;
    }

    @Override
    public String toString()
    {
        return "ConceptDerivationRuleTransferObject{" + "\n" +
                "idseq: '" + idseq + '\'' + ",\n" +
                "name: '" + name + '\'' + ",\n" +
                "type: '" + type + '\'' + ",\n" +
                "rule: '" + rule + '\'' + ",\n" +
                "methods: '" + methods + '\'' + ",\n" +
                "concatenationChar: '" + concatenationChar + '\'' + ",\n" +
                "componentConcepts: " + componentConcepts + "\n" +
                '}';
    }
}
