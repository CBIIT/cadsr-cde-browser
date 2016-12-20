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
