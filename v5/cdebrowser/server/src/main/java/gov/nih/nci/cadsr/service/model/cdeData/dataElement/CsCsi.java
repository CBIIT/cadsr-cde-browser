package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;

public class CsCsi
{
    private String csLongName;
    private String csDefinition;
    private String csiName;
    private String csiType;
    private String csId;
    private Float csVersion;
    private String formattedCsVersion;
    private Integer csiId;
    private Float csiVersion;
    private String formattedCsiVersion;
    private Boolean hide;
    
    private List<AlternateName> usedByAlternateNames;//CDEBROWSER-809 "Separate out the Alternate names of type = "Used_By" into their own sub-table"
    private List<AlternateName> alternateNames;
    private List<AlternateDefinition> alternateDefinitions;

    public CsCsi()
    {
    }

    public CsCsi( CsCsiModel csCsiModel )
    {
        csLongName = csCsiModel.getCsLongName();
        csDefinition = csCsiModel.getCsPreffredDefinition();
        csId = csCsiModel.getCsId();
        csVersion = csCsiModel.getCsVersion();
        if( csVersion != null )
        {
            formattedCsVersion = Float.toString( Float.valueOf( csVersion ) );
        }
        csiName = csCsiModel.getCsiName();
        csiType = csCsiModel.getCsitlName();
        csiId = csCsiModel.getCsiId();
        csiVersion = csCsiModel.getCsiVersion();
        if( csiVersion != null )
        {
            formattedCsiVersion = Float.toString( Float.valueOf( csiVersion ) );
        }
        if( csiName == CsCsiModel.UNCLASSIFIED )
        {
            hide = true;
        }
    }
    public CsCsi( CsCsiDeModel csCsiDeModel )
    {
        csLongName = csCsiDeModel.getCsLongName();
        csDefinition = csCsiDeModel.getCsDefinition();

        csiName = csCsiDeModel.getCsiName();
        csiType = csCsiDeModel.getCsitlName();
    }
    public String getCsLongName()
    {
        return csLongName;
    }

    public void setCsLongName( String csLongName )
    {
        this.csLongName = csLongName;
    }

    public String getCsDefinition()
    {
        return csDefinition;
    }

    public void setCsDefinition( String csDefinition )
    {
        this.csDefinition = csDefinition;
    }

    public String getCsiName()
    {
        return csiName;
    }

    public void setCsiName( String csiName )
    {
        this.csiName = csiName;
    }

    public String getCsiType()
    {
        return csiType;
    }

    public void setCsiType( String csiType )
    {
        this.csiType = csiType;
    }

    public String getCsId()
    {
        return csId;
    }

    public void setCsId( String csId )
    {
        this.csId = csId;
    }

    public Float getCsVersion()
    {
        return csVersion;
    }

    public void setCsVersion( Float csVersion )
    {
        this.csVersion = csVersion;
    }

    public Integer getCsiId()
    {
        return csiId;
    }

    public void setCsiId( Integer csiId )
    {
        this.csiId = csiId;
    }

    public String getFormattedCsiVersion()
    {
        return formattedCsiVersion;
    }

    public void setFormattedCsiVersion( String formattedCsiVersion )
    {
        this.formattedCsiVersion = formattedCsiVersion;
    }

    public String getFormattedCsVersion()
    {
        return formattedCsVersion;
    }

    public void setFormattedCsVersion( String formattedCsVersion )
    {
        this.formattedCsVersion = formattedCsVersion;
    }

    public Float getCsiVersion()
    {
        return csiVersion;
    }

    public void setCsiVersion( Float csiVersion )
    {
        this.csiVersion = csiVersion;
    }

    public Boolean getHide()
    {
        return hide;
    }

    public void setHide( Boolean hide )
    {
        this.hide = hide;
    }

    public List<AlternateName> getAlternateNames()
    {
        return alternateNames;
    }

    public void setAlternateNames( List<AlternateName> alternateNames )
    {
        this.alternateNames = alternateNames;
    }

    public List<AlternateDefinition> getAlternateDefinitions()
    {
        return alternateDefinitions;
    }

    public void setAlternateDefinitions( List<AlternateDefinition> alternateDefinitions )
    {
        this.alternateDefinitions = alternateDefinitions;
    }

    public List<AlternateName> getUsedByAlternateNames() {
		return usedByAlternateNames;
	}

	public void setUsedByAlternateNames(List<AlternateName> usedByAlternateNames) {
		this.usedByAlternateNames = usedByAlternateNames;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsCsi other = (CsCsi) obj;
		if (alternateDefinitions == null) {
			if (other.alternateDefinitions != null)
				return false;
		} else if (!alternateDefinitions.equals(other.alternateDefinitions))
			return false;
		if (alternateNames == null) {
			if (other.alternateNames != null)
				return false;
		} else if (!alternateNames.equals(other.alternateNames))
			return false;
		if (usedByAlternateNames == null) {
			if (other.usedByAlternateNames != null)
				return false;
		} else if (!usedByAlternateNames.equals(other.usedByAlternateNames))
			return false;
		if (csDefinition == null) {
			if (other.csDefinition != null)
				return false;
		} else if (!csDefinition.equals(other.csDefinition))
			return false;
		if (csId == null) {
			if (other.csId != null)
				return false;
		} else if (!csId.equals(other.csId))
			return false;
		if (csLongName == null) {
			if (other.csLongName != null)
				return false;
		} else if (!csLongName.equals(other.csLongName))
			return false;
		if (csVersion == null) {
			if (other.csVersion != null)
				return false;
		} else if (!csVersion.equals(other.csVersion))
			return false;
		if (csiId == null) {
			if (other.csiId != null)
				return false;
		} else if (!csiId.equals(other.csiId))
			return false;
		if (csiName == null) {
			if (other.csiName != null)
				return false;
		} else if (!csiName.equals(other.csiName))
			return false;
		if (csiType == null) {
			if (other.csiType != null)
				return false;
		} else if (!csiType.equals(other.csiType))
			return false;
		if (csiVersion == null) {
			if (other.csiVersion != null)
				return false;
		} else if (!csiVersion.equals(other.csiVersion))
			return false;
		if (hide == null) {
			if (other.hide != null)
				return false;
		} else if (!hide.equals(other.hide))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usedByAlternateNames == null) ? 0 : usedByAlternateNames.hashCode());
		result = prime * result + ((alternateDefinitions == null) ? 0 : alternateDefinitions.hashCode());
		result = prime * result + ((alternateNames == null) ? 0 : alternateNames.hashCode());
		result = prime * result + ((csDefinition == null) ? 0 : csDefinition.hashCode());
		result = prime * result + ((csId == null) ? 0 : csId.hashCode());
		result = prime * result + ((csLongName == null) ? 0 : csLongName.hashCode());
		result = prime * result + ((csVersion == null) ? 0 : csVersion.hashCode());
		result = prime * result + ((csiId == null) ? 0 : csiId.hashCode());
		result = prime * result + ((csiName == null) ? 0 : csiName.hashCode());
		result = prime * result + ((csiType == null) ? 0 : csiType.hashCode());
		result = prime * result + ((csiVersion == null) ? 0 : csiVersion.hashCode());
		result = prime * result + ((hide == null) ? 0 : hide.hashCode());
		return result;
	}

    @Override
    public String toString()
    {
        return "CsCsi{" +
                "csLongName='" + csLongName + '\'' +
                ", csDefinition='" + csDefinition + '\'' +
                ", csiName='" + csiName + '\'' +
                ", csiType='" + csiType + '\'' +
                ", csId='" + csId + '\'' +
                ", csVersion=" + csVersion +
                ", csiId=" + csiId +
                ", csiVersion=" + csiVersion +
                ", hide=" + hide +
                ", alternateNames=" + alternateNames +
                ", alternateDefinitions=" + alternateDefinitions +
                '}';
    }
}
