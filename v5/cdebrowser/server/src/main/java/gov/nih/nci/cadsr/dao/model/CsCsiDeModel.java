/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */
package gov.nih.nci.cadsr.dao.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is the model of DEs' Classifications used to map to Designations and Definitions. See CDEBROWSER-468
 * @author asafievan
 *
 */
public class CsCsiDeModel extends BaseModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
SELECT        
       ClassificationSchemes.LONG_NAME CS_LONG_NAME,
       ClassSchemeItems.LONG_NAME csi_name,
       cscsi.cs_csi_idseq,
       ClassificationSchemes.PREFERRED_DEFINITION cs_definition,
       ClassSchemeItems.CSITL_NAME
FROM sbr.AC_CSI AcCsi, sbr.ADMINISTERED_COMPONENTS AdministeredComponents, 
sbr.CLASSIFICATION_SCHEMES ClassificationSchemes, sbr.CS_ITEMS_VIEW ClassSchemeItems, sbr.CS_CSI CsCsi
WHERE 
(((AcCsi.AC_IDSEQ = AdministeredComponents.AC_IDSEQ) AND (CsCsi.CS_IDSEQ = ClassificationSchemes.CS_IDSEQ))AND 
(CsCsi.CSI_IDSEQ = ClassSchemeItems.CSI_IDSEQ)) AND (AcCsi.CS_CSI_IDSEQ = CsCsi.CS_CSI_IDSEQ)
and AdministeredComponents.ac_idseq = 'FD5ED5AE-2328-254F-E034-0003BA3F9857'
order by upper(ClassificationSchemes.LONG_NAME), upper(ClassSchemeItems.LONG_NAME);
    */	
	private String csCsiIdseq;
	private String csLongName;// goes to CS* Long Name
	private String csDefinition;// goes to CS* Definition
	private String csiName; // goes to CSI* Name
	private String csitlName; // goes to CSI* Type
	
	public CsCsiDeModel() {
	}

	public String getCsCsiIdseq() {
		return csCsiIdseq;
	}

	public void setCsCsiIdseq(String csCsiIdseq) {
		this.csCsiIdseq = csCsiIdseq;
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

	public String getCsitlName() {
		return csitlName;
	}

	public void setCsitlName(String csitlName) {
		this.csitlName = csitlName;
	}

	@Override
	public String toString() {
		return "CsCsiDeModel [csCsiIdseq=" + csCsiIdseq + ", csLongName=" + csLongName + ", csDefinition="
				+ csDefinition + ", csiName=" + csiName + ", csitlName=" + csitlName + "]";
	}

}
