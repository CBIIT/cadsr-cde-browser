/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ReferenceDocBlobModel;
/**
 *  To support CDEBROWSER-517 Download Form template/Reference Document.
 *  
 * @author asafievan
 *
 */
public interface ReferenceDocBlobDAO {
	/**
	 * Find Template object to use in Download Form Template operation if provided rdIdseq has any.
	 * 
	 * @param rdIdseq RefDoc ID SEQ
	 * @return String Idseq of downloadable Form Template or null
	 */
	ReferenceDocBlobModel retrieveReferenceDocBlobByRdIdseq(String rdIdseq);
	
	/**
	 * Find the latest Form Template ID SEQ if there is any. Internal use.
	 * 
	 * @param acIdseq Form ID SEQ
	 * @return String Idseq of downloadable Form Template or null
	 */
	String retrieveLatestRdIdseqByAcIdseq(String acIdseq);

	/**
	 * Check if a Downloadable Template Doc for Form exist. Used when we populate Protocol objects.
	 * 
	 * @param acIdseq
	 * @return IDSEQ of a Template (rd_idseq) if Form has any not empty BLOB.
	 */
	String retrieveDownloadBlobIdseqByAcIdseq(String acIdseq);

	
    /**
     * Find the latest Form Template object to use in Download Form Template operation if there is any.
     * 
     * @param acIdseq
     * @return ReferenceDocBlobModel or null
     */
	ReferenceDocBlobModel retrieveReferenceDocBlobByAcIdseq(String acIdseq);
}
