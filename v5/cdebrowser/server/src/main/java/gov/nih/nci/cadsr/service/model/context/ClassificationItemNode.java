package gov.nih.nci.cadsr.service.model.context;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassificationItemNode extends BaseNode
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger( ClassificationItemNode.class.getName() );

    public ClassificationItemNode()
    {
        super();

    }
	@Override
	public String toString() {
		return "ClassificationItemNode [ " + super.toString() + "]";
	}
}
