package gov.nih.nci.cadsr.dao.model;

import gov.nih.nci.cadsr.common.CaDSRConstants;

/**
 * Created with IntelliJ IDEA.
 * User: lernermh
 * Date: 3/2/15
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgramAreaModel extends BaseModel
{
    private String comments;
    private String description;
    private String palName;

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getPalName()
    {
        return palName;
    }

    public void setPalName( String palName )
    {
        this.palName = palName;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( CaDSRConstants.OBJ_SEPARATOR_START );
        sb.append( "name=" + getPalName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "description=" + getDescription() );
        sb.append( super.toString() );
        sb.append( CaDSRConstants.OBJ_SEPARATOR_END );
        sb.toString();

        return sb.toString();
    }

}
