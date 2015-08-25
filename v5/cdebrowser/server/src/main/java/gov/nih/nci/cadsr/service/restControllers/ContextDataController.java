/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.*;
import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.service.model.context.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ContextDataController
{

    private Logger logger = LogManager.getLogger( ContextDataController.class.getName() );

    private ContextDAOImpl contextDAO;
    private ClassificationSchemeDAOImpl classificationSchemeDAO;
    private CsCsiDAOImpl csCsiDAO;
    private ProtocolFormDAOImpl protocolFormDAO;
    private ProtocolDAOImpl protocolDAO;
    private List<CsCsiModel> csCsiNodelList = null;
    private List<ProgramAreaModel> programAreaModelList = null;
    private RestControllerCommon restControllerCommon;


    private Map csi = new HashMap();

    @Value("${maxHoverTextLen}")
    private String maxHoverTextLenStr;

    @Value("${oneContextRestService}")
    private String oneContextRestServiceName;

    @Value("${cdesByClassificationSchemeRestService}")
    private String cdesByClassificationSchemeRestServiceName;

    @Value("${cdesByClassificationSchemeItemRestService}")
    private String cdesByClassificationSchemeItemRestServiceName;

    @Value("${cdesByProtocolRestService}")
    private String cdesByProtocolRestServiceName;

    @Value("${cdesByProtocolFormRestService}")
    private String cdesByProtocolFormRestServiceName;

    @Value("${treeRetrievalWaitMessage}")
    private String treeRetrievalWaitMessage;

    private int contextPalNameCount;

    public ContextDataController()
    {
    }

     @RequestMapping(value = "/contextData")
    @ResponseBody
    public ContextNode[] contextData()
    {
        logger.debug( "Received rest call \"contextData\"" );
        // Get Program Areas
        try
        {
            programAreaModelList = restControllerCommon.getProgramAreaList();
        }
        catch( Exception e )
        {
            logger.error( "Server Error:\nCould not retrieve Program Areas from database" );
            ContextNode[] errorNode = new ContextNode[1];
            errorNode[0] = createErrorNode( "Server Error:\nCould not retrieve Program Areas from database", e, ContextNode.class );
            return errorNode;
        }

        // Get the (only) top level Context data
        ContextNode[] contextNodes;
        try
        {
            contextNodes = getAllTopLevelTreeData();
        }
        catch( Exception e )
        {
            logger.error( "Server Error:\nCould not retrieve Program Areas from database" );
            ContextNode[] errorNode = new ContextNode[1];
            errorNode[0] = createErrorNode( "Server Error:\nCould not retrieve Top Level Context data from database", e, ContextNode.class );
            return errorNode;
        }

        logger.debug( "Done rest call" );
        return contextNodes;
    }

    /**
     * @param contextId   The one Context who's tree we need to return to the client.
     * @param programArea We already have the original Program area, but this will tell us if it is being called from "All"
     * @param folderType  Is it a "Classifications" or "Protocol Forms" folder? We us this to determine which should be collapsed, and which oopen
     * @return The complete Context tree from "Classifications" and "Protocol Forms" down.
     */
    @RequestMapping(value = "/oneContextData")
    @ResponseBody
    public List<ParentNode> oneContextData( @RequestParam("contextId") String contextId, @RequestParam("programArea") int programArea, @RequestParam("folderType") int folderType )
    {
        logger.debug( "Received rest call \"oneContextData\" [" + contextId + "]   Program Area[" + programArea + "]   FolderType[" + folderType + "]" );
        programAreaModelList = restControllerCommon.getProgramAreaList();
        List<ParentNode> contextTree = getOneTreeByContextId( contextId, programArea, folderType );
        logger.debug( "Done rest call\n=========================\n" );
        return contextTree;
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
            classificationsParentNode.setHref( oneContextRestServiceName +
                    "," + model.getConteIdseq() +
                    "," + getProgramAreaByName( model.getPalName() ) +
                    "," + CaDSRConstants.CLASSIFICATIONS_TYPE_FOLDER );

            logger.debug( "Checking for Classification Schemes - " + model.getName() );
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
            protocolsParentNode.setHref( oneContextRestServiceName +
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
            contextNodes[0].setHover( "All Contexts" );
        }

        // Set bread crumb data
        for( int f = 0; f < contextPalNameCount; f++ )
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
            List<ClassificationSchemeModel> csModelList = this.classificationSchemeDAO.getClassificationSchemes( contextId );

            // Classification Scheme Item List - This is a list of all individual items that are in the Classifications of this Context
            // they will be place in their correct Classification later in insertClassifications.
            setCsCsiNodelList( this.csCsiDAO.getCsCsisByConteId( contextId ) );

            // Last parameter is "Collapsed" should be false if folderType is Classifications
            initClassificationsParentNode( parentNode, programArea, false );

            //Add the Classifications to the parentNode for this Context.
            insertClassifications( parentNode, csModelList, model, programArea );
        }
        else if( folderType == CaDSRConstants.PROTOCOLFORMS_TYPE_FOLDER )
        {
            // All ProtocolForms for this Context
            List<ProtocolFormModel> protocolFormModelList = this.protocolFormDAO.getProtocolFormByContextId( contextId );

            // Last parameter is "Collapsed" should be false
            initProtocolsFormsParentNode( parentNode, programArea, false );

            //////////////////////////////////////////////////
            //Protocol List for this Context, a Protocol will be a node which contains ProtocolForms
            List<ProtocolModel> protocolModelList = this.protocolDAO.getProtocolsByContext( model.getConteIdseq() );

            // Populate each Protocol with their ProtocolForms
            initProtocolModels( protocolModelList, protocolFormModelList, parentNode, programArea );

            logger.debug( "parentNode " + parentNode.getChildren().get( 0 ).getChildren().get( 0 ).toString() );
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
            protocolNode.setHref( cdesByProtocolRestServiceName + "," + protocolModel.getProtoIdseq() );

            // Add the ProtocolForms to this Protocol node
            String protoId = protocolModel.getProtoIdseq();
            for( ProtocolFormModel protocolFormModel : protocolFormModelList )
            {
                String protoFormId = protocolFormModel.getProtoIdseq();
                if( protoFormId != null && protoFormId.compareTo( protoId ) == 0 )
                {
                    ProtocolFormNode protocolFormNode = initProtocolFormNode( protocolFormModel, programArea );
                    logger.debug( "Setting ProtocolForm href: " + protocolFormModel.getQcIdseq() );
                    protocolFormNode.setHref( cdesByProtocolFormRestServiceName + "," + protocolFormModel.getQcIdseq() );

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
        contextPalNameCount = programAreaModelList.size() + 1;//The + 1 is for "All"

        ContextNode[] contextNodes = new ContextNode[contextPalNameCount];
        for( int i = 1; i < contextPalNameCount; i++ )
        {
            contextNodes[i] = new ContextNode( CaDSRConstants.FOLDER, true, programAreaModelList.get( i - 1 ).getPalName(), i );
            contextNodes[i].setPalNameDescription( programAreaModelList.get( i - 1 ).getDescription() );
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
    protected List<CsCsiModel> getCsCsisByParentCsCsi( String csCsiIdseq )
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
    protected void addChildrenToCsi( ClassificationItemNode classificationItemNodeParent )
    {
        //Find nodes which are children of this parent
        List<CsCsiModel> csCsiChildNodelList = getCsCsisByParentCsCsi( classificationItemNodeParent.getIdSeq() );

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

                // TODO  check this - it may not be right
                classificationItemNodeChild.setHref( cdesByClassificationSchemeItemRestServiceName + "," + csCsiChildModel.getCsCsiIdseq() );


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
        placeHolderNode.setText( treeRetrievalWaitMessage );
        placeHolderNode.setHover( treeRetrievalWaitMessage );
        placeHolderNode.setCollapsed( true );
        placeHolderNode.setIsParent( true );
        placeHolderNode.setProgramArea( classificationsParentNode.getProgramArea() );
        classificationsParentNode.addChildNode( placeHolderNode );
    }

    protected void insertClassifications( ParentNode classificationsParentNode, List<ClassificationSchemeModel> csModelList, ContextModel contextModel, int programArea )
    {
        logger.debug( "MHL classificationsParentNode: " + classificationsParentNode.getText() + "    csModelList.size: " + csModelList.size() );
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
                classificationSchemeNode.setType( CaDSRConstants.FOLDER );
                classificationSchemeNode.setText( classificationSchemeModel.getLongName() );

                //classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() + " Conte Idseq:" + contextModel.getConteIdseq() + " Cs Idseq:" + classificationSchemeModel.getCsIdseq() );
                classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() );
                classificationSchemeNode.setCollapsed( true );
                classificationSchemeNode.setIsParent( false );
                classificationSchemeNode.setIdSeq( classificationSchemeModel.getCsIdseq() );
                classificationSchemeNode.setProgramArea( programArea );
                classificationSchemeNode.setHref( cdesByClassificationSchemeRestServiceName + "," + classificationSchemeModel.getCsIdseq() );

                //logger.debug( "\nclassificationSchemeNode: " + classificationSchemeNode.toString() );

                ////////////////////////////////////////////////
                //Get CSI (Classification Scheme Item) list for this CS (Classification Scheme)
                ////////////////////////////////////////////////
                String csId = classificationSchemeModel.getCsIdseq();
                for( CsCsiModel csCsiModel : csCsiNodelList )
                {

                    if( csCsiModel.getCsIdseq().compareTo( csId ) == 0 )
                    {
                        //Create the new node, and set as much as we can without knowing if it has children
                        ClassificationItemNode classificationSchemeItemNode = new ClassificationItemNode();
                        classificationSchemeItemNode.setHref( cdesByClassificationSchemeItemRestServiceName + "," + csCsiModel.getCsCsiIdseq() );
                        classificationSchemeItemNode.setText( csCsiModel.getCsiName() );
                        //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + contextModel.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
                        classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
                        classificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
                        classificationSchemeItemNode.setCollapsed( true );
                        classificationSchemeItemNode.setProgramArea( programArea );
   logger.debug( "addChildrenToCsi(" + classificationSchemeItemNode.getText() + ")   " + csCsiModel.getCsiName() + "  " + csCsiModel.getCsiDescription() );
                        addChildrenToCsi( classificationSchemeItemNode );

                        //Add this CSI to the CS
                        classificationSchemeNode.addChildNode( classificationSchemeItemNode );
                    }
                }
                //Add this CS to the CS Folder
                classificationsParentNode.addChildNode( classificationSchemeNode );

                // FIXME - This is a hasty hack/work around.   TODO Explain
                cleanUpClassificationSchemeNode( classificationSchemeNode, 0 );
            }

        }

    }

    // FIXME - This is a hasty hack/work around.   TODO Explain
    private void cleanUpClassificationSchemeNode( BaseNode classificationSchemeNode, int depth )
    {
        //String space = "                                                          ";
        List<BaseNode> children = classificationSchemeNode.getChildren();

        Iterator<BaseNode> i = children.iterator();
        while( i.hasNext() )
        {
            BaseNode node = i.next();
            //Is it in the hashMap with a higher depth
            String searchKey = node.getText();
            if( csi.containsKey( searchKey ) )
            {
                if( depth < (int) csi.get( searchKey ) )
                {
                    i.remove();
                }
            }

            csi.put( node.getText(), depth );
            if( node.isIsParent() )
            {
                //logger.debug( space.substring( 0, ( depth * 4 ) ) + "+cNode[" + depth + "]: " + node.getText() + "   IsChild:" + node.isIsChild() + "   IsParent:" + node.isIsParent() + "  " + node.getType() );
                cleanUpClassificationSchemeNode( node, depth + 1 );
            }
        }
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
        int maxHoverTextLen = Integer.parseInt( maxHoverTextLenStr );
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
     * @param programArea The Protocol's Program Area
     * @return The new Protocol Form Node, which now has what data it needs from the Protocol Form Model
     */
    protected ProtocolFormNode initProtocolFormNode( ProtocolFormModel protocolFormModel, int programArea )
    {
        int maxHoverTextLen = Integer.parseInt( maxHoverTextLenStr );
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
        for( int i = 0; i < contextPalNameCount; i++ )
        {
            if( programAreaModelList.get( i ).getPalName().compareTo( contextPalName ) == 0 )
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
     *
     * TODO allow for a null value for the Exception, so we can use this when there was an error condition, but no Exception.
     *
     * @param text The text of the error message
     * @param e the exception ( if error eas the result of an Exception)
     * @param c The Class type that need to return.
     * @param <T> The Class type that need to return.
     * @return The new Error object
     */
    private <T extends BaseNode> T createErrorNode( String text, Exception e, Class<T> c )
    {
        logger.error( text + e.getMessage() );
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

    public void setProtocolDAO( ProtocolDAOImpl protocolDAO )
    {
        this.protocolDAO = protocolDAO;
    }

    public void setProtocolFormDAO( ProtocolFormDAOImpl protocolFormDAO )
    {
        this.protocolFormDAO = protocolFormDAO;
    }

    public void setCsCsiDAO( CsCsiDAOImpl csCsiDAO )
    {
        this.csCsiDAO = csCsiDAO;
    }

    public void setContextDAO( ContextDAOImpl contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public void setClassificationSchemeDAO( ClassificationSchemeDAOImpl classificationSchemeDAO )
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

    public void setCsCsiNodelList( List<CsCsiModel> csCsiNodelList )
    {
        this.csCsiNodelList = csCsiNodelList;
    }

    public void setMaxHoverTextLenStr( String maxHoverTextLen )
    {
        this.maxHoverTextLenStr = maxHoverTextLen;
    }

    public String getMaxHoverTextLenStr()
    {
        return maxHoverTextLenStr;
    }

    public String getOneContextRestServiceName()
    {
        return oneContextRestServiceName;
    }

    public void setOneContextRestServiceName( String oneContextRestServiceName )
    {
        this.oneContextRestServiceName = oneContextRestServiceName;
    }

    public String getTreeRetrievalWaitMessage()
    {
        return treeRetrievalWaitMessage;
    }

    public void setTreeRetrievalWaitMessage( String treeRetrievalWaitMessage )
    {
        this.treeRetrievalWaitMessage = treeRetrievalWaitMessage;
    }

    public int getContextPalNameCount()
    {
        return contextPalNameCount;
    }

    public void setContextPalNameCount( int contextPalNameCount )
    {
        this.contextPalNameCount = contextPalNameCount;
    }

    public String getCdesByProtocolFormRestServiceName()
    {
        return cdesByProtocolFormRestServiceName;
    }

    public String getCdesByProtocolRestServiceName()
    {
        return cdesByProtocolRestServiceName;
    }

    public String getCdesByClassificationSchemeItemRestServiceName()
    {
        return cdesByClassificationSchemeItemRestServiceName;
    }

    public String getCdesByClassificationSchemeRestServiceName()
    {
        return cdesByClassificationSchemeRestServiceName;
    }

}
