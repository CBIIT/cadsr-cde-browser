package gov.nih.nci.ncicb.cadsr.persistence.bc4j;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------

public class QcRecsExtViewRowImpl extends ViewRowImpl 
{
  protected static final int QRIDSEQ = 0;


  protected static final int PQCIDSEQ = 1;
  protected static final int CQCIDSEQ = 2;
  protected static final int DISPLAYORDER = 3;
  protected static final int RLNAME = 4;
  protected static final int DATECREATED = 5;
  protected static final int CREATEDBY = 6;
  protected static final int DATEMODIFIED = 7;
  protected static final int MODIFIEDBY = 8;
  /**
   * 
   * This is the default constructor (do not remove)
   */
  public QcRecsExtViewRowImpl()
  {
  }

  /**
   * 
   * Gets QcRecsExt entity object.
   */
  public QcRecsExtImpl getQcRecsExt()
  {
    return (QcRecsExtImpl)getEntity(0);
  }

  /**
   * 
   * Gets the attribute value for QR_IDSEQ using the alias name QrIdseq
   */
  public String getQrIdseq()
  {
    return (String)getAttributeInternal(QRIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for QR_IDSEQ using the alias name QrIdseq
   */
  public void setQrIdseq(String value)
  {
    setAttributeInternal(QRIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for P_QC_IDSEQ using the alias name PQcIdseq
   */
  public String getPQcIdseq()
  {
    return (String)getAttributeInternal(PQCIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for P_QC_IDSEQ using the alias name PQcIdseq
   */
  public void setPQcIdseq(String value)
  {
    setAttributeInternal(PQCIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for C_QC_IDSEQ using the alias name CQcIdseq
   */
  public String getCQcIdseq()
  {
    return (String)getAttributeInternal(CQCIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for C_QC_IDSEQ using the alias name CQcIdseq
   */
  public void setCQcIdseq(String value)
  {
    setAttributeInternal(CQCIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for DISPLAY_ORDER using the alias name DisplayOrder
   */
  public Number getDisplayOrder()
  {
    return (Number)getAttributeInternal(DISPLAYORDER);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DISPLAY_ORDER using the alias name DisplayOrder
   */
  public void setDisplayOrder(Number value)
  {
    setAttributeInternal(DISPLAYORDER, value);
  }

  /**
   * 
   * Gets the attribute value for RL_NAME using the alias name RlName
   */
  public String getRlName()
  {
    return (String)getAttributeInternal(RLNAME);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for RL_NAME using the alias name RlName
   */
  public void setRlName(String value)
  {
    setAttributeInternal(RLNAME, value);
  }

  /**
   * 
   * Gets the attribute value for DATE_CREATED using the alias name DateCreated
   */
  public Date getDateCreated()
  {
    return (Date)getAttributeInternal(DATECREATED);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DATE_CREATED using the alias name DateCreated
   */
  public void setDateCreated(Date value)
  {
    setAttributeInternal(DATECREATED, value);
  }

  /**
   * 
   * Gets the attribute value for CREATED_BY using the alias name CreatedBy
   */
  public String getCreatedBy()
  {
    return (String)getAttributeInternal(CREATEDBY);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for CREATED_BY using the alias name CreatedBy
   */
  public void setCreatedBy(String value)
  {
    setAttributeInternal(CREATEDBY, value);
  }

  /**
   * 
   * Gets the attribute value for DATE_MODIFIED using the alias name DateModified
   */
  public Date getDateModified()
  {
    return (Date)getAttributeInternal(DATEMODIFIED);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for DATE_MODIFIED using the alias name DateModified
   */
  public void setDateModified(Date value)
  {
    setAttributeInternal(DATEMODIFIED, value);
  }

  /**
   * 
   * Gets the attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public String getModifiedBy()
  {
    return (String)getAttributeInternal(MODIFIEDBY);
  }

  /**
   * 
   * Sets <code>value</code> as attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public void setModifiedBy(String value)
  {
    setAttributeInternal(MODIFIEDBY, value);
  }
  //  Generated method. Do not modify.

  protected Object getAttrInvokeAccessor(int index, AttributeDefImpl attrDef) throws Exception
  {
    switch (index)
      {
      case QRIDSEQ:
        return getQrIdseq();
      case PQCIDSEQ:
        return getPQcIdseq();
      case CQCIDSEQ:
        return getCQcIdseq();
      case DISPLAYORDER:
        return getDisplayOrder();
      case RLNAME:
        return getRlName();
      case DATECREATED:
        return getDateCreated();
      case CREATEDBY:
        return getCreatedBy();
      case DATEMODIFIED:
        return getDateModified();
      case MODIFIEDBY:
        return getModifiedBy();
      default:
        return super.getAttrInvokeAccessor(index, attrDef);
      }
  }
  //  Generated method. Do not modify.

  protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception
  {
    switch (index)
      {
      case QRIDSEQ:
        setQrIdseq((String)value);
        return;
      case PQCIDSEQ:
        setPQcIdseq((String)value);
        return;
      case CQCIDSEQ:
        setCQcIdseq((String)value);
        return;
      case DISPLAYORDER:
        setDisplayOrder((Number)value);
        return;
      case RLNAME:
        setRlName((String)value);
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
      default:
        super.setAttrInvokeAccessor(index, value, attrDef);
        return;
      }
  }
}