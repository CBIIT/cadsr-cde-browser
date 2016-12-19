package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.VdPvsModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.ncicb.cadsr.common.dto.ConceptDerivationRuleTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class VdPvsDAOImpl extends AbstractDAOOperations implements VdPvsDAO
{
    private Logger logger = LogManager.getLogger( VdPvsDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;
    private ConceptDerivationRuleDAO conceptDerivationRuleDAO;


    @Autowired
    VdPvsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<VdPvsModel> getVdPvs( String vdIdseq )
    {
        String sql = " SELECT DISTINCT * FROM sbr.vd_pvs WHERE vd_idseq = ?";
        return getAll( sql, vdIdseq, VdPvsModel.class );
    }

    /**
     * Each ValidValueTransferObject in the list returned, may contain none or more nested ConceptDerivationRule objects
     * This list of ConceptDerivationRule objects is retrieved by a call to conceptDerivationRuleDAO for each ValidValue
     * @param vdIdseq matches the VD_IDSEQ column in SBR.VD_PVS
     * @return List of ValidValueTransferObject
     */
    @Override
    public List<ValidValueTransferObject> getVdVvs( String vdIdseq )
    {
        String sql = "select * from CABIO31_VM_VIEW where VM_IDSEQ in ( select VM_IDSEQ from CABIO31_PV_VIEW where PV_IDSEQ in (select PV_IDSEQ from SBR.VD_PVS where VD_IDSEQ like '" + vdIdseq + "') )";
        List<ValidValueTransferObject> vvtObjList = getAll( sql, ValidValueTransferObject.class );

        for( ValidValueTransferObject transferObject : vvtObjList )
        {
            // Add the concept derivation rules for this
            String condrIdseq = transferObject.getCondrIdseq();
            if( condrIdseq != null )
            {
                // conceptDerivationRuleDAO.getCDRByIdseq returns a ConceptDerivationRuleModel,
                // the ConceptDerivationRuleTransferObject constructor converts/copies it to a ConceptDerivationRuleTransferObject, which is what we need.
                ConceptDerivationRule conceptDerivationRule  =  new ConceptDerivationRuleTransferObject(conceptDerivationRuleDAO.getCDRByIdseq( condrIdseq ));
                transferObject.setConceptDerivationRule( conceptDerivationRule );
            }

            String contextIdseq = transferObject.getConteIdseq();
            if( contextIdseq != null )
            {
                // Get it's Context's name
                ContextModel contextModel = contextDAO.getContextByIdseq( contextIdseq );
                transferObject.setContext( contextModel.getName() );
            }
        }
        return vvtObjList;
    }

    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public ConceptDerivationRuleDAO getConceptDerivationRuleDAO()
    {
        return conceptDerivationRuleDAO;
    }

    public void setConceptDerivationRuleDAO( ConceptDerivationRuleDAO conceptDerivationRuleDAO )
    {
        this.conceptDerivationRuleDAO = conceptDerivationRuleDAO;
    }
}
