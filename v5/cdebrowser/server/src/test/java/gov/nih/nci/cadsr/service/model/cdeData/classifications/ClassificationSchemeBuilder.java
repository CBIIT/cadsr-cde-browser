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
    
    public ClassificationSchemeBuilder contextName(String contextName) {
    	ReflectionTestUtils.setField(model, "contextName", contextName);
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
    
    public ClassificationSchemeBuilder csCsiIdSeq(String csCsiIdSeq) {
    	ReflectionTestUtils.setField(model, "csCsiIdSeq", csCsiIdSeq);
        return this;
    }
    
    public ClassificationSchemeBuilder csCsiName(String csCsiName) {
    	ReflectionTestUtils.setField(model, "csCsiName", csCsiName);
        return this;
    }
    
    public ClassificationSchemeBuilder parentCsiIdSeq(String parentCsiIdSeq) {
    	ReflectionTestUtils.setField(model, "parentCsiIdSeq", parentCsiIdSeq);
        return this;
    }
    
    public ClassificationSchemeBuilder csiLevel(int csiLevel) {
    	ReflectionTestUtils.setField(model, "csiLevel", csiLevel);
        return this;
    }

    public ClassificationScheme build() {
        return model;
    }
}