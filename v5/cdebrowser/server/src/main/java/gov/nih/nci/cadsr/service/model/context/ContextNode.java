/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.model.context;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ContextNode extends BaseNode
{

    private Logger logger = LogManager.getLogger( ContextNode.class.getName() );
    private String palName ="";
    private String palNameDescription ="";

    public ContextNode()
    {
        super();
    }

    public String getPalName()
    {
        return palName;
    }

    public void setPalName( String palName )
    {
        this.palName = palName;
    }

    public String getPalNameDescription()
    {
        return palNameDescription;
    }

    public void setPalNameDescription( String palNameDescription )
    {
        this.palNameDescription = palNameDescription;
    }

    /**
     * Hover text is set to empty String
     *
     * @param type
     * @param isCollapsed
     * @param name
     */
    public ContextNode( int type, boolean isCollapsed, String name )
    {
        this( type, isCollapsed, name, "" );
    }


    /**
     * @param type
     * @param isCollapsed
     * @param name
     * @param hoverText
     */
    public ContextNode( int type, boolean isCollapsed, String name, String hoverText )
    {
        super();
        this.setChildren( new ArrayList<BaseNode>() );
        this.setIsParent( false );
        this.setType( type );
        this.setText( name );
        this.setHover( hoverText );
        // Contexts don't need tooltips
        //this.setHover( hoverText );

        //FIXME - just test text for now
        //this.setHref( "test.html?context=" + this.getText() );
        this.setCollapsed( isCollapsed );
    }

    /**
     * Create a new Context Node with the values of a Context Model from the database
     *
     * @param contextModel database model returned by SQL query
     */
    public ContextNode( ContextModel contextModel)
    {
        super();
        this.setText( contextModel.getName() + " (" + contextModel.getDescription() + ") " );
        this.setPalName( contextModel.getPalName() );
//this.setPalNameDescription( palNameDescription );
        this.setType( CaDSRConstants.FOLDER );
        this.setCollapsed( true );
        this.setIsParent( false );
        // Contexts don't need tooltips
        //this.setHover( this.getText() );

        //FIXME - just test text for now
        //this.setHref( "test.html?context=" + this.getText() );
        this.setChildType( 0 );
        this.setIdSeq( contextModel.getConteIdseq() );
        this.setChildren( new ArrayList<BaseNode>() );
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( super.toString() );
        sb.append( "palName=" + this.palName + "\n" );
        sb.append( "palNameDescription=" + this.palNameDescription + "\n" );
        return sb.toString();
    }
}
