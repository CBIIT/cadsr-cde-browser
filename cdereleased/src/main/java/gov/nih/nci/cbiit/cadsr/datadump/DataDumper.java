package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
public class DataDumper {
	public static void main(String[] args) {
		if (args.length <= 0)
			return;
		try {
			int mode = Integer.parseInt(args[0]);
			if (mode == 1) {
				SpringUtil.getXMLCDEDumper().doDump();
			} else if (mode == 2)
				SpringUtil.getExcelCDEDumper().doDump();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}