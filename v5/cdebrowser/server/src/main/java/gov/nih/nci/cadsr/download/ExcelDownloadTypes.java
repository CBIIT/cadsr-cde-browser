/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

public enum ExcelDownloadTypes {
	DE_EXCEL("deSearch"),
	DE_EXCEL_PRIOR("deSearchPrior"),
	CDR_CART_EXCEL("cdeCart"),
	CDR_CART_EXCEL_PRIOR("cdeCartPrior"),
	CDE_COMPARE_EXCEL("cdeCompare");
	
	private ExcelDownloadTypes(final String paramValue) {
		this.paramValue = paramValue;
	}
	public String toString() { return paramValue; }
	
	public String value() {return paramValue;}
	
	private final String paramValue;
	
	public static boolean isDownloadTypeValid(final String param) {
		ExcelDownloadTypes[] all = ExcelDownloadTypes.values();
		for (ExcelDownloadTypes curr : all) {
			if (curr.value().equals(param))
				return true;
		}
		return false;
	}
}
