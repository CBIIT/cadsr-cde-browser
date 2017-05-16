/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.cdecart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.CdeCartValidValueDAO;
import gov.nih.nci.cadsr.dao.ContextDAO;
import gov.nih.nci.cadsr.dao.DataElementDAO;
import gov.nih.nci.cadsr.dao.DataElementDerivationDAO;
import gov.nih.nci.cadsr.dao.ToolOptionsDAO;
import gov.nih.nci.cadsr.dao.VdPvsDAO;
import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.model.ValidValueCdeCartModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
import gov.nih.nci.ncicb.cadsr.common.dto.ConceptDerivationRuleTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DataElementDerivationTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DataElementDerivationTypeTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DataElementTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DerivedDataElementTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.RepresentationTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValueDomainTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.AdminComponent;
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.resource.DerivedDataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.Representation;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItemTransferObject;
import gov.nih.nci.ncicb.cadsr.objectCart.impl.CDECartOCImpl;
import gov.nih.nci.objectCart.client.ObjectCartClient;
import gov.nih.nci.objectCart.client.ObjectCartException;
import gov.nih.nci.objectCart.domain.Cart;

public class CdeCartUtil implements CdeCartUtilInterface {
    private static Logger log = LogManager.getLogger(CdeCartUtil.class.getName());

	@Autowired
	private ToolOptionsDAO toolOptionsDAO;
	@Autowired
	VdPvsDAO vdPvsDAO;
	@Autowired
	ContextDAO contextDAO;

	@Autowired
	DataElementDAO dataElementDAO;

	@Autowired
	CdeCartValidValueDAO cdeCartValidValueDAO;
	
	@Autowired
	DataElementDerivationDAO dataElementDerivationDAO;
	
	private final Integer lockToCreateCart = new Integer(1);

	public void setToolOptionsDAO(ToolOptionsDAO toolOptionsDAO) {
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

	public void setCdeCartValidValueDAO( CdeCartValidValueDAO cdeCartValidValueDAO )
	{
		this.cdeCartValidValueDAO = cdeCartValidValueDAO;
	}	
	
	public void setDataElementDerivationDAO(DataElementDerivationDAO dataElementDerivationDAO) {
		this.dataElementDerivationDAO = dataElementDerivationDAO;
	}

	//We assume this URL is not changes often as the system is running, so we will read it until it is not found
	static transient String ocURL;

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

			cdeCart = getCDECartOCImpl(ocClient, uid);
			//we need to set up this session attribute not to make the remote call again
			mySession.setAttribute(CaDSRConstants.CDE_CART, cdeCart);
			log.info("Object Cart cdeCart attribute is added to session of: " + principalName);
		}

		return cdeCart;
	}
	private CDECart getCDECartOCImpl(ObjectCartClient ocClient, String uid) {
		synchronized(lockToCreateCart) {
			return new CDECartOCImpl(ocClient, uid, CaDSRConstants.CDE_CART);
		}
	}
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.cdecart.CdeCartUtilInterface#deleteCartNodes(javax.servlet.http.HttpSession, java.lang.String, java.lang.String[])
	 */
	@Override
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
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.cdecart.CdeCartUtilInterface#findCartNodes(javax.servlet.http.HttpSession, java.lang.String)
	 */
	@Override
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
				log.info("Object Cart is not found in session: " + principalName);
				cdeCart = findCdeCart(mySession, principalName);
			}
			else {
				log.info("Object Cart is found in session: " + principalName);
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
	/* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.cdecart.CdeCartUtilInterface#addToCart(javax.servlet.http.HttpSession, java.lang.String, java.util.List)
	 */
	@Override
	public void addToCart(HttpSession mySession, String principalName, List<String> cdeIds) throws ObjectCartException, AutheticationFailureException {
		if ((cdeIds == null) || (mySession == null)) {
			log.debug("Nothing to save");
			return;
		}
		String userName = principalName;

		//we shall be after login here, and userName is never null
		if (userName == null) {
			log.error("........No user found in session in addToCart: " + userName);
			throw new AutheticationFailureException("Authenticated user not found in the session: " + userName);
		}
		
		CDECart sessionCart = (CDECart) mySession.getAttribute(CaDSRConstants.CDE_CART);

		if (sessionCart == null) {
			sessionCart = findCdeCart(mySession, principalName);
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

			if (StringUtils.isNotEmpty(ocURL))
				ocClient = new ObjectCartClient(ocURL);
			else
				ocClient = new ObjectCartClient();

			CDECart userCart = new CDECartOCImpl(ocClient, userName, CaDSRConstants.CDE_CART);

			List<DataElementModel> deModelList = dataElementDAO.getCdeByDeIdseqList(cdeIds);

			Collection<DataElementTransferObject> addCandidates = buildCartTransferObjects(deModelList);
			Collection<CDECartItem> items = new ArrayList<CDECartItem> ();

			for (DataElementTransferObject deto : addCandidates) {
				CDECartItem cartItem = sessionCart.findDataElement(deto.getIdseq());
				if (cartItem == null) {
	    			CDECartItem cdeItem = new CDECartItemTransferObject();
					cdeItem.setPersistedInd(false);//we have IDs only for items to save
					cdeItem.setItem(deto);
					items.add(cdeItem);
				}
				else {
					cartItem.setPersistedInd(true);
					items.add(cartItem);
				}
			}

			userCart.mergeDataElements(items);
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
		log.debug("buildSearchNodeList number of items built from CDECartItemTransferObject collection: " + res.size());
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

    public Collection<DataElementTransferObject> buildCartTransferObjects (List<DataElementModel> deList) {
    	ArrayList<DataElementTransferObject> resultModel = new ArrayList<DataElementTransferObject>();
    	if (deList != null) {
    		log.debug("Data to add to cart amount: " + deList.size() + deList);
    		for (DataElementModel deModel : deList) {
    			if (deModel != null) {
    				CDECartItem cdeItem = new CDECartItemTransferObject();
					cdeItem.setPersistedInd(false);//we have IDs only for items to save
					DataElementTransferObject deto = toDataElement(deModel);
					cdeItem.setItem(deto);
					resultModel.add(deto);
    			}
    		}
    	}
    	else {
    		log.debug("No data to add to cart");
    	}
    	return resultModel;
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

        //I believe this is the data stored which we need to the Question/Doc Text/Preferred Question Text
        de.setLongCDEName(deModel.getQuestion());

        de.setAslName(deModel.getAslName());
        de.setVersion(deModel.getVersion());
        de.setContextName(deModel.getContextName());
        de.setUsingContexts(deModel.getUsingContexts());
        de.setRegistrationStatus(deModel.getRegistrationStatus());
        
        //Add Derivation section 
        //FIXME this call creates a wrong format of Derived DE
//        DerivedDataElement derivedDataElement = buildDerivedDataElementTransferObject(deModel.getPublicId());
//        if (derivedDataElement != null) {
//        	de.setDerivedDataElement(derivedDataElement);
//        }
        return de;
    }
	public ValueDomainTransferObject buildValueDomainTransfer(ValueDomainModel modelVm){
		ValueDomainTransferObject dtoVd = new ValueDomainTransferObject();
    	if (modelVm == null) return dtoVd;

    	//same 15 methods
    	//setAslName, setCharSet, setCreatedBy, setDatatype, setDecimalPlace, setDeletedInd,
    	//setIdseq, setLongName, setMaxLength, setMinLength, setModifiedBy, setOrigin,
    	//setPreferredDefinition, setPreferredName, setVdIdseq
    	dtoVd.setAslName(modelVm.getAslName());
    	dtoVd.setCharSet(modelVm.getCharSet());
    	dtoVd.setCreatedBy(modelVm.getCreatedBy());
    	dtoVd.setDatatype(modelVm.getDatatype());
    	if (modelVm.getDecimalPlace() != null) {
    		dtoVd.setDecimalPlace(modelVm.getDecimalPlace().toString());
    	}

    	dtoVd.setDeletedInd(modelVm.getDeletedInd());
    	dtoVd.setIdseq(modelVm.getIdseq());
    	dtoVd.setLongName(modelVm.getLongName());
    	if (modelVm.getMaxLength() != null) {
    		dtoVd.setMaxLength(modelVm.getMaxLength().toString());
    	}
    	if (modelVm.getMinLength() != null) {
    		dtoVd.setMinLength(modelVm.getMinLength().toString());
    	}
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

       	dtoVd.setLowValue(modelVm.getLowVal());

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

    	// Add Valid Values
    	dtoVd.setValidValues(buildValidValueTransferList(modelVm.getVdIdseq()));

    	return dtoVd;
    }


	public List<ValidValueTransferObject> buildValidValueTransferList(String vdIdseq)
	{
		List<ValidValueCdeCartModel> validValueCdeCartModels = cdeCartValidValueDAO.getValueDomainTransferObjectsById(vdIdseq);

		// Convert data to objects that match what ObjectCart needs.
		List<ValidValueTransferObject> transferObjectList = new ArrayList<>(  );
		for(ValidValueCdeCartModel validValueCdeCartModel: validValueCdeCartModels)
		{
			transferObjectList.add( new ValidValueTransferObject(validValueCdeCartModel) );
		}
		return transferObjectList;
	}

/*

	public List<ValidValue> buildValidValueTransferListORIG(String vdIdseq) {
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
    				dtoVv.setShortMeaning("TEST SHORT MEANING");
    				dtoVv.setShortMeaningDescription(null);
    				dtoVv.setShortMeaningValue(null);
    				//FIXNE ValueMeaning object
    				dtoVv.setValueMeaning(null);

    				dtoVv.setVdIdseq(model.getVdIdseq());
    				dtoVv.setWorkflowstatus(null);

					// CHECKME
                    validValueList.add( dtoVv );
    			});
    		}
    	}
    	return validValueList;
    }
*/



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
    /**
     * This method finds CDE Derivation based on CDE Public ID.
     * 
     * @param deId Derived element public ID
     * @return DerivedDataElementTransferObject implementing interface DerivedDataElement
     */
    
    public DerivedDataElement buildDerivedDataElementTransferObject(int deId) {
    	DerivedDataElementTransferObject derivedDto = new DerivedDataElementTransferObject();
    	DataElementDerivationModel dataElementDerivationModel = dataElementDerivationDAO.getDataElementDerivationByCdeId(deId);
    	if (dataElementDerivationModel == null) {
    		return null;
    	}
    	//make mapping
    	DataElementDerivationTypeTransferObject dtoType = new DataElementDerivationTypeTransferObject();
    	dtoType.setName(dataElementDerivationModel.getDerivationType());
    	derivedDto.setType(dtoType);
    	derivedDto.setConcatenationCharacter(dataElementDerivationModel.getConcatenationCharacter());
    	derivedDto.setCreatedBy(dataElementDerivationModel.getCreatedBy());
    	derivedDto.setDateModified(dataElementDerivationModel.getDateModified());
    	//FIXME where this Dde ID? This model does not have it???
    	derivedDto.setDdeIdSeq(null);
    	derivedDto.setMethods(dataElementDerivationModel.getMethod());
    	derivedDto.setModifiedBy(dataElementDerivationModel.getModifiedBy());
    	derivedDto.setRule(dataElementDerivationModel.getRule());
        List<DataElementDerivationComponentModel> dataElementDerivationComponentModels = dataElementDerivationDAO.getDataElementDerivationComponentsByCdeId(deId);
        Collection<DataElementDerivationTransferObject> derivedCollection = new ArrayList<>();
        derivedDto.setDataElementDerivation(derivedCollection);
        if (dataElementDerivationComponentModels != null) {
	        DataElementDerivationTransferObject derivedItem;
	        DataElementTransferObject de;
	        for (DataElementDerivationComponentModel modelItem : dataElementDerivationComponentModels) {
	        	derivedItem = new DataElementDerivationTransferObject();
	        	de = new DataElementTransferObject();
	        	derivedItem.setDerivedDataElement(de);
	        	derivedCollection.add(derivedItem);
	        	derivedItem.setCreatedBy(modelItem.getCreatedBy());
	        	derivedItem.setDateCreated(modelItem.getDateCreated());
	        	derivedItem.setDateModified(modelItem.getDateModified());
	        	//FIXME where CDR ID? This model does not have it???
	        	derivedItem.setCdrIdSeq(null);
	        	try {
	        		int displayOrderValue = Integer.parseInt(modelItem.getDisplayOrder());
	        		derivedItem.setDisplayOrder(displayOrderValue);
	        	}
	        	catch (NumberFormatException e) {
	        		log.error("Error in DisplayOrder data in " + modelItem, e);
	        	}
	        	derivedItem.setModifiedBy(modelItem.getModifiedBy());
	        	try {
	        		int publicIdValue = Integer.parseInt(modelItem.getPublicId());
	        		derivedItem.setDisplayOrder(publicIdValue);
	        	}
	        	catch (NumberFormatException e) {
	        		log.error("Error in publicId data in " + modelItem, e);
	        	}
	        	
	            de.setIdseq(modelItem.getDeIdseq());
	            de.setLongName(modelItem.getLongName());
	            de.setAslName(modelItem.getWorkflowStatus());
	            try {
	            	de.setVersion(Float.parseFloat(modelItem.getVersion()));
	            }
	            catch(NumberFormatException e) {
	            	log.error("Error in Version data in " + modelItem, e);
	            }
	            de.setContextName(modelItem.getContext());
	            de.setDeIdseq(modelItem.getDeIdseq());
	        }//for
	    	//TODO find and map missing data
        }
    	return derivedDto;
    }
    /* This is old code from CDE Browser v.4 mapping DED and its internal DE.
    protected Object mapRow(
      ResultSet rs,
      int rownum) throws SQLException {
      DataElementDerivationTransferObject ded = new DataElementDerivationTransferObject();
      DataElementTransferObject de = new DataElementTransferObject();
      ded.setCdrIdSeq(rs.getString(1));
      de.setIdseq(rs.getString(2));
      de.setLongName(rs.getString(3));
      de.setCDEId(rs.getString(4));
      de.setAslName(rs.getString(5));
      de.setVersion(new Float(rs.getFloat(6)));
      ded.setDisplayOrder(rs.getInt(7));
      de.setContextName(rs.getString(8));
      de.setDeIdseq(rs.getString(9));
      ded.setDerivedDataElement(de);
      return ded;
    }
     */
    /*//CDEBROWSER-280 Derived requirement taking from XML Download. OC and XML Download are not the same XMLs.
<ComponentDataElementsList_ITEM>
<PublicId>2341957</PublicId>
<LongName>Address Secondary Unit Indicator/Designator Number</LongName>
<PreferredName>ADDR_SEC_U_DES_NUM</PreferredName>
<PreferredDefinition>A component of an address that specifies a location by identification of an additional subdivision of the primary address by number.</PreferredDefinition>
<Version>1</Version>
<WorkflowStatus>RELEASED</WorkflowStatus>
<ContextName>NCIP</ContextName>
<DisplayOrder>8</DisplayOrder>
</ComponentDataElementsList_ITEM>
     */
}
