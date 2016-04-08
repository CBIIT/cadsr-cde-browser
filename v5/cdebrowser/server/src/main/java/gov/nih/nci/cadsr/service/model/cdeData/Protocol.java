package gov.nih.nci.cadsr.service.model.cdeData;

import java.io.Serializable;

public class Protocol implements Serializable 
{
	private static final long serialVersionUID = 3817966656789825250L;

	public String programAreaPalName;
	public String contextIdSeq;
	public String protocolIdSeq;
	public String protocolLongName;
	
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
	
	public String getProtocolIdSeq() {
		return protocolIdSeq;
	}
	
	public void setProtocolIdSeq(String protocolIdSeq) {
		this.protocolIdSeq = protocolIdSeq;
	}
	
	public String getProtocolLongName() {
		return protocolLongName;
	}
	
	public void setProtocolLongName(String protocolLongName) {
		this.protocolLongName = protocolLongName;
	}

	@Override
	public String toString() {
		return "Protocol [programAreaPalName=" + programAreaPalName + ", contextIdSeq=" + contextIdSeq
				+ ", protocolIdSeq=" + protocolIdSeq + ", protocolLongName=" + protocolLongName + "]";
	}
	
}
