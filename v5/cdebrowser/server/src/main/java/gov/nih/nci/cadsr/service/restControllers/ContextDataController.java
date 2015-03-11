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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class ContextDataController
{

    private Logger logger = LogManager.getLogger( ContextDataController.class.getName() );

    private ContextDAOImpl contextDAO;
    private ClassificationSchemeDAOImpl classificationSchemeDAO;
    private CsCsiDAOImpl csCsiDAO;
    private ProtocolFormDAOImpl protocolFormDAO;
    private ProtocolDAOImpl protocolDAO;
    private ProgramAreaDAOImpl programAreaDAO;
    // private List<ClassificationSchemeModel> csModelList = null;
    private List<CsCsiModel> csCsiNodelList = null;
    private List<ProgramAreaModel> programAreaModelList = null;

    private boolean includeClassification = false;
    private boolean includeProtocol = false;
    private String message;

    @Value("${maxHoverTextLen}")
    String maxHoverTextLenStr;

    private int contextSubsetCount;
    private int contextPalNameCount;
    private int uiType;

    public ContextDataController()
    {
        logger.debug( "IN ContextDataController constructor" );
        setTestMode( true );
    }

    public void setTestMode( boolean testMode )
    {
        includeProtocol = ( !testMode );
        includeClassification = ( !testMode );
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

    public void setMessage( String message )
    {
        this.message = message;
    }


    public void init()
    {
        this.message = "Init ";
    }

    @RequestMapping(value = "/contextData")
    @ResponseBody
    public ContextNode[] contextData()
    {
        logger.debug( "Received rest call \"contextData\"" );
        ContextNode[] contextNodes = getAllTreeData();
        logger.debug( "Done rest call\n=========================\n" );
        return contextNodes;
    }

    private ContextNode[] getAllTreeData()
    {

        contextPalNameCount = initProgramAreaList();
        ContextNode[] contextNodes = new ContextNode[contextPalNameCount + 1]; //The + 1 is for "All"
        for( int i = 0; i < contextPalNameCount; i++ )
        {
            contextNodes[i] = new ContextNode( CaDSRConstants.FOLDER, true, programAreaModelList.get( i ).getPalName() );
            contextNodes[i].setPalNameDescription( programAreaModelList.get( i ).getDescription() );
        }

        //Add all contexts tab at the end
        contextNodes[contextPalNameCount] = new ContextNode( CaDSRConstants.FOLDER, true, "All" );

        /////////////////////////////////////////////////////////////////////////////
        //Get list of all Contexts
        /////////////////////////////////////////////////////////////////////////////
        List<ContextModel> contextModelList = this.contextDAO.getAllContexts();

        //Protocol List of all
        // Don't do this, no faster than getting  Protocols by Context
        //List<ProtocolModel> protocolModelList = this.protocolDAO.getAllProtocols();

        // All Protocol Forms
        List<ProtocolFormModel> protocolFormModelList = null;


        if( includeProtocol )
        {
            protocolFormModelList = this.protocolFormDAO.getAllProtocolForm();
        }


        // Just for dev time testing
        //List<ContextModel> contextModelList = this.contextDAO.getContextsByName( "CCR" );
        //List<ContextModel> contextModelList = this.contextDAO.getContextsByName( "ABTC" );
        //List<ContextModel> contextModelList = this.contextDAO.getContextsByName( "Alliance" );


        //The contents of a Classification folder
        List<ClassificationSchemeModel> csModelList = this.classificationSchemeDAO.getAllClassificationSchemes();
        //csModelList = this.classificationSchemeDAO.getAllClassificationSchemes();
        if( includeClassification )
        {
            //Classification Scheme Item List
            setCsCsiNodelList( this.csCsiDAO.getAllCsCsis() );
        }

        for( ContextModel model : contextModelList )
        {

            /////////////////////////////////////////////////////////////////////////////
            //Get all Classifications for this context
            /////////////////////////////////////////////////////////////////////////////

            //CS (Classification Scheme) folder
            ParentNode classificationsParentNode = new ParentNode();
            initClassificationsParentNode( classificationsParentNode );

            if( includeClassification )
            {
                //Add the Classifications to the classificationsParentNode for this Context.
                insertClassifications( classificationsParentNode, csModelList, model );
            }
            //END  (Classification Scheme) folder
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////

            //Protocol forms folder
            ParentNode protocolsParentNode = new ParentNode();
            protocolsParentNode.setText( "ProtocolForms" );
            protocolsParentNode.setCollapsed( true );
            protocolsParentNode.setIsParent( false );// If and when a child is added IsParent will change to true.
            protocolsParentNode.setType( CaDSRConstants.FOLDER );
            protocolsParentNode.setChildType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
            //Don't need tooltip for this
            //protocolsParentNode.setHover( protocolsParentNode.getText() );
            protocolsParentNode.setHref( protocolsParentNode.getHref() );
            protocolsParentNode.setChildren( new ArrayList<BaseNode>() );


            if( includeProtocol )
            {

                //////////////////////////////////////////////////
                //Protocol List for this Context
                //////////////////////////////////////////////////
                List<ProtocolModel> protocolModelList = this.protocolDAO.getProtocolsByContext( model.getConteIdseq() );
                for( ProtocolModel protocolModel : protocolModelList )
                {
                    if( protocolModel.getConteIdseq().compareTo( model.getConteIdseq() ) == 0 )
                    {
                        //logger.debug( "**** protocolModel " + protocolModel.getLongName() );
                        ProtocolNode protocolNode = initProtocolNode( protocolModel );

                        //////////////////////////////////
                        // Protocol Forms for this Protocol Folder
                        //////////////////////////////////
                        //getProtocolForms
                        //List<ProtocolFormModel> protocolFormModelList = this.protocolFormDAO.getProtocolFormByProtoId( protocolModel.getProtoIdseq() );
                        String protoId = protocolModel.getProtoIdseq();
                        for( ProtocolFormModel protocolFormModel : protocolFormModelList )
                        {
                            String protoFormId = protocolFormModel.getProtoIdseq();
                            if( protoFormId != null && protoFormId.compareTo( protoId ) == 0 )
                            {
                                ProtocolFormNode protocolFormNode = initProtocolFormNode( protocolFormModel );
                                protocolNode.addChildNode( protocolFormNode );
                            }
                        }
                        //logger.debug( "**** AFTER Get Protocol Forms" );

                        //If there are NO protocol forms for this protocol, don't add him.
                        if( protocolNode.isIsParent() )
                        {
                            protocolsParentNode.addChildNode( protocolNode );
                        }
                    }
                }
            }


            //////////////////////////////////////////////////////////////////////////
            //This top node
            //////////////////////////////////////////////////////////////////////////
            ContextNode contextNodeParent = new ContextNode( model );
            contextNodeParent.setHover( contextNodeParent.getHover() );
            contextNodeParent.addChildNode( classificationsParentNode );
            contextNodeParent.addChildNode( protocolsParentNode );


            //Add to the top node which is the Program Area.
            int programAreaIndex = getProgramArea( contextNodeParent.getPalName() );


            contextNodes[programAreaIndex].addTopNode( contextNodeParent );
            //Set the tabs hover text to the caption for this Program area
            contextNodes[programAreaIndex].setHover( getProgramAreaDiscriptionByIndex( programAreaIndex ) );

            //Also add to "All" which is the last program area.
            contextNodes[contextPalNameCount].addTopNode( contextNodeParent );
            contextNodes[contextPalNameCount].setHover( "All Contexts" );


        }
        return contextNodes;
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
        // No children
        else
        {
            //Parent
            classificationItemNodeParent.setIsParent( false );
            classificationItemNodeParent.setCollapsed( false );
            classificationItemNodeParent.setChildType( CaDSRConstants.EMPTY );
            classificationItemNodeParent.setType( CaDSRConstants.CSI );
        }
    }

    protected void initClassificationsParentNode( ParentNode classificationsParentNode )
    {
        //CS (Classification Scheme) folder
        classificationsParentNode.setChildType( CaDSRConstants.FOLDER );
        classificationsParentNode.setType( CaDSRConstants.FOLDER );
        classificationsParentNode.setText( "Classifications" );
        classificationsParentNode.setCollapsed( true );
        // If/When a child is added IsParent will change to true.
        classificationsParentNode.setIsParent( false );
        //No need for tooltip
        //classificationsParentNode.setHover( classificationsParentNode.getText() );
        classificationsParentNode.setHref( classificationsParentNode.getHref() );
        classificationsParentNode.setChildren( new ArrayList<BaseNode>() );

    }

    protected void insertClassifications( ParentNode classificationsParentNode, List<ClassificationSchemeModel> csModelList, ContextModel contextModel )
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
                //logger.debug( "\nclassificationSchemeNode: " + classificationSchemeNode.toString() );

                ////////////////////////////////////////////////
                //Get CSI (Classification Scheme Item) list for this CS (Classification Scheme)
                ////////////////////////////////////////////////
                String csId = classificationSchemeModel.getCsIdseq();
                for( CsCsiModel csCsiModel : csCsiNodelList )
                {
/*
                    //FIXME just for dev time debugging
                    if( csCsiModel.getCsConteIdseq().compareTo( "E5CA1CEF-E2C6-3073-E034-0003BA3F9857" ) == 0 )
                    {
                        logger.debug( "\n\n\ncsCsiNodelList - csCsiModel: " + csCsiModel.toString() + "   csCsiModel.getCsIdseq(): " +
                                csCsiModel.getCsIdseq() + "\n--------------------------------------" );
                    }

*/
                    if( csCsiModel.getCsIdseq().compareTo( csId ) == 0 )
                    {
                        //Create the new, and set as much as we can without knowing if it has children
                        ClassificationItemNode classificationSchemeItemNode = new ClassificationItemNode();
                        classificationSchemeItemNode.setText( csCsiModel.getCsiName() );
                        //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + contextModel.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
                        classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
                        classificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
                        classificationSchemeItemNode.setCollapsed( true );
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
    protected ProtocolFormNode initProtocolFormNode( ProtocolFormModel protocolFormModel )
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

        return protocolFormNode;
    }

    private ProtocolNode initProtocolNode( ProtocolModel protocolModel )
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
        return protocolNode;
    }

    /*
       for now we are breaking out the top level of the Context Tree menu by by alphabetical groups,
       This method determines which group a context will be added to.
     */
    protected int getContextSubsetIndex( String contextName )
    {

        if( this.contextSubsetCount < 2 )
        {
            return 0;
        }
        int index;
        int interval = CaDSRConstants.INTERVAL_SIZE[this.contextSubsetCount];
        Character firstChar = contextName.toUpperCase().charAt( 0 );
        index = ( firstChar - 65 ) / interval;
        return index;
    }

    private int getProgramArea( String contextPalName )
    {
        for( int i = 0; i < contextPalNameCount; i++ )
        {
            if( programAreaModelList.get( i ).getPalName().compareTo( contextPalName ) == 0 )
            {
                return i;
            }
        }
        return 0;
    }

    private String getProgramAreaDiscriptionByIndex( int i )
    {
        return programAreaModelList.get( i ).getDescription();
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


    /**
     * @return programArea count
     */
    private int initProgramAreaList()
    {
        programAreaModelList = programAreaDAO.getAllProgramAreas();
        Collections.sort( programAreaModelList, new ProgramAreaComparator() );
        return programAreaModelList.size();
    }

    private class ProgramAreaComparator implements Comparator<ProgramAreaModel>
    {
        @Override
        public int compare( ProgramAreaModel a, ProgramAreaModel b )
        {
            return a.getPalName().toUpperCase().compareTo( b.getPalName().toUpperCase() );

        }
    }
}
