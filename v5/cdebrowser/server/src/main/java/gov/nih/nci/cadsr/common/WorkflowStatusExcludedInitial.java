package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

public enum WorkflowStatusExcludedInitial
{
    CmteApproved( "CMTE APPROVED" ),
    CmteSubmtd( "CMTE SUBMTD" ),
    CmteSubmtdUsed( "CMTE SUBMTD USED" ),
    RetiredArchived( "RETIRED ARCHIVED" ),
    RetiredPhasedOut( "RETIRED PHASED OUT" ),
    RetiredWithdrawn( "RETIRED WITHDRAWN" );

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
	public static boolean isWorkflowStatusValid(final String param) {
		WorkflowStatusEnum[] all = WorkflowStatusEnum.values();
		for (WorkflowStatusEnum curr : all) {
			if (curr.getWorkflowStatus().equals(param))
				return true;
		}
		return false;
	}
	public static List<String> buildValidStatusList(final List<String> statusList) {
		List<String> cleanedUp = new ArrayList<String>();
		if (statusList != null)
			for (String status : statusList) {
				if (isWorkflowStatusValid(status)) {
					cleanedUp.add(status);
				}
			}
		return cleanedUp;
	}
}
