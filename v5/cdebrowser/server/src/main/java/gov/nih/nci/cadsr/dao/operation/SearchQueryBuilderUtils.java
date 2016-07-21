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
	
	public static String buildRegistrationWhere(String paramValue, String tableColumn) {
		String resultWhere = buildListStatusWhere(paramValue, ",", "ALL", tableColumn);
		return resultWhere;
	}
	
	public static String buildWorkflowWhere(String paramValue, String tableColumn) {
		String resultWhere = buildListStatusWhere(paramValue, ",", "ALL", tableColumn);
		return resultWhere;
	}
		
	public static String buildListStatusWhere(String sourceStr, String separator, String allInclusive, String tableFieldName) {
		String resultWhere = "";
		String auxStr = "";
		
		if ((sourceStr == null) || (tableFieldName == null))
			return "";
		
		String[] arrvalues = StringUtilities.buildArrayFromParameter(sourceStr, separator);
		
		if (arrvalues == null || StringUtilities.containsKeyLoop(arrvalues, allInclusive))
			resultWhere = "";
		else if (arrvalues.length == 1) {
			auxStr = arrvalues[0];
			resultWhere = " AND " + tableFieldName + " = '" + auxStr + "'";
		} 
		else {
			for (int i = 0; i < arrvalues.length; i++) {
				if (i == 0)
					auxStr = "'" + arrvalues[0] + "'";
				else
					auxStr = auxStr + "," + "'" + arrvalues[i] + "'";
			}
			resultWhere = " AND " + tableFieldName + " IN (" + auxStr + ")";
		}
		
		return resultWhere;
	}
}
