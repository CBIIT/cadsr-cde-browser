package gov.nih.nci.ncicb.cadsr.persistence.bc4j;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.Key;
//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------

public class CsCsiImpl extends EntityImpl 
{
  protected static final int CSCSIIDSEQ = 0;
  protected static final int CSIDSEQ = 1;
  protected static final int CSIIDSEQ = 2;
  protected static final int PCSCSIIDSEQ = 3;
  protected static final int LINKCSCSIIDSEQ = 4;
  protected static final int LABEL = 5;
  protected static final int DISPLAYORDER = 6;
  protected static final int DATECREATED = 7;
  protected static final int CREATEDBY = 8;
  protected static final int DATEMODIFIED = 9;
  protected static final int MODIFIEDBY = 10;
  protected static final int LINKCSCSIIDSEQCSCSI = 11;
  protected static final int PCSCSIIDSEQCSCSI = 12;
  protected static final int CLASSSCHEMEITEMS = 13;
  protected static final int CLASSIFICATIONSCHEMES = 14;
  protected static final int CSCSI = 15;
  protected static final int CSCSI1 = 16;
  protected static final int ACCSI = 17;







  private static EntityDefImpl mDefinitionObject;

  /**
   * 
   * This is the default constructor (do not remove)
   */
  public CsCsiImpl()
  {
  }

  /**
   * 
   * Retrieves the definition object for this instance class.
   */
  public static synchronized EntityDefImpl getDefinitionObject()
  {
    if (mDefinitionObject == null)
    {
      mDefinitionObject = (EntityDefImpl)EntityDefImpl.findDefObject("gov.nih.nci.ncicb.cadsr.persistence.bc4j.CsCsi");
    }
    return mDefinitionObject;
  }








  /**
   * 
   * Gets the attribute value for CsCsiIdseq, using the alias name CsCsiIdseq
   */
  public String getCsCsiIdseq()
  {
    return (String)getAttributeInternal(CSCSIIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for CsCsiIdseq
   */
  public void setCsCsiIdseq(String value)
  {
    setAttributeInternal(CSCSIIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for CsIdseq, using the alias name CsIdseq
   */
  public String getCsIdseq()
  {
    return (String)getAttributeInternal(CSIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for CsIdseq
   */
  public void setCsIdseq(String value)
  {
    setAttributeInternal(CSIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for CsiIdseq, using the alias name CsiIdseq
   */
  public String getCsiIdseq()
  {
    return (String)getAttributeInternal(CSIIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for CsiIdseq
   */
  public void setCsiIdseq(String value)
  {
    setAttributeInternal(CSIIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for PCsCsiIdseq, using the alias name PCsCsiIdseq
   */
  public String getPCsCsiIdseq()
  {
    return (String)getAttributeInternal(PCSCSIIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for PCsCsiIdseq
   */
  public void setPCsCsiIdseq(String value)
  {
    setAttributeInternal(PCSCSIIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for LinkCsCsiIdseq, using the alias name LinkCsCsiIdseq
   */
  public String getLinkCsCsiIdseq()
  {
    return (String)getAttributeInternal(LINKCSCSIIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for LinkCsCsiIdseq
   */
  public void setLinkCsCsiIdseq(String value)
  {
    setAttributeInternal(LINKCSCSIIDSEQ, value);
  }

  /**
   * 
   * Gets the attribute value for Label, using the alias name Label
   */
  public String getLabel()
  {
    return (String)getAttributeInternal(LABEL);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for Label
   */
  public void setLabel(String value)
  {
    setAttributeInternal(LABEL, value);
  }

  /**
   * 
   * Gets the attribute value for DisplayOrder, using the alias name DisplayOrder
   */
  public Number getDisplayOrder()
  {
    return (Number)getAttributeInternal(DISPLAYORDER);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for DisplayOrder
   */
  public void setDisplayOrder(Number value)
  {
    setAttributeInternal(DISPLAYORDER, value);
  }

  /**
   * 
   * Gets the attribute value for DateCreated, using the alias name DateCreated
   */
  public Date getDateCreated()
  {
    return (Date)getAttributeInternal(DATECREATED);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for DateCreated
   */
  public void setDateCreated(Date value)
  {
    setAttributeInternal(DATECREATED, value);
  }

  /**
   * 
   * Gets the attribute value for CreatedBy, using the alias name CreatedBy
   */
  public String getCreatedBy()
  {
    return (String)getAttributeInternal(CREATEDBY);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for CreatedBy
   */
  public void setCreatedBy(String value)
  {
    setAttributeInternal(CREATEDBY, value);
  }

  /**
   * 
   * Gets the attribute value for DateModified, using the alias name DateModified
   */
  public Date getDateModified()
  {
    return (Date)getAttributeInternal(DATEMODIFIED);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for DateModified
   */
  public void setDateModified(Date value)
  {
    setAttributeInternal(DATEMODIFIED, value);
  }

  /**
   * 
   * Gets the attribute value for ModifiedBy, using the alias name ModifiedBy
   */
  public String getModifiedBy()
  {
    return (String)getAttributeInternal(MODIFIEDBY);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for ModifiedBy
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
      case CSCSIIDSEQ:
        return getCsCsiIdseq();
      case CSIDSEQ:
        return getCsIdseq();
      case CSIIDSEQ:
        return getCsiIdseq();
      case PCSCSIIDSEQ:
        return getPCsCsiIdseq();
      case LINKCSCSIIDSEQ:
        return getLinkCsCsiIdseq();
      case LABEL:
        return getLabel();
      case DISPLAYORDER:
        return getDisplayOrder();
      case DATECREATED:
        return getDateCreated();
      case CREATEDBY:
        return getCreatedBy();
      case DATEMODIFIED:
        return getDateModified();
      case MODIFIEDBY:
        return getModifiedBy();
      case CSCSI:
        return getCsCsi();
      case CSCSI1:
        return getCsCsi1();
      case ACCSI:
        return getAcCsi();
      case LINKCSCSIIDSEQCSCSI:
        return getLinkCsCsiIdseqCsCsi();
      case PCSCSIIDSEQCSCSI:
        return getPCsCsiIdseqCsCsi();
      case CLASSSCHEMEITEMS:
        return getClassSchemeItems();
      case CLASSIFICATIONSCHEMES:
        return getClassificationSchemes();
      default:
        return super.getAttrInvokeAccessor(index, attrDef);
      }
  }
  //  Generated method. Do not modify.

  protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception
  {
    switch (index)
      {
      case CSCSIIDSEQ:
        setCsCsiIdseq((String)value);
        return;
      case CSIDSEQ:
        setCsIdseq((String)value);
        return;
      case CSIIDSEQ:
        setCsiIdseq((String)value);
        return;
      case PCSCSIIDSEQ:
        setPCsCsiIdseq((String)value);
        return;
      case LINKCSCSIIDSEQ:
        setLinkCsCsiIdseq((String)value);
        return;
      case LABEL:
        setLabel((String)value);
        return;
      case DISPLAYORDER:
        setDisplayOrder((Number)value);
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

  /**
   * 
   * Gets the associated entity CsCsiImpl
   */
  public CsCsiImpl getLinkCsCsiIdseqCsCsi()
  {
    return (CsCsiImpl)getAttributeInternal(LINKCSCSIIDSEQCSCSI);
  }

  /**
   * 
   * Sets <code>value</code> as the associated entity CsCsiImpl
   */
  public void setLinkCsCsiIdseqCsCsi(CsCsiImpl value)
  {
    setAttributeInternal(LINKCSCSIIDSEQCSCSI, value);
  }

  /**
   * 
   * Gets the associated entity CsCsiImpl
   */
  public CsCsiImpl getPCsCsiIdseqCsCsi()
  {
    return (CsCsiImpl)getAttributeInternal(PCSCSIIDSEQCSCSI);
  }

  /**
   * 
   * Sets <code>value</code> as the associated entity CsCsiImpl
   */
  public void setPCsCsiIdseqCsCsi(CsCsiImpl value)
  {
    setAttributeInternal(PCSCSIIDSEQCSCSI, value);
  }

  /**
   * 
   * Gets the associated entity ClassSchemeItemsImpl
   */
  public ClassSchemeItemsImpl getClassSchemeItems()
  {
    return (ClassSchemeItemsImpl)getAttributeInternal(CLASSSCHEMEITEMS);
  }

  /**
   * 
   * Sets <code>value</code> as the associated entity ClassSchemeItemsImpl
   */
  public void setClassSchemeItems(ClassSchemeItemsImpl value)
  {
    setAttributeInternal(CLASSSCHEMEITEMS, value);
  }

  /**
   * 
   * Gets the associated entity ClassificationSchemesImpl
   */
  public ClassificationSchemesImpl getClassificationSchemes()
  {
    return (ClassificationSchemesImpl)getAttributeInternal(CLASSIFICATIONSCHEMES);
  }

  /**
   * 
   * Sets <code>value</code> as the associated entity ClassificationSchemesImpl
   */
  public void setClassificationSchemes(ClassificationSchemesImpl value)
  {
    setAttributeInternal(CLASSIFICATIONSCHEMES, value);
  }

  /**
   * 
   * Gets the associated entity oracle.jbo.RowIterator
   */
  public RowIterator getCsCsi()
  {
    return (RowIterator)getAttributeInternal(CSCSI);
  }

  /**
   * 
   * Gets the associated entity oracle.jbo.RowIterator
   */
  public RowIterator getCsCsi1()
  {
    return (RowIterator)getAttributeInternal(CSCSI1);
  }

  /**
   * 
   * Gets the associated entity oracle.jbo.RowIterator
   */
  public RowIterator getAcCsi()
  {
    return (RowIterator)getAttributeInternal(ACCSI);
  }

  /**
   * 
   * Creates a Key object based on given key constituents
   */
  public static Key createPrimaryKey(String csCsiIdseq)
  {
    return new Key(new Object[] {csCsiIdseq});
  }








}