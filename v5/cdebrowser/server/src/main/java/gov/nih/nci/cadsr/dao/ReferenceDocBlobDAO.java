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
    String getLatestRdIdseqByAcIdseq(String acIdseq);
	ReferenceDocBlobModel getReferenceDocBlobByAcIdseq(String acIdseq);
}
