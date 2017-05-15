package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gov.nih.nci.cadsr.service.restControllers.ControllerUtils;

/**
 * 
 * @author asafievan
 *
 */
public class AlternateNameCsCsi implements Comparable
{
    private String name;
    private String type;
    private String context;
    private String language;
    private List<String> csCsi;//could be null
    
    public AlternateNameCsCsi()
    {
    }

    public AlternateNameCsCsi( AlternateName designationModel )
    {
        setName( designationModel.getName());
        setType( designationModel.getType());
        setContext( designationModel.getContext());
        setLanguage( designationModel.getLanguage());
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage( String language )
    {
        this.language = language;
    }

	public List<String> getCsCsi() {
		return csCsi;
	}

	public void setCsCsi(List<String> csCsi) {
		this.csCsi = csCsi;
	}
	@Override
	public int compareTo(Object other) {
		if (other instanceof AlternateNameCsCsi) {
			AlternateNameCsCsi that = (AlternateNameCsCsi)other;
			//to avoid null pointer for the values which are never null in our DB
			String thisName = (this.name != null) ? this.name : "";
			String thisType = (this.type != null) ? this.type : "";
			String thisContext = (this.context != null) ? this.context : "";
			String thatName = (that.name != null) ? that.name : "";
			String thatType = (that.type != null) ? that.type : "";
			String thatContext = (that.context != null) ? that.context : "";
			//Sorting order: empty csCsi, name, type, context
			if ((ControllerUtils.isArrayEmpty(this.csCsi)) && (ControllerUtils.isArrayNotEmpty(that.csCsi))) {
				return -1;
			}
			else if ((ControllerUtils.isArrayNotEmpty(this.csCsi)) && (ControllerUtils.isArrayEmpty(that.csCsi))) {
				return 1;
			}			
			else if (!(thisName.equals(thatName))) {
				return thisName.compareToIgnoreCase(thatName);
			}
			else if (!(thisType.equals(thatType))){
				return thisType.compareTo(thatType);
			}
			else {
				return thisContext.compareTo(thatContext);
			}
		}
		else if (other instanceof AlternateName) {
			return 1;
		}
		else {
			return -1;
		}
	}
}
