package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProtocolFormModel;

import java.util.List;

public interface ProtocolFormDAO
{
    public List<ProtocolFormModel> getProtocolForms( String csidSeq );
    public List<ProtocolFormModel> getProtocolFormByContextId( String ContextId );
    public List<ProtocolFormModel> getAllProtocolForm();
    public List<ProtocolFormModel> getProtocolFormByProtoIdseq( String protoIdseq );
    //CDEBROWSER-440
    /**
     * This method returns a list of Protocols based on Protocol IDSEQ List.
     * 
     * @param protoIdseqList 
     * @return sorted List<ProtocolFormModel>
     */
    public List<ProtocolFormModel> getProtocolFormListByProtoIdseqList( List<String> protoIdseqList );
}
