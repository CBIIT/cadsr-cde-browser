/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;
/**
 * This class contains workflow status list for a user to see on the client search preferences Web pages.
 * 
 * @author purnimac, asafievan
 *
 */
public enum WorkflowStatusEnum
{
    Released( "RELEASED" ),
    ApprvdForTrialUse( "APPRVD FOR TRIAL USE" ),
    DraftNew( "DRAFT NEW" ),
    CmteApproved( "CMTE APPROVED" ),
    CmteSubmtd( "CMTE SUBMTD" ),
    CmteSubmtdUsed( "CMTE SUBMTD USED" ),
    DraftMod( "DRAFT MOD" ),
    RetiredArchived( "RETIRED ARCHIVED" ),
    RetiredPhasedOut( "RETIRED PHASED OUT" ),
    RetiredWithdrawn( "RETIRED WITHDRAWN" ),
    ReleasedNonComlnt( "RELEASED-NON-CMPLNT" );

    private String workflowStatus;

    private WorkflowStatusEnum( String workflowStatus )
    {
        this.workflowStatus = workflowStatus;
    }

    public String getWorkflowStatus()
    {
        return workflowStatus;
    }

    public void setWorkflowStatus( String workflowStatus )
    {
        this.workflowStatus = workflowStatus;
    }

    public static List<String> getAsList()
    {
        List<String> workflowStatusList = new ArrayList<String>();

        for( WorkflowStatusEnum ws : WorkflowStatusEnum.values() )
        {
            workflowStatusList.add( ws.getWorkflowStatus() );
        }

        return workflowStatusList;
    }
}
