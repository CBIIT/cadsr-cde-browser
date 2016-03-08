/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.util.Collection;

public interface GetXmlDownloadInterface {

	/**
	 * This method is called from DownloadzzXmlController.
	 * Parameters validity is expected to be checked by the service.
	 * This method does not check validity of parameters.
	 *
	 * @param itemIds
	 * @param RAI
	 * @param source
	 * @return String resource ID for GET operation to retrieve created file resource
	 * @throws Exception
	 */
	String persist(Collection<String> itemIds, String RAI, String source) throws Exception;
	void setLocalDownloadDirectory(String localDownloadDirectory);
	public void setFileNamePrefix(String excelFileNamePrefix);
}