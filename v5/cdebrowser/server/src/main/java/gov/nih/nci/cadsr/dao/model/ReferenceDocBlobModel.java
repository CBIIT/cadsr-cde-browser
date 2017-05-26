/**
 * Copyright (C) 2017 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.model;

import java.io.InputStream;

/**
 * To support CDEBROWSER-517 Download Form template/Reference Document
 * 
 * @author asafievan
 *
 */
public class ReferenceDocBlobModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String rdIdseq;
	public String docName;
	public String mimeType;
	public InputStream docContent;
	
	public ReferenceDocBlobModel(String rdIdseq, String docName, String mimeType, InputStream docContent) {
		super();
		this.rdIdseq = rdIdseq;
		this.docName = docName;
		this.mimeType = mimeType;
		this.docContent = docContent;
	}
	
	public ReferenceDocBlobModel() {
		super();
	}
	
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public InputStream getDocContent() {
		return docContent;
	}
	public void setDocContent(InputStream docContent) {
		this.docContent = docContent;
	}
	public String getRdIdseq() {
		return rdIdseq;
	}
	public void setRdIdseq(String rdIdseq) {
		this.rdIdseq = rdIdseq;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	@Override
	public String toString() {
		return "ReferenceDocBlobModel [rdIdseq=" + rdIdseq + ", docName=" + docName + ", mimeType=" + mimeType
				+ ", docContent=" + docContent + "]";
	}

}
