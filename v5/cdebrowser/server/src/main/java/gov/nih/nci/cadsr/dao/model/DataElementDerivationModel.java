package gov.nih.nci.cadsr.dao.model;

/**
 * Created by lernermh on 6/17/15.
 */
public class DataElementDerivationModel extends BaseModel
{
    private String derivationType;
    private String rule;
    private String method;
    private String concatenationCharacter;

    public String getDerivationType()
    {
        return derivationType;
    }

    public void setDerivationType( String derivationType )
    {
        this.derivationType = derivationType;
    }

    public String getRule()
    {
        return rule;
    }

    public void setRule( String rule )
    {
        this.rule = rule;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod( String method )
    {
        this.method = method;
    }

    public String getConcatenationCharacter()
    {
        return concatenationCharacter;
    }

    public void setConcatenationCharacter( String concatenationCharacter )
    {
        this.concatenationCharacter = concatenationCharacter;
    }

    @Override
    public String toString()
    {
        return "DataElementDerivationModel{" +
                "derivationType='" + derivationType + '\'' +
                ", rule='" + rule + '\'' +
                ", method='" + method + '\'' +
                ", concatenationCharacter='" + concatenationCharacter + '\'' +
                '}';
    }

}
