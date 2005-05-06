package gov.nih.nci.ncicb.cadsr.cdebrowser.tree.service.impl;

import gov.nih.nci.ncicb.cadsr.cdebrowser.tree.BaseTreeNode;
import gov.nih.nci.ncicb.cadsr.cdebrowser.tree.TreeConstants;
import gov.nih.nci.ncicb.cadsr.cdebrowser.tree.TreeFunctions;
import gov.nih.nci.ncicb.cadsr.cdebrowser.tree.TreeIdGenerator;
import gov.nih.nci.ncicb.cadsr.cdebrowser.tree.service.CDEBrowserTreeService;
import gov.nih.nci.ncicb.cadsr.dto.CSITransferObject;
import gov.nih.nci.ncicb.cadsr.dto.ContextHolderTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.TreeProtocolNodesTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.jdbc.ProtocolValueObject;
import gov.nih.nci.ncicb.cadsr.persistence.dao.AbstractDAOFactory;
import gov.nih.nci.ncicb.cadsr.persistence.dao.ContextDAO;
import gov.nih.nci.ncicb.cadsr.persistence.dao.FormDAO;
import gov.nih.nci.ncicb.cadsr.resource.ClassSchemeItem;
import gov.nih.nci.ncicb.cadsr.resource.Context;
import gov.nih.nci.ncicb.cadsr.resource.ContextHolder;
import gov.nih.nci.ncicb.cadsr.resource.Form;
import gov.nih.nci.ncicb.cadsr.resource.Protocol;
import gov.nih.nci.ncicb.cadsr.servicelocator.ServiceLocator;
import gov.nih.nci.ncicb.cadsr.servicelocator.ServiceLocatorFactory;
import gov.nih.nci.ncicb.cadsr.util.TimeUtils;
import gov.nih.nci.ncicb.webtree.WebNode;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

public class CDEBrowserTreeServiceImpl
 implements CDEBrowserTreeService {
 ServiceLocator locator;
 AbstractDAOFactory daoFactory;

 /**
  * Currently the locator classname is hard code need to be refactored
  */
 public CDEBrowserTreeServiceImpl() {
  locator = ServiceLocatorFactory.getLocator("gov.nih.nci.ncicb.cadsr.servicelocator.ejb.ServiceLocatorImpl");

  daoFactory = AbstractDAOFactory.getDAOFactory(locator);
 }

 /**
  * @returns a Context holder that contains a contect object and context we node
  */
 public List getContextNodeHolders(TreeFunctions treeFunctions, TreeIdGenerator idGen,
                                   String excludeList) throws Exception {
  ContextDAO dao = daoFactory.getContextDAO();

  List contexts = dao.getAllContexts(excludeList);
  ListIterator it = contexts.listIterator();
  List holders = new ArrayList();

  while (it.hasNext()) {
   Context context = (Context)it.next();

   ContextHolder holder = new ContextHolderTransferObject();
   holder.setContext(context);
   holder.setNode(getContextNode(idGen.getNewId(), context, treeFunctions));
   holders.add(holder);
  }

  return holders;
 }
 /**
 * @returns two maps with contextid as key and value a holder object containing web node
 * one containg the forms with no protocol and other with protocols
 */
 public List getAllContextProtocolNodes(TreeFunctions treeFunctions, TreeIdGenerator idGen) throws Exception {
  Map allFormsWithProtocol = new HashMap();

  Map allFormsWithNoProtocol = new HashMap();
  FormDAO dao = daoFactory.getFormDAO();
  List forms = dao.getAllFormsOrderByContextProtocol();
  Map protocolHolder = new HashMap();

  Iterator iter = forms.iterator();

  while (iter.hasNext()) {
   Form currForm = (Form)iter.next();

   String currContextId = currForm.getContext().getConteIdseq();
   String currProtoIdSeq = null;

   currProtoIdSeq = currForm.getProtoIdseq();
   DefaultMutableTreeNode formNode = getFormNode(idGen.getNewId(), currForm, treeFunctions);

   //
   if (currProtoIdSeq != null && !currProtoIdSeq.equals("")) {
    List protocolList = (List)allFormsWithProtocol.get(currContextId);

    if (protocolList == null) {
     protocolList = new ArrayList();

     allFormsWithProtocol.put(currContextId, protocolList);
    }

    DefaultMutableTreeNode protoNode = (DefaultMutableTreeNode)protocolHolder.get(currProtoIdSeq);

    if (protoNode == null) {
     protoNode = getProtocolNode(idGen.getNewId(), currForm.getProtocol(), currContextId, treeFunctions);

     protocolHolder.put(currProtoIdSeq, protoNode);
     protocolList.add(protoNode);
    }

    protoNode.add(formNode);
   } else {

    List formWithNoProto = (List)allFormsWithNoProtocol.get(currContextId);

    if (formWithNoProto == null) {
     formWithNoProto = new ArrayList();

     allFormsWithNoProtocol.put(currContextId, formWithNoProto);
    }

    formWithNoProto.add(formNode);
   }
  }

  List resultList = new ArrayList();
  resultList.add(0, allFormsWithNoProtocol);
  resultList.add(1, allFormsWithProtocol);
  return resultList;
 }
 /**
 * @returns a map with contextid as key and value a list of template nodes
 */
 public Map getAllContextTemplateNodes(TreeFunctions treeFunctions, TreeIdGenerator idGen) throws Exception {
  Map allTemplatesByContext = new HashMap();
  FormDAO dao = daoFactory.getFormDAO();
  List templates = dao.getAllTemplatesOrderByContext();

  Iterator iter = templates.iterator();

  while (iter.hasNext()) {
    Form currTemplate = (Form)iter.next();
    
    String currContextId = currTemplate.getContext().getConteIdseq();
    DefaultMutableTreeNode tmpNode = getTemplateNode(idGen.getNewId(), currTemplate, treeFunctions);     
                 
    List nodes = (List)allTemplatesByContext.get(currContextId);
    if(nodes==null)
    {
      nodes= new ArrayList(); 
      allTemplatesByContext.put(currContextId,nodes);
    }
    nodes.add(tmpNode);        
  }
 
  return allTemplatesByContext;
 }
 /**
  * @returns a map with contextid as key and value a list of Classification nodes
  */
 public List getAllClassificationNodes(TreeFunctions treeFunctions, TreeIdGenerator idGen) throws Exception {
  return null;
 }

 private DefaultMutableTreeNode getContextNode(String nodeId, Context context,
                                               TreeFunctions treeFunctions) throws Exception {
  String currContextId = context.getConteIdseq();

  String name = context.getName();
  String desc = context.getDescription();

  DefaultMutableTreeNode
     contextNode = new DefaultMutableTreeNode(
                      new WebNode(nodeId,
                                  name + " (" + desc + ")",
                                  "javascript:" + treeFunctions.getJsFunctionName() + "('P_PARAM_TYPE=CONTEXT&P_IDSEQ="
                                     + currContextId + "&P_CONTE_IDSEQ=" + currContextId
                                     + treeFunctions.getExtraURLParameters() + "')",
                                  desc + " (" + name + ")"));
  return contextNode;
 }

 private DefaultMutableTreeNode getFormNode(String nodeId, Form form, 
                                            TreeFunctions treeFunctions) throws Exception {
  String formIdseq = form.getFormIdseq();

  String longName = form.getLongName();
  String preferred_definition = form.getPreferredDefinition();
  String currContextId = form.getContext().getConteIdseq();
  
  DefaultMutableTreeNode
     formNode = new DefaultMutableTreeNode( new WebNode(nodeId,
         longName,
         "javascript:" + treeFunctions.getFormJsFunctionName() + "('P_PARAM_TYPE=CRF&P_IDSEQ="
            + formIdseq + "&P_CONTE_IDSEQ=" + " " + "&P_PROTO_IDSEQ=" + ""
            + treeFunctions.getExtraURLParameters() + "')",
         preferred_definition));
  return formNode;
 }

 private DefaultMutableTreeNode getTemplateNode(String nodeId, Form template, 
                                                TreeFunctions treeFunctions) throws Exception {
  String templateIdseq = template.getFormIdseq();

  String longName = template.getLongName();
  String preferred_definition = template.getPreferredDefinition();
  String contextName = template.getContext().getName();
  String currContextId = template.getContext().getConteIdseq();
  DefaultMutableTreeNode tmpNode =
                            new DefaultMutableTreeNode(new WebNode(nodeId,
                                                                   longName                                              //long name
                                                                      ,
                                                                   "javascript:" + treeFunctions.getFormJsFunctionName()
                                                                      + "('P_PARAM_TYPE=TEMPLATE&P_IDSEQ="
                                                                      + templateIdseq + "&P_CONTE_IDSEQ="
                                                                      + currContextId +                                  //context idseq
                                                                      "&templateName=" + URLEncoder.encode(longName) +   //longname
                                                                      "&contextName=" + URLEncoder.encode(contextName) + // context name
                                                                      treeFunctions.getExtraURLParameters() + "')",
                                                                   preferred_definition));                               //preffered definition      
  return tmpNode;
 }

 private DefaultMutableTreeNode getTemplateNode(String nodeId,       Form template,        String contextName,
                                                ClassSchemeItem csi, String currContextId, String categoryName,
                                                TreeFunctions treeFunctions) throws Exception {
  String templateIdseq = template.getFormIdseq();

  String longName = template.getLongName();
  String prefferedDefinition = template.getPreferredDefinition();

  DefaultMutableTreeNode
     tmpNode = new DefaultMutableTreeNode(
                  new WebNode(nodeId,
                              longName,
                              "javascript:" + treeFunctions.getFormJsFunctionName() + "('P_PARAM_TYPE=TEMPLATE&P_IDSEQ="
                                 + templateIdseq + "&P_CONTE_IDSEQ=" + currContextId + "&csName="
                                 + URLEncoder.encode(
                                      csi.getClassSchemeLongName()) + "&diseaseName=" + URLEncoder.encode(
                                                                                           csi.getClassSchemeItemName())
                                 + "&templateType="
                                 + URLEncoder.encode(categoryName) + "&templateName=" + URLEncoder.encode(
                                                                                           longName) + "&contextName="
                                 + URLEncoder.encode(contextName) + treeFunctions.getExtraURLParameters() + "')",
                              prefferedDefinition));

  return tmpNode;
 }

 private DefaultMutableTreeNode getProtocolNode(String nodeId, Protocol protocol, String currContextId,
                                                TreeFunctions treeFunctions) throws Exception {
  String protoIdseq = protocol.getProtoIdseq();

  String longName = protocol.getLongName();
  String preferred_definition = protocol.getPreferredDefinition();

  DefaultMutableTreeNode
     protocolNode = new DefaultMutableTreeNode(
                       new WebNode(nodeId,
                                   longName,
                                   "javascript:" + treeFunctions.getJsFunctionName()
                                      + "('P_PARAM_TYPE=PROTOCOL&P_IDSEQ=" + protoIdseq + "&P_CONTE_IDSEQ="
                                      + currContextId + "&protocolLongName=" + longName
                                      + treeFunctions.getExtraURLParameters() + "')",
                                   preferred_definition));
  return protocolNode;
 }

 private DefaultMutableTreeNode getWebNode(String name, String id) {
  return new DefaultMutableTreeNode(new WebNode(id, name));
 }
}