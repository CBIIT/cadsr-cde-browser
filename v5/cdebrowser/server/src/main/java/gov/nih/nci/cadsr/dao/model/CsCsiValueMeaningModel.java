/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */
package gov.nih.nci.cadsr.dao.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is the model of PVs' VM Classifications used to map tp Designation and Definitions. See CDEBROWSER-437
 * @author asafievan
 *
 */
public class CsCsiValueMeaningModel extends BaseModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
     From CDE Browser v.4.x
SELECT ext.att_idseq, csi.long_name csi_name, csi.csitl_name, csi.csi_idseq, 
cscsi.cs_csi_idseq, cs.preferred_definition, cs.long_name,
ext.aca_idseq, cs.cs_idseq, cs.version , csi.preferred_definition description,
cs.cs_id, csi.csi_id, csi.version csi_version 
FROM sbrext.ac_att_cscsi_view_ext ext, sbr.cs_csi_view cscsi, 
sbr.cs_items_view csi, sbr.classification_schemes_view cs  
WHERE ext.ATT_IDSEQ = '746A8063-D650-3C9D-E040-BB89AD437A9B' 
AND   ext.cs_csi_idseq = cscsi.cs_csi_idseq 
AND   cscsi.csi_idseq = csi.csi_idseq 
AND   cscsi.cs_idseq = cs.cs_idseq 
ORDER BY upper(csi.long_name);
    /*
    For CDE Browser v.5.x
SELECT cs.long_name cs_long_name, 
cs.preferred_definition cs_definition,
csi.long_name csi_name,
csi.csitl_name csitl_name, 
csi.csi_id csi_id,
csi.csi_idseq, 
csi.version csi_version, 
ext.att_idseq,
ext.aca_idseq, 
cs.cs_idseq,
csi.csi_idseq,
cscsi.cs_csi_idseq
FROM sbrext.ac_att_cscsi_view_ext ext, sbr.cs_csi_view cscsi, 
sbr.cs_items_view csi, sbr.classification_schemes_view cs  
WHERE ext.att_idseq = '746A8063-D650-3C9D-E040-BB89AD437A9B' 
AND   ext.cs_csi_idseq = cscsi.cs_csi_idseq 
AND   cscsi.csi_idseq = csi.csi_idseq 
AND   cscsi.cs_idseq = cs.cs_idseq 
ORDER BY upper(csi.long_name)
    */	
	private String csLongName;// goes to CS* Long Name
	private String csDefinition;// goes to CS* Definition
	private String csiName; // goes to CSI* Name
	private String csitlName; // goes to CSI* Type
	private Integer csiId; // goes to CSI* Public Id
	private Float csiVersion; // goes to CSI* Version
	//attIdseq identifies definition or designation; designations has a "DESIG_IDSEQ", we use this ID as ATT_IDSEQ 
	//definition has DEFIN_IDSEQ we use this ID as ATT_IDSEQ
	private String attIdseq; 
	private String acaIdseq;
	private String csIdseq;
	private String csiIdseq;
	private String csCsiIdseq;
	
	public CsCsiValueMeaningModel() {
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


	public Integer getCsiId() {
		return csiId;
	}


	public void setCsiId(Integer csiId) {
		this.csiId = csiId;
	}


	public Float getCsiVersion() {
		return csiVersion;
	}


	public void setCsiVersion(Float csiVersion) {
		this.csiVersion = csiVersion;
	}


	public String getAttIdseq() {
		return attIdseq;
	}


	public void setAttIdseq(String attIdseq) {
		this.attIdseq = attIdseq;
	}


	public String getAcaIdseq() {
		return acaIdseq;
	}


	public void setAcaIdseq(String acaIdseq) {
		this.acaIdseq = acaIdseq;
	}


	public String getCsIdseq() {
		return csIdseq;
	}


	public void setCsIdseq(String csIdseq) {
		this.csIdseq = csIdseq;
	}


	public String getCsiIdseq() {
		return csiIdseq;
	}


	public void setCsiIdseq(String csiIdseq) {
		this.csiIdseq = csiIdseq;
	}


	public String getCsCsiIdseq() {
		return csCsiIdseq;
	}


	public void setCsCsiIdseq(String csCsiIdseq) {
		this.csCsiIdseq = csCsiIdseq;
	}


	@Override
	public String toString() {
		return "CsCsiValueMeaningModel [csLongName=" + csLongName + ", csDefinition=" + csDefinition + ", csiName="
				+ csiName + ", csitlName=" + csitlName + ", csiId=" + csiId + ", csiVersion=" + csiVersion
				+ ", attIdseq=" + attIdseq + ", acaIdseq=" + acaIdseq + ", csIdseq=" + csIdseq + ", csiIdseq="
				+ csiIdseq + ", csCsiIdseq=" + csCsiIdseq + "]";
	}

}
