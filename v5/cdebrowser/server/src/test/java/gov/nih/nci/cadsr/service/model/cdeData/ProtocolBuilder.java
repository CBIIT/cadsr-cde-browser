package gov.nih.nci.cadsr.service.model.cdeData;

import org.springframework.test.util.ReflectionTestUtils;

public class ProtocolBuilder 
{
	private Protocol model;

    public ProtocolBuilder() {
        model = new Protocol();
    }

    public ProtocolBuilder programAreaPalName(String programAreaPalName) {
        ReflectionTestUtils.setField(model, "programAreaPalName", programAreaPalName);
        return this;
    }

    public ProtocolBuilder contextIdSeq(String contextIdSeq) {
    	ReflectionTestUtils.setField(model, "contextIdSeq", contextIdSeq);
        return this;
    }

    public ProtocolBuilder protocolIdSeq(String protocolIdSeq) {
    	ReflectionTestUtils.setField(model, "protocolIdSeq", protocolIdSeq);
        return this;
    }

    public ProtocolBuilder protocolLongName(String protocolLongName) {
    	ReflectionTestUtils.setField(model, "protocolLongName", protocolLongName);
        return this;
    }

    public Protocol build() {
        return model;
    }

}
