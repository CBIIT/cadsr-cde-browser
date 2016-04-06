package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

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
    RetiredDeleted( "RETIRED DELETED" ),
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


    /**
     * @return The default list of Workflow Status/sbr.ac_status_lov_view.asl_name to be excluded.
     */
    public static String getExcludList()
    {
        StringBuilder sb = new StringBuilder( " asl.asl_name NOT IN ( '" );
        sb.append( WorkflowStatusEnum.CmteApproved.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.CmteSubmtd.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.CmteSubmtdUsed.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.RetiredArchived.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.RetiredPhasedOut.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.RetiredWithdrawn.getWorkflowStatus() + "' ) " );
        return sb.toString();
    }

}
