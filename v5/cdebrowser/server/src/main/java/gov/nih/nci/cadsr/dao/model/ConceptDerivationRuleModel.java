package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class ConceptDerivationRuleModel extends BaseModel
{

    // Since all local fields are strings, no need to worry about use
    private String condrIdseq;
    private String methods;
    private String rule;
    private String concatChar; // this is a one byte char in Oracle who the @#$% designed this?
    private String ctrlName;
    private String name;

    public ConceptDerivationRuleModel()
    {
    }

    public String getCondrIdseq()
    {
        return condrIdseq;
    }

    public void setCondrIdseq( String condrIdseq )
    {
        this.condrIdseq = condrIdseq;
    }

    public String getMethods()
    {
        return methods;
    }

    public void setMethods( String methods )
    {
        this.methods = methods;
    }

    public String getRule()
    {
        return rule;
    }

    public void setRule( String rule )
    {
        this.rule = rule;
    }

    public String getConcatChar()
    {
        return concatChar;
    }

    public void setConcatChar( String concatChar )
    {
        this.concatChar = concatChar;
    }

    public String getCtrlName()
    {
        return ctrlName;
    }

    public void setCtrlName( String ctrlName )
    {
        this.ctrlName = ctrlName;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ConceptDerivationRuleModel{" +
                "condrIdseq='" + condrIdseq + '\'' +
                ", methods='" + methods + '\'' +
                ", rule='" + rule + '\'' +
                ", concatChar='" + concatChar + '\'' +
                ", ctrlName='" + ctrlName + '\'' +
                ", name='" + name + '\'' +
                ", BaseModel=" + super.toString() +
                '}';
    }
}
