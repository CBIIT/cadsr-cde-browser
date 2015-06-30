package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;

import java.util.ArrayList;
import java.util.List;

public class OtherVersion
{
    private float version;
    private String formattedVersion;
    private String longName;
    private String workFlowStatus;
    private String registrationStatus;
    private String context;
    private List<CsCsi> csCsis;



    public OtherVersion(DEOtherVersionsModel deOtherVersionsModel) {

        this.version = deOtherVersionsModel.getVersion();
        this.formattedVersion = deOtherVersionsModel.getFormattedVersion();
        this.longName = deOtherVersionsModel.getLongName();
        this.workFlowStatus = deOtherVersionsModel.getWorkflowStatus();
        setRegistrationStatus(deOtherVersionsModel.getRegistrationStatus());
        this.context = deOtherVersionsModel.getContextName();
        csCsis = new ArrayList<CsCsi>(deOtherVersionsModel.getCsCsiModelList().size());
        for (CsCsiModel csCsiModel : deOtherVersionsModel.getCsCsiModelList()) {
            csCsis.add(new CsCsi(csCsiModel));
        }
    }

    public float getVersion()
    {
        return version;
    }

    public void setVersion( float version )
    {
        this.version = version;
    }

    public String getFormattedVersion()
    {
        return formattedVersion;
    }

    public void setFormattedVersion( String formattedVersion )
    {
        this.formattedVersion = formattedVersion;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getWorkFlowStatus()
    {
        return workFlowStatus;
    }

    public void setWorkFlowStatus( String workFlowStatus )
    {
        this.workFlowStatus = workFlowStatus;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        if (registrationStatus == null) {
            registrationStatus = "Standard";
        }
        this.registrationStatus = registrationStatus;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public List<CsCsi> getCsCsis() {
        return csCsis;
    }

    public void setCsCsis(List<CsCsi> csCsis) {
        this.csCsis = csCsis;
    }
}
