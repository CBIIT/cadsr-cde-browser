/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

import java.util.ArrayList;
import java.util.List;
/**
 * This is to present Value Domain Permissible Value Alternates grouped by Classification items.
 * @author asafievan
 *
 */
public class PermissibleValueExt {
	private String pvIdseq;
	private String pvMeaning;
	private String vmPublicId;
	private String vmVersion;
	List<ClassificationSchemaAlternate> classificationSchemaList = new ArrayList<>();
	public String getPvIdseq() {
		return pvIdseq;
	}
	public void setPvIdseq(String pvIdseq) {
		this.pvIdseq = pvIdseq;
	}
	public String getPvMeaning() {
		return pvMeaning;
	}
	public void setPvMeaning(String pvMeaning) {
		this.pvMeaning = pvMeaning;
	}
	public String getVmPublicId() {
		return vmPublicId;
	}
	public void setVmPublicId(String vmPublicId) {
		this.vmPublicId = vmPublicId;
	}
	public String getVmVersion() {
		return vmVersion;
	}
	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}
	public List<ClassificationSchemaAlternate> getClassificationSchemaList() {
		return classificationSchemaList;
	}
	public void setClassificationSchemaList(List<ClassificationSchemaAlternate> classificationSchemaList) {
		this.classificationSchemaList = classificationSchemaList;
	}
	@Override
	public String toString() {
		return "PermissibleValueExt [pvIdseq=" + pvIdseq + ", pvMeaning=" + pvMeaning + ", vmPublicId=" + vmPublicId
				+ ", vmVersion=" + vmVersion + ", classificationSchemaList=" + classificationSchemaList + "]";
	}
	
	
}
