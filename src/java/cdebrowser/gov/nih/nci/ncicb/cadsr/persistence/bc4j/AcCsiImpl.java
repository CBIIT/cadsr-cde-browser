package gov.nih.nci.ncicb.cadsr.persistence.bc4j;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Date;
import oracle.jbo.Key;
//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------

public class AcCsiImpl extends EntityImpl 
{
  protected static final int ACCSIIDSEQ = 0;
  protected static final int CSCSIIDSEQ = 1;
  protected static final int ACIDSEQ = 2;
  protected static final int DATECREATED = 3;
  protected static final int CREATEDBY = 4;
  protected static final int DATEMODIFIED = 5;
  protected static final int MODIFIEDBY = 6;
  protected static final int ADMINISTEREDCOMPONENTS = 7;
  protected static final int CSCSI = 8;


  private static EntityDefImpl mDefinitionObject;

  /**
   * 
   * This is the default constructor (do not remove)
   */
  public AcCsiImpl()
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
      mDefinitionObject = (EntityDefImpl)EntityDefImpl.findDefObject("gov.nih.nci.ncicb.cadsr.persistence.bc4j.AcCsi");
    }
    return mDefinitionObject;
  }



  /**
   * 
   * Gets the attribute value for AcCsiIdseq, using the alias name AcCsiIdseq
   */
  public String getAcCsiIdseq()
  {
    return (String)getAttributeInternal(ACCSIIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for AcCsiIdseq
   */
  public void setAcCsiIdseq(String value)
  {
    setAttributeInternal(ACCSIIDSEQ, value);
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
   * Gets the attribute value for AcIdseq, using the alias name AcIdseq
   */
  public String getAcIdseq()
  {
    return (String)getAttributeInternal(ACIDSEQ);
  }

  /**
   * 
   * Sets <code>value</code> as the attribute value for AcIdseq
   */
  public void setAcIdseq(String value)
  {
    setAttributeInternal(ACIDSEQ, value);
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
      case ACCSIIDSEQ:
        return getAcCsiIdseq();
      case CSCSIIDSEQ:
        return getCsCsiIdseq();
      case ACIDSEQ:
        return getAcIdseq();
      case DATECREATED:
        return getDateCreated();
      case CREATEDBY:
        return getCreatedBy();
      case DATEMODIFIED:
        return getDateModified();
      case MODIFIEDBY:
        return getModifiedBy();
      case ADMINISTEREDCOMPONENTS:
        return getAdministeredComponents();
      case CSCSI:
        return getCsCsi();
      default:
        return super.getAttrInvokeAccessor(index, attrDef);
      }
  }
  //  Generated method. Do not modify.

  protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception
  {
    switch (index)
      {
      case ACCSIIDSEQ:
        setAcCsiIdseq((String)value);
        return;
      case CSCSIIDSEQ:
        setCsCsiIdseq((String)value);
        return;
      case ACIDSEQ:
        setAcIdseq((String)value);
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
   * Gets the associated entity AdministeredComponentsImpl
   */
  public AdministeredComponentsImpl getAdministeredComponents()
  {
    return (AdministeredComponentsImpl)getAttributeInternal(ADMINISTEREDCOMPONENTS);
  }

  /**
   * 
   * Sets <code>value</code> as the associated entity AdministeredComponentsImpl
   */
  public void setAdministeredComponents(AdministeredComponentsImpl value)
  {
    setAttributeInternal(ADMINISTEREDCOMPONENTS, value);
  }


  /**
   * 
   * Gets the associated entity CsCsiImpl
   */
  public CsCsiImpl getCsCsi()
  {
    return (CsCsiImpl)getAttributeInternal(CSCSI);
  }

  /**
   * 
   * Sets <code>value</code> as the associated entity CsCsiImpl
   */
  public void setCsCsi(CsCsiImpl value)
  {
    setAttributeInternal(CSCSI, value);
  }

  /**
   * 
   * Creates a Key object based on given key constituents
   */
  public static Key createPrimaryKey(String acCsiIdseq)
  {
    return new Key(new Object[] {acCsiIdseq});
  }


}