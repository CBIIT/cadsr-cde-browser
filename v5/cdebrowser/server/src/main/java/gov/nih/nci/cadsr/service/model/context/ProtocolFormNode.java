package gov.nih.nci.cadsr.service.model.context;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProtocolFormNode extends BaseNode
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger( ProtocolFormNode.class.getName() );

    public ProtocolFormNode()
    {
        super();
    }
	@Override
	public String toString() {
		return "ProtocolFormNode [" + super.toString() + "]";
	}
}
