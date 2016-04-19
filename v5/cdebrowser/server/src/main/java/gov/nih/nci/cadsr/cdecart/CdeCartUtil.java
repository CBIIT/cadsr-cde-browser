/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.cdecart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.ContextDAO;
import gov.nih.nci.cadsr.dao.DataElementDAO;
import gov.nih.nci.cadsr.dao.ToolOptionsDAOImpl;
import gov.nih.nci.cadsr.dao.VdPvsDAO;
import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
import gov.nih.nci.cadsr.dao.model.VdPvsModel;
import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
import gov.nih.nci.ncicb.cadsr.common.dto.ConceptDerivationRuleTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DataElementTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.RepresentationTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValueDomainTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.AdminComponent;
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.resource.Representation;
import gov.nih.nci.ncicb.cadsr.common.resource.ValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItemTransferObject;
import gov.nih.nci.ncicb.cadsr.objectCart.impl.CDECartOCImpl;
import gov.nih.nci.objectCart.client.ObjectCartClient;
import gov.nih.nci.objectCart.client.ObjectCartException;
import gov.nih.nci.objectCart.domain.Cart;

public class CdeCartUtil {
    private static Logger log = LogManager.getLogger(CdeCartUtil.class.getName());

	@Autowired
	private ToolOptionsDAOImpl toolOptionsDAO;
	@Autowired
	VdPvsDAO vdPvsDAO;
	@Autowired
	ContextDAO contextDAO;
	
	@Autowired
	DataElementDAO dataElementDAO;
	
	public void setToolOptionsDAO(ToolOptionsDAOImpl toolOptionsDAO) {
		this.toolOptionsDAO = toolOptionsDAO;
	}

	public void setVdPvsDAO(VdPvsDAO vdPvsDAO) {
		this.vdPvsDAO = vdPvsDAO;
	}
	public void setContextDAO(ContextDAO contextDAO) {
		this.contextDAO = contextDAO;
	}
	public void setDataElementDAO(DataElementDAO dataElementDAO) {
		this.dataElementDAO = dataElementDAO;
	}

	//We assume this URL is not changes often as the system is running, so we will read it until it is not found
	public static transient String ocURL;

	protected CDECart findCdeCart(final HttpSession mySession, final String principalName) throws ObjectCartException, AutheticationFailureException {
		CDECart cdeCart = (CDECart) mySession.getAttribute(CaDSRConstants.CDE_CART);
		String uid = null;
		if (cdeCart == null) {
			if (ocURL == null) {//try to get it again for a chance fixed in DB
				ToolOptionsModel cdeCartOptionsModel = toolOptionsDAO.getToolOptionsByToolNameAndProperty("ObjectCartAPI", "URL");
				if (cdeCartOptionsModel != null) {
					ocURL = cdeCartOptionsModel.getValue();
				}
				if (ocURL == null) {
					log.warn("Cannot get a valid value of ObjectCart URL from the system configuration");
				}
				else {
					log.debug("Found Object Cart URL:" + ocURL);
				}
			}
			
			ObjectCartClient ocClient = null;

			if (ocURL != null)
				ocClient = new ObjectCartClient(ocURL);
			else
				ocClient = new ObjectCartClient();
			
			uid = principalName;

			//we shall be after login here, and uid is never null
			if (uid == null) {
				log.error("........No user found in session in findCdeCart");
				throw new AutheticationFailureException("Authenticated user not found in the session");
			}

			cdeCart = new CDECartOCImpl(ocClient, uid, CaDSRConstants.CDE_CART);
			//we need to set up this session attribute not to make the remote call again
			mySession.setAttribute(CaDSRConstants.CDE_CART, cdeCart);
			@SuppressWarnings("rawtypes")
			Collection col = cdeCart.getDataElements();
			String toPrint = (col != null) ? "" + col.size() : "0";
			log.debug("Object Cart is retrived from remote site; # of objects: " + toPrint);
		}
		
		return cdeCart;
	}
	/**
	 * This function is called form the controller to delete CDEs from the user object cart.
	 * 
	 * @param mySession
	 * @param principalName
	 * @param ids
	 * @throws Exception
	 */
	public void deleteCartNodes (final HttpSession mySession, final String principalName, final String[] ids) throws Exception {
		if ((ids == null) || (ids.length == 0)) {
			log.warn("Nothing to delete no ID received");
			return;
		}
		
		if (mySession == null) {
			//this shall never happen; controller provides the session
			log.warn("Session is not found");
			return;
		}

		//we shall be after login here, and principalName is never null
		if (principalName == null) {
			log.error("........No user found in session in findCartNodes");
			throw new AutheticationFailureException("Authenticated user not found in the session");
		}
		try {
			if (ocURL == null) {//try to get it again for a chance fixed in DB
				ToolOptionsModel cdeCartOptionsModel = toolOptionsDAO.getToolOptionsByToolNameAndProperty("ObjectCartAPI", "URL");
				if (cdeCartOptionsModel != null) {
					ocURL = cdeCartOptionsModel.getValue();
				}
				if (ocURL == null) {
					log.warn("Cannot get a value of ObjectCart URL from the system configuration");
				}
			}
			
			ObjectCartClient ocClient = null;
	
			if (!ocURL.equals(""))
				ocClient = new ObjectCartClient(ocURL);
			else
				ocClient = new ObjectCartClient();
			
			//Get the cart in the session
			CDECart sessionCart = (CDECart) mySession.getAttribute(CaDSRConstants.CDE_CART);
			
			CDECart userCart = new CDECartOCImpl(ocClient, principalName, CaDSRConstants.CDE_CART);
			
			Collection<String> items = Arrays.asList(ids);

			sessionCart.removeDataElements(items);
			userCart.removeDataElements(items);	
		}
		catch (ObjectCartException oce){
			log.error("Exception on cdeCart.getDataElements", oce);
			throw oce;
		}
		
	}
	/**
	 * This is a method to support Controller retrieve operation.
	 * 
	 * @param mySession
	 * @param principalName
	 * @return List<SearchNode>
	 * @throws Exception
	 */
	public List<SearchNode> findCartNodes(HttpSession mySession, String principalName) throws Exception{
		
		String uid = principalName;

		//we shall be after login here, and uid is never null
		if (uid == null) {
			log.error("........No user found in session in findCartNodes");
			throw new AutheticationFailureException("Authenticated user not found in the session");
		}

		//we want to keep this cart in session not to retrieve it every time
		CDECart cdeCart = (CDECart) mySession.getAttribute(CaDSRConstants.CDE_CART);
		List<SearchNode> arr = null;

		try{
			if (cdeCart == null) {
				cdeCart = findCdeCart(mySession, principalName);
			}
			else {
				@SuppressWarnings("rawtypes")
				Collection col = cdeCart.getDataElements();
				String toPrint = (col != null) ? "" + col.size() : "0";
				log.debug("Object Cart is found in session; # of objects: " + toPrint);
			}
			//We use either retrieved cart or found in the session cart to build the result for the client page
			@SuppressWarnings("rawtypes")
			Collection col = cdeCart.getDataElements();
			
			arr = buildSearchNodeList(col);
			return arr;
		}
		catch (ObjectCartException oce){
			log.error("Exception on cdeCart.getDataElements", oce);
			throw oce;
		}
	}
	/**
	 * This is a method to support Controller POST operation.
	 * 
	 * @param mySession
	 * @param sessionCart
	 * @param principalName
	 * @throws ObjectCartException 
	 * @throws AutheticationFailureException 
	 */
	public void addToCart(HttpSession mySession, String principalName, List<String> cdeIds) throws ObjectCartException, AutheticationFailureException {
		if ((cdeIds == null) || (mySession == null)) {
			log.debug("Nothing to save");
			return;
		}
		
		CDECart sessionCart = (CDECart) mySession.getAttribute(CaDSRConstants.CDE_CART);
		
		if (sessionCart == null) {
			sessionCart = findCdeCart(mySession, principalName);
		}
		
		String userName = principalName;

		//we shall be after login here, and userName is never null
		if (userName == null) {
			log.error("........No user found in session in addToCart");
			throw new AutheticationFailureException("Authenticated user not found in the session");
		}
		
		try {
			if (ocURL == null) {//try to get it again for a chance fixed in DB
				ToolOptionsModel cdeCartOptionsModel = toolOptionsDAO.getToolOptionsByToolNameAndProperty("ObjectCartAPI", "URL");
				if (cdeCartOptionsModel != null) {
					ocURL = cdeCartOptionsModel.getValue();
				}
				if (ocURL == null) {
					log.warn("Cannot get a value of ObjectCart URL from the system configuration");
				}
			}
			
			ObjectCartClient ocClient = null;
	
			if (!ocURL.equals(""))
				ocClient = new ObjectCartClient(ocURL);
			else
				ocClient = new ObjectCartClient();
			
			CDECart userCart = new CDECartOCImpl(ocClient, userName, CaDSRConstants.CDE_CART);
	
			List<DataElementModel> deModelList = dataElementDAO.getCdeByDeIdseqList(cdeIds);
			
			buildCartTransferObjects(deModelList, sessionCart);
	
			userCart.mergeCart(sessionCart);
		}
		catch (ObjectCartException oce){
			log.error("Exception on cdeCart.getDataElements", oce);
			throw oce;
		}
	}
	
	public Cart buildObjectCart(ObjectCartClient client, String uid, String cName) {
		Cart oCart = new Cart();

		try {
			oCart = client.createCart(uid, CaDSRConstants.CDE_CART);
		} catch (ObjectCartException oce) {
			throw new RuntimeException("Constructor: Error creating the Object Cart ", oce);
		}
		return oCart;
	}
	
	@SuppressWarnings("unchecked")
	protected List<SearchNode> buildSearchNodeList(@SuppressWarnings("rawtypes") Collection col) {
		List<SearchNode> res = new ArrayList<SearchNode>();
		col.forEach((obj) -> {
			if (obj instanceof CDECartItemTransferObject) {
				CDECartItemTransferObject curr = (CDECartItemTransferObject)obj;
				AdminComponent admin = curr.getItem();
				
				SearchNode searchNode = buildSearchModel(admin);
				res.add(searchNode);
			}
		});
		return res;
	}

	protected SearchNode buildSearchModel(AdminComponent admin) {
		SearchNode res = new SearchNode();
		res.setLongName(admin.getLongName());

		DataElementTransferObject dtoItem;
		if (admin instanceof DataElementTransferObject) {
			dtoItem = (DataElementTransferObject) admin;
			res.setOwnedBy(dtoItem.getContextName());
			res.setPreferredQuestionText(dtoItem.getLongCDEName());
		}

		res.setRegistrationStatus(admin.getRegistrationStatus());
		res.setWorkflowStatus(admin.getAslName());
		res.setPublicId(admin.getPublicId());
		res.setVersion(""+admin.getVersion());
		res.setDeIdseq(admin.getIdseq());
		
		//printAdminComponent(admin);
		return res;
	}
	
    public void buildCartTransferObjects (List<DataElementModel> deList, CDECart cdeCart) {
    	if ((deList == null) || (cdeCart == null)) {
    		log.debug("No data to add to cart");
    		return;
    	}
    	deList.forEach((deModel) -> {
    		if (deModel != null) {
    			CDECartItem cdeItem = new CDECartItemTransferObject();
				cdeItem.setPersistedInd(true);//we have IDs only for items to save
				DataElementTransferObject deto = toDataElement(deModel);
				cdeItem.setItem(deto);
    			cdeCart.setDataElement(cdeItem);
    		}
    	});
    }
    
    public DataElementTransferObject toDataElement(DataElementModel deModel) {
    	//this is the code modified from v.4.0.5 DTOTransformer DTOTransformer.toDataElement
    	//we need to assign all the data for compatibility
        DataElementTransferObject de = new DataElementTransferObject();
        if (deModel == null) return de;
        
        de.setDeIdseq(deModel.getDeIdseq().trim());
        de.setPreferredName(deModel.getPreferredName());
        de.setPublicId(deModel.getPublicId());
        de.setLongName(deModel.getLongName());
        de.setPreferredDefinition(deModel.getPreferredDefinition());
        ValueDomainModel vdm = deModel.getValueDomainModel();
        
        //This was commented in v.4.0.5 I keep in here for consistency
        //List values = toPermissibleValueList(vd);
        //vd.setValidValues(values);
        de.setValueDomain(buildValueDomainTransfer(vdm));

        //FIXME implement
//        ContextModel cm = deModel.getContext();
        de.setContext(null);
        deModel.getContext();
        
        de.setLongCDEName(deModel.getPreferredName());
        de.setAslName(deModel.getAslName());
        de.setVersion(deModel.getVersion());
        de.setContextName(deModel.getContextName());
        de.setUsingContexts(deModel.getUsingContexts());
        de.setRegistrationStatus(deModel.getRegistrationStatus());

        return de;
    }
    public ValueDomain buildValueDomainTransfer(ValueDomainModel modelVm){
    	ValueDomain dtoVd = new ValueDomainTransferObject();
    	if (modelVm == null) return dtoVd;
    	
    	//same 15 methods
    	//setAslName, setCharSet, setCreatedBy, setDatatype, setDecimalPlace, setDeletedInd, 
    	//setIdseq, setLongName, setMaxLength, setMinLength, setModifiedBy, setOrigin, 
    	//setPreferredDefinition, setPreferredName, setVdIdseq
    	dtoVd.setAslName(modelVm.getAslName());
    	dtoVd.setCharSet(modelVm.getCharSet());
    	dtoVd.setCreatedBy(modelVm.getCreatedBy());
    	dtoVd.setDatatype(modelVm.getDatatype());
    	dtoVd.setDecimalPlace(""+modelVm.getDecimalPlace());
    	dtoVd.setDeletedInd(modelVm.getDeletedInd());
    	dtoVd.setIdseq(modelVm.getIdseq());
    	dtoVd.setLongName(modelVm.getLongName());
    	dtoVd.setMaxLength(""+modelVm.getMaxLength());
    	dtoVd.setMinLength(""+modelVm.getMinLength());
    	dtoVd.setModifiedBy(modelVm.getModifiedBy());
    	dtoVd.setOrigin(modelVm.getOrigin()); 
    	dtoVd.setPreferredDefinition(modelVm.getPreferredDefinition());
    	dtoVd.setPreferredName(modelVm.getPreferredName());
    	dtoVd.setVdIdseq(modelVm.getVdIdseq());
    	
    	//setDateCreated, setDateModified
    	dtoVd.setDateCreated(modelVm.getDateCreated());
    	dtoVd.setDateModified(modelVm.getDateModified());
    	//different 17
    	//setConceptDerivationRule, setContacts, setConteIdseq, setContext, setDefinitions, setDesignations, 
    	//setDisplayFormat, setHighValue, setLatestVersionInd, setLowValue, setPublished, setReferenceDocs, 
    	//setRegistrationStatus, setRepresentation, setUnitOfMeasure, setVDType, setValidValues
    	
    	//FIXME we have not find all the data for ConceptDerivationRule
    	dtoVd.setConceptDerivationRule(buildConceptDerivationRuleTransfer(modelVm.getConceptDerivationRuleModel()));
    	
    	//FIXME expected List<Contact> contacts do not see in v5
    	dtoVd.setContacts(null);
    	
    	//FIXME take it from ContextTransferObject
    	dtoVd.setConteIdseq(null);
    	
    	//FIXME I do not see in VDM ???
    	dtoVd.setContext(null);
    	
    	//FIXME
    	dtoVd.setDefinitions(null);
    	
    	//FIXME
    	dtoVd.setDesignations(null);
    	
    	dtoVd.setDisplayFormat(modelVm.getDispFormat());
    	
    	dtoVd.setHighValue(modelVm.getHighVal());
    	
    	dtoVd.setLatestVersionInd(modelVm.getLatestVerInd());

       	dtoVd.setLatestVersionInd(modelVm.getLatestVerInd());
    	
       	dtoVd.setLowValue(""+modelVm.getLowVal());
       	
       	//FIXME What is this data?
       	dtoVd.setPublished(false);
       	
       	//FIXME
       	dtoVd.setReferenceDocs(null);
       
       	//FIXME
    	dtoVd.setRegistrationStatus(null);
    	
    	//FIXME
    	RepresentationModel representationModel = modelVm.getRepresentationModel();
    	
    	dtoVd.setRepresentation(buildRepresentationTransfer(representationModel)); //modelVm.getRepresentationModel()
    	
    	//FIXME
    	dtoVd.setUnitOfMeasure(null);
    	
    	dtoVd.setVDType(modelVm.getVdType());
    	
    	//FIXME where is is list in ValueDomainModel
    	dtoVd.setValidValues(buildValidValueTransferList(modelVm.getVdIdseq()));
    	
    	return dtoVd;
    }
    public List<ValidValue> buildValidValueTransferList(String vdIdseq) {
    	java.text.SimpleDateFormat simpleDate = new java.text.SimpleDateFormat("yyyy-MM-dd");

    	List<ValidValue> validValueList = new ArrayList<ValidValue>();
    	if (vdIdseq != null) {
        	List<VdPvsModel> modelList = vdPvsDAO.getVdPvs(vdIdseq);
    		if ((modelList != null) && (!(modelList.isEmpty()))) {
    			modelList.forEach((model) -> {
    				ValidValue dtoVv = new ValidValueTransferObject();
    				if (model.getBeginDate() != null)
    					dtoVv.setBeginDate(simpleDate.format(model.getBeginDate()));
    				if (model.getEndDate() != null)
    					dtoVv.setEndDate(simpleDate.format(model.getEndDate()));
    				//FIXME all the fields need to be re-visited especially null
    				dtoVv.setConceptDerivationRule(null);//ConceptDerivationRuleModel.java
    				String contextIdseq = model.getConteIdseq();
    				if (contextIdseq != null) {
    					//set up Context related properties
    					ContextModel contextModel = contextDAO.getContextByIdseq(contextIdseq);
    					dtoVv.setContext(contextModel.getName());
    					dtoVv.setDescription(contextModel.getDescription());
    				}
    				dtoVv.setShortMeaning(null);
    				dtoVv.setShortMeaningDescription(null);
    				dtoVv.setShortMeaningValue(null);
    				//FIXNE ValueMeaning object
    				dtoVv.setValueMeaning(null); 
    				
    				dtoVv.setVdIdseq(model.getVdIdseq());
    				dtoVv.setWorkflowstatus(null);
    			});
    		}
    	}
    	return validValueList;
    }
    public ConceptDerivationRule buildConceptDerivationRuleTransfer(ConceptDerivationRuleModel model) {
    	ConceptDerivationRule rule = new ConceptDerivationRuleTransferObject();
    	if (model == null) return rule;
    	//FIXME clean up required; not a complete map
    	rule.setIdseq(model.getCondrIdseq());
    	//FIXME where this data comes from?
    	rule.setComponentConcepts(null);
    	rule.setConcatenationChar(model.getConcatChar());
    	rule.setMethods(model.getMethods());
    	//FIXME where does type come from?
    	rule.setType(null);
    	rule.setName(model.getName());
    	return rule;
    }
    public Representation buildRepresentationTransfer(RepresentationModel model) {
    	//FIXME this mapping is not full: not all the AdminComponent methods are called check if we need them
    	Representation representation = new RepresentationTransferObject();
    	if (model == null) return representation;
    	representation.setIdseq(model.getIdseq());
    	representation.setLongName(model.getLongName());
    	representation.setModifiedBy(model.getModifiedBy());
    	representation.setDateCreated(model.getDateCreated());
    	representation.setDateModified(model.getDateModified());
    	representation.setPreferredName(model.getPreferredName());
    	
    	ContextModel contextModel = model.getContext();
    	if (contextModel != null) {
    		representation.setPreferredDefinition(contextModel.getPreferredDefinition());
    		representation.setVersion(contextModel.getVersion());
    		representation.setName(contextModel.getName());
    		//FIXME what else comes from ContextModel
    	}
    	ConceptDerivationRuleModel conceptDerivationRuleModel = model.getConceptDerivationRuleModel();
    	if (conceptDerivationRuleModel != null) {
    		representation.setConceptDerivationRule(buildConceptDerivationRuleTransfer(conceptDerivationRuleModel));
    	}
    	//FIXME where this data comes form
    	representation.setRegistrationStatus(null);

    	return representation;
    }

}
