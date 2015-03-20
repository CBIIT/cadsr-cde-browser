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
    private ProgramAreaDAOImpl programAreaDAO;//FIXME - do we need this anymore (moved to restControllerCommon ?
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

    public RestControllerCommon getRestControllerCommon()
    {
        return restControllerCommon;
    }

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

    public void setProgramAreaDAO( ProgramAreaDAOImpl programAreaDAO )
    {
        this.programAreaDAO = programAreaDAO;
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

    public void setCsCsiNodelList( List<CsCsiModel> csCsiNodelList )
    {
        this.csCsiNodelList = csCsiNodelList;
    }

    public void setMaxHoverTextLenStr( String maxHoverTextLen )
    {
        this.maxHoverTextLenStr = maxHoverTextLen;
    }

    @RequestMapping( value = "/contextData" )
    @ResponseBody
    public ContextNode[] contextData()
    {
        logger.debug( "Received rest call \"contextData\"" );
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
        List<BaseNode> contextNodes = getOneTreeByContextId( contextId, programArea, folderType );
        logger.debug( "Done rest call\n=========================\n" );
        return contextNodes;
    }

    private ContextNode[] getAllTopLevelTreeData()
    {
        /////////////////////////////////////////////////////////////////////////////
        //The top level, Program areas
        /////////////////////////////////////////////////////////////////////////////
        programAreaModelList = restControllerCommon.getProgramAreaList();
        contextPalNameCount = programAreaModelList.size() + 1;//The + 1 is for "All"
        ContextNode[] contextNodes = new ContextNode[contextPalNameCount];
        for( int i = 1; i < contextPalNameCount; i++ )
        {
            contextNodes[i] = new ContextNode( CaDSRConstants.FOLDER, true, programAreaModelList.get( i - 1 ).getPalName(), i );
            contextNodes[i].setPalNameDescription( programAreaModelList.get( i - 1 ).getDescription() );
        }
        //Add "all" contexts tab at the end
        contextNodes[0] = new ContextNode( CaDSRConstants.FOLDER, true, "All" , 0);

        /////////////////////////////////////////////////////////////////////////////
        //Get list of all Contexts
        /////////////////////////////////////////////////////////////////////////////
        List<ContextModel> contextModelList = this.contextDAO.getAllContexts();

        for( ContextModel model : contextModelList )
        {
            int programAreaIndex = getProgramArea( model.getPalName() );

            /////////////////////////////////////////////////////////////////////////////
            //Get all Classifications for this context
            /////////////////////////////////////////////////////////////////////////////
            //CS (Classification Scheme) folder
            ParentNode classificationsParentNode = new ParentNode();
            initClassificationsParentNode( classificationsParentNode, programAreaIndex, true ); //true = collapsed folder

            //Add the data for the rest call back to get these classifications
            classificationsParentNode.setHref( oneContextRestServiceName + "?contextId=" + model.getConteIdseq() +
                    "&programArea=" + getProgramArea( model.getPalName() ) +
                    "&folderType=0");// folderType: 0 = classifications folder, 1 = protocol forms folder

            //TODO remove - Hover for the Classifications folder node is just for dev time debugging
            classificationsParentNode.setHover( model.getConteIdseq() );

            //Is there at least one classification?
            if( this.classificationSchemeDAO.haveClassificationSchemes( model.getConteIdseq() ) )
            {
                insertPlaceHolderClassifications( classificationsParentNode );
            }

            //////////////////////////////////////////////////////////////////////////////////////////////////////
            //END  (Classification Scheme) folder
            //////////////////////////////////////////////////////////////////////////////////////////////////////

            //Protocol forms folder
            ParentNode protocolsParentNode = new ParentNode();
            initProtocolsFormsParentNode(protocolsParentNode, programAreaIndex, true ); //true = collapsed folder

            protocolsParentNode.setHref( oneContextRestServiceName + "?contextId=" + model.getConteIdseq() +
                    "&programArea=" + getProgramArea( model.getPalName() ) +
                    "&folderType=1");// folderType: 0 = classifications folder, 1 = protocol forms folder
            protocolsParentNode.setHover( model.getConteIdseq() );

            //////////////////////////////////////////////////
            //Protocol sList for this Context
            //////////////////////////////////////////////////
            //If protocolModelList > 0 then we have ProtocolForms, We add a dummy to make sure we show the Protocol Icon as enabled
            if( this.protocolDAO.haveProtocolsByContext( model.getConteIdseq() ) )
            {
                insertPlaceHolderProtocolForms(protocolsParentNode);
            }

            //////////////////////////////////////////////////////////////////////////
            //This Context node
            //////////////////////////////////////////////////////////////////////////
            ContextNode contextNodeParent = new ContextNode( model, programAreaIndex );
            contextNodeParent.setHover( contextNodeParent.getHover() );
            //Add Classification node
            contextNodeParent.addChildNode( classificationsParentNode );
            contextNodeParent.addChildNode( protocolsParentNode );

            //Add to the top node which is the Program Area.
            contextNodes[programAreaIndex].addTopNode( contextNodeParent );

            //Set the tabs hover text to the caption for this Program area, which will be shown below the selected tab.
            contextNodes[programAreaIndex].setHover( getProgramAreaDiscriptionByIndex( programAreaIndex ) );

            //Also add to "All" which is the first (0) program area.
            ContextNode contextNodeForAll = new ContextNode( contextNodeParent );
            contextNodeForAll.setProgramArea( 0 );
            setHrefProgramArea( contextNodeForAll, 0 );
            contextNodes[0].addTopNode( contextNodeForAll );
            contextNodes[0].setHover( "All Contexts" );
        }

        for( int f = 0; f < contextPalNameCount; f++ )
        {
            addBreadCrumbs( contextNodes[f], null );
        }

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
        //We will still include the parent program area as we build the tree, to get our BreadCrumbs right.
        programAreaModelList = restControllerCommon.getProgramAreaList();
        contextPalNameCount = programAreaModelList.size() + 1;//The + 1 is for "All"

        ContextNode[] contextNodes = new ContextNode[contextPalNameCount];
        for( int i = 1; i < contextPalNameCount; i++ )
        {
            contextNodes[i] = new ContextNode( CaDSRConstants.FOLDER, true, programAreaModelList.get( i - 1 ).getPalName(), i );
            contextNodes[i].setPalNameDescription( programAreaModelList.get( i - 1).getDescription() );
        }

        //Add all contexts tab at the end
        contextNodes[0] = new ContextNode( CaDSRConstants.FOLDER, true, "All", 0 );

        /////////////////////////////////////////////////////////////////////////////
        //Get list of all Contexts
        /////////////////////////////////////////////////////////////////////////////
        ContextModel model = this.contextDAO.getContextById( contextId );

        // All Protocol Forms
        List<ProtocolFormModel> protocolFormModelList = this.protocolFormDAO.getProtocolFormByContextId( contextId );

        //The contents of Classifications folder
        List<ClassificationSchemeModel> csModelList = this.classificationSchemeDAO.getClassificationSchemes( contextId );

        //Classification Scheme Item List
        setCsCsiNodelList( this.csCsiDAO.getCsCsisByConteId( contextId ) );

        /////////////////////////////////////////////////////////////////////////////
        //Get all Classifications for this context
        /////////////////////////////////////////////////////////////////////////////

        //CS (Classification Scheme) folder
        ParentNode classificationsParentNode = new ParentNode();
        initClassificationsParentNode( classificationsParentNode, programArea, (folderType == 1));//folderType  0 = classifications folder, 1 = protocol forms folder

        //Add the Classifications to the classificationsParentNode for this Context.
        insertClassifications( classificationsParentNode, csModelList, model, programArea );

        //END  (Classification Scheme) folder
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        //Protocol forms folder
        ParentNode protocolsParentNode = new ParentNode();
        initProtocolsFormsParentNode(protocolsParentNode, programArea, (folderType == 0) );//folderType  0 = classifications folder, 1 = protocol forms folder

        //////////////////////////////////////////////////
        //Protocol List for this Context
        //////////////////////////////////////////////////
        List<ProtocolModel> protocolModelList = this.protocolDAO.getProtocolsByContext( model.getConteIdseq() );
        for( ProtocolModel protocolModel : protocolModelList )
        {
            if( protocolModel.getConteIdseq().compareTo( model.getConteIdseq() ) == 0 )
            {
                //logger.debug( "**** protocolModel " + protocolModel.getLongName() );
                ProtocolNode protocolNode = initProtocolNode( protocolModel, programArea );

                //////////////////////////////////
                // Protocol Forms for this Protocol Folder
                //////////////////////////////////
                //getProtocolForms

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

                //If there are NO protocol forms for this protocol, don't add him.
                if( protocolNode.isIsParent() )
                {
                    protocolsParentNode.addChildNode( protocolNode );
                }
            }
        }

        //////////////////////////////////////////////////////////////////////////
        //This top node
        //////////////////////////////////////////////////////////////////////////
        ContextNode contextNodeParent = new ContextNode( model, programArea );
        contextNodeParent.setHover( contextNodeParent.getHover() );
        contextNodeParent.addChildNode( classificationsParentNode );
        contextNodeParent.addChildNode( protocolsParentNode );


        //Add to the top node, which is the Program Area.
        contextNodes[programArea].addTopNode( contextNodeParent );
        addBreadCrumbs( contextNodes[programArea], null );

        return contextNodes[programArea].getChildren();
    }


    private void addBreadCrumbs( BaseNode contextNode, List treePath )
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
    private void addBreadCrumbsAll( BaseNode contextNode, String programAreaStr )
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
     * Only used to set the program area in the href, when adding a context to "All"
     * also sets programArea.
     * TODO explain better
     *
     * @param contextNode
     * @param programArea
     */
    private void setHrefProgramArea( BaseNode contextNode, int programArea )
    {
        for( BaseNode node : contextNode.getChildren() )
        {
            String regex = "&programArea=[0-9]+";
            node.setHref( node.getHref().replaceAll( regex, "&programArea=" + programArea ) );
            node.setProgramArea( programArea );
        }
    }



    /*
    Folders within Classifications can have nested folders.
    Returns a list of children of this parent, if any.
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
        protocolsParentNode.setCollapsed( collapsed );
        protocolsParentNode.setType( CaDSRConstants.FOLDER );
        protocolsParentNode.setChildType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
        protocolsParentNode.setIsParent( false );// If and when a child is added IsParent will change to true.
        protocolsParentNode.setProgramArea( programArea );
        protocolsParentNode.setChildren( new ArrayList<BaseNode>() );

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
        classificationsParentNode.setType( CaDSRConstants.FOLDER );
        classificationsParentNode.setChildType( CaDSRConstants.FOLDER );
        classificationsParentNode.setCollapsed( collapsed );
        classificationsParentNode.setIsParent( false );        // If/When a child is added IsParent will change to true.
        classificationsParentNode.setProgramArea( programArea );
        classificationsParentNode.setChildren( new ArrayList<BaseNode>() );

    }


    protected void insertPlaceHolderClassifications( ParentNode classificationsParentNode )
    {
        ClassificationNode classificationSchemeNode = new ClassificationNode();
        //classificationSchemeNode.setChildType( CaDSRConstants.EMPTY );
        //classificationSchemeNode.setType( CaDSRConstants.FOLDER );
        classificationSchemeNode.setText( "Place Holder" );
        classificationSchemeNode.setHover( "Place Holder" );
        classificationSchemeNode.setCollapsed( true );
        classificationSchemeNode.setIsParent( false );
        classificationsParentNode.addChildNode( classificationSchemeNode );
    }

protected void insertPlaceHolderProtocolForms( ParentNode protocolsParentNode )
{
    ProtocolNode protocolNode = new ProtocolNode();
    protocolNode.setText( "Place Holder" );
    protocolNode.setHover( "Place Holder" );
    protocolNode.setCollapsed( true );
    protocolNode.setIsParent( false );
    protocolsParentNode.addChildNode( protocolNode );
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
        protocolFormNode.setText( protocolFormModel.getLongName() );
        String hoverText = protocolFormModel.getProtoPreferredDefinition();
        if( !hoverText.isEmpty() && hoverText.length() > maxHoverTextLen )
        {
            hoverText = hoverText.substring( 0, maxHoverTextLen ) + "...";
        }
        protocolFormNode.setHover( hoverText );
        protocolFormNode.setChildType( CaDSRConstants.EMPTY );
        protocolFormNode.setType( CaDSRConstants.PROTOCOL );
        protocolFormNode.setCollapsed( false );
        protocolFormNode.setIsParent( false );
        protocolFormNode.setProgramArea( programArea );
        return protocolFormNode;
    }

    private ProtocolNode initProtocolNode( ProtocolModel protocolModel, int programArea )
    {
        int maxHoverTextLen = Integer.parseInt( maxHoverTextLenStr );
        ProtocolNode protocolNode = new ProtocolNode();
        protocolNode.setText( protocolModel.getLongName() );
        String hoverText = protocolModel.getPreferredDefinition();
        if( !hoverText.isEmpty() && hoverText.length() > maxHoverTextLen )
        {
            hoverText = hoverText.substring( 0, maxHoverTextLen );
        }
        protocolNode.setHover( hoverText + "..." );
        protocolNode.setType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
        protocolNode.setChildType( CaDSRConstants.PROTOCOL );
        protocolNode.setCollapsed( true );
        protocolNode.setIsParent( false );
        protocolNode.setProgramArea( programArea );
        return protocolNode;
    }

    private int getProgramArea( String contextPalName )
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

    private String getProgramAreaDiscriptionByIndex( int i )
    {
        return programAreaModelList.get( i - 1 ).getDescription();
    }


    private String getContextSubsetString( int count, int i )
    {
        int interval = CaDSRConstants.INTERVAL_SIZE[count];

        int startLetter = ( interval * i );
        int endLetter = ( startLetter + interval );

        String subsetString = "";

        if( endLetter > 26 )
        {
            endLetter = 26;

        }

        if( endLetter != ( startLetter + 1 ) )
        {
            subsetString += Character.toChars( 65 + startLetter )[0] + "-";
        }
        subsetString += Character.toChars( 64 + endLetter )[0];
        return subsetString;
    }

/*


    private List<ProgramAreaModel> getProgramAreaList()
    {
        List<ProgramAreaModel> programAreaModelList = programAreaDAO.getAllProgramAreas();
        Collections.sort( programAreaModelList, new ProgramAreaComparator() );
        return programAreaModelList;
    }

    private class ProgramAreaComparator implements Comparator<ProgramAreaModel>
    {
        @Override
        public int compare( ProgramAreaModel a, ProgramAreaModel b )
        {
            return a.getPalName().toUpperCase().compareTo( b.getPalName().toUpperCase() );

        }
    }
*/
}
