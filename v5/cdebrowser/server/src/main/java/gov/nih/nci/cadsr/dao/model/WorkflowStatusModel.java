package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */

public class WorkflowStatusModel extends BaseModel
{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String aslName;
    private String desc;
    private String comments;
    private int displayOrder;

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc( String desc )
    {
        this.desc = desc;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public int getDisplayOrder()
    {
        return displayOrder;
    }

    public void setDisplayOrder( int displayOrder )
    {
        this.displayOrder = displayOrder;
    }

}
