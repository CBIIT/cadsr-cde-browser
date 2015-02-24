/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.util.StringUtils;
import gov.nih.nci.cadsr.dao.*;
import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.service.model.context.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    private List<ClassificationSchemeModel> csModelList = null;
    private List<CsCsiModel> csCsiNodelList = null;

    private boolean includeClassification = true;
    private boolean includeProtocol = true;
    private String message;

    @Value("${maxHoverTextLen}")
    String maxHoverTextLenStr;

    private int contextSubsetCount;
    private int uiType;

    public ContextDataController()
    {

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

    public void setCsCsiNodelList( List<CsCsiModel> csCsiNodelList )
    {
        this.csCsiNodelList = csCsiNodelList;
    }

    public void setMaxHoverTextLenStr( String maxHoverTextLen)
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

 /*
    @RequestMapping(value = "/contextDataTest")
    @ResponseBody
    public String contextDataTest( @RequestParam("uiType") int clientUiType )
    {

        String results = null;
        try
        {
            results = StringUtils.readFile( "/opt/devel/projects/cadsr-cde-browser/v5/cdebrowser/server/src/main/webapp/data1.json" );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return results;
    }
*/

    @RequestMapping(value = "/contextDataTest")
    @ResponseBody
    public String contextDataTest( @RequestParam("uiType") int clientUiType )
    {

        String json = null;
        try
        {
            json = StringUtils.readFile( "data1.json" );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        return json;
    }


    @RequestMapping(value = "/contextData")
    @ResponseBody
    public ContextNode[] contextData( @RequestParam("uiType") int clientUiType )
    {
        logger.debug( "Received rest call \"contextData\" uiType: " + clientUiType );
        ContextNode[] contextNodes = getAllTreeData( clientUiType);
        logger.debug( "Done rest call\n=========================\n" );
        return contextNodes;
    }

private ContextNode[] getAllTreeData(int clientUiType)
{
    this.contextSubsetCount = clientUiType;
    ContextNode[] contextNodes = null;

    if( this.contextSubsetCount < 2 )
    {
        contextNodes = new ContextNode[1];
        contextNodes[0] = new ContextNode( CaDSRConstants.FOLDER, false, "caDSR Contexts" );
    }
    else
    {
        contextNodes = new ContextNode[this.contextSubsetCount];
        for( int i = 0; i < this.contextSubsetCount; i++ )
        {
            //logger.debug( " >>> " + getContextSubsetString( uiType, i ) );
            contextNodes[i] = new ContextNode( CaDSRConstants.FOLDER, true, getContextSubsetString( this.contextSubsetCount, i ) );
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    //Get list of all Contexts
    /////////////////////////////////////////////////////////////////////////////
    List<ContextModel> contextModelList = this.contextDAO.getAllContexts();
    logger.debug("contextModelList[0]: " + contextModelList.get( 0 ).toString());

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
    csCsiNodelList = this.csCsiDAO.getAllCsCsis();

    for( ContextModel model : contextModelList )
    {
        /////////////////////////////////////////////////////////////////////////////
        //Get all Classifications for this context
        /////////////////////////////////////////////////////////////////////////////

        //CS (Classification Scheme) folder
        ParentNode classificationsParentNode = new ParentNode();
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

        if( includeClassification )
        {
            //////////////////////////////////////////////////
            //CS (Classification Scheme) List for this Context
            //////////////////////////////////////////////////
            //List<ClassificationSchemeModel> csModelList = this.classificationSchemeDAO.getClassificationSchemes( model.getConteIdseq() );
            String contextId = model.getConteIdseq();
            for( ClassificationSchemeModel classificationSchemeModel : csModelList )
            {
                if( classificationSchemeModel.getConteIdseq().compareTo( contextId ) == 0 )
                {
                    //logger.debug( "**** classificationSchemeModel " + classificationSchemeModel.getLongName() );
                    ClassificationNode classificationSchemeNode = new ClassificationNode();
                    classificationSchemeNode.setChildType( CaDSRConstants.EMPTY );
                    classificationSchemeNode.setType( CaDSRConstants.FOLDER );
                    classificationSchemeNode.setText( classificationSchemeModel.getLongName() );
                    //classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() + " Conte Idseq:" + model.getConteIdseq() + " Cs Idseq:" + classificationSchemeModel.getCsIdseq() );
                    classificationSchemeNode.setHover( classificationSchemeModel.getPreferredDefinition() );
                    classificationSchemeNode.setCollapsed( true );
                    classificationSchemeNode.setIsParent( false );
                    classificationSchemeNode.setIdSeq( classificationSchemeModel.getCsIdseq() );
                    //logger.debug( "\nclassificationSchemeNode: " + classificationSchemeNode.toString() );

                    ////////////////////////////////////////////////
                    //Get CSI (Classification Scheme Item) list for this CS (Classification Scheme)
                    ////////////////////////////////////////////////
                    //csCsiNodelList = this.csCsiDAO.getCsCsisById( classificationSchemeModel.getCsIdseq() );
                    String csId = classificationSchemeModel.getCsIdseq();
                    for( CsCsiModel csCsiModel : csCsiNodelList )
                    {
                        if( csCsiModel.getCsIdseq().compareTo( csId ) == 0 )
                        {
                            //logger.debug( "GetCsi [" + classificationSchemeModel.getLongName() + "] : " + csCsiModel.getCsiName() );
                            //Set as much as we can without knowing if it has children
                            ClassificationItemNode classificationSchemeItemNode = new ClassificationItemNode();
                            classificationSchemeItemNode.setText( csCsiModel.getCsiName() );
                            //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + model.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
                            classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
                            classificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
                            classificationSchemeItemNode.setCollapsed( true );
                            logger.debug( "addChildrenToCsi(" +  classificationSchemeItemNode.getText() +")   " + csCsiModel.getCsiName() + "  " + csCsiModel.getCsiDescription());
                            addChildrenToCsi( classificationSchemeItemNode );

                            //Add this CSI to the CS
                            classificationSchemeNode.addChildNode( classificationSchemeItemNode );
                        }
                    }
                    //    logger.debug( "DONE Getting CSIs for " + model.getConteIdseq() );
                    //Add this CS to the CS Folder
                    classificationsParentNode.addChildNode( classificationSchemeNode );
                }
            }
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
        //ContextNode contextNodeParent = new ContextNode( model );
        ContextNode contextNodeParent = new ContextNode( model );
        contextNodeParent.setHover( contextNodeParent.getHover() );
        contextNodeParent.addChildNode( classificationsParentNode );
        contextNodeParent.addChildNode( protocolsParentNode );

//logger.debug( "index for " + contextNodeParent.getText() + "is "  +  getContextSubsetIndex( contextNodeParent.getText()));

        //contextNodes[getContextSubsetIndex( contextNodeParent.getText() )].addChildNode( contextNodeParent );
        contextNodes[getContextSubsetIndex( contextNodeParent.getText() )].addTopNode( contextNodeParent );
    }
    return contextNodes;
}


    /**
     * Produce test data
     *
     * @return
     */
/*

    public BaseNode getContextTestNodes()
    {

        int nodeCount = 20;
        BaseNode[] contextNode = new BaseNode[nodeCount];
        for( int f = 0; f < nodeCount; f++ )
        {
            contextNode[f] = new BaseNode();
        }
        
            */
/*
                 "container"  = 1;
	             "csi folder"  = 2;
                 "folder" = 3;
                 "protocol forms folder"  = 4;
                 "leaf"  = 6;
                *//*



        int i = 0;

        contextNode[i++] = new BaseNode( 3, false, "caDSR Contexts", "caDSR Contexts - hover text" );
        contextNode[i++] = new BaseNode( 3, true, "ABTC (Adult Brain Tumor Consortium)", "ABTC (Adult Brain Tumor Consortium)" );
        contextNode[i++] = new BaseNode( 3, false, "Classifications", "Collections of CDEs by type of CRF" );
        contextNode[i++] = new BaseNode( 5, true, "CRF CDEs", "ollections of CDEs by type of CRF" );
        contextNode[i++] = new BaseNode( 2, false, "30-Day Follow-Up", "CDEs used on 30-Day Follow-Up CRFs" );
        contextNode[i++] = new BaseNode( 2, false, "Adverse Event Cover Sheet", "Collection of CDEs used on Adverse Event Cover Sheet CRFs." );
        contextNode[i++] = new BaseNode( 5, true, "Dosing", "Collection of CDEs used on Dosing CRFs." );
        contextNode[i++] = new BaseNode( 2, false, "Lapatinib Dosing", "CDEs for Lapatinib Dosing" );
        contextNode[i++] = new BaseNode( 2, false, "MK-1775 Dosing", "CCDEs for MK-1775 Dosing" );
        contextNode[i++] = new BaseNode( 2, false, "ECG", "Collection of CDEs used on ECG CRFs." );
        contextNode[i++] = new BaseNode( 5, true, "Protocols", "Collection of CDEs by ABTC Protocols" );
        contextNode[i++] = new BaseNode( 2, false, "ABTC 1202", "ABTC 1202" );
        contextNode[i++] = new BaseNode( 2, false, "ABTC 0904", "ABTC 0904" );
        contextNode[i++] = new BaseNode( 3, true, "Protocol Forms", "Protocol Forms" );
        contextNode[i++] = new BaseNode( 4, true, "ABTC-0904", "ABTC-0904" );
        contextNode[i++] = new BaseNode( 6, false, "ABTC 30-Day Follow Up", "ABTC 30-Day Follow Up" );
        contextNode[i++] = new BaseNode( 6, false, "ABTC Adverse Event Cover Sheet", "ABTC Adverse Event Cover Sheet" );
        contextNode[i++] = new BaseNode( 4, true, "ABTC-1202", "ABTC-1202" );
        contextNode[i++] = new BaseNode( 6, false, "MK-1775: 30-Day Follow-Up", "MK-1775: 30-Day Follow-Up" );
        contextNode[i++] = new BaseNode( 6, false, "MK-1775: Adverse Event Cover Sheet", "MK-1775: Adverse Event Cover Sheet" );



        contextNode[6].addChildNode( contextNode[7] );
        contextNode[6].addChildNode( contextNode[8] );
        contextNode[6].addChildNode( contextNode[8] );

        contextNode[3].addChildNode( contextNode[4] );
        contextNode[3].addChildNode( contextNode[5] );
        contextNode[3].addChildNode( contextNode[6] );
        contextNode[3].addChildNode( contextNode[9] );

        contextNode[10].addChildNode( contextNode[11] );
        contextNode[10].addChildNode( contextNode[12] );

        contextNode[17].addChildNode( contextNode[18] );
        contextNode[17].addChildNode( contextNode[19] );

        contextNode[14].addChildNode( contextNode[15] );
        contextNode[13].addChildNode( contextNode[14] );
        contextNode[13].addChildNode( contextNode[17] );

        contextNode[2].addChildNode( contextNode[3] );
        contextNode[2].addChildNode( contextNode[10] );
        contextNode[1].addChildNode( contextNode[2] );
        contextNode[1].addChildNode( contextNode[13] );
        contextNode[0].addChildNode( contextNode[1] );

        return contextNode[0];
    }

*/

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
                logger.debug( "IN addChildrenToCsi Child from list of children: " + classificationItemNodeChild.getText() + "  Parent: " + classificationItemNodeParent.getText());

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
/*
        else
        {
            logger.debug( "endLetter: " + endLetter + " NOT > 26" );
        }
*/

        if( endLetter != ( startLetter + 1 ) )
        {
            subsetString += Character.toChars( 65 + startLetter )[0] + "-";
        }
        subsetString += Character.toChars( 64 + endLetter )[0];
        return subsetString;
    }
}
