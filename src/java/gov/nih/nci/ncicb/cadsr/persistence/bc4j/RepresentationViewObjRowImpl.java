package gov.nih.nci.ncicb.cadsr.persistence.bc4j;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
//  ---------------------------------------------------------------------
//  ---    File generated by Oracle ADF Business Components Design Time.
//  ---    Custom code may be added to this class.
//  ---------------------------------------------------------------------

public class RepresentationViewObjRowImpl extends ViewRowImpl 
{


  public static final int REPIDSEQ = 0;
  public static final int PREFERREDNAME = 1;
  public static final int LONGNAME = 2;
  public static final int PREFERREDDEFINITION = 3;
  public static final int CONTEIDSEQ = 4;
  public static final int VERSION = 5;
  public static final int ASLNAME = 6;
  public static final int LATESTVERSIONIND = 7;
  public static final int CHANGENOTE = 8;
  public static final int BEGINDATE = 9;
  public static final int ENDDATE = 10;
  public static final int ORIGIN = 11;
  public static final int DEFINITIONSOURCE = 12;
  public static final int DATECREATED = 13;
  public static final int CREATEDBY = 14;
  public static final int DELETEDIND = 15;
  public static final int DATEMODIFIED = 16;
  public static final int MODIFIEDBY = 17;
  public static final int REPID = 18;
  public static final int CONDRIDSEQ = 19;
  public static final int VALUEDOMAINSVIEW = 20;
  /**
   * 
   *  This is the default constructor (do not remove)
   */
  public RepresentationViewObjRowImpl()
  {
  }

  /**
   * 
   *  Gets Representation entity object.
   */
  public RepresentationImpl getRepresentation()
  {
    return (RepresentationImpl)getEntity(0);
  }

  /**
   * 
   *  Gets the attribute value for REP_IDSEQ using the alias name RepIdseq
   */
  public String getRepIdseq()
  {
    return (String)getAttributeInternal(REPIDSEQ);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for REP_IDSEQ using the alias name RepIdseq
   */
  public void setRepIdseq(String value)
  {
    setAttributeInternal(REPIDSEQ, value);
  }

  /**
   * 
   *  Gets the attribute value for PREFERRED_NAME using the alias name PreferredName
   */
  public String getPreferredName()
  {
    return (String)getAttributeInternal(PREFERREDNAME);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for PREFERRED_NAME using the alias name PreferredName
   */
  public void setPreferredName(String value)
  {
    setAttributeInternal(PREFERREDNAME, value);
  }

  /**
   * 
   *  Gets the attribute value for LONG_NAME using the alias name LongName
   */
  public String getLongName()
  {
    return (String)getAttributeInternal(LONGNAME);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for LONG_NAME using the alias name LongName
   */
  public void setLongName(String value)
  {
    setAttributeInternal(LONGNAME, value);
  }

  /**
   * 
   *  Gets the attribute value for PREFERRED_DEFINITION using the alias name PreferredDefinition
   */
  public String getPreferredDefinition()
  {
    return (String)getAttributeInternal(PREFERREDDEFINITION);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for PREFERRED_DEFINITION using the alias name PreferredDefinition
   */
  public void setPreferredDefinition(String value)
  {
    setAttributeInternal(PREFERREDDEFINITION, value);
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

  /**
   * 
   *  Gets the attribute value for VERSION using the alias name Version
   */
  public Number getVersion()
  {
    return (Number)getAttributeInternal(VERSION);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for VERSION using the alias name Version
   */
  public void setVersion(Number value)
  {
    setAttributeInternal(VERSION, value);
  }

  /**
   * 
   *  Gets the attribute value for ASL_NAME using the alias name AslName
   */
  public String getAslName()
  {
    return (String)getAttributeInternal(ASLNAME);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for ASL_NAME using the alias name AslName
   */
  public void setAslName(String value)
  {
    setAttributeInternal(ASLNAME, value);
  }

  /**
   * 
   *  Gets the attribute value for LATEST_VERSION_IND using the alias name LatestVersionInd
   */
  public String getLatestVersionInd()
  {
    return (String)getAttributeInternal(LATESTVERSIONIND);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for LATEST_VERSION_IND using the alias name LatestVersionInd
   */
  public void setLatestVersionInd(String value)
  {
    setAttributeInternal(LATESTVERSIONIND, value);
  }

  /**
   * 
   *  Gets the attribute value for CHANGE_NOTE using the alias name ChangeNote
   */
  public String getChangeNote()
  {
    return (String)getAttributeInternal(CHANGENOTE);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for CHANGE_NOTE using the alias name ChangeNote
   */
  public void setChangeNote(String value)
  {
    setAttributeInternal(CHANGENOTE, value);
  }

  /**
   * 
   *  Gets the attribute value for BEGIN_DATE using the alias name BeginDate
   */
  public Date getBeginDate()
  {
    return (Date)getAttributeInternal(BEGINDATE);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for BEGIN_DATE using the alias name BeginDate
   */
  public void setBeginDate(Date value)
  {
    setAttributeInternal(BEGINDATE, value);
  }

  /**
   * 
   *  Gets the attribute value for END_DATE using the alias name EndDate
   */
  public Date getEndDate()
  {
    return (Date)getAttributeInternal(ENDDATE);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for END_DATE using the alias name EndDate
   */
  public void setEndDate(Date value)
  {
    setAttributeInternal(ENDDATE, value);
  }

  /**
   * 
   *  Gets the attribute value for ORIGIN using the alias name Origin
   */
  public String getOrigin()
  {
    return (String)getAttributeInternal(ORIGIN);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for ORIGIN using the alias name Origin
   */
  public void setOrigin(String value)
  {
    setAttributeInternal(ORIGIN, value);
  }

  /**
   * 
   *  Gets the attribute value for DEFINITION_SOURCE using the alias name DefinitionSource
   */
  public String getDefinitionSource()
  {
    return (String)getAttributeInternal(DEFINITIONSOURCE);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for DEFINITION_SOURCE using the alias name DefinitionSource
   */
  public void setDefinitionSource(String value)
  {
    setAttributeInternal(DEFINITIONSOURCE, value);
  }

  /**
   * 
   *  Gets the attribute value for DATE_CREATED using the alias name DateCreated
   */
  public Date getDateCreated()
  {
    return (Date)getAttributeInternal(DATECREATED);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for DATE_CREATED using the alias name DateCreated
   */
  public void setDateCreated(Date value)
  {
    setAttributeInternal(DATECREATED, value);
  }

  /**
   * 
   *  Gets the attribute value for CREATED_BY using the alias name CreatedBy
   */
  public String getCreatedBy()
  {
    return (String)getAttributeInternal(CREATEDBY);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for CREATED_BY using the alias name CreatedBy
   */
  public void setCreatedBy(String value)
  {
    setAttributeInternal(CREATEDBY, value);
  }

  /**
   * 
   *  Gets the attribute value for DELETED_IND using the alias name DeletedInd
   */
  public String getDeletedInd()
  {
    return (String)getAttributeInternal(DELETEDIND);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for DELETED_IND using the alias name DeletedInd
   */
  public void setDeletedInd(String value)
  {
    setAttributeInternal(DELETEDIND, value);
  }

  /**
   * 
   *  Gets the attribute value for DATE_MODIFIED using the alias name DateModified
   */
  public Date getDateModified()
  {
    return (Date)getAttributeInternal(DATEMODIFIED);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for DATE_MODIFIED using the alias name DateModified
   */
  public void setDateModified(Date value)
  {
    setAttributeInternal(DATEMODIFIED, value);
  }

  /**
   * 
   *  Gets the attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public String getModifiedBy()
  {
    return (String)getAttributeInternal(MODIFIEDBY);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for MODIFIED_BY using the alias name ModifiedBy
   */
  public void setModifiedBy(String value)
  {
    setAttributeInternal(MODIFIEDBY, value);
  }

  /**
   * 
   *  Gets the attribute value for REP_ID using the alias name RepId
   */
  public Number getRepId()
  {
    return (Number)getAttributeInternal(REPID);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for REP_ID using the alias name RepId
   */
  public void setRepId(Number value)
  {
    setAttributeInternal(REPID, value);
  }

  /**
   * 
   *  Gets the attribute value for CONDR_IDSEQ using the alias name CondrIdseq
   */
  public String getCondrIdseq()
  {
    return (String)getAttributeInternal(CONDRIDSEQ);
  }

  /**
   * 
   *  Sets <code>value</code> as attribute value for CONDR_IDSEQ using the alias name CondrIdseq
   */
  public void setCondrIdseq(String value)
  {
    setAttributeInternal(CONDRIDSEQ, value);
  }


  //  Generated method. Do not modify.

  protected Object getAttrInvokeAccessor(int index, AttributeDefImpl attrDef) throws Exception
  {
    switch (index)
      {
      case REPIDSEQ:
        return getRepIdseq();
      case PREFERREDNAME:
        return getPreferredName();
      case LONGNAME:
        return getLongName();
      case PREFERREDDEFINITION:
        return getPreferredDefinition();
      case CONTEIDSEQ:
        return getConteIdseq();
      case VERSION:
        return getVersion();
      case ASLNAME:
        return getAslName();
      case LATESTVERSIONIND:
        return getLatestVersionInd();
      case CHANGENOTE:
        return getChangeNote();
      case BEGINDATE:
        return getBeginDate();
      case ENDDATE:
        return getEndDate();
      case ORIGIN:
        return getOrigin();
      case DEFINITIONSOURCE:
        return getDefinitionSource();
      case DATECREATED:
        return getDateCreated();
      case CREATEDBY:
        return getCreatedBy();
      case DELETEDIND:
        return getDeletedInd();
      case DATEMODIFIED:
        return getDateModified();
      case MODIFIEDBY:
        return getModifiedBy();
      case REPID:
        return getRepId();
      case CONDRIDSEQ:
        return getCondrIdseq();
      case VALUEDOMAINSVIEW:
        return getValueDomainsView();
      default:
        return super.getAttrInvokeAccessor(index, attrDef);
      }
  }
  //  Generated method. Do not modify.

  protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception
  {
    switch (index)
      {
      case REPIDSEQ:
        setRepIdseq((String)value);
        return;
      case PREFERREDNAME:
        setPreferredName((String)value);
        return;
      case LONGNAME:
        setLongName((String)value);
        return;
      case PREFERREDDEFINITION:
        setPreferredDefinition((String)value);
        return;
      case CONTEIDSEQ:
        setConteIdseq((String)value);
        return;
      case VERSION:
        setVersion((Number)value);
        return;
      case ASLNAME:
        setAslName((String)value);
        return;
      case LATESTVERSIONIND:
        setLatestVersionInd((String)value);
        return;
      case CHANGENOTE:
        setChangeNote((String)value);
        return;
      case BEGINDATE:
        setBeginDate((Date)value);
        return;
      case ENDDATE:
        setEndDate((Date)value);
        return;
      case ORIGIN:
        setOrigin((String)value);
        return;
      case DEFINITIONSOURCE:
        setDefinitionSource((String)value);
        return;
      case DATECREATED:
        setDateCreated((Date)value);
        return;
      case CREATEDBY:
        setCreatedBy((String)value);
        return;
      case DELETEDIND:
        setDeletedInd((String)value);
        return;
      case DATEMODIFIED:
        setDateModified((Date)value);
        return;
      case MODIFIEDBY:
        setModifiedBy((String)value);
        return;
      case REPID:
        setRepId((Number)value);
        return;
      case CONDRIDSEQ:
        setCondrIdseq((String)value);
        return;
      default:
        super.setAttrInvokeAccessor(index, value, attrDef);
        return;
      }
  }

  /**
   * 
   *  Gets the associated <code>RowIterator</code> using master-detail link ValueDomainsView
   */
  public oracle.jbo.RowIterator getValueDomainsView()
  {
    return (oracle.jbo.RowIterator)getAttributeInternal(VALUEDOMAINSVIEW);
  }


}