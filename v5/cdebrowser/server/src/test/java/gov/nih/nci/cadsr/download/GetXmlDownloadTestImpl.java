/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.util.Collection;
/**
 * This is a placeholder test class to inject into DownloadExcelController to create a Unit Test bean.
 * 
 * @author asafievan
 *
 */
public class GetXmlDownloadTestImpl implements GetXmlDownloadInterface {

	@Override
	public String persist(Collection<String> itemIds, String RAI, String source) throws Exception {
		return null;
	}

	@Override
	public void setLocalDownloadDirectory(String localDownloadDirectory) {

	}

	@Override
	public void setFileNamePrefix(String excelFileNamePrefix) {

	}

}
