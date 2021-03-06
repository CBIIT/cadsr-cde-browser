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
import gov.nih.nci.ncicb.cadsr.common.dto.ContextTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.util.NCIBC4JUtil;
import java.util.List;
import oracle.clex.persistence.bc4j.BC4JUtil;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import gov.nih.nci.ncicb.cadsr.common.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.common.util.logging.LogFactory;
//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------

public class ReferenceDocumentsViewRowImpl extends ViewRowImpl  {


  public static final int RDIDSEQ = 0;
  public static final int NAME = 1;
  public static final int ORGIDSEQ = 2;
  public static final int DCTLNAME = 3;
  public static final int ACIDSEQ = 4;
  public static final int ACHIDSEQ = 5;
  public static final int ARIDSEQ = 6;
  public static final int RDTLNAME = 7;
  public static final int DOCTEXT = 8;
  public static final int DATECREATED = 9;
  public static final int CREATEDBY = 10;
  public static final int DATEMODIFIED = 11;
  public static final int MODIFIEDBY = 12;
  public static final int URL = 13;
  public static final int LAENAME = 14;
  public static final int DISPLAYORDER = 15;
  public static final int CONTEIDSEQ = 16;
  public static final int REFERENCEBLOBSVIEW = 17;
  private static Log log = LogFactory.getLog(ReferenceDocumentsViewRowImpl.class.getName());
  
  /**
   * 
   * This is the default constructor (do not remove)
   */
  public ReferenceDocumentsViewRowImpl() {
  }


  public Context getContext() {

    NCIBC4JUtil bc4jUtil = new NCIBC4JUtil();
    Context context = null;
    try {
      oracle.jbo.ViewObject contextView =
        getViewObject().getApplicationModule().findViewObject(
          "ContextsView");


      ViewObjectImpl contextView1 =
        (ContextsViewImpl) bc4jUtil.cloneViewObject(contextView);
      contextView1.setWhereClause("CONTE_IDSEQ='" + getConteIdseq() + "'");
      contextView1.executeQuery();
      
      if(contextView1.hasNext()) {
        context = new ContextTransferObject();
        ContextsViewRowImpl row = (ContextsViewRowImpl)contextView1.next();
        context.setConteIdseq(row.getConteIdseq());
        context.setName(row.getName());
      }
    } // end try
    catch (Exception e) {
      log.error("Error in getContext(): ", e);
    }

    return context;
  }
  
  /**
   * 
   * Gets ReferenceDocuments entity object.
   */
  public ReferenceDocumentsImpl getReferenceDocuments() {
    return (ReferenceDocumentsImpl)getEntity(0);
  }

  /**
   * 
   * Gets the attribute value for RD_IDSEQ using the alias name RdIdseq
   */
  public String getRdIdseq() {
    return (String)getAttributeInternal(RDIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for RD_IDSEQ using the alias name RdIdseq
   */
  public void setRdIdseq(String value) {
    setAttributeInternal(RDIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for NAME using the alias name Name
   */
  public String getName() {
    return (String)getAttributeInternal(NAME);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for NAME using the alias name Name
   */
  public void setName(String value) {
    setAttributeInternal(NAME, value);
  }

  /**
   * 
   * Gets the attribute value for ORG_IDSEQ using the alias name OrgIdseq
   */
  public String getOrgIdseq() {
    return (String)getAttributeInternal(ORGIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for ORG_IDSEQ using the alias name OrgIdseq
   */
  public void setOrgIdseq(String value) {
    setAttributeInternal(ORGIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for DCTL_NAME using the alias name DctlName
   */
  public String getDctlName() {
    return (String)getAttributeInternal(DCTLNAME);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DCTL_NAME using the alias name DctlName
   */
  public void setDctlName(String value) {
    setAttributeInternal(DCTLNAME, value);
  }

  /**
   * 
   * Gets the attribute value for AC_IDSEQ using the alias name AcIdseq
   */
  public String getAcIdseq() {
    return (String)getAttributeInternal(ACIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for AC_IDSEQ using the alias name AcIdseq
   */
  public void setAcIdseq(String value) {
    setAttributeInternal(ACIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for ACH_IDSEQ using the alias name AchIdseq
   */
  public String getAchIdseq() {
    return (String)getAttributeInternal(ACHIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for ACH_IDSEQ using the alias name AchIdseq
   */
  public void setAchIdseq(String value) {
    setAttributeInternal(ACHIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for AR_IDSEQ using the alias name ArIdseq
   */
  public String getArIdseq() {
    return (String)getAttributeInternal(ARIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for AR_IDSEQ using the alias name ArIdseq
   */
  public void setArIdseq(String value) {
    setAttributeInternal(ARIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for RDTL_NAME using the alias name RdtlName
   */
  public String getRdtlName() {
    return (String)getAttributeInternal(RDTLNAME);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for RDTL_NAME using the alias name RdtlName
   */
  public void setRdtlName(String value) {
    setAttributeInternal(RDTLNAME, value);
  }

  /**
   * 
   * Gets the attribute value for DOC_TEXT using the alias name DocText
   */
  public String getDocText() {
    return (String)getAttributeInternal(DOCTEXT);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DOC_TEXT using the alias name DocText
   */
  public void setDocText(String value) {
    setAttributeInternal(DOCTEXT, value);
  }

  /**
   * 
   * Gets the attribute value for DATE_CREATED using the alias name DateCreated
   */
  public Date getDateCreated() {
    return (Date)getAttributeInternal(DATECREATED);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DATE_CREATED using the alias name DateCreated
   */
  public void setDateCreated(Date value) {
    setAttributeInternal(DATECREATED, value);
  }

  /**
   * 
   * Gets the attribute value for CREATED_BY using the alias name CreatedBy
   */
  public String getCreatedBy() {
    return (String)getAttributeInternal(CREATEDBY);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for CREATED_BY using the alias name CreatedBy
   */
  public void setCreatedBy(String value) {
    setAttributeInternal(CREATEDBY, value);
  }

  /**
   * 
   * Gets the attribute value for DATE_MODIFIED using the alias name DateModified
   */
  public Date getDateModified() {
    return (Date)getAttributeInternal(DATEMODIFIED);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DATE_MODIFIED using the alias name DateModified
   */
  public void setDateModified(Date value) {
    setAttributeInternal(DATEMODIFIED, value);
  }

  /**
   * 
   * Gets the attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public String getModifiedBy() {
    return (String)getAttributeInternal(MODIFIEDBY);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public void setModifiedBy(String value) {
    setAttributeInternal(MODIFIEDBY, value);
  }

  /**
   * 
   * Gets the attribute value for URL using the alias name Url
   */
  public String getUrl() {
    return (String)getAttributeInternal(URL);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for URL using the alias name Url
   */
  public void setUrl(String value) {
    setAttributeInternal(URL, value);
  }

  /**
   * 
   * Gets the attribute value for LAE_NAME using the alias name LaeName
   */
  public String getLaeName() {
    return (String)getAttributeInternal(LAENAME);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for LAE_NAME using the alias name LaeName
   */
  public void setLaeName(String value) {
    setAttributeInternal(LAENAME, value);
  }

  /**
   * 
   * Gets the attribute value for DISPLAY_ORDER using the alias name DisplayOrder
   */
  public Number getDisplayOrder() {
    return (Number)getAttributeInternal(DISPLAYORDER);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DISPLAY_ORDER using the alias name DisplayOrder
   */
  public void setDisplayOrder(Number value) {
    setAttributeInternal(DISPLAYORDER, value);
  }

  /**
   * 
   * Gets the associated <code>RowIterator</code> using master-detail link ReferenceBlobsView
   */
  public RowIterator getReferenceBlobsView() {
    return (RowIterator)getAttributeInternal(REFERENCEBLOBSVIEW);
  }
  //  Generated method. Do not modify.

  protected Object getAttrInvokeAccessor(int index, AttributeDefImpl attrDef) throws Exception {
    switch (index)
      {
      case RDIDSEQ:
        return getRdIdseq();
      case NAME:
        return getName();
      case ORGIDSEQ:
        return getOrgIdseq();
      case DCTLNAME:
        return getDctlName();
      case ACIDSEQ:
        return getAcIdseq();
      case ACHIDSEQ:
        return getAchIdseq();
      case ARIDSEQ:
        return getArIdseq();
      case RDTLNAME:
        return getRdtlName();
      case DOCTEXT:
        return getDocText();
      case DATECREATED:
        return getDateCreated();
      case CREATEDBY:
        return getCreatedBy();
      case DATEMODIFIED:
        return getDateModified();
      case MODIFIEDBY:
        return getModifiedBy();
      case URL:
        return getUrl();
      case LAENAME:
        return getLaeName();
      case DISPLAYORDER:
        return getDisplayOrder();
      case CONTEIDSEQ:
        return getConteIdseq();
      case REFERENCEBLOBSVIEW:
        return getReferenceBlobsView();
      default:
        return super.getAttrInvokeAccessor(index, attrDef);
      }
  }
  //  Generated method. Do not modify.

  protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception {
    switch (index)
      {
      case RDIDSEQ:
        setRdIdseq((String)value);
        return;
      case NAME:
        setName((String)value);
        return;
      case ORGIDSEQ:
        setOrgIdseq((String)value);
        return;
      case DCTLNAME:
        setDctlName((String)value);
        return;
      case ACIDSEQ:
        setAcIdseq((String)value);
        return;
      case ACHIDSEQ:
        setAchIdseq((String)value);
        return;
      case ARIDSEQ:
        setArIdseq((String)value);
        return;
      case RDTLNAME:
        setRdtlName((String)value);
        return;
      case DOCTEXT:
        setDocText((String)value);
        return;
      case DATECREATED:
        setDateCreated((Date)value);
        return;
      case CREATEDBY:
        setCreatedBy((String)value);
        return;
      case DATEMODIFIED:
        setDateModified((Date)value);
        return;
      case MODIFIEDBY:
        setModifiedBy((String)value);
        return;
      case URL:
        setUrl((String)value);
        return;
      case LAENAME:
        setLaeName((String)value);
        return;
      case DISPLAYORDER:
        setDisplayOrder((Number)value);
        return;
      case CONTEIDSEQ:
        setConteIdseq((String)value);
        return;
      default:
        super.setAttrInvokeAccessor(index, value, attrDef);
        return;
      }
  }

  /**
   * 
   *  Gets the attribute value for CONTE_IDSEQ using the alias name ConteIdseq
   */
  public String getConteIdseq()
  {
    return (String)getAttributeInternal(CONTEIDSEQ);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for CONTE_IDSEQ using the alias name ConteIdseq
   */
  public void setConteIdseq(String value)
  {
    setAttributeInternal(CONTEIDSEQ, value);
  }
}