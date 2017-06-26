package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.CaDSRConstants;

public class ProtocolFormModel extends BaseModel implements Comparable
{
	private static final long serialVersionUID = 1L;
	
	private String qcIdseq;
    private String version;
    private String type;
    private String conteIdseq;
    private String categoryName;
    private String workflow;
    private String preferredName;
    private String definition;
    private String longName;
    private String protoIdseq;
    private String protocolLongName;
    private String contextName;
    private String protoPreferredDefinition;
    private int publicId;
    private String changeNote;
    private String latestVersionInd;
    // csc/acs private String csCsiIdseq;
    private String csIdseq;
    private String pCsCsiIdseq;
    private String linkCsCsiIdseq;
    private String label;
    private String dispalyOrder;
    private String acCsiIdseq;
    private String csCsiIdseq;
    private String acIdseq;

    public ProtocolFormModel()
    {
    }

    public String getQcIdseq()
    {
        return qcIdseq;
    }

    public void setQcIdseq( String qcIdseq )
    {
        this.qcIdseq = qcIdseq;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
        setFormattedVersion( version );
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName( String categoryName )
    {
        this.categoryName = categoryName;
    }

    public String getWorkflow()
    {
        return workflow;
    }

    public void setWorkflow( String workflow )
    {
        this.workflow = workflow;
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition( String definition )
    {
        this.definition = definition;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getProtoIdseq()
    {
        return protoIdseq;
    }

    public void setProtoIdseq( String protoIdseq )
    {
        this.protoIdseq = protoIdseq;
    }

    public String getProtocolLongName()
    {
        return protocolLongName;
    }

    public void setProtocolLongName( String protocolLongName )
    {
        this.protocolLongName = protocolLongName;
    }

    public String getContextName()
    {
        return contextName;
    }

    public void setContextName( String contextName )
    {
        this.contextName = contextName;
    }

    public String getProtoPreferredDefinition()
    {
        return protoPreferredDefinition;
    }

    public void setProtoPreferredDefinition( String protoPreferredDefinition )
    {
        this.protoPreferredDefinition = protoPreferredDefinition;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public String getChangeNote()
    {
        return changeNote;
    }

    public void setChangeNote( String changeNote )
    {
        this.changeNote = changeNote;
    }

    public String getLatestVersionInd()
    {
        return latestVersionInd;
    }

    public void setLatestVersionInd( String latestVersionInd )
    {
        this.latestVersionInd = latestVersionInd;
    }

    public String getCsIdseq()
    {
        return csIdseq;
    }

    public void setCsIdseq( String csIdseq )
    {
        this.csIdseq = csIdseq;
    }

    public String getpCsCsiIdseq()
    {
        return pCsCsiIdseq;
    }

    public void setpCsCsiIdseq( String pCsCsiIdseq )
    {
        this.pCsCsiIdseq = pCsCsiIdseq;
    }

    public String getLinkCsCsiIdseq()
    {
        return linkCsCsiIdseq;
    }

    public void setLinkCsCsiIdseq( String linkCsCsiIdseq )
    {
        this.linkCsCsiIdseq = linkCsCsiIdseq;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }

    public String getDispalyOrder()
    {
        return dispalyOrder;
    }

    public void setDispalyOrder( String dispalyOrder )
    {
        this.dispalyOrder = dispalyOrder;
    }

    public String getAcCsiIdseq()
    {
        return acCsiIdseq;
    }

    public void setAcCsiIdseq( String acCsiIdseq )
    {
        this.acCsiIdseq = acCsiIdseq;
    }

    public String getCsCsiIdseq()
    {
        return csCsiIdseq;
    }

    public void setCsCsiIdseq( String csCsiIdseq )
    {
        this.csCsiIdseq = csCsiIdseq;
    }

    public String getAcIdseq()
    {
        return acIdseq;
    }

    public void setAcIdseq( String acIdseq )
    {
        this.acIdseq = acIdseq;
    }

    public String toString()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "qcIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + qcIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "version " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + version + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "type " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + type + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "conteIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + conteIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "categoryName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + categoryName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "workflow " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + workflow + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "preferredName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + preferredName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "definition " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + definition + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "longName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + longName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "protoIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + protoIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "protocolLongName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + protocolLongName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "contextName " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + contextName + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "protoPreferredDefinition " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + protoPreferredDefinition + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "publicId " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + publicId + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "changeNote " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + changeNote + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "latestVersionInd " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + latestVersionInd + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "csIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + csIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "pCsCsiIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + pCsCsiIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "linkCsCsiIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + linkCsCsiIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "label " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + label + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "dispalyOrder " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + dispalyOrder + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "acCsiIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + acCsiIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "csCsiIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + csCsiIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        stringBuffer.append( "acIdseq " + CaDSRConstants.KEY_VALUE_DISPLAY_SEPARATOR + acIdseq + CaDSRConstants.KEY_VALUE_DISPLAY_EOL );
        super.toString();

        return stringBuffer.toString();
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof ProtocolFormModel ) ) return false;

        ProtocolFormModel that = ( ProtocolFormModel ) o;

        if( getPublicId() != that.getPublicId() ) return false;
        if( getQcIdseq() != null ? !getQcIdseq().equals( that.getQcIdseq() ) : that.getQcIdseq() != null ) return false;
        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getType() != null ? !getType().equals( that.getType() ) : that.getType() != null ) return false;
        if( getConteIdseq() != null ? !getConteIdseq().equals( that.getConteIdseq() ) : that.getConteIdseq() != null )
            return false;
        if( getCategoryName() != null ? !getCategoryName().equals( that.getCategoryName() ) : that.getCategoryName() != null )
            return false;
        if( getWorkflow() != null ? !getWorkflow().equals( that.getWorkflow() ) : that.getWorkflow() != null )
            return false;
        if( getPreferredName() != null ? !getPreferredName().equals( that.getPreferredName() ) : that.getPreferredName() != null )
            return false;
        if( getDefinition() != null ? !getDefinition().equals( that.getDefinition() ) : that.getDefinition() != null )
            return false;
        if( getLongName() != null ? !getLongName().equals( that.getLongName() ) : that.getLongName() != null )
            return false;
        if( getProtoIdseq() != null ? !getProtoIdseq().equals( that.getProtoIdseq() ) : that.getProtoIdseq() != null )
            return false;
        if( getProtocolLongName() != null ? !getProtocolLongName().equals( that.getProtocolLongName() ) : that.getProtocolLongName() != null )
            return false;
        if( getContextName() != null ? !getContextName().equals( that.getContextName() ) : that.getContextName() != null )
            return false;
        if( getProtoPreferredDefinition() != null ? !getProtoPreferredDefinition().equals( that.getProtoPreferredDefinition() ) : that.getProtoPreferredDefinition() != null )
            return false;
        if( getChangeNote() != null ? !getChangeNote().equals( that.getChangeNote() ) : that.getChangeNote() != null )
            return false;
        if( getLatestVersionInd() != null ? !getLatestVersionInd().equals( that.getLatestVersionInd() ) : that.getLatestVersionInd() != null )
            return false;
        if( getCsIdseq() != null ? !getCsIdseq().equals( that.getCsIdseq() ) : that.getCsIdseq() != null ) return false;
        if( getpCsCsiIdseq() != null ? !getpCsCsiIdseq().equals( that.getpCsCsiIdseq() ) : that.getpCsCsiIdseq() != null )
            return false;
        if( getLinkCsCsiIdseq() != null ? !getLinkCsCsiIdseq().equals( that.getLinkCsCsiIdseq() ) : that.getLinkCsCsiIdseq() != null )
            return false;
        if( getLabel() != null ? !getLabel().equals( that.getLabel() ) : that.getLabel() != null ) return false;
        if( getDispalyOrder() != null ? !getDispalyOrder().equals( that.getDispalyOrder() ) : that.getDispalyOrder() != null )
            return false;
        if( getAcCsiIdseq() != null ? !getAcCsiIdseq().equals( that.getAcCsiIdseq() ) : that.getAcCsiIdseq() != null )
            return false;
        if( getCsCsiIdseq() != null ? !getCsCsiIdseq().equals( that.getCsCsiIdseq() ) : that.getCsCsiIdseq() != null )
            return false;
        return !( getAcIdseq() != null ? !getAcIdseq().equals( that.getAcIdseq() ) : that.getAcIdseq() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getQcIdseq() != null ? getQcIdseq().hashCode() : 0;
        result = 31 * result + ( getVersion() != null ? getVersion().hashCode() : 0 );
        result = 31 * result + ( getType() != null ? getType().hashCode() : 0 );
        result = 31 * result + ( getConteIdseq() != null ? getConteIdseq().hashCode() : 0 );
        result = 31 * result + ( getCategoryName() != null ? getCategoryName().hashCode() : 0 );
        result = 31 * result + ( getWorkflow() != null ? getWorkflow().hashCode() : 0 );
        result = 31 * result + ( getPreferredName() != null ? getPreferredName().hashCode() : 0 );
        result = 31 * result + ( getDefinition() != null ? getDefinition().hashCode() : 0 );
        result = 31 * result + ( getLongName() != null ? getLongName().hashCode() : 0 );
        result = 31 * result + ( getProtoIdseq() != null ? getProtoIdseq().hashCode() : 0 );
        result = 31 * result + ( getProtocolLongName() != null ? getProtocolLongName().hashCode() : 0 );
        result = 31 * result + ( getContextName() != null ? getContextName().hashCode() : 0 );
        result = 31 * result + ( getProtoPreferredDefinition() != null ? getProtoPreferredDefinition().hashCode() : 0 );
        result = 31 * result + getPublicId();
        result = 31 * result + ( getChangeNote() != null ? getChangeNote().hashCode() : 0 );
        result = 31 * result + ( getLatestVersionInd() != null ? getLatestVersionInd().hashCode() : 0 );
        result = 31 * result + ( getCsIdseq() != null ? getCsIdseq().hashCode() : 0 );
        result = 31 * result + ( getpCsCsiIdseq() != null ? getpCsCsiIdseq().hashCode() : 0 );
        result = 31 * result + ( getLinkCsCsiIdseq() != null ? getLinkCsCsiIdseq().hashCode() : 0 );
        result = 31 * result + ( getLabel() != null ? getLabel().hashCode() : 0 );
        result = 31 * result + ( getDispalyOrder() != null ? getDispalyOrder().hashCode() : 0 );
        result = 31 * result + ( getAcCsiIdseq() != null ? getAcCsiIdseq().hashCode() : 0 );
        result = 31 * result + ( getCsCsiIdseq() != null ? getCsCsiIdseq().hashCode() : 0 );
        result = 31 * result + ( getAcIdseq() != null ? getAcIdseq().hashCode() : 0 );
        return result;
    }

	@Override
	public int compareTo(Object o) {
		if ((o == null) || (!(o instanceof ProtocolFormModel)) || (this.getLongName() == null)) {
			return -1;
		}
		else {
			return this.getLongName().compareToIgnoreCase(((ProtocolFormModel)o).getLongName());
		}
	}
}
