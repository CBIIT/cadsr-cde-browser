package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

public enum WorkflowStatusEnum
{
	ApprvdForTrialUse("APPRVD FOR TRIAL USE"),
	CmteApproved("CMTE APPROVED"),
	CmteSubmtd("CMTE SUBMTD"),
	CmteSubmtdUsed("CMTE SUBMTD USED"),
	DraftMod("DRAFT MOD"),
	DraftNew("DRAFT NEW"),
	Released("RELEASED"),
	ReleasedNonComlnt("RELEASED-NON-CMPLNT"),
	RetiredArchived("RETIRED ARCHIVED"),
	RetiredPhasedOut("RETIRED PHASED OUT"),
	RetiredWithdrawn("RETIRED WITHDRAWN");
	
	private String workflowStatus;

	private WorkflowStatusEnum(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	
	public static List<String> getAsList()
	{
		List<String> workflowStatusList = new ArrayList<String>();
		
		for (WorkflowStatusEnum ws: WorkflowStatusEnum.values())
		{
			workflowStatusList.add(ws.getWorkflowStatus());
		}
		
		return workflowStatusList;
	}

}
