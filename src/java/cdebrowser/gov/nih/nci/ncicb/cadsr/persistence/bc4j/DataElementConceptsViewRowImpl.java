package gov.nih.nci.ncicb.cadsr.persistence.bc4j;

import oracle.jbo.RowIterator;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;

import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;


//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------
public class DataElementConceptsViewRowImpl extends ViewRowImpl {
  protected static final int DECIDSEQ = 0;
  protected static final int VERSION = 1;
  protected static final int PREFERREDNAME = 2;
  protected static final int CONTEIDSEQ = 3;
  protected static final int CDIDSEQ = 4;
  protected static final int PROPLNAME = 5;
  protected static final int OCLNAME = 6;
  protected static final int PREFERREDDEFINITION = 7;
  protected static final int ASLNAME = 8;
  protected static final int LONGNAME = 9;
  protected static final int LATESTVERSIONIND = 10;
  protected static final int DELETEDIND = 11;
  protected static final int DATECREATED = 12;
  protected static final int BEGINDATE = 13;
  protected static final int CREATEDBY = 14;
  protected static final int ENDDATE = 15;
  protected static final int DATEMODIFIED = 16;
  protected static final int MODIFIEDBY = 17;
  protected static final int OBJCLASSQUALIFIER = 18;
  protected static final int PROPERTYQUALIFIER = 19;
  protected static final int CHANGENOTE = 20;
  protected static final int OCIDSEQ = 21;
  protected static final int PROPIDSEQ = 22;
  protected static final int ORIGIN = 23;
  protected static final int DECID = 24;
  protected static final int CONTEXTSROW = 25;
  protected static final int PROPERTIESEXTROW = 26;
  protected static final int OBJECTCLASSESEXTROW = 27;
  protected static final int CONCEPTUALDOMAINSROW = 28;
  protected static final int DATAELEMENTSROWS = 29;
  protected static final int OBJECTCLASSROW = 24;
  protected static final int DATAELEMENTSVIEW = 26;
  protected static final int CONTEXTSVIEWROW = 23;

  /**
   * This is the default constructor (do not remove)
   */
  public DataElementConceptsViewRowImpl() {
  }

  /**
   * Gets DataElementConcepts entity object.
   */
  public DataElementConceptsImpl getDataElementConcepts() {
    return (DataElementConceptsImpl) getEntity(0);
  }

  /**
   * Gets the attribute value for DEC_IDSEQ using the alias name DecIdseq
   */
  public String getDecIdseq() {
    return (String) getAttributeInternal(DECIDSEQ);
  }

  /**
   * Sets <code>value</code> as attribute value for DEC_IDSEQ using the alias
   * name DecIdseq
   */
  public void setDecIdseq(String value) {
    setAttributeInternal(DECIDSEQ, value);
  }

  /**
   * Gets the attribute value for VERSION using the alias name Version
   */
  public Number getVersion() {
    return (Number) getAttributeInternal(VERSION);
  }

  /**
   * Sets <code>value</code> as attribute value for VERSION using the alias
   * name Version
   */
  public void setVersion(Number value) {
    setAttributeInternal(VERSION, value);
  }

  /**
   * Gets the attribute value for PREFERRED_NAME using the alias name
   * PreferredName
   */
  public String getPreferredName() {
    return (String) getAttributeInternal(PREFERREDNAME);
  }

  /**
   * Sets <code>value</code> as attribute value for PREFERRED_NAME using the
   * alias name PreferredName
   */
  public void setPreferredName(String value) {
    setAttributeInternal(PREFERREDNAME, value);
  }

  /**
   * Gets the attribute value for CONTE_IDSEQ using the alias name ConteIdseq
   */
  public String getConteIdseq() {
    return (String) getAttributeInternal(CONTEIDSEQ);
  }

  /**
   * Sets <code>value</code> as attribute value for CONTE_IDSEQ using the alias
   * name ConteIdseq
   */
  public void setConteIdseq(String value) {
    setAttributeInternal(CONTEIDSEQ, value);
  }

  /**
   * Gets the attribute value for CD_IDSEQ using the alias name CdIdseq
   */
  public String getCdIdseq() {
    return (String) getAttributeInternal(CDIDSEQ);
  }

  /**
   * Sets <code>value</code> as attribute value for CD_IDSEQ using the alias
   * name CdIdseq
   */
  public void setCdIdseq(String value) {
    setAttributeInternal(CDIDSEQ, value);
  }

  /**
   * Gets the attribute value for PROPL_NAME using the alias name ProplName
   */
  public String getProplName() {
    return (String) getAttributeInternal(PROPLNAME);
  }

  /**
   * Sets <code>value</code> as attribute value for PROPL_NAME using the alias
   * name ProplName
   */
  public void setProplName(String value) {
    setAttributeInternal(PROPLNAME, value);
  }

  /**
   * Gets the attribute value for OCL_NAME using the alias name OclName
   */
  public String getOclName() {
    return (String) getAttributeInternal(OCLNAME);
  }

  /**
   * Sets <code>value</code> as attribute value for OCL_NAME using the alias
   * name OclName
   */
  public void setOclName(String value) {
    setAttributeInternal(OCLNAME, value);
  }

  /**
   * Gets the attribute value for PREFERRED_DEFINITION using the alias name
   * PreferredDefinition
   */
  public String getPreferredDefinition() {
    return (String) getAttributeInternal(PREFERREDDEFINITION);
  }

  /**
   * Sets <code>value</code> as attribute value for PREFERRED_DEFINITION using
   * the alias name PreferredDefinition
   */
  public void setPreferredDefinition(String value) {
    setAttributeInternal(PREFERREDDEFINITION, value);
  }

  /**
   * Gets the attribute value for ASL_NAME using the alias name AslName
   */
  public String getAslName() {
    return (String) getAttributeInternal(ASLNAME);
  }

  /**
   * Sets <code>value</code> as attribute value for ASL_NAME using the alias
   * name AslName
   */
  public void setAslName(String value) {
    setAttributeInternal(ASLNAME, value);
  }

  /**
   * Gets the attribute value for LONG_NAME using the alias name LongName
   */
  public String getLongName() {
    return (String) getAttributeInternal(LONGNAME);
  }

  /**
   * Sets <code>value</code> as attribute value for LONG_NAME using the alias
   * name LongName
   */
  public void setLongName(String value) {
    setAttributeInternal(LONGNAME, value);
  }

  /**
   * Gets the attribute value for LATEST_VERSION_IND using the alias name
   * LatestVersionInd
   */
  public String getLatestVersionInd() {
    return (String) getAttributeInternal(LATESTVERSIONIND);
  }

  /**
   * Sets <code>value</code> as attribute value for LATEST_VERSION_IND using
   * the alias name LatestVersionInd
   */
  public void setLatestVersionInd(String value) {
    setAttributeInternal(LATESTVERSIONIND, value);
  }

  /**
   * Gets the attribute value for DELETED_IND using the alias name DeletedInd
   */
  public String getDeletedInd() {
    return (String) getAttributeInternal(DELETEDIND);
  }

  /**
   * Sets <code>value</code> as attribute value for DELETED_IND using the alias
   * name DeletedInd
   */
  public void setDeletedInd(String value) {
    setAttributeInternal(DELETEDIND, value);
  }

  /**
   * Gets the attribute value for DATE_CREATED using the alias name DateCreated
   */
  public Date getDateCreated() {
    return (Date) getAttributeInternal(DATECREATED);
  }

  /**
   * Sets <code>value</code> as attribute value for DATE_CREATED using the
   * alias name DateCreated
   */
  public void setDateCreated(Date value) {
    setAttributeInternal(DATECREATED, value);
  }

  /**
   * Gets the attribute value for BEGIN_DATE using the alias name BeginDate
   */
  public Date getBeginDate() {
    return (Date) getAttributeInternal(BEGINDATE);
  }

  /**
   * Sets <code>value</code> as attribute value for BEGIN_DATE using the alias
   * name BeginDate
   */
  public void setBeginDate(Date value) {
    setAttributeInternal(BEGINDATE, value);
  }

  /**
   * Gets the attribute value for CREATED_BY using the alias name CreatedBy
   */
  public String getCreatedBy() {
    return (String) getAttributeInternal(CREATEDBY);
  }

  /**
   * Sets <code>value</code> as attribute value for CREATED_BY using the alias
   * name CreatedBy
   */
  public void setCreatedBy(String value) {
    setAttributeInternal(CREATEDBY, value);
  }

  /**
   * Gets the attribute value for END_DATE using the alias name EndDate
   */
  public Date getEndDate() {
    return (Date) getAttributeInternal(ENDDATE);
  }

  /**
   * Sets <code>value</code> as attribute value for END_DATE using the alias
   * name EndDate
   */
  public void setEndDate(Date value) {
    setAttributeInternal(ENDDATE, value);
  }

  /**
   * Gets the attribute value for DATE_MODIFIED using the alias name
   * DateModified
   */
  public Date getDateModified() {
    return (Date) getAttributeInternal(DATEMODIFIED);
  }

  /**
   * Sets <code>value</code> as attribute value for DATE_MODIFIED using the
   * alias name DateModified
   */
  public void setDateModified(Date value) {
    setAttributeInternal(DATEMODIFIED, value);
  }

  /**
   * Gets the attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public String getModifiedBy() {
    return (String) getAttributeInternal(MODIFIEDBY);
  }

  /**
   * Sets <code>value</code> as attribute value for MODIFIED_BY using the alias
   * name ModifiedBy
   */
  public void setModifiedBy(String value) {
    setAttributeInternal(MODIFIEDBY, value);
  }

  /**
   * Gets the attribute value for OBJ_CLASS_QUALIFIER using the alias name
   * ObjClassQualifier
   */
  public String getObjClassQualifier() {
    return (String) getAttributeInternal(OBJCLASSQUALIFIER);
  }

  /**
   * Sets <code>value</code> as attribute value for OBJ_CLASS_QUALIFIER using
   * the alias name ObjClassQualifier
   */
  public void setObjClassQualifier(String value) {
    setAttributeInternal(OBJCLASSQUALIFIER, value);
  }

  /**
   * Gets the attribute value for PROPERTY_QUALIFIER using the alias name
   * PropertyQualifier
   */
  public String getPropertyQualifier() {
    return (String) getAttributeInternal(PROPERTYQUALIFIER);
  }

  /**
   * Sets <code>value</code> as attribute value for PROPERTY_QUALIFIER using
   * the alias name PropertyQualifier
   */
  public void setPropertyQualifier(String value) {
    setAttributeInternal(PROPERTYQUALIFIER, value);
  }

  /**
   * Gets the attribute value for CHANGE_NOTE using the alias name ChangeNote
   */
  public String getChangeNote() {
    return (String) getAttributeInternal(CHANGENOTE);
  }

  /**
   * Sets <code>value</code> as attribute value for CHANGE_NOTE using the alias
   * name ChangeNote
   */
  public void setChangeNote(String value) {
    setAttributeInternal(CHANGENOTE, value);
  }

  /**
   * Gets the associated <code>RowIterator</code> using master-detail link
   * DataElementsView
   */
  public RowIterator getDataElementsView() {
    return (RowIterator) getAttributeInternal(DATAELEMENTSVIEW);
  }

  //  Generated method. Do not modify.
  protected Object getAttrInvokeAccessor(
    int index,
    AttributeDefImpl attrDef) throws Exception {
    switch (index) {
    case DECIDSEQ:
      return getDecIdseq();

    case VERSION:
      return getVersion();

    case PREFERREDNAME:
      return getPreferredName();

    case CONTEIDSEQ:
      return getConteIdseq();

    case CDIDSEQ:
      return getCdIdseq();

    case PROPLNAME:
      return getProplName();

    case OCLNAME:
      return getOclName();

    case PREFERREDDEFINITION:
      return getPreferredDefinition();

    case ASLNAME:
      return getAslName();

    case LONGNAME:
      return getLongName();

    case LATESTVERSIONIND:
      return getLatestVersionInd();

    case DELETEDIND:
      return getDeletedInd();

    case DATECREATED:
      return getDateCreated();

    case BEGINDATE:
      return getBeginDate();

    case CREATEDBY:
      return getCreatedBy();

    case ENDDATE:
      return getEndDate();

    case DATEMODIFIED:
      return getDateModified();

    case MODIFIEDBY:
      return getModifiedBy();

    case OBJCLASSQUALIFIER:
      return getObjClassQualifier();

    case PROPERTYQUALIFIER:
      return getPropertyQualifier();

    case CHANGENOTE:
      return getChangeNote();

    case OCIDSEQ:
      return getOcIdseq();

    case PROPIDSEQ:
      return getPropIdseq();

    case ORIGIN:
      return getOrigin();

    case DECID:
      return getDecId();

    case DATAELEMENTSROWS:
      return getDataElementsRows();

    case CONTEXTSROW:
      return getContextsRow();

    case PROPERTIESEXTROW:
      return getPropertiesExtRow();

    case OBJECTCLASSESEXTROW:
      return getObjectClassesExtRow();

    case CONCEPTUALDOMAINSROW:
      return getConceptualDomainsRow();

    default:
      return super.getAttrInvokeAccessor(index, attrDef);
    }
  }

  //  Generated method. Do not modify.
  protected void setAttrInvokeAccessor(
    int index,
    Object value,
    AttributeDefImpl attrDef) throws Exception {
    switch (index) {
    case DECIDSEQ:
      setDecIdseq((String) value);

      return;

    case VERSION:
      setVersion((Number) value);

      return;

    case PREFERREDNAME:
      setPreferredName((String) value);

      return;

    case CONTEIDSEQ:
      setConteIdseq((String) value);

      return;

    case CDIDSEQ:
      setCdIdseq((String) value);

      return;

    case PROPLNAME:
      setProplName((String) value);

      return;

    case OCLNAME:
      setOclName((String) value);

      return;

    case PREFERREDDEFINITION:
      setPreferredDefinition((String) value);

      return;

    case ASLNAME:
      setAslName((String) value);

      return;

    case LONGNAME:
      setLongName((String) value);

      return;

    case LATESTVERSIONIND:
      setLatestVersionInd((String) value);

      return;

    case DELETEDIND:
      setDeletedInd((String) value);

      return;

    case DATECREATED:
      setDateCreated((Date) value);

      return;

    case BEGINDATE:
      setBeginDate((Date) value);

      return;

    case CREATEDBY:
      setCreatedBy((String) value);

      return;

    case ENDDATE:
      setEndDate((Date) value);

      return;

    case DATEMODIFIED:
      setDateModified((Date) value);

      return;

    case MODIFIEDBY:
      setModifiedBy((String) value);

      return;

    case OBJCLASSQUALIFIER:
      setObjClassQualifier((String) value);

      return;

    case PROPERTYQUALIFIER:
      setPropertyQualifier((String) value);

      return;

    case CHANGENOTE:
      setChangeNote((String) value);

      return;

    case OCIDSEQ:
      setOcIdseq((String) value);

      return;

    case PROPIDSEQ:
      setPropIdseq((String) value);

      return;

    case ORIGIN:
      setOrigin((String) value);

      return;

    case DECID:
      setDecId((Number) value);

      return;

    default:
      super.setAttrInvokeAccessor(index, value, attrDef);

      return;
    }
  }

  /**
   * Gets the attribute value for OC_IDSEQ using the alias name OcIdseq
   */
  public String getOcIdseq() {
    return (String) getAttributeInternal(OCIDSEQ);
  }

  /**
   * Sets <code>value</code> as attribute value for OC_IDSEQ using the alias
   * name OcIdseq
   */
  public void setOcIdseq(String value) {
    setAttributeInternal(OCIDSEQ, value);
  }

  /**
   * Gets the attribute value for PROP_IDSEQ using the alias name PropIdseq
   */
  public String getPropIdseq() {
    return (String) getAttributeInternal(PROPIDSEQ);
  }

  /**
   * Sets <code>value</code> as attribute value for PROP_IDSEQ using the alias
   * name PropIdseq
   */
  public void setPropIdseq(String value) {
    setAttributeInternal(PROPIDSEQ, value);
  }

  /**
   * Gets the associated <code>Row</code> using master-detail link
   * ObjectClassRow
   */
  public oracle.jbo.Row getObjectClassRow() {
    return (oracle.jbo.Row) getAttributeInternal(OBJECTCLASSROW);
  }

  /**
   * Gets the associated <code>Row</code> using master-detail link
   * PropertiesExtRow
   */
  public oracle.jbo.Row getPropertiesExtRow() {
    return (oracle.jbo.Row) getAttributeInternal(PROPERTIESEXTROW);
  }

  /**
   * Gets the associated <code>Row</code> using master-detail link
   * ContextsViewRow
   */
  public oracle.jbo.Row getContextsViewRow() {
    return (oracle.jbo.Row) getAttributeInternal(CONTEXTSVIEWROW);
  }

  /**
   * Gets the associated <code>Row</code> using master-detail link ContextsRow
   */
  public oracle.jbo.Row getContextsRow() {
    return (oracle.jbo.Row) getAttributeInternal(CONTEXTSROW);
  }

  //Custom Methods

  /**
   * Gets the Context Name
   */
  public String getContextName() {
    return (String) getContextsRow().getAttribute("Name");
  }

  /**
   * Gets the associated Object Class Preferred Name
   */
  public String getObjectClassPrefName() {
    if (getOcIdseq() != null) {
      return (String) getObjectClassesExtRow().getAttribute("PreferredName");
    }
    else {
      return "";
    }
  }

  /**
   * Gets the associated Object Class Context Name
   */
  public String getObjectClassContext() {
    if (getOcIdseq() != null) {
      return ((ObjectClassesExtViewRowImpl) getObjectClassesExtRow()).getContextName();
    }
    else {
      return "";
    }
  }

  /**
   * Gets the associated Object Class Version
   */
  public Number getObjectClassVersion() {
    if (getOcIdseq() != null) {
      return (Number) getObjectClassesExtRow().getAttribute("Version");
    }
    else {
      return null;
    }
  }

  /**
   * Gets the associated Object Class OcId
   */
  public Number getObjectClassPublicId() {
    if (getOcIdseq() != null) {
      return (Number) getObjectClassesExtRow().getAttribute("OcId");
    }
    else {
      return null;
    }
  }

  /**
   * Gets the associated Property Preferred Name
   */
  public String getPropertyPrefName() {
    if (getPropIdseq() != null) {
      return (String) getPropertiesExtRow().getAttribute("PreferredName");
    }
    else {
      return "";
    }
  }

  /**
   * Gets the associated Property Context Name
   */
  public String getPropertyContextName() {
    if (getPropIdseq() != null) {
      return ((PropertiesExtViewRowImpl) getPropertiesExtRow()).getContextName();
    }
    else {
      return "";
    }
  }

  /**
   * Gets the associated Property Version
   */
  public Number getPropertyVersion() {
    if (getPropIdseq() != null) {
      return (Number) getPropertiesExtRow().getAttribute("Version");
    }
    else {
      return null;
    }
  }

  /**
   * Gets the associated Property Version
   */
  public Number getPropertyPublicId() {
    if (getPropIdseq() != null) {
      return (Number) getPropertiesExtRow().getAttribute("PropId");
    }
    else {
      return null;
    }
  }

  /**
   * Gets the associated Conceptual Domain Preferred Name
   */
  public String getCDPrefName() {
    return (String) getConceptualDomainsRow().getAttribute("PreferredName");
  }

  /**
   * Gets the associated Conceptual Domain Version
   */
  public Number getCDVersion() {
    return (Number) getConceptualDomainsRow().getAttribute("Version");
  }

  /**
   * Gets the associated Conceptual Domain Public Id
   */
  public Number getCDPublicId() {
    return (Number) getConceptualDomainsRow().getAttribute("CdId");
  }

  /**
   * Gets the associated Conceptual Domain Context Name
   */
  public String getCDContextName() {
    return ((ConceptualDomainsViewRowImpl) getConceptualDomainsRow()).getContextName();
  }

  /**
   * Gets the associated <code>RowIterator</code> using master-detail link
   * DataElementsRows
   */
  public RowIterator getDataElementsRows() {
    return (RowIterator) getAttributeInternal(DATAELEMENTSROWS);
  }

  /**
   * Gets the associated <code>Row</code> using master-detail link
   * ObjectClassesExtRow
   */
  public oracle.jbo.Row getObjectClassesExtRow() {
    return (oracle.jbo.Row) getAttributeInternal(OBJECTCLASSESEXTROW);
  }

  /**
   * Gets the associated <code>Row</code> using master-detail link
   * ConceptualDomainsRow
   */
  public oracle.jbo.Row getConceptualDomainsRow() {
    return (oracle.jbo.Row) getAttributeInternal(CONCEPTUALDOMAINSROW);
  }

  /**
   * Gets the attribute value for ORIGIN using the alias name Origin
   */
  public String getOrigin() {
    return (String) getAttributeInternal(ORIGIN);
  }

  /**
   * Sets <code>value</code> as attribute value for ORIGIN using the alias name
   * Origin
   */
  public void setOrigin(String value) {
    setAttributeInternal(ORIGIN, value);
  }

  /**
   * Gets the attribute value for DEC_ID using the alias name DecId
   */
  public Number getDecId() {
    return (Number) getAttributeInternal(DECID);
  }

  /**
   * Sets <code>value</code> as attribute value for DEC_ID using the alias name
   * DecId
   */
  public void setDecId(Number value) {
    setAttributeInternal(DECID, value);
  }
}
