/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.model;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ProtocolFormNode extends BaseNode
{
    private Logger logger = LogManager.getLogger( ProtocolFormNode.class.getName() );

    public ProtocolFormNode()
    {
        super();
    }

}
