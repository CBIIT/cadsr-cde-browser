package gov.nih.nci.ncicb.cadsr.formbuilder.struts.actions;

import gov.nih.nci.ncicb.cadsr.dto.ContextTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.FormTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.ProtocolTransferObject;
import gov.nih.nci.ncicb.cadsr.exception.FatalException;
import gov.nih.nci.ncicb.cadsr.formbuilder.common.FormBuilderException;
import gov.nih.nci.ncicb.cadsr.formbuilder.service.FormBuilderServiceDelegate;
import gov.nih.nci.ncicb.cadsr.formbuilder.struts.common.FormActionUtil;
import gov.nih.nci.ncicb.cadsr.formbuilder.struts.formbeans.FormBuilderBaseDynaFormBean;
import gov.nih.nci.ncicb.cadsr.resource.Context;
import gov.nih.nci.ncicb.cadsr.resource.Form;
import gov.nih.nci.ncicb.cadsr.resource.Module;
import gov.nih.nci.ncicb.cadsr.resource.Orderable;

import gov.nih.nci.ncicb.cadsr.resource.Protocol;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;


public class FormEditAction extends FormBuilderBaseDispatchAction {
  /**
   * Returns Complete form given an Id for Edit.
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward getFormToEdit(
    ActionMapping mapping,
    ActionForm formEditForm,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    Form crf = null;
    Form clonedCrf = null;
    setInitLookupValues(request);

    try {
      crf = setFormForAction(formEditForm, request);
      clonedCrf = (Form) crf.clone();
      setSessionObject(request, CLONED_CRF, clonedCrf);
    }
    catch (FormBuilderException exp) {
      if (log.isDebugEnabled()) {
        log.debug("Exception on getFormForEdit =  " + exp);
      }
    }
    catch (CloneNotSupportedException clexp) {
      if (log.isDebugEnabled()) {
        log.debug("Exception on Clone =  " + clexp);
      }
      throw new FatalException(clexp);
    }

    if ((crf != null) && (formEditForm != null)) {
      FormBuilderBaseDynaFormBean dynaFormEditForm =
        (FormBuilderBaseDynaFormBean) formEditForm;
      dynaFormEditForm.clear();
      dynaFormEditForm.set(FORM_ID_SEQ, crf.getFormIdseq());
      dynaFormEditForm.set(FORM_LONG_NAME, crf.getLongName());
      dynaFormEditForm.set(
        this.PREFERRED_DEFINITION, crf.getPreferredDefinition());
      dynaFormEditForm.set(CONTEXT_ID_SEQ, crf.getContext().getConteIdseq());
      dynaFormEditForm.set(
        this.PROTOCOL_ID_SEQ, crf.getProtocol().getProtoIdseq());
      dynaFormEditForm.set(
        this.PROTOCOLS_LOV_NAME_FIELD, crf.getProtocol().getLongName());
      dynaFormEditForm.set(CATEGORY_NAME, crf.getFormCategory());      
      dynaFormEditForm.set(
        this.FORM_TYPE, crf.getFormType());
      dynaFormEditForm.set(CATEGORY_NAME, crf.getFormCategory()); 
      dynaFormEditForm.set(this.WORKFLOW, crf.getAslName()); 
    }

    if (log.isDebugEnabled()) {
      log.debug("crf =  " + crf);
      log.debug("Cloned crf =  " + clonedCrf);
    }

    setSessionObject(request, DELETED_MODULES, null);

    return mapping.findForward(SUCCESS);
  }

  /**
   * Swap the display order of the Module with the previous Module.
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward moveModuleUp(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    DynaActionForm editForm = (DynaActionForm) form;
    Integer moduleIndex = (Integer) editForm.get(MODULE_INDEX);
    int currModuleIndex = moduleIndex.intValue();
    Form crf = (Form) getSessionObject(request, CRF);

    List modules = crf.getModules();

    if ((modules != null) && (modules.size() > 1)) {
      Module currModule = (Module) modules.get(currModuleIndex);
      Module prvModule = (Module) modules.get(currModuleIndex - 1);
      int currModuleDisplayOrder = currModule.getDisplayOrder();
      currModule.setDisplayOrder(prvModule.getDisplayOrder());
      prvModule.setDisplayOrder(currModuleDisplayOrder);
      modules.remove(currModuleIndex);
      modules.add(currModuleIndex - 1, currModule);
    }

    if (log.isDebugEnabled()) {
      log.info("Move up Module ");
    }

    return mapping.findForward(FORM_EDIT);
  }

  /**
   * Swap the display order of the Module with the next Module.
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward moveModuleDown(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    DynaActionForm editForm = (DynaActionForm) form;
    Integer moduleIndex = (Integer) editForm.get(MODULE_INDEX);
    int currModuleIndex = moduleIndex.intValue();

    Form crf = (Form) getSessionObject(request, CRF);

    List modules = crf.getModules();

    if ((modules != null) && (modules.size() > 1)) {
      Module currModule = (Module) modules.get(currModuleIndex);
      Module nextModule = (Module) modules.get(currModuleIndex + 1);
      int currModuleDisplayOrder = currModule.getDisplayOrder();
      currModule.setDisplayOrder(nextModule.getDisplayOrder());
      nextModule.setDisplayOrder(currModuleDisplayOrder);
      modules.remove(currModuleIndex);
      modules.add(currModuleIndex + 1, currModule);
    }

    if (log.isDebugEnabled()) {
      log.info("Move Down Module ");
    }

    return mapping.findForward(FORM_EDIT);
  }

  /**
   * Deletes the module of specified index and adds the Module to deleted list.
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward deleteModule(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    DynaActionForm editForm = (DynaActionForm) form;
    Integer moduleIndex = (Integer) editForm.get(MODULE_INDEX);

    Form crf = (Form) getSessionObject(request, CRF);
    List deletedModules = (List) getSessionObject(request, DELETED_MODULES);

    if (deletedModules == null) {
      deletedModules = new ArrayList();
    }

    List modules = crf.getModules();

    if ((modules != null) && (modules.size() > 0)) {
      if (log.isDebugEnabled()) {
        printDisplayOrder(modules);
      }

      Module deletedModule = (Module) modules.remove(moduleIndex.intValue());
      FormActionUtil.decrementDisplayOrder(modules, moduleIndex.intValue());
      deletedModules.add(deletedModule);
    }

    setSessionObject(request, DELETED_MODULES, deletedModules);

    if (log.isDebugEnabled()) {
      printDisplayOrder(modules);
    }

    return mapping.findForward(FORM_EDIT);
  }

  /**
   * Add Module from deleted list.
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward addFromDeletedList(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    DynaActionForm editForm = (DynaActionForm) form;
    Integer moduleIndex = (Integer) editForm.get(MODULE_INDEX);

    String addDeletedModuleIdSeq =
      (String) editForm.get(ADD_DELETED_MODULE_IDSEQ);

    Form crf = (Form) getSessionObject(request, CRF);
    List deletedModules = (List) getSessionObject(request, DELETED_MODULES);

    List modules = crf.getModules();

    if ((modules != null) && (moduleIndex != null) && (deletedModules != null)) {
      Module moduleToAdd =
        removeModuleFromList(addDeletedModuleIdSeq, deletedModules);

      if (log.isDebugEnabled()) {
        printDisplayOrder(modules);
      }

      if ((moduleIndex.intValue() < modules.size()) && (moduleToAdd != null)) {
        Module currModule = (Module) modules.get(moduleIndex.intValue());
        int displayOrder = currModule.getDisplayOrder();
        FormActionUtil.incrementDisplayOrder(modules, moduleIndex.intValue());
        moduleToAdd.setDisplayOrder(displayOrder);
        modules.add((moduleIndex.intValue()), moduleToAdd);
      }
      else {
        int newDisplayOrder = 0;

        if (moduleIndex.intValue() != 0) {
          Module lastModule = (Module) modules.get(modules.size() - 1);
          newDisplayOrder = lastModule.getDisplayOrder() + 1;
        }

        moduleToAdd.setDisplayOrder(newDisplayOrder);
        modules.add(moduleToAdd);
      }
    }

    if (log.isDebugEnabled()) {
      printDisplayOrder(modules);
    }

    return mapping.findForward(FORM_EDIT);
  }

  /**
   * Delete Form.
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward deleteForm(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    DynaActionForm hrefCRFForm = (DynaActionForm) form;
    String formIdSeq = (String) hrefCRFForm.get(FORM_ID_SEQ);
    if (log.isDebugEnabled()) {
      log.info("Delete Form With Id " + formIdSeq);
    }
    FormBuilderServiceDelegate service = getFormBuilderService();
    try {    
        service.deleteForm(formIdSeq);
      }
    catch (FormBuilderException exp) {
        if (log.isDebugEnabled()) {
          log.debug("Exception on delete  " + exp);
        }
        saveMessage(ERROR_FORM_DELETE_FAILED, request);
        saveMessage(exp.getErrorCode(), request);
        return mapping.findForward(FAILURE);
      }    
    setSessionObject(request, DELETED_MODULES, null);
    setSessionObject(request, CLONED_CRF, null);
    setSessionObject(request, CRF, null);
    saveMessage("cadsr.formbuilder.form.delete.success", request);
    ActionForward forward = mapping.findForward(SUCCESS);
    return forward;
  }


  /**
   * Save Changes
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward saveForm(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    Form crf = (Form) getSessionObject(request, CRF);
    boolean hasUpdate  = setValuesForUpdate(mapping,form,request);
    if(hasUpdate)
    {
        try {
          FormBuilderServiceDelegate service = getFormBuilderService();
          Form header = (Form)request.getAttribute("header");
          Collection updatedModules = (Collection)request.getAttribute("updatedModules");
          Collection deletedModules = (Collection)request.getAttribute("deletedModules");
          Form updatedCrf = service.updateForm(crf.getFormIdseq(),header, updatedModules, deletedModules);
          setSessionObject(request,CRF, updatedCrf);
        }
        catch (FormBuilderException exp) {
          if (log.isDebugEnabled()) {
            log.debug("Exception on service.updateForm=  " + exp);
          }
  
          saveMessage(ERROR_FORM_SAVE_FAILED, request);
          saveMessage(exp.getErrorCode(), request);
          return mapping.findForward(FAILURE);
        }
        saveMessage("cadsr.formbuilder.form.edit.save.success", request);
        removeSessionObject(request, DELETED_MODULES);
        removeSessionObject(request, CLONED_CRF);
        return mapping.findForward(SUCCESS); 
       }
    else
    {
      saveMessage("cadsr.formbuilder.form.edit.nochange", request);
       return mapping.findForward(FORM_EDIT); 
    }
    }

  /**
   * Cancel Edit and back to Search results
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward cancelFormEdit(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException, ServletException {
    FormBuilderBaseDynaFormBean editForm = (FormBuilderBaseDynaFormBean) form;
    removeSessionObject(request, DELETED_MODULES);
    removeSessionObject(request, CLONED_CRF);
    editForm.clear();
    return mapping.findForward(SUCCESS);
      
    }

  /**
   * Check if there are updated to form and set the value in the request
   *
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  private boolean setValuesForUpdate(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request)  {
    DynaActionForm editForm = (DynaActionForm) form;
    Form clonedCrf = (Form) getSessionObject(request, CLONED_CRF);
    Form crf = (Form) getSessionObject(request, CRF);
    List deletedModules = (List) getSessionObject(request, DELETED_MODULES);
    Form header = new FormTransferObject();

    //Add the header info into TransferObj after checking for update
    boolean headerUpdate = false;
    header.setFormIdseq((String) editForm.get(FORM_ID_SEQ));
    header.setPreferredName(clonedCrf.getPreferredName());
    String longName = (String) editForm.get(FORM_LONG_NAME);
  
    if ((longName != null) && (clonedCrf.getLongName() != null)) {
       header.setLongName(longName);
      if (!longName.equals(clonedCrf.getLongName())) {
        headerUpdate = true;
      }
    }
    else if(longName==null)
    {
       header.setLongName(null);
       headerUpdate = true;
    }

    String contextIdSeq = (String) editForm.get(CONTEXT_ID_SEQ);
    String orgContextIdSeq = null;

    if (clonedCrf.getContext() != null) {
      orgContextIdSeq = clonedCrf.getContext().getConteIdseq();
    }

    if ((contextIdSeq != null) && (orgContextIdSeq != null)) {
      Context context = new ContextTransferObject();
      context.setConteIdseq(contextIdSeq);
      header.setContext(context);
      if (!contextIdSeq.equals(orgContextIdSeq)) {
        headerUpdate = true;
      }
    }

    String protocolIdSeq = (String) editForm.get(PROTOCOL_ID_SEQ);
    String orgProtocolIdSeq = null;

    if (clonedCrf.getProtocol() != null) {
      orgProtocolIdSeq = clonedCrf.getProtocol().getProtoIdseq();
    }

    if ((protocolIdSeq != null) && (orgProtocolIdSeq != null)) {
     Protocol protocol = new ProtocolTransferObject();
     protocol.setProtoIdseq(protocolIdSeq);
      header.setProtocol(protocol);
      if (!orgProtocolIdSeq.equals(protocolIdSeq)) {       
        headerUpdate = true;
      }
    }
    else if(protocolIdSeq==null)
    {
      header.setProtocol(null);
      headerUpdate = true;
    }

    String workflow = (String) editForm.get(WORKFLOW);
    String orgWorkflow = clonedCrf.getAslName();
   if ((workflow != null) && orgWorkflow!=null) {
      header.setAslName(workflow);
      if (!workflow.equals(orgWorkflow)) {       
        headerUpdate = true;
      }
    }

    String categoryName = (String) editForm.get(CATEGORY_NAME);
    String orgCategoryName = clonedCrf.getFormCategory();
   if ((categoryName != null) && orgCategoryName!=null) {
      header.setFormCategory(categoryName);
      if (!categoryName.equals(orgCategoryName)) {       
        headerUpdate = true;
      }
    }
   else if(categoryName==null)
   {
     header.setFormCategory(null);
     headerUpdate = true;
   }

    String formType = (String) editForm.get(this.FORM_TYPE);
    String orgFormType = clonedCrf.getFormType();
   if ((formType != null) && orgFormType!=null) {
      header.setFormType(formType);
      if (!formType.equals(orgFormType)) {       
        headerUpdate = true;
      }
    }

    String preferredDef = (String) editForm.get(PREFERRED_DEFINITION);
    String orgPreferredDef = clonedCrf.getPreferredDefinition();
    if ((preferredDef != null) && orgPreferredDef != null) {
      header.setPreferredDefinition(preferredDef);
      if (!preferredDef.equals(clonedCrf.getPreferredDefinition())) {        
        headerUpdate = true;
      }
    }
    else if(preferredDef != null)
    {
      header.setPreferredDefinition(null);
      headerUpdate = true;
    }
    
   List updatedModules =
       getUpdatedModules(clonedCrf.getModules(), crf.getModules());
    if(!headerUpdate)
       header=null;
    if (
        header!=null || ((deletedModules != null) && !deletedModules.isEmpty()) ||
          !updatedModules.isEmpty()) {
        request.setAttribute("header",header);
        request.setAttribute("updatedModules",updatedModules);
        request.setAttribute("deletedModules",deletedModules);
        return true;
      }
    else
    {
      return false;      
    }
  }
    
    
  /**
   * Removes the module given by "moduleIdSeq" from the module list
   *
   * @param moduleIdSeq
   * @param modules
   *
   * @return the removed module
   */
  protected Module removeModuleFromList(
    String moduleIdSeq,
    List modules) {
    ListIterator iterate = modules.listIterator();

    while (iterate.hasNext()) {
      Module module = (Module) iterate.next();

      if (module.getModuleIdseq().equals(moduleIdSeq)) {
        iterate.remove();

        return module;
      }
    }

    return null;
  }

  /**
   * Gets the module given by "moduleIdSeq" from the module list
   *
   * @param moduleIdSeq
   * @param modules
   *
   * @return the  module else returns null;
   */
  protected Module getModuleFromList(
    String moduleIdSeq,
    List modules) {
    ListIterator iterate = modules.listIterator();

    while (iterate.hasNext()) {
      Module module = (Module) iterate.next();

      if (module.getModuleIdseq().equals(moduleIdSeq)) {
        return module;
      }
    }

    return null;
  }


  /**
   * Compares the display order of the modules from the original form and the
   * edited form.
   *
   * @param orgModules
   * @param newModules
   *
   * @return a list containg the modules with changed display order and the
   *         newly added modules. Returns empty list if "newModules" is null;
   *         If no modules present returns empty list;
   */
  protected List getUpdatedModules(
    List orgModules,
    List newModules) {
    List updatedModules = new ArrayList();

    if (orgModules == null) {
      return newModules;
    }

    if (newModules == null) {
      return updatedModules;
    }

    ListIterator newIterate = newModules.listIterator();

    while (newIterate.hasNext()) {
      Module newModule = (Module) newIterate.next();
      Module orgModule =
        (Module) getModuleFromList(newModule.getModuleIdseq(), orgModules);

      if (orgModule != null) {
        if (orgModule.getDisplayOrder() != newModule.getDisplayOrder()) {
          updatedModules.add(newModule);
        }
      }
      else {
        updatedModules.add(newModule);
      }
    }

    return updatedModules;
  }

  /**
   * Prints the Display Order of Modules
   *
   * @param modules
   */
  protected void printDisplayOrder(List modules) {
    ListIterator iterate = modules.listIterator();
    log.debug("Module Display order");

    while (iterate.hasNext()) {
      Module module = (Module) iterate.next();
      log.debug(module.getLongName() + "-->" + module.getDisplayOrder());
    }
  }
}
