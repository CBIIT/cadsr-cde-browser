package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import java.util.List;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;

/**
 * Created by lavezzojl on 5/8/15.
 */
public class CsCsi {
    private String csLongName;
    private String csDefinition;
    private String csiName;
    private String csiType;

    private List<AlternateName> alternateNames;
    private List<AlternateDefinition> alternateDefinitions;

    public CsCsi() {
    }

    public CsCsi(CsCsiModel csCsiModel) {
        csLongName = csCsiModel.getCsLongName();
        csDefinition = csCsiModel.getCsPreffredDefinition();
        csiName = csCsiModel.getCsiName();
        csiType = csCsiModel.getCsitlName();
    }

    public String getCsLongName() {
        return csLongName;
    }

    public void setCsLongName(String csLongName) {
        this.csLongName = csLongName;
    }

    public String getCsDefinition() {
        return csDefinition;
    }

    public void setCsDefinition(String csDefinition) {
        this.csDefinition = csDefinition;
    }

    public String getCsiName() {
        return csiName;
    }

    public void setCsiName(String csiName) {
        this.csiName = csiName;
    }

    public String getCsiType() {
        return csiType;
    }

    public void setCsiType(String csiType) {
        this.csiType = csiType;
    }

    public List<AlternateName> getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(List<AlternateName> alternateNames) {
        this.alternateNames = alternateNames;
    }

    public List<AlternateDefinition> getAlternateDefinitions() {
        return alternateDefinitions;
    }

    public void setAlternateDefinitions(List<AlternateDefinition> alternateDefinitions) {
        this.alternateDefinitions = alternateDefinitions;
    }
}
