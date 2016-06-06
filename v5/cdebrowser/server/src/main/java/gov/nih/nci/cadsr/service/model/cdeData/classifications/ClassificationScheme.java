package gov.nih.nci.cadsr.service.model.cdeData.classifications;

import java.io.Serializable;

public final class ClassificationScheme implements Serializable
{
	private static final long serialVersionUID = 8727901340113932831L;
	
	private String programAreaPalName;
	private String contextIdSeq;
	private String contextName;
	private String csIdSeq;
	private String csLongName;
	private String csCsiIdSeq;
	private String csCsiName;
	private String parentCsiIdSeq;
	private int csiLevel;
	
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

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getCsCsiIdSeq() {
		return csCsiIdSeq;
	}

	public void setCsCsiIdSeq(String csCsiIdSeq) {
		this.csCsiIdSeq = csCsiIdSeq;
	}

	public String getCsCsiName() {
		return csCsiName;
	}

	public void setCsCsiName(String csCsiName) {
		this.csCsiName = csCsiName;
	}

	public String getParentCsiIdSeq() {
		return parentCsiIdSeq;
	}

	public void setParentCsiIdSeq(String parentCsiIdSeq) {
		this.parentCsiIdSeq = parentCsiIdSeq;
	}

	public int getCsiLevel() {
		return csiLevel;
	}

	public void setCsiLevel(int csiLevel) {
		this.csiLevel = csiLevel;
	}

	@Override
	public String toString() {
		return "ClassificationScheme [programAreaPalName=" + programAreaPalName + ", contextIdSeq=" + contextIdSeq
				+ ", contextName=" + contextName + ", csIdSeq=" + csIdSeq + ", csLongName=" + csLongName
				+ ", csCsiIdSeq=" + csCsiIdSeq + ", csCsiName=" + csCsiName + ", parentCsiIdSeq=" + parentCsiIdSeq
				+ ", csiLevel=" + csiLevel + "]";
	}
	
}
