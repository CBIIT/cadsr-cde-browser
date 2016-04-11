package gov.nih.nci.cadsr.service.model.cdeData.classifications;

import org.springframework.test.util.ReflectionTestUtils;

public class ClassificationSchemeBuilder {

    private ClassificationScheme model;

    public ClassificationSchemeBuilder() {
        model = new ClassificationScheme();
    }

    public ClassificationSchemeBuilder programAreaPalName(String programAreaPalName) {
        ReflectionTestUtils.setField(model, "programAreaPalName", programAreaPalName);
        return this;
    }

    public ClassificationSchemeBuilder contextIdSeq(String contextIdSeq) {
    	ReflectionTestUtils.setField(model, "contextIdSeq", contextIdSeq);
        return this;
    }

    public ClassificationSchemeBuilder csIdSeq(String csIdSeq) {
    	ReflectionTestUtils.setField(model, "csIdSeq", csIdSeq);
        return this;
    }

    public ClassificationSchemeBuilder csLongName(String csLongName) {
    	ReflectionTestUtils.setField(model, "csLongName", csLongName);
        return this;
    }

    public ClassificationScheme build() {
        return model;
    }
}