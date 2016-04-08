package gov.nih.nci.cadsr.service.model.cdeData.classifications;

import java.io.Serializable;

public final class ClassificationScheme implements Serializable
{
	private static final long serialVersionUID = 8727901340113932831L;
	
	private String programAreaPalName;
	private String contextIdSeq;
	private String csIdSeq;
	private String csLongName;
	
	public String getProgramAreaPalName() {
		return programAreaPalName;
	}

	public void setProgramAreaPalName(String programAreaPalName) {
		this.programAreaPalName = programAreaPalName;
	}

	public String getContextIdSeq() {
		return contextIdSeq;
	}
	
	public void setContextIdSeq(String contextIdSeq) {
		this.contextIdSeq = contextIdSeq;
	}
	
	public String getCsIdSeq() {
		return csIdSeq;
	}
	
	public void setCsIdSeq(String csIdSeq) {
		this.csIdSeq = csIdSeq;
	}
	
	public String getCsLongName() {
		return csLongName;
	}
	
	public void setCsLongName(String csLongName) {
		this.csLongName = csLongName;
	}

	@Override
	public String toString() {
		return "ClassificationScheme [programAreaPalName=" + programAreaPalName + ", contextIdSeq=" + contextIdSeq + ", csIdSeq=" + csIdSeq + ", csLongName="
				+ csLongName + "]";
	}
	
}
