/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.common.downloads;

import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;

import java.sql.Connection;

public interface GetExcelDownload {
	public void generateExcelForCDECart(CDECart cart, String src, String _jndiName) throws Exception;
	
	public void generateExcelForDESearch(String sWhere, String src, String _jndiName) throws Exception;
	
	public String getFileName();
	
	public void setFileName(String sfile);
}
