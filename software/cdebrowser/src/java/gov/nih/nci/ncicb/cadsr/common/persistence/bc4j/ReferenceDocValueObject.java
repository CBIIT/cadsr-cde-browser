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

package gov.nih.nci.ncicb.cadsr.common.persistence.bc4j;

import gov.nih.nci.ncicb.cadsr.common.persistence.base.BaseValueObject;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.ReferenceDocument;

import java.sql.Timestamp;
import java.util.List;

public class ReferenceDocValueObject extends BaseValueObject
        implements ReferenceDocument, java.io.Serializable {
    String docName = null;
    String docType = null;
    String docIDSeq = null;
    String docText = null;
    String lang = null;
    String url = null;
    String docIdSeq;
    String language;
    List attachments;
    Context context;
    private int displayOrder;
    protected String createdBy;
    protected Timestamp createdDate;
    protected String modifiedBy;
    protected Timestamp modifiedDate;

    public ReferenceDocValueObject(ReferenceDocumentsViewRowImpl refDoc) {
        docName = checkForNull(refDoc.getName());
        docType = refDoc.getDctlName();
        docIDSeq = refDoc.getRdIdseq();
        docText = checkForNull(refDoc.getDocText());
        lang = refDoc.getLaeName();
        url = checkForNull(refDoc.getUrl());
        context = refDoc.getContext();
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocText() {
        return docText;
    }

    public String getDocIDSeq() {
        return docIDSeq;
    }

    public void setDocIDSeq(String docIdSeq) {
        this.docIdSeq = docIdSeq;
    }

    public String getUrl() {
        return url;
    }

    public String getLanguage() {
        return lang;
    }

    public void setDocText(String docText) {
        this.docText = docText;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context newContext) {
        context = newContext;
    }

    public List getAttachments() {
        return attachments;
    }

    public void setAttachments(List newAttachements) {
        attachments = newAttachements;
    }


    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }


    public int getDisplayOrder() {
        return displayOrder;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String p0) {
        this.createdBy = p0;
    }

    public Timestamp getDateCreated() {
        return createdDate;
    }

    public void setDateCreated(Timestamp p0) {
        this.createdDate = p0;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String p0) {
        this.modifiedBy = p0;
    }

    public Timestamp getDateModified() {
        return modifiedDate;
    }

    public void setDateModified(Timestamp p0) {
        this.modifiedDate = p0;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}