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

package gov.nih.nci.ncicb.cadsr.cdebrowser.struts.actions;

import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.AbstractDAOFactory;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.ValueDomainDAO;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueMeaning;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocator;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocatorFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ValueMeaningAction extends BrowserBaseDispatchAction{
    public ActionForward showValueMeaningAlternates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                             HttpServletResponse response) throws IOException, ServletException {
       DynaActionForm formBean = (DynaActionForm)form;
       
       Object obj = formBean.get("id");
       if (obj==null){
           log.error("shortMeaning is not passed in");
           return mapping.findForward(FAILURE);
       }
       Integer vmId = Integer.valueOf((String)obj);
       
       ServiceLocator locator = 
        ServiceLocatorFactory.getLocator(CaDSRConstants.CDEBROWSER_SERVICE_LOCATOR_CLASSNAME);
       AbstractDAOFactory daoFactory = AbstractDAOFactory.getDAOFactory(locator);
       ValueDomainDAO dao = daoFactory.getValueDomainDAO();
       ValueMeaning vm = dao.getValueMeaningById(vmId);

       request.setAttribute(CaDSRConstants.VALUE_MEANING_OBJ, vm);
       request.setAttribute("CDEBrowser", "true");

       return mapping.findForward(SUCCESS);
    }

}
