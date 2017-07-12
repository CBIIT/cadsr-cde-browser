/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author asafievan
 *
 */
public enum WorkflowStatusExcludedInitial
{
    CmteApproved( WorkflowStatusEnum.CmteApproved.getWorkflowStatus() ),
    CmteSubmtd( WorkflowStatusEnum.CmteSubmtd.getWorkflowStatus() ),
    CmteSubmtdUsed( WorkflowStatusEnum.CmteSubmtdUsed.getWorkflowStatus() ),
    RetiredArchived( WorkflowStatusEnum.RetiredArchived.getWorkflowStatus() ),
    RetiredPhasedOut( WorkflowStatusEnum.RetiredPhasedOut.getWorkflowStatus() ),
    RetiredWithdrawn( WorkflowStatusEnum.RetiredWithdrawn.getWorkflowStatus() ),
    RetiredDeleted( "RETIRED DELETED" );//this status is for server preferences only; CDE Browser client does not see this status

    private String workflowStatus;

    private WorkflowStatusExcludedInitial( String workflowStatus )
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

    public static List<String> getInitialExcludedList()
    {
        List<String> workflowStatusList = new ArrayList<String>();

        for( WorkflowStatusExcludedInitial ws : WorkflowStatusExcludedInitial.values() )
        {
            workflowStatusList.add( ws.getWorkflowStatus() );
        }

        return workflowStatusList;
    }

}
