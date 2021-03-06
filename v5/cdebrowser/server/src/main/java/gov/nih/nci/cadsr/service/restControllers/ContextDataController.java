package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import gov.nih.nci.cadsr.common.AppConfig;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.UsageLog;
import gov.nih.nci.cadsr.dao.ClassificationSchemeDAO;
import gov.nih.nci.cadsr.dao.ContextDAO;
import gov.nih.nci.cadsr.dao.CsCsiDAO;
import gov.nih.nci.cadsr.dao.ProtocolDAO;
import gov.nih.nci.cadsr.dao.ProtocolFormDAO;
import gov.nih.nci.cadsr.dao.model.ClassificationSchemeModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.dao.model.ProtocolFormModel;
import gov.nih.nci.cadsr.dao.model.ProtocolModel;
import gov.nih.nci.cadsr.service.model.context.BaseNode;
import gov.nih.nci.cadsr.service.model.context.ClassificationItemNode;
import gov.nih.nci.cadsr.service.model.context.ClassificationNode;
import gov.nih.nci.cadsr.service.model.context.ContextNode;
import gov.nih.nci.cadsr.service.model.context.ParentNode;
import gov.nih.nci.cadsr.service.model.context.ProtocolFormNode;
import gov.nih.nci.cadsr.service.model.context.ProtocolNode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ContextDataController
{

    private static Logger logger = LogManager.getLogger( ContextDataController.class.getName() );

    @Autowired
    private ContextDAO contextDAO;

    @Autowired
    private ClassificationSchemeDAO classificationSchemeDAO;

    @Autowired
    private CsCsiDAO csCsiDAO;

    @Autowired
    private ProtocolFormDAO protocolFormDAO;

    @Autowired
    private ProtocolDAO protocolDAO;

    private List<ProgramAreaModel> programAreaModelList = null;

    private RestControllerCommon restControllerCommon;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UsageLog usageLog;

    private static final Integer rootLevel = 1;

	private static final String TEXT_PLAIN_MIME_TYPE = null;

    @Autowired
    public ContextDataController(RestControllerCommon restControllerCommon)
    {
        this.restControllerCommon = restControllerCommon;
        if (restControllerCommon != null) {
	    	try
	        {
	            programAreaModelList = restControllerCommon.getProgramAreaList();
	            logger.info("Loaded Program Areas from database in amount: " + programAreaModelList.size() + programAreaModelList);
	        } catch( Exception e )
	        {
		    	try
		        {
		            Thread.sleep(1000);
		    		programAreaModelList = restControllerCommon.getProgramAreaList();
		            logger.info("Loaded Program Areas from database in amount: " + programAreaModelList.size() + programAreaModelList);
		        } catch( Exception ex )
		        {
		            logger.error( "Server Error:\nCould not retrieve Program Areas from database on the second attempt", ex );
		        }
	        	if (CollectionUtils.isEmpty(programAreaModelList)) {
	        		logger.error( "Server Error: Could not retrieve Program Areas from database");
	        		programAreaModelList = new ArrayList<>();
	        	}
	        }
        }
        else {
        	logger.info( "Server Error: RestControllerCommon Spring framework component is null; cannot populate programAreaModelList");
        	programAreaModelList = new ArrayList<>();
        }
    }

    @RequestMapping( value = "/contextData" )
    @ResponseBody
    public ContextNode[] contextData()
    {
        logger.debug( "Received rest call \"contextData\"" );
        // Get Program Areas
        if (CollectionUtils.isEmpty(programAreaModelList))
        {
        	synchronized(this) {
            	if ((CollectionUtils.isEmpty(programAreaModelList)) && restControllerCommon != null) {
            		programAreaModelList = restControllerCommon.getProgramAreaList();
            	}
            }
            if (CollectionUtils.isEmpty(programAreaModelList)) {
	            ContextNode[] errorNode = new ContextNode[1];
	            errorNode[0] = createErrorNode( "Server Error:\nCould not retrieve Program Areas from database", new Exception("Program Areas failed to be retrieved"), ContextNode.class );
	            return errorNode;
            }
        }

        // Get the (only) top level Context data
        ContextNode[] contextNodes;
        try
        {
            contextNodes = getAllTopLevelTreeData();
        } 
        catch(IndexOutOfBoundsException e) {
        	logger.info( "IndexOutOfBoundsException on retrieve Top Level Context data from database, reloading all program areas from DB. ", e );
        	synchronized(this) {
        		programAreaModelList = restControllerCommon.getProgramAreaList();
        	}
        	contextNodes = getAllTopLevelTreeData();
        }
        catch (Exception e)
        {
            logger.error( "Server Error:\nCould not retrieve Top Level Context data from database. ", e );
            ContextNode[] errorNode = new ContextNode[1];
            errorNode[0] = createErrorNode( "Server Error:\nCould not retrieve Top Level Context data from database. ", e, ContextNode.class );
            return errorNode;
        }

        logger.debug( "Done rest call contextData" );
        return contextNodes;
    }
    
    
    
    /**
     * Checks database's health status by retrieving the top level tree data
     * @return ResponseEntity with 'CDEBrowser Status : OK' or 'ERROR' for database availability
     */    
    @RequestMapping( value = "/monitordb" )
    @ResponseBody
    public ResponseEntity<String> monitorDbCheck()
    {
    	ContextNode[] contextNodes;
		try {
			contextNodes = getAllTopLevelTreeData();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Content-Type", ContextDataController.TEXT_PLAIN_MIME_TYPE);
			return new ResponseEntity<String>("CDEBrowser Status : OK", httpHeaders, HttpStatus.OK);
		}
		catch (Exception hcee){
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Content-Type", ContextDataController.TEXT_PLAIN_MIME_TYPE);
			logger.error("ERROR");
			return new ResponseEntity<String>("ERROR", httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}  	
    }    

    
    /**
     * @param contextId   The one Context who's tree we need to return to the client.
     * @param programArea We already have the original Program area, but this will tell us if it is being called from "All"
     * @param folderType  Is it a "Classifications" or "Protocol Forms" folder? We us this to determine which should be collapsed, and which oopen
     * @return The complete Context tree from "Classifications" and "Protocol Forms" down.
     */
    @RequestMapping( value = "/oneContextData" )
    @ResponseBody
    public List<ParentNode> oneContextData( @RequestParam( "contextId" ) String contextId, @RequestParam( "programArea" ) int programArea, @RequestParam( "folderType" ) int folderType )
    {
        logger.debug( "Received rest call \"oneContextData\" [" + contextId + "]   Program Area[" + programArea + "]   FolderType[" + folderType + "]" );
        programAreaModelList = restControllerCommon.getProgramAreaList();
        List<ParentNode> contextTree = getOneTreeByContextId( contextId, programArea, folderType );

        usageLog.log( "oneContextData",  "contextId=" + contextId + "programArea=" + programArea + "folderType=" + folderType +" [" + contextTree.size() + " results returned]" );
        return contextTree;
    }

    @RequestMapping( value = "/programAreaNames" )
    @ResponseBody
    public List<String> retrieveProgramAreas()
    {
        List<String> results = new ArrayList<>(  );

        results.add( new String("All Program Areas") );
        results.addAll( programAreaModelList.stream().map( ProgramAreaModel::getPalName ).collect( Collectors.toList() ) );
        logger.debug("returning Program Areas RESTful call result: " + results);
        return results;
    }

    private ContextNode[] getAllTopLevelTreeData()
    {
        // The top level - Program areas
        ContextNode[] contextNodes = initTopLevelContextNodes();

        // Get list of all Contexts
        List<ContextModel> contextModelList = this.contextDAO.getAllContexts();

        // Process each Context
        for( ContextModel model : contextModelList )
        {
            // Get Program area for this Context
            int programAreaIndex = getProgramAreaByName( model.getPalName() );


            ////////////////////////////////////////
            // This Contexts Classification folder
            ParentNode classificationsParentNode = new ParentNode();
            initClassificationsParentNode( classificationsParentNode, programAreaIndex, true ); //true = collapsed folder

            // Add the data for the rest call back to get the complete data tree for this context
            classificationsParentNode.setHref( appConfig.getOneContextRestServiceName() +
                    "," + model.getConteIdseq() +
                    "," + getProgramAreaByName( model.getPalName() ) +
                    "," + CaDSRConstants.CLASSIFICATIONS_TYPE_FOLDER );

            //logger.debug( "Checking for Classification Schemes - " + model.getName() );
            // Is there at least one classification?
            if( this.classificationSchemeDAO.haveClassificationSchemes( model.getConteIdseq() ) )
            {
                insertPlaceHolderNode( classificationsParentNode );
            }

            ////////////////////////////////////////
            // This Contexts Protocol forms folder
            ParentNode protocolsParentNode = new ParentNode();
            initProtocolsFormsParentNode( protocolsParentNode, programAreaIndex, true ); //true = collapsed folder

            // Add the data for the rest call back to get the complete data tree for this context - same as the "Classifications"
            protocolsParentNode.setHref( appConfig.getOneContextRestServiceName() +
                    "," + model.getConteIdseq() +
                    "," + getProgramAreaByName( model.getPalName() ) +
                    "," + CaDSRConstants.PROTOCOLFORMS_TYPE_FOLDER );

            // If protocolModelList > 0 then we have at least one ProtocolForms, add a dummy to make sure we show the Protocol Icon as enabled
            if( this.protocolDAO.haveProtocolsByContext( model.getConteIdseq() ) )
            {
                insertPlaceHolderNode( protocolsParentNode );
            }

            // This parent Context node
            ContextNode contextNodeParent = new ContextNode( model, programAreaIndex );

            // Add Classifications and ProtocolForms to this Context node
            contextNodeParent.addChildNode( classificationsParentNode );
            contextNodeParent.addChildNode( protocolsParentNode );

            // Add this parent Context node to the top node which is the Program Area.
            contextNodes[programAreaIndex].addTopNode( contextNodeParent );

            // Set the tabs hover text to the caption for this Program area, which will be shown below the selected tab in the client.
            contextNodes[programAreaIndex].setHover( getProgramAreaDescriptionByIndex( programAreaIndex ) );

            //Also add to "All" which is the first (0) program area.
            ContextNode contextNodeForAll = new ContextNode( contextNodeParent );
            contextNodeForAll.setProgramArea( 0 );
            setHrefProgramArea( contextNodeForAll, 0 );


            contextNodes[0].addTopNode( contextNodeForAll );
            contextNodes[0].setHover(CaDSRConstants.ALL_CONTEXTS_STRING);
        }

        // Set bread crumb data
        for( int f = 0; f < contextNodes.length; f++ )
        {
            addBreadCrumbs( contextNodes[f], null );
        }

        // Set bread crumb data for Contexts in "All" Program Area
        addBreadCrumbsAll( contextNodes[0], CaDSRConstants.ALL_CONTEXTS_STRING );

        return contextNodes;
    }


    /**
     * @param contextId   Which Context tree to return.
     * @param programArea We already have the original Program area, but this will tell us if it is being called from "All"
     * @param folderType  0 = classifications folder, 1 = protocol forms folder
     * @return The complete Context tree from "Classifications" and "Protocol Forms" down.
     */
    private List<ParentNode> getOneTreeByContextId( String contextId, int programArea, int folderType )
    {
        // The top level - Program areas
        ContextNode[] contextNodes = initTopLevelContextNodes();

        // Get this Context from the database
        ContextModel model = this.contextDAO.getContextByIdseq( contextId );

        // This parent Context node
        ContextNode contextNodeParent = new ContextNode( model, programArea );

        ////////////////////////////////////////
        // This Context's Classifications or ProtocolForms folder
        ParentNode parentNode = new ParentNode();


        if( folderType == CaDSRConstants.CLASSIFICATIONS_TYPE_FOLDER )
        {
            // The contents for the Classifications folder - the Classification Scheme, which is the list of individual Classifications.
            List<ClassificationSchemeModel> csModelList = this.classificationSchemeDAO.getAllClassificationSchemes(  );

            // Classification Scheme Item List - This is a list of all individual items that are in the Classifications of this Context
            // they will be place in their correct Classification later in insertClassifications.
            List<CsCsiModel> csCsiNodelList = this.csCsiDAO.getCsCsisByConteId( contextId );

            // Last parameter is "Collapsed" should be false if folderType is Classifications
            initClassificationsParentNode( parentNode, programArea, false );

            //Add the Classifications to the parentNode for this Context.
            insertClassifications( parentNode, csModelList, model, programArea, csCsiNodelList);
        }
        else if( folderType == CaDSRConstants.PROTOCOLFORMS_TYPE_FOLDER )
        {
            List<ProtocolModel> protocolModelList = this.protocolDAO.getProtocolsByContext( model.getConteIdseq() );
            // Last parameter is "Collapsed" should be false
            initProtocolsFormsParentNode( parentNode, programArea, false );
            
            //CDEBROWSER-440 find all protocol IDSEQs in this context
            List<String> protoIdseqList = protocolModelList.stream().map(sc -> sc.getProtoIdseq()).collect(Collectors.toList());
            
        	//CDEBROWSER-440 All ProtocolForms for this context's protocol list. We ignore Forms' Context.
            List<ProtocolFormModel> protocolFormModelList = this.protocolFormDAO.getProtocolFormListByProtoIdseqList(protoIdseqList);

            //Populate each Protocol with their ProtocolForms.
            initProtocolModels(protocolModelList, protocolFormModelList, parentNode, programArea );
//            try {
//            	logger.debug( "parentNode " + parentNode.getChildren().get( 0 ).getChildren().get( 0 ).toString() );
//            }
//            catch (IndexOutOfBoundsException ex) {
//            	logger.debug( "...parentNode has no children after initProtocolModels");
//            }
        }
        // Add Classifications or ProtocolForms to this Context node
        contextNodeParent.addChildNode( parentNode );

        // Add this parent Context node to the top node which is the Program Area.
        contextNodes[programArea].addTopNode( contextNodeParent );

        // Set the tabs hover text to the caption for this Program area, which will be shown below the selected tab in the client.
        contextNodes[programArea].setHover( getProgramAreaDescriptionByIndex( programArea ) );
        //logger.debug( "programArea: " + programArea );
        if( programArea == 0 ) //All
        {

            addBreadCrumbsAll( contextNodes[0], CaDSRConstants.ALL_CONTEXTS_STRING );
        }
        else
        {
            addBreadCrumbs( contextNodes[programArea], null );
        }

        //Now that we have complete Bread Crumbs, we only need to send the parentNode and protocolsParentNode.
        ArrayList<ParentNode> classificationsAndProtocolForms = new ArrayList<>();
        classificationsAndProtocolForms.add( parentNode );

        return classificationsAndProtocolForms;
    }

    /**
     * Populate each Protocol with it's Protocol Forms and add to the Parent Protocol node (the "Protocol Forms" folder)
     *
     * @param protocolModelList     The list of Protocols (for one Context)
     * @param protocolFormModelList List of all the Protocol Forms (for this one Context)
     * @param protocolsParentNode   The parent "Protocol Forms" node/folder
     * @param programArea           Program Area for this Context/Protocol
     */
    protected void initProtocolModels( List<ProtocolModel> protocolModelList, List<ProtocolFormModel> protocolFormModelList, ParentNode protocolsParentNode, int programArea )
    {

        // Populate each Protocol with their ProtocolForms
        for( ProtocolModel protocolModel : protocolModelList )
        {
            // The node for this individual Protocol
            ProtocolNode protocolNode = initProtocolNode( protocolModel, programArea );
            protocolNode.setHref( appConfig.getCdesByProtocolRestServiceName() + "," + protocolModel.getProtoIdseq() );

            // Add the ProtocolForms to this Protocol node
            String protoId = protocolModel.getProtoIdseq();
            for( ProtocolFormModel protocolFormModel : protocolFormModelList )
            {
                String protoFormId = protocolFormModel.getProtoIdseq();
                if( protoFormId != null && protoFormId.compareTo( protoId ) == 0 )
                {
                    ProtocolFormNode protocolFormNode = initProtocolFormNode( protocolFormModel, programArea );
                    //logger.debug( "Setting ProtocolForm href: " + protocolFormModel.getQcIdseq() );
                    protocolFormNode.setHref( appConfig.getCdesByProtocolFormRestServiceName() + "," + protocolFormModel.getQcIdseq() );

                    protocolNode.addChildNode( protocolFormNode );
                }
            }

            // Some Protocols have no ProtocolForms, if there are NO protocol forms for this protocol, don't add him.
            if( protocolNode.isIsParent() )
            {
                protocolsParentNode.addChildNode( protocolNode );
            }
        }
    }

    /**
     * Set up the top level Context nodes, which are the program areas.
     * This will be the first call when constructing the Context nodes.
     *
     * @return Array of top level nodes, which are the Program Areas.
     */
    protected ContextNode[] initTopLevelContextNodes()
    {
        ContextNode[] contextNodes = new ContextNode[programAreaModelList.size() + 1];//The + 1 is for "All"
        for( int i = 0; i < programAreaModelList.size(); i++ )
        {
            contextNodes[i+1] = new ContextNode( CaDSRConstants.FOLDER, true, programAreaModelList.get(i).getPalName(), i + 1);
            contextNodes[i+1].setPalNameDescription( programAreaModelList.get(i).getDescription() );
        }

        //Add "All" contexts tab at the end
        contextNodes[0] = new ContextNode( CaDSRConstants.FOLDER, true, CaDSRConstants.ALL_CONTEXTS_STRING, 0 );

        return contextNodes;
    }

    /**
     * Also sets the top/First treePath value to programAreaStr, so far, this is only
     * used when setting a context tree the "All" program area.
     *
     * @param contextNode    The Context node for "All" Program Area.
     * @param programAreaStr TheProgram Area ( should be "All).
     */
    protected void addBreadCrumbsAll( BaseNode contextNode, String programAreaStr )
    {
        List treePath = contextNode.getTreePath();

        if( ( treePath == null ) || ( treePath.isEmpty() ) )
        {
            contextNode.getTreePath().add( contextNode.getText() );
        }

        treePath.set( 0, programAreaStr );

        for( BaseNode node : contextNode.getChildren() )
        {
            {
                node.setTreePath( new ArrayList<String>( treePath ) );
            }
            node.getTreePath().add( node.getText() );
            if( node.isIsParent() )
            {
                addBreadCrumbsAll( node, programAreaStr );
            }
        }
    }

    /**
     * Recursively builds the treePath (bread crumbs)
     *
     * @param contextNode The Context node for "All" Program Area.
     * @param treePath    The tree of "Bread crumbs"
     */

    protected void addBreadCrumbs( BaseNode contextNode, List treePath )
    {
        if( ( treePath == null ) || ( treePath.isEmpty() ) )
        {
            contextNode.getTreePath().add( contextNode.getText() );
        }
        for( BaseNode node : contextNode.getChildren() )
        {
            node.setTreePath( new ArrayList<>( contextNode.getTreePath() ) );
            node.getTreePath().add( node.getText() );

            if( node.getChildren().size() > 0 )
            {
                addBreadCrumbs( node, node.getTreePath() );
            }
        }

    }


    /**
     * Resets the program area to "All" in the rest call provided to the client (href field) and its programArea field when the contexts Program Area needs to be changed.
     * It should only need to be changed when it is add to to the "All" program area.
     * <p/>
     * This rest call is used to bring back the whole tree when only the top level of the Context is loaded, so this is only set for the
     * top nodes "Classifications" and "Protocol Forms"
     * <p/>
     * Only used to set the program area in the href, when adding a context to "All"
     * also sets programArea.
     *
     * @param contextNode The node which needs its href and Program Area (re)set.  This should only be needed when copying a Context to the "ALL" Program Area.
     * @param programArea The Program Area to set this node to. This function is (currently) only used/needed to set a Program Area to "All".
     */
    protected void setHrefProgramArea( BaseNode contextNode, int programArea )
    {
        for( BaseNode node : contextNode.getChildren() )
        {
            String regex = ",[0-9]+,";
            node.setHref( node.getHref().replaceAll( regex, "," + programArea + "," ) );
            node.setProgramArea( programArea );
        }
    }


    /**
     * Folders within Classifications can have nested folders.
     * Returns a list of children of this parent, if any.
     *
     * @param csCsiIdseq parent Classification Scheme Item.
     * @return list of children of this parent Classification Scheme Item.
     */
    protected List<CsCsiModel> getCsCsisByParentCsCsi( String csCsiIdseq, List<CsCsiModel> csCsiNodelList )
    {
        List<CsCsiModel> childrenList = new ArrayList<>();

        for( CsCsiModel csCsiModel : csCsiNodelList )
        {
            if( ( csCsiModel.getParentCsiIdseq() != null ) && ( csCsiModel.getParentCsiIdseq().compareTo( csCsiIdseq ) == 0 ) )
            {

                childrenList.add( csCsiModel );
            }
        }

        return childrenList;
    }

    /**
     * Adds any children of this Classification node to this Classification node with initial fields set from data in parent.
     *
     * @param classificationItemNodeParent The parent Classification node, to which any children will be added.
     */
    protected void addChildrenToCsi( ClassificationItemNode classificationItemNodeParent , List<CsCsiModel> csCsiNodelList)
    {
        //Find nodes which are children of this parent
        List<CsCsiModel> csCsiChildNodelList = getCsCsisByParentCsCsi( classificationItemNodeParent.getIdSeq(), csCsiNodelList);

        if( !csCsiChildNodelList.isEmpty() )
        {
            for( CsCsiModel csCsiChildModel : csCsiChildNodelList )
            {
                ClassificationItemNode classificationItemNodeChild = new ClassificationItemNode();
                classificationItemNodeChild.setIsParent( false );
                classificationItemNodeChild.setCollapsed( false );
                classificationItemNodeChild.setChildType( CaDSRConstants.EMPTY );
                classificationItemNodeChild.setType( CaDSRConstants.CSI );
                classificationItemNodeChild.setText( csCsiChildModel.getCsiName() );
                classificationItemNodeChild.setHover( csCsiChildModel.getCsiDescription() );
                classificationItemNodeChild.setIdSeq( csCsiChildModel.getCsiIdseq() );
                classificationItemNodeChild.setProgramArea( classificationItemNodeParent.getProgramArea() );
                classificationItemNodeChild.setIsChildOfContainer(classificationItemNodeParent.isIsChildOfContainer());

                // TODO  check this - it may not be right
                classificationItemNodeChild.setHref( appConfig.getCdesByClassificationSchemeItemRestServiceName() + "," + csCsiChildModel.getCsCsiIdseq() );


                //Add this child to the Parent
                classificationItemNodeParent.addChildNode( classificationItemNodeChild );
                //logger.debug( "IN addChildrenToCsi Child from list of children: " + classificationItemNodeChild.getText() + "  Parent: " + classificationItemNodeParent.getText() );
            }

            //Parent
            classificationItemNodeParent.setIsParent( true );
            classificationItemNodeParent.setCollapsed( true );
            classificationItemNodeParent.setChildType( CaDSRConstants.CSI );
            classificationItemNodeParent.setType( CaDSRConstants.CIS_FOLDER );
        }
        else // No children
        {
            //Parent
            classificationItemNodeParent.setIsParent( false );
            classificationItemNodeParent.setCollapsed( false );
            classificationItemNodeParent.setChildType( CaDSRConstants.EMPTY );
            classificationItemNodeParent.setType( CaDSRConstants.CSI );
        }
    }

    protected void initProtocolsFormsParentNode( ParentNode protocolsParentNode, int programArea, boolean collapsed )
    {
        protocolsParentNode.setText( "ProtocolForms" );
        protocolsParentNode.setChildType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
        initParentNode( protocolsParentNode, programArea, collapsed );
    }

    /**
     * @param classificationsParentNode
     * @param programArea
     * @param collapsed
     */
    protected void initClassificationsParentNode( ParentNode classificationsParentNode, int programArea, boolean collapsed )
    {
        //CS (Classification Scheme) folder
        classificationsParentNode.setText( "Classifications" );
        classificationsParentNode.setChildType( CaDSRConstants.FOLDER );
        initParentNode( classificationsParentNode, programArea, collapsed );
    }

    protected void initParentNode( ParentNode classificationsParentNode, int programArea, boolean collapsed )
    {
        classificationsParentNode.setType( CaDSRConstants.FOLDER );
        classificationsParentNode.setCollapsed( collapsed );
        classificationsParentNode.setIsParent( false );        // If/When a child is added IsParent will change to true.
        classificationsParentNode.setProgramArea( programArea );
        classificationsParentNode.setChildren( new ArrayList<BaseNode>() );

    }


    protected void insertPlaceHolderNode( ParentNode classificationsParentNode )
    {
        ParentNode placeHolderNode = new ParentNode();
        placeHolderNode.setText( appConfig.getTreeRetrievalWaitMessage() );
        placeHolderNode.setHover( appConfig.getTreeRetrievalWaitMessage() );
        placeHolderNode.setCollapsed( true );
        placeHolderNode.setIsParent( true );
        placeHolderNode.setProgramArea( classificationsParentNode.getProgramArea() );
        classificationsParentNode.addChildNode( placeHolderNode );
    }

    
    /**
     * Adds the classification schemes and its own children (CS and CSI) of this Classification node to the parent, recursively. 
     *
     * @param classificationsNode The parent Classification node, to which any children will be added.
     * @param csModelList List of Classification Scheme Models.
     * @param contextModel Context data & its attributes to which the Classification Scheme belongs. 
     */        
    protected void insertClassifications( ParentNode classificationsParentNode, List<ClassificationSchemeModel> csModelList, ContextModel contextModel, int programArea, List<CsCsiModel> csCsiNodelList )
    {
        //////////////////////////////////////////////////
        //CS (Classification Scheme) List for this Context
        //////////////////////////////////////////////////
        String contextId = contextModel.getConteIdseq();

        //Loop through the Classification Schemes, and find the ones with the same contextID as the ContextModel
        for( ClassificationSchemeModel classificationSchemeModel : csModelList )
        {

            //Is this CS (Classification scheme) for this Context
            if( classificationSchemeModel.getConteIdseq().compareTo( contextId ) == 0 )
            {
                ClassificationNode classificationSchemeNode = new ClassificationNode();
                classificationSchemeNode.setChildType( CaDSRConstants.EMPTY );
               
                // Check for and assign the Container type folder for the Classifications 
                if (classificationSchemeModel.getCstlName().equalsIgnoreCase("Container")) {
                	classificationSchemeNode.setType( CaDSRConstants.CONTAINER );
                } else {
                	classificationSchemeNode.setType( CaDSRConstants.FOLDER );	
                }
                
                classificationSchemeNode.setText( classificationSchemeModel.getLongName() );

                //classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() + " Conte Idseq:" + contextModel.getConteIdseq() + " Cs Idseq:" + classificationSchemeModel.getCsIdseq() );
                classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() );
                classificationSchemeNode.setCollapsed( true );
                classificationSchemeNode.setIsParent( false );
                classificationSchemeNode.setIdSeq( classificationSchemeModel.getCsIdseq() );
                classificationSchemeNode.setProgramArea( programArea );
                classificationSchemeNode.setHref( appConfig.getCdesByClassificationSchemeRestServiceName() + "," + classificationSchemeModel.getCsIdseq() );

                //logger.debug( "\nclassificationSchemeNode: " + classificationSchemeNode.toString() );

                ////////////////////////////////////////////////
                //Get CSI (Classification Scheme Item) list for this CS (Classification Scheme)
                ////////////////////////////////////////////////
                String csId = classificationSchemeModel.getCsIdseq();
                for( CsCsiModel csCsiModel : csCsiNodelList )
                {
                    if( csCsiModel.getCsIdseq().compareTo( csId ) == 0 )
                    {
                    	if (rootLevel.equals(csCsiModel.getCsiLevel())) {
	                    	//Create the new node, and set as much as we can without knowing if it has children
	                        ClassificationItemNode classificationSchemeItemNode = new ClassificationItemNode();
	                        classificationSchemeItemNode.setHref( appConfig.getCdesByClassificationSchemeItemRestServiceName() + "," + csCsiModel.getCsCsiIdseq() );
	                        classificationSchemeItemNode.setText( csCsiModel.getCsiName() );
	                        //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + contextModel.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
	                        classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
	                        classificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
	                        classificationSchemeItemNode.setCollapsed( true );
	                        classificationSchemeItemNode.setProgramArea( programArea );
	                        logger.debug( "addChildrenToCsi(" + classificationSchemeItemNode.getText() + ")   " + csCsiModel.getCsiName() + "  " + csCsiModel.getCsiDescription() );

	                        addChildrenToCsi( classificationSchemeItemNode, csCsiNodelList);

	                        //Add this CSI to the CS
	                        classificationSchemeNode.addChildNode( classificationSchemeItemNode );
                    	}
                    }
                }
                // Retrieving the children of Classification Schemes (which are also Classification Schemes)
                List<ClassificationSchemeModel> csChildModelList = this.classificationSchemeDAO.getChildrenClassificationSchemesByCsId(csId);
                //Add this CS to the CS Folder
                classificationsParentNode.addChildNode( insertChildClassifications(classificationSchemeNode, csChildModelList, contextModel, programArea, csCsiNodelList));
            }
        }
    }
    
    
    
    /**
     * Adds the CS and CSI of the Classification Scheme, recursively. 
     *
     * @param classificationsNode The parent Classification node, to which any children will be added.
     * @param childCsModelList List of Child Classification Scheme Models.
     * @param contextModel Context data & its attributes to which the Classification Scheme belongs. 
     */    
    protected ClassificationNode insertChildClassifications( ClassificationNode classificationsNode, List<ClassificationSchemeModel> childCsModelList, ContextModel contextModel, int programArea, List<CsCsiModel> csCsiNodelList )
    {
        //////////////////////////////////////////////////
        //CS (Classification Scheme) List for this Context
        //////////////////////////////////////////////////
        String contextId = contextModel.getConteIdseq();

        //Loop through the Classification Schemes, and find the ones with the same contextID as the ContextModel
        for( ClassificationSchemeModel childClassificationSchemeModel : childCsModelList )
        {

                ClassificationNode childClassificationSchemeNode = new ClassificationNode();
                childClassificationSchemeNode.setChildType( CaDSRConstants.EMPTY );
                childClassificationSchemeNode.setType( CaDSRConstants.FOLDER );
                childClassificationSchemeNode.setText( childClassificationSchemeModel.getLongName() );
                childClassificationSchemeNode.setIsChildOfContainer(true);

                //classificationSchemeNode.setHover( classificachildClassificationl.getPreferredDefinition() + " Conte Idseq:" + contextModel.getConteIdseq() + " Cs Idseq:" + classificationSchemeModel.getCsIdseq() );
                childClassificationSchemeNode.setHover( childClassificationSchemeModel.getPreferredDefinition() );
                childClassificationSchemeNode.setCollapsed( true );
                childClassificationSchemeNode.setIsParent( false );
                childClassificationSchemeNode.setIdSeq( childClassificationSchemeModel.getCsIdseq() );
                childClassificationSchemeNode.setProgramArea( programArea );
                childClassificationSchemeNode.setHref( appConfig.getCdesByClassificationSchemeRestServiceName() + "," + childClassificationSchemeModel.getCsIdseq() );

                //logger.debug( "\nclassificationSchemeNode: " + classificationSchemeNode.toString() );

                ////////////////////////////////////////////////
                //Get CSI (Classification Scheme Item) list for this CS (Classification Scheme)
                ////////////////////////////////////////////////
                String csId = childClassificationSchemeModel.getCsIdseq();
                if (csCsiNodelList.size() == 0) {
                	csCsiNodelList = this.csCsiDAO.getCsCsisById(csId);
                }
                
                for( CsCsiModel csCsiModel : csCsiNodelList )
                {
                    if( csCsiModel.getCsIdseq().compareTo( csId ) == 0 )
                    {
                    	if (rootLevel.equals(csCsiModel.getCsiLevel())) {
	                    	//Create the new node, and set as much as we can without knowing if it has children
	                        ClassificationItemNode childClassificationSchemeItemNode = new ClassificationItemNode();
	                        childClassificationSchemeItemNode.setHref( appConfig.getCdesByClassificationSchemeItemRestServiceName() + "," + csCsiModel.getCsCsiIdseq() );
	                        childClassificationSchemeItemNode.setText( csCsiModel.getCsiName() );
	                        //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + contextModel.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
	                        childClassificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
	                        childClassificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
	                        childClassificationSchemeItemNode.setCollapsed( true );
	                        childClassificationSchemeItemNode.setProgramArea( programArea );
	                        childClassificationSchemeItemNode.setIsChildOfContainer(true);
	                        logger.debug( "addChildrenToCsi(" + childClassificationSchemeItemNode.getText() + ")   " + csCsiModel.getCsiName() + "  " + csCsiModel.getCsiDescription() );

	                        addChildrenToCsi( childClassificationSchemeItemNode, csCsiNodelList);

	                        //Add this CSI to the CS
	                        childClassificationSchemeNode.addChildNode( childClassificationSchemeItemNode );
                    	}
                    }
                }                
                //Add this CS to the CS Folder
                classificationsNode.addChildNode( childClassificationSchemeNode );
        }
       return classificationsNode;
    }        

    /**
     * Create a protocolNode (for the client side) from a ProtocolModel (from the database)
     *
     * @param protocolModel The date model to populate a Protocol node from
     * @param programArea   Top level Program Area for the new ProtocolNode.
     * @return The new ProtocolNode which is to be part of the object passed on to the client.
     */
    protected ProtocolNode initProtocolNode( ProtocolModel protocolModel, int programArea )
    {
        int maxHoverTextLen = Integer.parseInt( appConfig.getMaxHoverTextLenStr() );
        ProtocolNode protocolNode = new ProtocolNode();
        protocolNode.setType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
        protocolNode.setChildType( CaDSRConstants.PROTOCOL );
        protocolNode.setCollapsed( true );
        protocolNode.setIsParent( false );

        protocolNode.setText( protocolModel.getLongName() );
        String hoverText = protocolModel.getPreferredDefinition();
        //Trim the hover text length if needed, Max length for hover text is set in the properties file.
        if( !hoverText.isEmpty() && hoverText.length() > maxHoverTextLen )
        {
            hoverText = hoverText.substring( 0, maxHoverTextLen ) + "...";
        }
        protocolNode.setHover( hoverText );
        protocolNode.setProgramArea( programArea );

        return protocolNode;
    }


    /**
     * Populate a Protocol form model node from a  ProtocolFormModel.
     * The ProtocolFormModel represents the data straight from the Database.
     * The ProtocolFormNode is data sent to the client.
     *
     * @param protocolFormModel The Protocol Form Model from the database
     * @param programArea       The Protocol's Program Area
     * @return The new Protocol Form Node, which now has what data it needs from the Protocol Form Model
     */
    protected ProtocolFormNode initProtocolFormNode( ProtocolFormModel protocolFormModel, int programArea )
    {
        int maxHoverTextLen = Integer.parseInt( appConfig.getMaxHoverTextLenStr() );
        ProtocolFormNode protocolFormNode = new ProtocolFormNode();
        protocolFormNode.setChildType( CaDSRConstants.EMPTY );
        protocolFormNode.setType( CaDSRConstants.PROTOCOL );
        protocolFormNode.setCollapsed( false );
        protocolFormNode.setIsParent( false );

        protocolFormNode.setText( protocolFormModel.getLongName() );
        String hoverText = protocolFormModel.getProtoPreferredDefinition();
        if( !hoverText.isEmpty() && hoverText.length() > maxHoverTextLen )
        {
            hoverText = hoverText.substring( 0, maxHoverTextLen ) + "...";
        }
        protocolFormNode.setHover( hoverText );
        protocolFormNode.setProgramArea( programArea );

        return protocolFormNode;
    }


    /**
     * Get a Program Areas number/index by its name.
     *
     * @param contextPalName Program Area name.
     * @return Program Area index.
     */
    protected int getProgramAreaByName( String contextPalName )
    {
    	int palIndex = findProgramAreaByName(contextPalName);
        if (palIndex > 0) {
        	return palIndex;//we found Program Area by name
        }
        else {//reload programAreaModelList when we have not found one by name, probably this cached list is stale
        	synchronized(this) {//TODO is this enough when Program Area list is changed by Admin Tool?
        		programAreaModelList = restControllerCommon.getProgramAreaList();
        		logger.info("programAreaModelList was re-loaded from DB on search context: " + contextPalName);
        	}
        	return findProgramAreaByName(contextPalName);
        }
    }
    /**
     * Iterate through cached list.
     * 
     * @param contextPalName
     * @return int Program Area index which shall be more than 0 if the name is found
     */
    protected int findProgramAreaByName ( String contextPalName ) {
        for( int i = 0; i < programAreaModelList.size(); i++ )
        {
            String palNameCurr = programAreaModelList.get( i ).getPalName();
        	if((palNameCurr != null) && (palNameCurr.equals(contextPalName)))
            {
                return i + 1; //The +1 is for "All" to be 0
            }
        }
        return 0;
    }
    
    protected String getProgramAreaDescriptionByIndex( int i )
    {
        //If it's "All"(0)
        if( i < 1 )
        {
            return "";
        }
        return programAreaModelList.get( i - 1 ).getDescription();
    }

    /**
     * Creates an Error message to send back to a client.
     * <p/>
     * TODO allow for a null value for the Exception, so we can use this when there was an error condition, but no Exception.
     *
     * @param text The text of the error message
     * @param e    the exception ( if error eas the result of an Exception)
     * @param c    The Class type that need to return.
     * @param <T>  The Class type that need to return.
     * @return The new Error object
     */
    private <T extends BaseNode> T createErrorNode( String text, Exception e, Class<T> c )
    {
        logger.error( text + e.getMessage(), e);
        BaseNode errorNode = new ContextNode();

        errorNode.setStatus( CaDSRConstants.ERROR );
        errorNode.setText( text + e.getMessage() );
        return c.cast( errorNode );
    }


    ///////////////////////////////////
    // Setters & Getters
    public void setRestControllerCommon( RestControllerCommon restControllersCommon )
    {
        this.restControllerCommon = restControllersCommon;
    }

    public void setProtocolDAO( ProtocolDAO protocolDAO )
    {
        this.protocolDAO = protocolDAO;
    }

    public void setProtocolFormDAO( ProtocolFormDAO protocolFormDAO )
    {
        this.protocolFormDAO = protocolFormDAO;
    }

    public void setCsCsiDAO( CsCsiDAO csCsiDAO )
    {
        this.csCsiDAO = csCsiDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public void setClassificationSchemeDAO( ClassificationSchemeDAO classificationSchemeDAO )
    {
        this.classificationSchemeDAO = classificationSchemeDAO;
    }

    public List<ProgramAreaModel> getProgramAreaModelList()
    {
        return programAreaModelList;
    }

    public void setProgramAreaModelList( List<ProgramAreaModel> programAreaModelList )
    {
        this.programAreaModelList = programAreaModelList;
    }

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}


}
