/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.service.ClientException;

public class DownloadUtils {
    private static Logger logger = LogManager.getLogger(DownloadUtils.class.getName());
	
	public static int calcNumberOfGroups(final int lengthOfCollection, final int maxRecords) {
		return (lengthOfCollection / maxRecords) + 
			(((lengthOfCollection % maxRecords) == 0)? 0 : 1);
	}
	/**
	 * This method builds a String object as "where DE_IDSEQ IN ('1','2','3',   , '1000')".
	 * It is used by Download services.
	 * 
	 * @param iter Iterator <String> to iterate through the next objects to build SQL condition.
	 * @param max maximum amount of item from the provided Iterator object.
	 * @return String SQL 'where' statement with IN condition.
	 */
	protected static String buildSqlInCondition(Iterator <String> iter, final int max) {
		StringBuilder sb = new StringBuilder("where DE_IDSEQ IN (");
		String currVal;
		for (int indx = 0; indx < max; indx++) {
			if (iter.hasNext()) {
				currVal = iter.next();
				sb.append("'" + currVal + "', ");
			}
			else break;
		}
		int len = sb.length();
		sb.setCharAt(len - 2, ')');
		return sb.toString().substring(0, len - 1);		
	}
	
	public static void checkInCondition(final Collection<String> itemIds) throws Exception {
		if ((itemIds == null) || (itemIds.isEmpty())) {
			throw new ClientException("Expected Download CDE IDs are not provided");
		}
	}
	
	public static String buildSqlInCondition(final Collection<String> itemIds) throws Exception {
		if ((itemIds != null) && (!(itemIds.isEmpty()))) {
			
			StringBuilder sb = new StringBuilder();
			for (String item : itemIds) {
				sb.append("'" + item + "', ");
			}
			int len = sb.length();
			return sb.toString().substring(0, len - 2);
		}
		else 
			return null;
	}
	
	public static void writeToFile(String xmlStr, String fn) throws Exception {
		FileWriter newFos = null;
		BufferedWriter bos = null;

		try {
			newFos = new FileWriter(fn, true);
			bos = new BufferedWriter(newFos);
			bos.write(xmlStr);
		} 
		finally {
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} 
				catch (Exception e) {
					logger.debug("Unable to close underlying stream due to the following error ", e);
				}
			}
			if (newFos != null) {
				try {
					newFos.close();
				} 
				catch (Exception e) {
					logger.debug("Unable to close temporarily file due to the following error ", e);
				}
			}
		}
	}
}
