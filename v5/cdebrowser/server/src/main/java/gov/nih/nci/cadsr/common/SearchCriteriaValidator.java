/**
 * Copyright (C) 2017 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

import gov.nih.nci.cadsr.common.util.ParameterValidator;
import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
/**
 * @Validated(SearchCriteriaValidator.class) 
 * @author asafievan
 *
 */
@Component
public class SearchCriteriaValidator implements Validator {
    private Logger logger = LogManager.getLogger(SearchCriteriaValidator.class.getName());
    //This was for Debug only
//	public SearchCriteriaValidator() {
//		super();
//		logger.debug("SearchCriteriaValidator Constructor");
//	}

	@Override
    public boolean supports(Class<?> clazz) {
        return SearchCriteria.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
    	SearchCriteria searchCriteria = (SearchCriteria) target;
    	String curr;
        if ((StringUtils.isNotBlank(curr = searchCriteria.getPublicId())) && (! ParameterValidator.validatePublicIdWIthStar(curr))) {
            logger.warn("Error Validate! SearchCriteria PublicId malformed: " + searchCriteria.getConceptInput());
            errors.reject("PublicId:"+searchCriteria.getPublicId()+".malformed");
        }
        if ((StringUtils.isNotBlank(curr = searchCriteria.getClassification())) && (! ParameterValidator.validateIdSeq(curr))) {
            logger.warn("Error Validate! SearchCriteria Classification malformed: " + curr);
            errors.reject("Classification:"+curr+".malformed");
        }
        if ((StringUtils.isNotBlank(curr = searchCriteria.getCsCsiIdSeq())) && (! ParameterValidator.validateIdSeq(curr))) {
            logger.warn("Error Validate! SearchCriteria CsCsiIdSeq malformed: " + curr);
            errors.reject("CsCsiIdSeq:"+curr+".malformed");
        }
        if ((StringUtils.isNotBlank(curr = searchCriteria.getProtocol())) && (! ParameterValidator.validateIdSeq(curr))) {
            logger.warn("Error Validate! SearchCriteria Protocol malformed: " + curr);
            errors.reject("Protocol:"+curr+".malformed");
        }
        if ((StringUtils.isNotBlank(curr = searchCriteria.getFormIdSeq())) && (! ParameterValidator.validateIdSeq(curr))) {
            logger.warn("Error Validate! SearchCriteria FormIdSeq malformed: " + curr);
            errors.reject("FormIdSeq:"+curr+".malformed");
        }
        if ((StringUtils.isNotBlank(curr = searchCriteria.getContext())) && (! ParameterValidator.validateIdSeq(curr))) {
            logger.warn("Error Validate! SearchCriteria Context malformed: " + curr);
            errors.reject("Context:"+curr+".malformed");
        }
        if ((StringUtils.isNotBlank(curr = searchCriteria.getProgramArea())) && (! StringUtils.isAlphanumeric(curr))) {
            logger.warn("Error Validate! SearchCriteria ProgramArea malformed: " + curr);
            errors.reject("ProgramArea:"+curr+".malformed");
        }
    }
}
