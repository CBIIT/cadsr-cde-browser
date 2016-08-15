/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.util.Collection;

public interface GetExcelDownloadInterface {

	/**
	 * This method is called from DownloadExcelController.
	 * Parameters validity is expected to be checked by the service.
	 * This method does not check validity of parameters.
	 * 
	 * @param itemIds shall be not null or empty. These are internal DE IDs.
	 * @param RAI application parameter
	 * @param source type of download @see gov.nih.nci.cadsr.download.ExcelDownloadTypes
	 * @return a local file ID used to save Excel table which will be return by the service.
	 * @throws Exception
	 */
	String persist(Collection<String> itemIds, String RAI, String source) throws Exception;
	void setLocalDownloadDirectory(String localDownloadDirectory);
	public void setFileNamePrefix(String excelFileNamePrefix);
	public String generateExcelFileId() throws Exception;
}