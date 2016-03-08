/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

public enum XmlDownloadTypes {
	DE_EXCEL("deSearch"),
	CDR_CART_EXCEL("cdrCart");
	
	private XmlDownloadTypes(final String paramValue) {
		this.paramValue = paramValue;
	}
	public String toString() { return paramValue; }
	
	public String value() {return paramValue;}
	
	private final String paramValue;
	
	public static boolean isDownloadTypeValid(final String param) {
		XmlDownloadTypes[] all = XmlDownloadTypes.values();
		for (XmlDownloadTypes curr : all) {
			if (curr.value().equals(param))
				return true;
		}
		return false;
	}
}
