/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author asafievan
 *
 */
public class ParameterValidator {

	public static boolean validatePublicIdWIthStar(String publicId) {
		if (StringUtils.isNotBlank(publicId)) {
			//digits and start synbols only are allowed in here
			Pattern p = Pattern.compile("^[0-9*]*$");
			Matcher m = p.matcher(publicId);
			return m.matches();
		}
		else return false;
	}
}
