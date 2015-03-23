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

    @Value( "${maxHoverTextLen}" )
    String maxHoverTextLenStr;

    @Value( "${oneContextRestService}" )
    String oneContextRestServiceName;

    private int contextPalNameCount;

    public ContextDataController()
    {   }

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

    public int getContextPalNameCount()
    {
        return contextPalNameCount;
    }

    public void setContextPalNameCount( int contextPalNameCount )
    {
        this.contextPalNameCount = contextPalNameCount;
    }

    @RequestMapping( value = "/contextData" )
    @ResponseBody
    public ContextNode[] contextData()
    {
        logger.debug( "Received rest call \"contextData\"" );
        programAreaModelList = restControllerCommon.getProgramAreaList();
        ContextNode[] contextNodes = getAllTopLevelTreeData();

        logger.debug( "Done rest call\n=========================\n" );
        return contextNodes;
    }

    /**
     *
     * @param contextId
     * @param programArea  We already have the original Program area, but this will tell us if it is being called from "All"
     * @param folderType
     * @return
     */
    @RequestMapping( value = "/oneContextData" )
    @ResponseBody
    public List<BaseNode> oneContextData( @RequestParam( "contextId" ) String contextId, @RequestParam( "programArea" ) int programArea , @RequestParam("folderType") int folderType)
    {
        logger.debug( "Received rest call \"oneContextData\" [" +  contextId +"]   Program Area[" + programArea + "]   FolderType[" + folderType + "]");
        programAreaModelList = restControllerCommon.getProgramAreaList();
        List<BaseNode> contextNodes = getOneTreeByContextId( contextId, programArea, folderType );
        logger.debug( "Done rest call\n=========================\n" );
        return contextNodes;
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
            classificationsParentNode.setHref( oneContextRestServiceName + "?contextId=" + model.getConteIdseq() +
                    "&programArea=" + getProgramAreaByName( model.getPalName() ) +
                    "&folderType=0" );// folderType: 0 = classifications folder, 1 = protocol forms folder

            logger.debug( "Checking for Classification Schemes" );
            // Is there at least one classification?
            if( this.classificationSchemeDAO.haveClassificationSchemes( model.getConteIdseq() ) )
            {
                insertPlaceHolderNode( classificationsParentNode );
            }


            ////////////////////////////////////////
            // This Contexts Protocol forms folder
            ParentNode protocolsParentNode = new ParentNode();
            initProtocolsFormsParentNode(protocolsParentNode, programAreaIndex, true ); //true = collapsed folder

            // Add the data for the rest call back to get the complete data tree for this context - same as the "Classifications"
            protocolsParentNode.setHref( classificationsParentNode.getHref());

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
        addBreadCrumbsAll( contextNodes[0], "All" );

        return contextNodes;
    }


    /**
     *
     * @param contextId
     * @param programArea  We already have the original Program area, but this will tell us if it is being called from "All"
     * @param folderType  0 = classifications folder, 1 = protocol forms folder
     * @return
     */
    private List<BaseNode> getOneTreeByContextId( String contextId, int programArea, int folderType )
    {
        // The top level - Program areas
        ContextNode[] contextNodes = initTopLevelContextNodes();

        // Get this Context from the database
        ContextModel model = this.contextDAO.getContextById( contextId );

        // All ProtocolForms for this Context
        List<ProtocolFormModel> protocolFormModelList = this.protocolFormDAO.getProtocolFormByContextId( contextId );

        // The contents for the Classifications folder - the Classification Scheme, which is the list of individual Classifications.
        List<ClassificationSchemeModel> csModelList = this.classificationSchemeDAO.getClassificationSchemes( contextId );

        // Classification Scheme Item List - This is a list of all individual items that are in the Classifications of this Context
        // they will be place in their correct Classification later in insertClassifications.
        setCsCsiNodelList( this.csCsiDAO.getCsCsisByConteId( contextId ) );

        ////////////////////////////////////////
        // This Contexts Classification folder
        ParentNode classificationsParentNode = new ParentNode();

        // Last parameter is "Collapsed" should be false if folderType is Classifications  0 = Classifications folder, 1 = Protocol forms folder
        initClassificationsParentNode( classificationsParentNode, programArea, (folderType == 1));

        //Add the Classifications to the classificationsParentNode for this Context.
        insertClassifications( classificationsParentNode, csModelList, model, programArea );

        ////////////////////////////////////////
        // This Contexts Protocol forms folder
        ParentNode protocolsParentNode = new ParentNode();

        // Last parameter is "Collapsed" should be false if folderType is Classifications  0 = Classifications folder, 1 = Protocol forms folder
        initProtocolsFormsParentNode(protocolsParentNode, programArea, (folderType == 0) );

        //////////////////////////////////////////////////
        //Protocol List for this Context, a Protocol will be a node which contains ProtocolForms
        List<ProtocolModel> protocolModelList = this.protocolDAO.getProtocolsByContext( model.getConteIdseq() );

        // Populate each Protocol with their ProtocolForms
        for( ProtocolModel protocolModel : protocolModelList )
        {
                // The node for this individual Protocol
                ProtocolNode protocolNode = initProtocolNode( protocolModel, programArea );

                // Add the ProtocolForms to this Protocol node
                String protoId = protocolModel.getProtoIdseq();
                for( ProtocolFormModel protocolFormModel : protocolFormModelList )
                {
                    String protoFormId = protocolFormModel.getProtoIdseq();
                    if( protoFormId != null && protoFormId.compareTo( protoId ) == 0 )
                    {
                        ProtocolFormNode protocolFormNode = initProtocolFormNode( protocolFormModel, programArea );
                        protocolNode.addChildNode( protocolFormNode );
                    }
                }

                // Some Protocols have nor ProtocolForms, if there are NO protocol forms for this protocol, don't add him.
                if( protocolNode.isIsParent() )
                {
                    protocolsParentNode.addChildNode( protocolNode );
                }
        }

        // This parent Context node
        ContextNode contextNodeParent = new ContextNode( model, programArea );

        // Add Classifications and ProtocolForms to this Context node
        contextNodeParent.addChildNode( classificationsParentNode );
        contextNodeParent.addChildNode( protocolsParentNode );

        // Add this parent Context node to the top node which is the Program Area.
        contextNodes[programArea].addTopNode( contextNodeParent );

        // Set the tabs hover text to the caption for this Program area, which will be shown below the selected tab in the client.
        contextNodes[programArea].setHover( getProgramAreaDescriptionByIndex( programArea ) );

        addBreadCrumbs( contextNodes[programArea], null );

        return contextNodes[programArea].getChildren();
    }


    /**
     * Set up the top level Context nodes, which are the program areas.
     * @return
     */
    protected ContextNode[] initTopLevelContextNodes()
    {
        contextPalNameCount = programAreaModelList.size() + 1;//The + 1 is for "All"

        ContextNode[] contextNodes = new ContextNode[contextPalNameCount];
        for( int i = 1; i < contextPalNameCount; i++ )
        {
            contextNodes[i] = new ContextNode( CaDSRConstants.FOLDER, true, programAreaModelList.get( i - 1 ).getPalName(), i );
            contextNodes[i].setPalNameDescription( programAreaModelList.get( i - 1).getDescription() );
        }

        //Add all contexts tab at the end
        contextNodes[0] = new ContextNode( CaDSRConstants.FOLDER, true, "All", 0 );

        return contextNodes;
    }

    protected void addBreadCrumbs( BaseNode contextNode, List treePath )
    {
        if( ( treePath == null ) || ( treePath.isEmpty() ) )
        {
            contextNode.getTreePath().add( contextNode.getText() );
        }
        for( BaseNode node : contextNode.getChildren() )
        {
            node.setTreePath( new ArrayList<String>( contextNode.getTreePath() ) );
            node.getTreePath().add( node.getText() );
            if( node.isIsParent() )
            {
                addBreadCrumbs( node, node.getTreePath() );
            }
        }

    }

    /**
     * Also sets the top/First treePath value to programAreaStr, so far, this is only
     * used when setting a context tree the "All" program area.
     * @param contextNode
     * @param programAreaStr
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
     * Resets the program area to "All" in the rest call provided to the client (href field) and its programArea field when the contexts Program Area needs to be changed.
     * It should only need to be changed when it is add to to the "All" program area.
     *
     * This rest call is used to bring back the whole tree when only the top level of the Context is loaded, so this is only set for the
     * top nodes "Classifications" and "Protocol Forms"
     *
     * Only used to set the program area in the href, when adding a context to "All"
     * also sets programArea.
     *
     * @param contextNode
     * @param programArea
     */
    protected void setHrefProgramArea( BaseNode contextNode, int programArea )
    {
        for( BaseNode node : contextNode.getChildren() )
        {
            String regex = "&programArea=[0-9]+";
            node.setHref( node.getHref().replaceAll( regex, "&programArea=" + programArea ) );
            node.setProgramArea( programArea );
        }
    }


    /**
     * Folders within Classifications can have nested folders.
     * Returns a list of children of this parent, if any.
     *
     * @param csCsiIdseq
     * @return
     */
    protected List<CsCsiModel> getCsCsisByParentCsCsi( String csCsiIdseq )
    {
        List<CsCsiModel> childrenList = new ArrayList<CsCsiModel>();

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
     * Adds a new child classification node with initial fields set from data in parent
     * Find the children, and add them.
     *
     * @param classificationItemNodeParent
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

    protected void initProtocolsFormsParentNode(ParentNode protocolsParentNode, int programArea, boolean collapsed)
    {
        protocolsParentNode.setText( "ProtocolForms" );
        protocolsParentNode.setChildType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
        initParentNode(protocolsParentNode, programArea, collapsed);
    }

    /**
     *
     * @param classificationsParentNode
     * @param collapsed
     */
    protected void initClassificationsParentNode( ParentNode classificationsParentNode, int programArea, boolean collapsed )
    {
        //CS (Classification Scheme) folder
        classificationsParentNode.setText( "Classifications" );
        classificationsParentNode.setChildType( CaDSRConstants.FOLDER );
        initParentNode(classificationsParentNode, programArea, collapsed);
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
        placeHolderNode.setText( "Place Holder" );
        placeHolderNode.setHover( "Place Holder" );
        placeHolderNode.setCollapsed( true );
        placeHolderNode.setIsParent( false );
        classificationsParentNode.addChildNode( placeHolderNode );
    }

    protected void insertClassifications( ParentNode classificationsParentNode, List<ClassificationSchemeModel> csModelList, ContextModel contextModel, int programArea )
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
                classificationSchemeNode.setType( CaDSRConstants.FOLDER );

                classificationSchemeNode.setText( classificationSchemeModel.getLongName() );
                //classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() + " Conte Idseq:" + contextModel.getConteIdseq() + " Cs Idseq:" + classificationSchemeModel.getCsIdseq() );
                classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() );
                classificationSchemeNode.setCollapsed( true );
                classificationSchemeNode.setIsParent( false );
                classificationSchemeNode.setIdSeq( classificationSchemeModel.getCsIdseq() );
                classificationSchemeNode.setProgramArea( programArea );
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
                        classificationSchemeItemNode.setText( csCsiModel.getCsiName() );
                        //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + contextModel.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
                        classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
                        classificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
                        classificationSchemeItemNode.setCollapsed( true );
                        classificationSchemeItemNode.setProgramArea( programArea );
                        //logger.debug( "addChildrenToCsi(" + classificationSchemeItemNode.getText() + ")   " + csCsiModel.getCsiName() + "  " + csCsiModel.getCsiDescription() );
                        addChildrenToCsi( classificationSchemeItemNode );

                        //Add this CSI to the CS
                        classificationSchemeNode.addChildNode( classificationSchemeItemNode );
                    }
                }
                //    logger.debug( "DONE Getting CSIs for " + contextModel.getConteIdseq() );
                //Add this CS to the CS Folder
                classificationsParentNode.addChildNode( classificationSchemeNode );
            }
        }
    }

    /*
    Populate a Protocol form model node from a  ProtocolFormModel.
    The ProtocolFormModel represents the data straight from the Database.
    The ProtocolFormNode is data sent to the client.
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
     * Create a protocolNode (for the client side) from a ProtocolModel (from the database)
     * @param protocolModel
     * @param programArea
     * @return
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
            hoverText = hoverText.substring( 0, maxHoverTextLen )  + "..." ;
        }
        protocolNode.setHover( hoverText );
        protocolNode.setProgramArea( programArea );

        return protocolNode;
    }

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
        return programAreaModelList.get( i - 1 ).getDescription();
    }

}
