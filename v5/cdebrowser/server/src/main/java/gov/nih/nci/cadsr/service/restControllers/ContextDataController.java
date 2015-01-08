/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.*;
import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.service.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Value("${maxHoverTextLen}")  String maxHoverTextLenStr;

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

    public void setMessage( String message )
    {
        this.message = message;
    }


    public void init()
    {
        this.message = "Init ";
    }

    @RequestMapping("/context_data")
    public ContextNode[] getContextData()
    {
        logger.debug( "Received rest call \"context_data\"" );

        ContextNode[] contextNodes = new ContextNode[2];
        contextNodes[0] = new ContextNode( CaDSRConstants.FOLDER, false, "caDSR Contexts" );

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
            classificationsParentNode.setContextName( "Classifications" );
            classificationsParentNode.setCollapsed( true );
            // If/When a child is added IsParent will change to true.
            classificationsParentNode.setIsParent( false );
            //No need for tooltip
            //classificationsParentNode.setHover( classificationsParentNode.getContextName() );
            classificationsParentNode.setAction( "Action for " + classificationsParentNode.getContextName() );
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
                        classificationSchemeNode.setContextName( classificationSchemeModel.getLongName() );
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
                                classificationSchemeItemNode.setContextName( csCsiModel.getCsiName() );
                                //classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() + "    Conte Idseq:" + model.getConteIdseq() + "     Csi Idseq:" + csCsiModel.getCsiIdseq() );
                                classificationSchemeItemNode.setHover( csCsiModel.getCsiDescription() );
                                classificationSchemeItemNode.setIdSeq( csCsiModel.getCsCsiIdseq() );
                                classificationSchemeItemNode.setCollapsed( true );
                                addChildrenToCsi( classificationSchemeItemNode );

                                //Add this CSI to the CS
                                classificationSchemeNode.addChild( classificationSchemeItemNode );
                            }
                        }
                        //    logger.debug( "DONE Getting CSIs for " + model.getConteIdseq() );
                        //Add this CS to the CS Folder
                        classificationsParentNode.addChild( classificationSchemeNode );
                    }
                }
            }
            //END  (Classification Scheme) folder
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////

            //Protocol forms folder
            ParentNode protocolsParentNode = new ParentNode();
            protocolsParentNode.setContextName( "ProtocolForms" );
            protocolsParentNode.setCollapsed( true );
            protocolsParentNode.setIsParent( false );// If and when a child is added IsParent will change to true.
            protocolsParentNode.setType( CaDSRConstants.FOLDER );
            protocolsParentNode.setChildType( CaDSRConstants.PROTOCOL_FORMS_FOLDER );
            //Don't need tooltip for this
            //protocolsParentNode.setHover( protocolsParentNode.getContextName() );
            protocolsParentNode.setAction( "Action for " + protocolsParentNode.getContextName() );
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
                                protocolNode.addChild( protocolFormNode );
                            }
                        }
                        //logger.debug( "**** AFTER Get Protocol Forms" );

                        //If there are NO protocol forms for this protocol, don't add him.
                        if( protocolNode.isIsParent() )
                        {
                            protocolsParentNode.addChild( protocolNode );
                        }
                    }
                }
            }

            //////////////////////////////////////////////////////////////////////////
            //This top node
            //////////////////////////////////////////////////////////////////////////
            ContextNode contextNodeParent = new ContextNode( model );
            contextNodeParent.setHover( contextNodeParent.getHover() );
            contextNodeParent.addChild( classificationsParentNode );
            contextNodeParent.addChild( protocolsParentNode );

            contextNodes[0].addChild( contextNodeParent );
        }
        logger.debug( "Done rest call\n=========================\n" );

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



        contextNode[6].addChild( contextNode[7] );
        contextNode[6].addChild( contextNode[8] );
        contextNode[6].addChild( contextNode[8] );

        contextNode[3].addChild( contextNode[4] );
        contextNode[3].addChild( contextNode[5] );
        contextNode[3].addChild( contextNode[6] );
        contextNode[3].addChild( contextNode[9] );

        contextNode[10].addChild( contextNode[11] );
        contextNode[10].addChild( contextNode[12] );

        contextNode[17].addChild( contextNode[18] );
        contextNode[17].addChild( contextNode[19] );

        contextNode[14].addChild( contextNode[15] );
        contextNode[13].addChild( contextNode[14] );
        contextNode[13].addChild( contextNode[17] );

        contextNode[2].addChild( contextNode[3] );
        contextNode[2].addChild( contextNode[10] );
        contextNode[1].addChild( contextNode[2] );
        contextNode[1].addChild( contextNode[13] );
        contextNode[0].addChild( contextNode[1] );

        return contextNode[0];
    }

*/
    private List<CsCsiModel> getCsCsisByParentCsCsi( String csCsiIdseq )
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
     *
     * @param ClassificationItemNodeParent
     */
    private void addChildrenToCsi( ClassificationItemNode ClassificationItemNodeParent )
    {
        List<CsCsiModel> csCsiChildModelParentList = getCsCsisByParentCsCsi( ClassificationItemNodeParent.getIdSeq() );
//logger.debug( "cNodeCsi["+ cNodeCsi.getContextName() + " Child count: " + csCsiChildModelParentList.size() );

        if( !csCsiChildModelParentList.isEmpty() )
        {
            for( CsCsiModel csCsiChildModel : csCsiChildModelParentList )
            {
//logger.debug( "CSI Child: " + csCsiChildModel.getCsiName() );
                ClassificationItemNode classificationItemNodeChild = new ClassificationItemNode();

                classificationItemNodeChild.setIsParent( false );
                classificationItemNodeChild.setCollapsed( false );
                classificationItemNodeChild.setChildType( CaDSRConstants.EMPTY );
                classificationItemNodeChild.setType( CaDSRConstants.CSI );
                classificationItemNodeChild.setContextName( csCsiChildModel.getCsiName() );
                classificationItemNodeChild.setHover( csCsiChildModel.getCsiDescription() );
                classificationItemNodeChild.setIdSeq( csCsiChildModel.getCsiIdseq() );

                //Add this child to the Parent
                ClassificationItemNodeParent.addChild( classificationItemNodeChild );
            }

            //Parent
            ClassificationItemNodeParent.setIsParent( true );
            ClassificationItemNodeParent.setCollapsed( true );
            ClassificationItemNodeParent.setChildType( CaDSRConstants.CSI );
            ClassificationItemNodeParent.setType( CaDSRConstants.CIS_FOLDER );
        }
        // No children
        else
        {
            //Parent
            ClassificationItemNodeParent.setIsParent( false );
            ClassificationItemNodeParent.setCollapsed( false );
            ClassificationItemNodeParent.setChildType( CaDSRConstants.EMPTY );
            ClassificationItemNodeParent.setType( CaDSRConstants.CSI );
        }
    }


    private ProtocolFormNode initProtocolFormNode( ProtocolFormModel protocolFormModel )
    {
        int maxHoverTextLen = Integer.parseInt(maxHoverTextLenStr);
        ProtocolFormNode protocolFormNode = new ProtocolFormNode();
        protocolFormNode.setContextName( protocolFormModel.getLongName() );
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
        int maxHoverTextLen = Integer.parseInt(maxHoverTextLenStr);
        ProtocolNode protocolNode = new ProtocolNode();
        protocolNode.setContextName( protocolModel.getLongName() );
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
}
