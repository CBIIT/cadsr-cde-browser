package gov.nih.nci.ncicb.cadsr.persistence.bc4j;

import gov.nih.nci.ncicb.cadsr.cdebrowser.userexception.*;
import gov.nih.nci.ncicb.cadsr.dto.ComponentConceptTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.ConceptDerivationRuleTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.ConceptTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.ValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.bc4j.BC4JClassificationsTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.bc4j.BC4JDataElementFormUsageTO;
import gov.nih.nci.ncicb.cadsr.dto.bc4j.BC4JDataElementTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.bc4j.BC4JReferenceBlobTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.bc4j.BC4JValueDomainTransferObject;
import gov.nih.nci.ncicb.cadsr.resource.*;
import gov.nih.nci.ncicb.cadsr.util.BC4JPageIterator;
import gov.nih.nci.ncicb.cadsr.util.PageIterator;
import gov.nih.nci.ncicb.cadsr.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.util.logging.LogFactory;

import oracle.clex.persistence.bc4j.*;

import oracle.jbo.*;
import oracle.jbo.ViewObject;

import oracle.jbo.server.*;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewLinkImpl;

import java.util.*;


//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------
public class CDEBrowserBc4jModuleImpl extends ApplicationModuleImpl {
  private static Log log =
    LogFactory.getLog(CDEBrowserBc4jModuleImpl.class.getName());

  /**
   * This is the default constructor (do not remove)
   */
  public CDEBrowserBc4jModuleImpl() {
  }

  /**
   * Container's getter for ContextsView
   */
  public ContextsViewImpl getContextsView() {
    return (ContextsViewImpl) findViewObject("ContextsView");
  }

  /**
   * Container's getter for DataElementsView
   */
  public DataElementsViewImpl getDataElementsView() {
    return (DataElementsViewImpl) findViewObject("DataElementsView");
  }

  /**
   * Custom Method: Dataelement object for a search
   */
  public BC4JDataElementTransferObject getDataElements(Object key)
    throws Exception {
    BC4JDataElementTransferObject dataElementsValueObject = null;
    DataElementsViewRowImpl dataElementsViewRowImpl = null;

    try {
      dataElementsViewRowImpl = getDataElementRow(key);
      dataElementsValueObject =
        new BC4JDataElementTransferObject(dataElementsViewRowImpl);

      return dataElementsValueObject;
    }

    //end try
    catch (Exception e) {
      throw e;
    }

    //end catch
  }

  //end method

  /**
   * Custom Method: Gets a data element row
   */
  private DataElementsViewRowImpl getDataElementRow(Object key)
    throws Exception {
    DataElementsViewImpl view = null;
    DataElementsViewRowImpl dataElementsViewRowImpl = null;
    BC4JUtil bc4jUtil = new BC4JUtil();

    try {
      view = getDataElementsView();

      ViewObjectImpl view1 =
        (DataElementsViewImpl) bc4jUtil.cloneViewObject(view);
      view1.setWhereClause(" DE_IDSEQ = '" + key + "'");
      view1.executeQuery();

      //System.out.println(view1.getQuery());
      dataElementsViewRowImpl =
        (DataElementsViewRowImpl) bc4jUtil.getRow(view1); //view1 is row iterator

      return dataElementsViewRowImpl;
    }
    catch (Exception e) {
      throw e;
    }
  }

  /**
   * Custom Method: Valid Values
   */
  public Vector getValidValues(Object aVdIdseq) throws Exception {
    BC4JUtil bc4jUtil = new BC4JUtil();
    Vector rows = new Vector();

    try {
      ViewObject view = getValidValuesView();
      ViewObjectImpl view1 =
        (ValidValuesViewImpl) bc4jUtil.cloneViewObject(view);
      view1.setWhereClause(" VD_IDSEQ = '" + aVdIdseq + "'");
      view1.executeQuery();
      rows = new Vector(view1.getRowCount());

      while (view1.hasNext()) {
        /*rows.addElement(
           new ValidValuesValueObject((ValidValuesViewRowImpl) view1.next()));*/
        ValidValuesViewRowImpl vvImpl = (ValidValuesViewRowImpl) view1.next();
        ValidValue vv = new ValidValueTransferObject();
        vv.setVdIdseq(vvImpl.getVdIdseq());
        vv.setDescription(vvImpl.getDescription());
        vv.setShortMeaning(vvImpl.getShortMeaning());
        vv.setShortMeaningDescription(vvImpl.getMeaningDescription());
        vv.setShortMeaningValue(vvImpl.getValue());
        vv.setVpIdseq(vvImpl.getVpIdseq());

        String cdrIdseq = vvImpl.getCondrIdseq();

        if (cdrIdseq != null) {
          ConceptDerivationRule rule =
            new ConceptDerivationRuleTransferObject();
          rule.setIdseq(cdrIdseq);
          vv.setConceptDerivationRule(rule);
        }

        rows.add(vv);
      }
    }
    catch (Exception e) {
      throw e;
    }

    return rows;
  }

  /**
   * Custom Method: Valid Values Author: Ram Chilukuri
   */
  public ViewObject getMyValidValuesView(Object aVdIdseq)
    throws Exception {
    ViewObject view = null;
    BC4JUtil bc4jUtil = new BC4JUtil();

    try {
      view = getValidValuesView();

      //view = (VDValidValuesViewImpl)bc4jUtil.cloneViewObject(view);
      view.setWhereClause(" VD_IDSEQ = '" + aVdIdseq + "'");
      view.executeQuery();
    }
    catch (Exception e) {
      throw e;
    }

    return view;
  }



  /**
   * Custom Method: Classification Schemes
   */
  public Vector getClassificationSchemes(Object aDeIdseq)
    throws Exception {
    BC4JUtil bc4jUtil = new BC4JUtil();
    Vector rows = new Vector();

    try {
      ViewObject view = getClassificationsView();
      ViewObjectImpl view1 =
        (ClassificationsViewImpl) bc4jUtil.cloneViewObject(view);
      view1.setWhereClause(" AC_IDSEQ = '" + aDeIdseq + "'");
      view1.executeQuery();
      rows = new Vector(view1.getRowCount());

      while (view1.hasNext()) {
        rows.addElement(
          new BC4JClassificationsTransferObject(
            (ClassificationsViewRowImpl) view1.next()));
      }
    }
    catch (Exception e) {
      throw e;
    }

    return rows;
  }

  /**
   * Custom Method: Classification Schemes Author: Ram Chilukuri
   */
  public Vector getClassificationSchemes(
    Object aDeIdseq,
    PageIterator pgIter) throws Exception {
    BC4JUtil bc4jUtil = new BC4JUtil();
    Vector rows = new Vector();
    Row[] queryResults;

    try {
      ViewObject view = getClassificationsView();
      ViewObjectImpl view1 =
        (ClassificationsViewImpl) bc4jUtil.cloneViewObject(view);
      view1.setWhereClause(" AC_IDSEQ = '" + aDeIdseq + "'");
      view1.executeQuery();
      pgIter.setScrollableObject(view1);
      queryResults = (Row[]) pgIter.getRowsInRange();
      rows = new Vector(queryResults.length);

      for (int i = 0; i < queryResults.length; i++) {
        rows.addElement(
          new BC4JClassificationsTransferObject(
            (ClassificationsViewRowImpl) queryResults[i]));
      }
    }
    catch (Exception e) {
      throw e;
    }

    return rows;
  }

  public List getFormUsagesForADataElement(
    Object aDeIdseq,
    PageIterator pgIter) throws Exception {
    List results = new ArrayList(29);
    Row[] queryResults;
    FormUsagesForACdeViewRowImpl usageRow;

    ViewObject view = this.getFormUsagesForACdeView();
    view.setWhereClause("DE_IDSEQ = '" + aDeIdseq + "'");
    view.executeQuery();
    pgIter.setScrollableObject(view);
    queryResults = (Row[]) pgIter.getRowsInRange();

    for (int i = 0; i < queryResults.length; i++) {
      usageRow = (FormUsagesForACdeViewRowImpl) queryResults[i];
      results.add(new BC4JDataElementFormUsageTO(usageRow));
    }

    return results;
  }

  public BC4JValueDomainTransferObject getValueDomains(Object key)
    throws Exception {
    BC4JValueDomainTransferObject valueDomainValueObject = null;
    ValueDomainsViewRowImpl valueDomainsViewRowImpl = null;

    try {
      valueDomainsViewRowImpl = getValueDomainRow(key);
      valueDomainValueObject =
        new BC4JValueDomainTransferObject(valueDomainsViewRowImpl);

      return valueDomainValueObject;
    }
    catch (Exception e) {
      throw e;
    }
  }

  private ValueDomainsViewRowImpl getValueDomainRow(Object key)
    throws Exception {
    ValueDomainsViewImpl view = null;
    ValueDomainsViewRowImpl valueDomainsViewRowImpl = null;
    BC4JUtil bc4jUtil = new BC4JUtil();

    try {
      view = getValueDomainsView();

      ViewObjectImpl view1 =
        (ValueDomainsViewImpl) bc4jUtil.cloneViewObject(view);
      view1.setWhereClause(" VD_IDSEQ = '" + key + "'");
      view1.executeQuery();
      valueDomainsViewRowImpl =
        (ValueDomainsViewRowImpl) bc4jUtil.getRow(view1); //view1 is row iterator

      return valueDomainsViewRowImpl;
    }
    catch (Exception e) {
      throw e;
    }
  }

  private AdministeredComponentsViewRowImpl getAdminComponentRow(Object key)
    throws Exception {
    AdministeredComponentsViewImpl view = null;
    AdministeredComponentsViewRowImpl acViewRowImpl = null;
    BC4JUtil bc4jUtil = new BC4JUtil();

    try {
      view = getAdministeredComponentsView();

      ViewObjectImpl view1 =
        (AdministeredComponentsViewImpl) bc4jUtil.cloneViewObject(view);
      view1.setWhereClause(" AC_IDSEQ = '" + key + "'");
      view1.executeQuery();
      acViewRowImpl =
        (AdministeredComponentsViewRowImpl) bc4jUtil.getRow(view1); //view1 is row iterator

      return acViewRowImpl;
    }
    catch (Exception e) {
      throw e;
    }
  }

  public RowIterator getReferenceDocsForAdminComponent(
    AdministeredComponentsViewRowImpl acViewRowImpl) {
    RowIterator refDocs = acViewRowImpl.getReferenceDocumentsView();

    return refDocs;
  }

  public RowIterator getReferenceBlobsForRefDoc(
    ReferenceDocumentsViewRowImpl rdViewRowImpl) {
    RowIterator refBlobs = rdViewRowImpl.getReferenceBlobsView();

    return refBlobs;
  }

  public BC4JReferenceBlobTransferObject getRefBlobsForAdminComponent(
    Object acIdseq,
    String docType) throws DocumentNotFoundException, Exception {
    try {
      ReferenceDocumentsViewImpl rdVO = getReferenceDocumentsView();
      String where =
        "DCTL_NAME = '" + docType + "' AND AC_IDSEQ = '" + acIdseq + "'";
      rdVO.setWhereClause(where);
      rdVO.executeQuery();

      ReferenceDocumentsViewRowImpl rdViewRowImpl;
      rdViewRowImpl = (ReferenceDocumentsViewRowImpl) rdVO.first();

      if (rdViewRowImpl == null) {
        throw new DocumentNotFoundException("Document not found");
      }

      String rdIdseq = rdViewRowImpl.getRdIdseq();

      ReferenceBlobsViewImpl rbVO = getReferenceBlobsView();
      rbVO.setWhereClause("RD_IDSEQ = '" + rdIdseq + "'");
      rbVO.executeQuery();

      ReferenceBlobsViewRowImpl rbViewRowImpl;
      rbViewRowImpl = (ReferenceBlobsViewRowImpl) rbVO.first();

      if (rbViewRowImpl == null) {
        throw new DocumentNotFoundException("Document not found");
      }

      return new BC4JReferenceBlobTransferObject(rbViewRowImpl);
    }
    catch (DocumentNotFoundException ex) {
      log.error("Exception caught", ex);
      throw ex;
    }
    catch (Exception ex) {
      log.error("Exception caught", ex);
      throw ex;
    }
    finally {
    }
  }

  public BC4JReferenceBlobTransferObject getRefBlobsForAdminComponent(
    Object refDocIdseq) throws Exception {
    try {
      ReferenceBlobsViewImpl rbVO = getReferenceBlobsView();
      rbVO.setWhereClause("RD_IDSEQ = '" + refDocIdseq + "'");
      rbVO.executeQuery();

      ReferenceBlobsViewRowImpl rbViewRowImpl;
      rbViewRowImpl = (ReferenceBlobsViewRowImpl) rbVO.first();

      return new BC4JReferenceBlobTransferObject(rbViewRowImpl);
    }
    catch (Exception ex) {
      log.error("Exception caught", ex);
      throw ex;
    }
    finally {
    }
  }

  public PageContextValueObject getPageContextInfo(
    String nodeType,
    String nodeIdseq) throws Exception {
    ContextsViewRowImpl conteRow = null;
    ClassificationSchemesViewRowImpl classSchemeRow = null;
    ClassSchemeItemsViewRowImpl classSchemeItemRow = null;
    CsCsiViewRowImpl csCsiRow = null;
    QuestContentsExtViewRowImpl qcRow = null;
    ProtocolsViewRowImpl protoRow = null;
    PageContextValueObject pc = null;
    BC4JUtil bc4jUtil = new BC4JUtil();
    Hashtable pageContextHT = new Hashtable(10);

    try {
      if (nodeType.equals("CONTEXT")) {
        ContextsViewImpl conteView = getContextsView();
        ContextsViewImpl conteView1 =
          (ContextsViewImpl) bc4jUtil.cloneViewObject(conteView);
        conteView1.setWhereClause("CONTE_IDSEQ = '" + nodeIdseq + "'");
        conteView1.executeQuery();
        conteRow = (ContextsViewRowImpl) bc4jUtil.getRow(conteView1);
        pageContextHT.put("ParamType", "CONTEXT");
        pageContextHT.put("ContextName", conteRow.getName());
        pageContextHT.put("ConteIdseq", nodeIdseq);
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("CLASSIFICATION")) {
        ClassificationSchemesViewImpl csView = getClassificationSchemesView();
        ClassificationSchemesViewImpl csView1 =
          (ClassificationSchemesViewImpl) bc4jUtil.cloneViewObject(csView);
        csView1.setWhereClause("CS_IDSEQ = '" + nodeIdseq + "'");
        classSchemeRow =
          (ClassificationSchemesViewRowImpl) bc4jUtil.getRow(csView1);
        pageContextHT.put("ParamType", "CLASSIFICATION");
        pageContextHT.put("ContextName", classSchemeRow.getContextName());

        //pageContextHT.put("ClassSchemeName",classSchemeRow.getPreferredName());
        pageContextHT.put("ClassSchemeName", classSchemeRow.getLongName());
        pageContextHT.put("ConteIdseq", classSchemeRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("CSI")) {
        CsCsiViewImpl csCsiView = getCsCsiView();
        CsCsiViewImpl csCsiView1 =
          (CsCsiViewImpl) bc4jUtil.cloneViewObject(csCsiView);
        csCsiView1.setWhereClause("CS_CSI_IDSEQ= '" + nodeIdseq + "'");
        csCsiRow = (CsCsiViewRowImpl) bc4jUtil.getRow(csCsiView1);
        classSchemeRow =
          (ClassificationSchemesViewRowImpl) csCsiRow.getClassificationSchemesView();
        classSchemeItemRow =
          (ClassSchemeItemsViewRowImpl) csCsiRow.getClassSchemeItemsView();
        pageContextHT.put("ParamType", "CSI");
        pageContextHT.put("ContextName", classSchemeRow.getContextName());

        //pageContextHT.put("ClassSchemeName",classSchemeRow.getPreferredName());
        pageContextHT.put("ClassSchemeName", classSchemeRow.getLongName());
        pageContextHT.put(
          "ClassSchemeItemName", classSchemeItemRow.getCsiName());
        pageContextHT.put("ConteIdseq", classSchemeRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("TEMPLATE")) {
        QuestContentsExtViewImpl qcView = getQuestContentsExtView();
        QuestContentsExtViewImpl qcView1 =
          (QuestContentsExtViewImpl) bc4jUtil.cloneViewObject(qcView);
        qcView1.setWhereClause("QC_IDSEQ= '" + nodeIdseq + "'");
        qcRow = (QuestContentsExtViewRowImpl) bc4jUtil.getRow(qcView1);
        pageContextHT.put("ParamType", "TEMPLATE");
        pageContextHT.put("ContextName", qcRow.getContextName());
        pageContextHT.put("CRFName", qcRow.getLongName());
        pageContextHT.put("CRFType", qcRow.getQcdlName());
        pageContextHT.put("ConteIdseq", qcRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("CRF")) {
        QuestContentsExtViewImpl qcView = getQuestContentsExtView();
        qcView.setWhereClause("QC_IDSEQ= '" + nodeIdseq + "'");
        qcRow = (QuestContentsExtViewRowImpl) bc4jUtil.getRow(qcView);
        protoRow = (ProtocolsViewRowImpl) qcRow.getProtocol();
        pageContextHT.put("ParamType", "CRF");
        pageContextHT.put("ContextName", qcRow.getContextName());
        pageContextHT.put("CRFName", qcRow.getLongName());
        //Publish Change Request
        if(protoRow!=null)
          pageContextHT.put("ProtocolName", protoRow.getLongName());
          
        pageContextHT.put("ConteIdseq", qcRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("PROTOCOL")) {
        ProtocolsViewImpl protoView = getProtocolsView();
        protoView.setWhereClause("PROTO_IDSEQ='" + nodeIdseq + "'");
        protoRow = (ProtocolsViewRowImpl) bc4jUtil.getRow(protoView);
        pageContextHT.put("ParamType", "PROTOCOL");
        pageContextHT.put(
          "ContextName", protoRow.getContext().getAttribute("Name"));
        pageContextHT.put("ProtocolName", protoRow.getLongName());
        pageContextHT.put("ConteIdseq", protoRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("PUBLISHING_PROTOCOL")) {
        ProtocolsViewImpl protoView = getProtocolsView();
        protoView.setWhereClause("PROTO_IDSEQ='" + nodeIdseq + "'");
        protoRow = (ProtocolsViewRowImpl) bc4jUtil.getRow(protoView);
        pageContextHT.put("ParamType", "PUBLISHING_PROTOCOL");
        pageContextHT.put(
          "ContextName", protoRow.getContext().getAttribute("Name"));
        pageContextHT.put("ProtocolName", protoRow.getLongName());
        pageContextHT.put("ConteIdseq", protoRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }        
      else if (nodeType.equals("CORE")) {
        CsCsiViewImpl csCsiView = getCsCsiView();
        CsCsiViewImpl csCsiView1 =
          (CsCsiViewImpl) bc4jUtil.cloneViewObject(csCsiView);
        csCsiView1.setWhereClause("CS_CSI_IDSEQ= '" + nodeIdseq + "'");
        csCsiRow = (CsCsiViewRowImpl) bc4jUtil.getRow(csCsiView1);
        classSchemeRow =
          (ClassificationSchemesViewRowImpl) csCsiRow.getClassificationSchemesView();
        classSchemeItemRow =
          (ClassSchemeItemsViewRowImpl) csCsiRow.getClassSchemeItemsView();
        pageContextHT.put("ParamType", "CORE");
        pageContextHT.put("ContextName", classSchemeRow.getContextName());
        pageContextHT.put("ClassSchemeName", classSchemeRow.getPreferredName());
        pageContextHT.put(
          "ClassSchemeItemName", classSchemeItemRow.getCsiName());
        pageContextHT.put("ConteIdseq", classSchemeRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("NON-CORE")) {
        CsCsiViewImpl csCsiView = getCsCsiView();
        CsCsiViewImpl csCsiView1 =
          (CsCsiViewImpl) bc4jUtil.cloneViewObject(csCsiView);
        csCsiView1.setWhereClause("CS_CSI_IDSEQ= '" + nodeIdseq + "'");
        csCsiRow = (CsCsiViewRowImpl) bc4jUtil.getRow(csCsiView1);
        classSchemeRow =
          (ClassificationSchemesViewRowImpl) csCsiRow.getClassificationSchemesView();
        classSchemeItemRow =
          (ClassSchemeItemsViewRowImpl) csCsiRow.getClassSchemeItemsView();
        pageContextHT.put("ParamType", "NON-CORE");
        pageContextHT.put("ContextName", classSchemeRow.getContextName());
        pageContextHT.put("ClassSchemeName", classSchemeRow.getPreferredName());
        pageContextHT.put(
          "ClassSchemeItemName", classSchemeItemRow.getCsiName());
        pageContextHT.put("ConteIdseq", classSchemeRow.getConteIdseq());
        pc = new PageContextValueObject(pageContextHT);
      }
      else if (nodeType.equals("P_PARAM_TYPE")) {
        pageContextHT.put("ParamType", "P_PARAM_TYPE");
        pageContextHT.put("ContextName", "");
        pageContextHT.put("ClassSchemeName", "");
        pageContextHT.put("ClassSchemeItemName", "");
        pageContextHT.put("ConteIdseq", "");
        pc = new PageContextValueObject(pageContextHT);
      }
    }
    catch (Exception ex) {
      log.error("Exception caught", ex);
      throw ex;
    }

    return pc;
  }

  public DataElement findDataElementByCdeId(
    int cdeId,
    float version) throws DataElementNotFoundException, Exception {
    DataElementsViewRowImpl deRow = null;
    DataElement de = null;
    String deIdseq = null;

    ViewObject vw = this.getDataElementsView();
    vw.setWhereClause(" CDE_ID= " + cdeId + " AND VERSION=" + version);
    vw.executeQuery();

    if (vw.first() == null) {
      ViewObject view1 = this.getHistoricalCdeIdsView();
      view1.setWhereClause(
        "to_char(to_number(ltrim(substr(name,1,7)))) =  '" + cdeId +
        "' AND version= " + version);

      if (view1.first() != null) {
        HistoricalCdeIdsViewRowImpl desRow =
          (HistoricalCdeIdsViewRowImpl) view1.first();
        deIdseq = desRow.getDeIdseq();
        de = this.getDataElements(deIdseq);
      }
      else {
        throw new DataElementNotFoundException(
          "Data element with CDE ID= " + cdeId + " and version= " + version +
          " does not exist in the database. ");
      }
    }
    else {
      deRow = (DataElementsViewRowImpl) vw.first();
      de = new BC4JDataElementTransferObject(deRow);
    }

    return de;
  }

  /**
   * Container's getter for DataElementConceptsView
   */
  public DataElementConceptsViewImpl getDataElementConceptsView() {
    return (DataElementConceptsViewImpl) findViewObject(
      "DataElementConceptsView");
  }

  /**
   * Container's getter for PermissibleValuesView
   */
  public PermissibleValuesViewImpl getPermissibleValuesView() {
    return (PermissibleValuesViewImpl) findViewObject("PermissibleValuesView");
  }

  /**
   * Container's getter for ReferenceDocumentsView
   */
  public ReferenceDocumentsViewImpl getReferenceDocumentsView() {
    return (ReferenceDocumentsViewImpl) findViewObject(
      "ReferenceDocumentsView");
  }

  /**
   * Container's getter for ValueDomainsView
   */
  public ValueDomainsViewImpl getValueDomainsView() {
    return (ValueDomainsViewImpl) findViewObject("ValueDomainsView");
  }

  /**
   * Container's getter for ValueMeaningsLovView
   */
  public ValueMeaningsLovViewImpl getValueMeaningsLovView() {
    return (ValueMeaningsLovViewImpl) findViewObject("ValueMeaningsLovView");
  }

  /**
   * Container's getter for VdPvsView
   */
  public VdPvsViewImpl getVdPvsView() {
    return (VdPvsViewImpl) findViewObject("VdPvsView");
  }

  /**
   * Container's getter for DataElementsView1
   */
  public DataElementsViewImpl getDataElementsView1() {
    return (DataElementsViewImpl) findViewObject("DataElementsView1");
  }

  /**
   * Container's getter for DataElementsView2
   */
  public DataElementsViewImpl getDataElementsView2() {
    return (DataElementsViewImpl) findViewObject("DataElementsView2");
  }

  /**
   * Container's getter for DataElementConceptsView1
   */
  public DataElementConceptsViewImpl getDataElementConceptsView1() {
    return (DataElementConceptsViewImpl) findViewObject(
      "DataElementConceptsView1");
  }

  /**
   * Container's getter for DataElementsView3
   */
  public DataElementsViewImpl getDataElementsView3() {
    return (DataElementsViewImpl) findViewObject("DataElementsView3");
  }

  /**
   * Container's getter for ValueDomainsView1
   */
  public ValueDomainsViewImpl getValueDomainsView1() {
    return (ValueDomainsViewImpl) findViewObject("ValueDomainsView1");
  }

  /**
   * Container's getter for PermissibleValuesView1
   */
  public PermissibleValuesViewImpl getPermissibleValuesView1() {
    return (PermissibleValuesViewImpl) findViewObject("PermissibleValuesView1");
  }

  /**
   * Container's getter for VdPvsView1
   */
  public VdPvsViewImpl getVdPvsView1() {
    return (VdPvsViewImpl) findViewObject("VdPvsView1");
  }

  /**
   * Container's getter for VdPvsView2
   */
  public VdPvsViewImpl getVdPvsView2() {
    return (VdPvsViewImpl) findViewObject("VdPvsView2");
  }

  /**
   * Container's getter for VdPvsView3
   */
  public VdPvsViewImpl getVdPvsView3() {
    return (VdPvsViewImpl) findViewObject("VdPvsView3");
  }

  /**
   * Container's getter for DeConteFkLink
   */
  public ViewLinkImpl getDeConteFkLink() {
    return (ViewLinkImpl)findViewLink("DeConteFkLink");
  }

  /**
   * Container's getter for DeDecFkLink
   */
  public ViewLinkImpl getDeDecFkLink() {
    return (ViewLinkImpl)findViewLink("DeDecFkLink");
  }

  /**
   * Container's getter for DecConteFkLink
   */
  public ViewLinkImpl getDecConteFkLink() {
    return (ViewLinkImpl)findViewLink("DecConteFkLink");
  }

  /**
   * Container's getter for DeVdFkLink
   */
  public ViewLinkImpl getDeVdFkLink() {
    return (ViewLinkImpl)findViewLink("DeVdFkLink");
  }

  /**
   * Container's getter for VdConteFkLink
   */
  public ViewLinkImpl getVdConteFkLink() {
    return (ViewLinkImpl)findViewLink("VdConteFkLink");
  }

  /**
   * Container's getter for PvVmvFkLink
   */
  public ViewLinkImpl getPvVmvFkLink() {
    return (ViewLinkImpl)findViewLink("PvVmvFkLink");
  }

  /**
   * Container's getter for VpConteFkLink
   */
  public ViewLinkImpl getVpConteFkLink() {
    return (ViewLinkImpl)findViewLink("VpConteFkLink");
  }

  /**
   * Container's getter for VpPvFkLink
   */
  public ViewLinkImpl getVpPvFkLink() {
    return (ViewLinkImpl)findViewLink("VpPvFkLink");
  }

  /**
   * Container's getter for VpVdFkLink
   */
  public ViewLinkImpl getVpVdFkLink() {
    return (ViewLinkImpl)findViewLink("VpVdFkLink");
  }

  /**
   * Sample main for debugging Business Components code using the tester.
   */
  public static void main(String[] args) {
    launchTester(
      "gov.nih.nci.ncicb.cadsr.persistence.bc4j", "CDEBrowserBc4jModuleLocal");
  }

  /**
   * Container's getter for AdministeredComponentsView
   */
  public AdministeredComponentsViewImpl getAdministeredComponentsView() {
    return (AdministeredComponentsViewImpl) findViewObject(
      "AdministeredComponentsView");
  }

  /**
   * Container's getter for ValidValuesView
   */
  public ValidValuesViewImpl getValidValuesView() {
    return (ValidValuesViewImpl) findViewObject("ValidValuesView");
  }

  /**
   * Container's getter for CsCsiView
   */
  public CsCsiViewImpl getCsCsiView() {
    return (CsCsiViewImpl) findViewObject("CsCsiView");
  }

  /**
   * Container's getter for ClassSchemeItemsView
   */
  public ClassSchemeItemsViewImpl getClassSchemeItemsView() {
    return (ClassSchemeItemsViewImpl) findViewObject("ClassSchemeItemsView");
  }

  /**
   * Container's getter for ClassificationSchemesView
   */
  public ClassificationSchemesViewImpl getClassificationSchemesView() {
    return (ClassificationSchemesViewImpl) findViewObject(
      "ClassificationSchemesView");
  }

  /**
   * Container's getter for AcCsiView
   */
  public AcCsiViewImpl getAcCsiView() {
    return (AcCsiViewImpl) findViewObject("AcCsiView");
  }

  /**
   * Container's getter for ClassificationsView
   */
  public ClassificationsViewImpl getClassificationsView() {
    return (ClassificationsViewImpl) findViewObject("ClassificationsView");
  }

  /**
   * Container's getter for DeCdeIdView
   */
  public DeCdeIdViewImpl getDeCdeIdView() {
    return (DeCdeIdViewImpl) findViewObject("DeCdeIdView");
  }

  /**
   * Container's getter for ReferenceBlobsView
   */
  public ReferenceBlobsViewImpl getReferenceBlobsView() {
    return (ReferenceBlobsViewImpl) findViewObject("ReferenceBlobsView");
  }

  /**
   * Container's getter for QuestContentsExtView
   */
  public QuestContentsExtViewImpl getQuestContentsExtView() {
    return (QuestContentsExtViewImpl) findViewObject("QuestContentsExtView");
  }

  /**
   * Container's getter for ObjectClassesExtView
   */
  public ObjectClassesExtViewImpl getObjectClassesExtView() {
    return (ObjectClassesExtViewImpl) findViewObject("ObjectClassesExtView");
  }

  /**
   * Container's getter for PropertiesExtView
   */
  public PropertiesExtViewImpl getPropertiesExtView() {
    return (PropertiesExtViewImpl) findViewObject("PropertiesExtView");
  }

  /**
   * Container's getter for PropertiesExtView1
   */
  public PropertiesExtViewImpl getPropertiesExtView1() {
    return (PropertiesExtViewImpl) findViewObject("PropertiesExtView1");
  }

  /**
   * Container's getter for ObjectClassesExtView1
   */
  public ObjectClassesExtViewImpl getObjectClassesExtView1() {
    return (ObjectClassesExtViewImpl) findViewObject("ObjectClassesExtView1");
  }

  /**
   * Container's getter for QuestContentsExtView1
   */
  public QuestContentsExtViewImpl getQuestContentsExtView1() {
    return (QuestContentsExtViewImpl) findViewObject("QuestContentsExtView1");
  }

  /**
   * Container's getter for ClassificationSchemesView1
   */
  public ClassificationSchemesViewImpl getClassificationSchemesView1() {
    return (ClassificationSchemesViewImpl) findViewObject(
      "ClassificationSchemesView1");
  }

  /**
   * Container's getter for ProConteFKLink
   */
  public ViewLinkImpl getProConteFKLink() {
    return (ViewLinkImpl)findViewLink("ProConteFKLink");
  }

  /**
   * Container's getter for OcConteFKLink
   */
  public ViewLinkImpl getOcConteFKLink() {
    return (ViewLinkImpl)findViewLink("OcConteFKLink");
  }

  /**
   * Container's getter for QcConteFKLink
   */
  public ViewLinkImpl getQcConteFKLink() {
    return (ViewLinkImpl)findViewLink("QcConteFKLink");
  }

  /**
   * Container's getter for CsConteFKLink
   */
  public ViewLinkImpl getCsConteFKLink() {
    return (ViewLinkImpl)findViewLink("CsConteFKLink");
  }

  /**
   * Container's getter for ConceptualDomainsView
   */
  public ConceptualDomainsViewImpl getConceptualDomainsView() {
    return (ConceptualDomainsViewImpl) findViewObject("ConceptualDomainsView");
  }

  /**
   * Container's getter for ConceptualDomainsView1
   */
  public ConceptualDomainsViewImpl getConceptualDomainsView1() {
    return (ConceptualDomainsViewImpl) findViewObject("ConceptualDomainsView1");
  }

  /**
   * Container's getter for CdConteFKLink
   */
  public ViewLinkImpl getCdConteFKLink() {
    return (ViewLinkImpl)findViewLink("CdConteFKLink");
  }

  /**
   * Container's getter for DataElementConceptsView2
   */
  public DataElementConceptsViewImpl getDataElementConceptsView2() {
    return (DataElementConceptsViewImpl) findViewObject(
      "DataElementConceptsView2");
  }

  /**
   * Container's getter for ValueDomainsView2
   */
  public ValueDomainsViewImpl getValueDomainsView2() {
    return (ValueDomainsViewImpl) findViewObject("ValueDomainsView2");
  }

  /**
   * Container's getter for DecCdFKLink
   */
  public ViewLinkImpl getDecCdFKLink() {
    return (ViewLinkImpl)findViewLink("DecCdFKLink");
  }

  /**
   * Container's getter for VdCdFKLink
   */
  public ViewLinkImpl getVdCdFKLink() {
    return (ViewLinkImpl)findViewLink("VdCdFKLink");
  }

  /**
   * Container's getter for DesignationsView
   */
  public DesignationsViewImpl getDesignationsView() {
    return (DesignationsViewImpl) findViewObject("DesignationsView");
  }

  /**
   * Container's getter for DesignationsView1
   */
  public DesignationsViewImpl getDesignationsView1() {
    return (DesignationsViewImpl) findViewObject("DesignationsView1");
  }

  /**
   * Container's getter for DesigConteFkLink
   */
  public ViewLinkImpl getDesigConteFkLink() {
    return (ViewLinkImpl)findViewLink("DesigConteFkLink");
  }

  /**
   * Container's getter for DataElementSearchView
   */
  public DataElementSearchViewImpl getDataElementSearchView() {
    return (DataElementSearchViewImpl) findViewObject("DataElementSearchView");
  }

  /**
   * Container's getter for ProtocolsView
   */
  public ProtocolsViewImpl getProtocolsView() {
    return (ProtocolsViewImpl) findViewObject("ProtocolsView");
  }

  /**
   * Container's getter for FormUsagesForACdeView
   */
  public FormUsagesForACdeViewImpl getFormUsagesForACdeView() {
    return (FormUsagesForACdeViewImpl) findViewObject("FormUsagesForACdeView");
  }

  /**
   * Container's getter for HistoricalCdeIdsView
   */
  public HistoricalCdeIdsViewImpl getHistoricalCdeIdsView() {
    return (HistoricalCdeIdsViewImpl) findViewObject("HistoricalCdeIdsView");
  }

  /**
   * Container's getter for AcRegistrationsView
   */
  public AcRegistrationsViewImpl getAcRegistrationsView() {
    return (AcRegistrationsViewImpl) findViewObject("AcRegistrationsView");
  }

  /**
   * Container's getter for RepresentationViewObj1
   */
  public RepresentationViewObjImpl getRepresentationViewObj1() {
    return (RepresentationViewObjImpl) findViewObject("RepresentationViewObj1");
  }




}
