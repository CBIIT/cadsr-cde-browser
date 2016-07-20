/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtilities;

/**
 * 
 * @author asafievan
 *
 */
public class SearchQueryBuilderUtils {

	public static String buildAltNamesWhere(String text, String altNameTypesStr) {
		String altWhere = "";
		String newSearchStr = "";
		String typeWhere = "";
		String altTypeStr = "";
		String searchWhere = "";
		
		if (text == null)
			return "";
		
		String[] altNameTypes = StringUtilities.buildArrayFromParameter(altNameTypesStr);
		
		newSearchStr = StringReplace.strReplace(text, "*", "%");
		newSearchStr = StringReplace.strReplace(newSearchStr, "'", "''");
		if (altNameTypes == null || StringUtilities.containsKeyLoop(altNameTypes, "ALL"))
			typeWhere = "";
		else if (altNameTypes.length == 1) {
			altTypeStr = altNameTypes[0];
			typeWhere = " and dsn.detl_name = '" + altTypeStr + "'";
		} 
		else {
			for (int i = 0; i < altNameTypes.length; i++) {
				if (i == 0)
					altTypeStr = "'" + altNameTypes[0] + "'";
				else
					altTypeStr = altTypeStr + "," + "'" + altNameTypes[i] + "'";
			}
			typeWhere = " and dsn.detl_name IN (" + altTypeStr + ")";
		}

		searchWhere = " and upper (nvl(dsn.name,'%')) like upper ('" + newSearchStr + "') ";

		altWhere = " and de.de_idseq IN " + "(select de_idseq "
				+ " from sbr.designations_view dsn, sbr.data_elements_view de1 "
				+ " where  de1.de_idseq  = dsn.ac_idseq " + typeWhere + searchWhere + " ) ";
		
		return altWhere;
	}
}
