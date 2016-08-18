/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 
 * @author asafievan
 *
 */
public class ParameterValidator {
	public static final int EXPECTED_ID_SEQ_LENGTH = 36;
	private static final Logger logger = LogManager.getLogger( ParameterValidator.class.getName() );

	//expected format example: D6CA1C24-3726-672B-E034-0003BA12F5E7
	public static final String idSeqPattern = "^[A-Z0-9]{8}\\-[A-Z0-9]{4}\\-[A-Z0-9]{4}\\-[A-Z0-9]{4}\\-[A-Z0-9]{12}$";
	public static boolean validatePublicIdWIthStar(String publicId) {
		if (StringUtils.isNotBlank(publicId)) {
			//digits and star symbols only are allowed in here
			Pattern p = Pattern.compile("^[0-9*]*$");
			Matcher m = p.matcher(publicId);
			return m.matches();
		}
		else return false;
	}
	/**
	 * 
	 * @param idSeq
	 * @return true if validated
	 */
	public static boolean validateIdSeq(String idSeq) {
		if ((StringUtils.isNotBlank(idSeq)) && (idSeq.length() == EXPECTED_ID_SEQ_LENGTH)) {
			Pattern p = Pattern.compile(idSeqPattern);
			Matcher m = p.matcher(idSeq);
			return m.matches();
		}
		else {
			logger.debug("Wrong length");
			return false;
		}
	}
}
