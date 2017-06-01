/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
/**
 * 
 */
package gov.nih.nci.cadsr.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cadsr.dao.model.AcRegistrationsModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;

/**
 * @author asafievan
 *
 */
public class ValueDomainDAOImplTest {

	//JdbcTemplate jdbcTemplateMock = mock(JdbcTemplate.class);
	@Mock
	JdbcTemplate jdbcTemplateMock = mock(JdbcTemplate.class);
	@Mock
	DataSource dataSourceMock = mock(DataSource.class);
	@Mock
	AcRegistrationsDAO acRegistrationsDAO = mock(AcRegistrationsDAO.class);
	
	ValueDomainDAOImpl valueDomainDAO;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		valueDomainDAO = new ValueDomainDAOImpl(jdbcTemplateMock);
		valueDomainDAO.setAcRegistrationsDAO(acRegistrationsDAO);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link gov.nih.nci.cadsr.dao.ValueDomainDAOImpl#getValueDomainByIdseq(java.lang.String)}.
	 */
	@Test
	public void testGetValueDomainByIdseqNullVdRegStatus() {
		ValueDomainModel valueDomainModelExpected = new ValueDomainModel();
		valueDomainModelExpected.setVdContextName("vdContextNameTest");
		valueDomainModelExpected.setVdIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		
		ValueDomainModel valueDomainModelToReturn = new ValueDomainModel();
		valueDomainModelToReturn.setVdContextName("vdContextNameTest");
		valueDomainModelToReturn.setVdIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		
		Mockito.when(acRegistrationsDAO.getAcRegistrationByAcIdseq(Mockito.eq("AA557316-F69A-05DB-E040-BB89AD436C28"))).thenReturn(null);
		Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"AA557316-F69A-05DB-E040-BB89AD436C28"}),any(RowMapper.class))).thenReturn(valueDomainModelToReturn);
		//MUT
		ValueDomainModel valueDomainModelReceived = valueDomainDAO.getValueDomainByIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		Mockito.verify(jdbcTemplateMock).queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"AA557316-F69A-05DB-E040-BB89AD436C28"}),any(RowMapper.class));
		assertEquals(valueDomainModelExpected, valueDomainModelReceived);
		
	}
	@Test
	public void testGetValueDomainByIdseqWithVdRegStatus() {
		ValueDomainModel valueDomainModelExpected = new ValueDomainModel();
		valueDomainModelExpected.setVdContextName("vdContextNameTest");
		valueDomainModelExpected.setVdIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		AcRegistrationsModel vdRegistrationsModel = new AcRegistrationsModel();
		vdRegistrationsModel.setRegistrationStatus("Candidate");
		valueDomainModelExpected.setVdRegistrationsModel(vdRegistrationsModel);
		
		ValueDomainModel valueDomainModelToReturn = new ValueDomainModel();
		valueDomainModelToReturn.setVdContextName("vdContextNameTest");
		valueDomainModelToReturn.setVdIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		
		Mockito.when(acRegistrationsDAO.getAcRegistrationByAcIdseq(Mockito.eq("AA557316-F69A-05DB-E040-BB89AD436C28"))).thenReturn(vdRegistrationsModel);
		Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"AA557316-F69A-05DB-E040-BB89AD436C28"}),any(RowMapper.class))).thenReturn(valueDomainModelToReturn);
		//MUT
		ValueDomainModel valueDomainModelReceived = valueDomainDAO.getValueDomainByIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		Mockito.verify(jdbcTemplateMock).queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"AA557316-F69A-05DB-E040-BB89AD436C28"}),any(RowMapper.class));
		assertEquals(valueDomainModelExpected, valueDomainModelReceived);
		
	}
	@Test
	public void testGetValueDomainByIdseqErrorVdRegStatus() {
		ValueDomainModel valueDomainModelExpected = new ValueDomainModel();
		valueDomainModelExpected.setVdContextName("vdContextNameTest");
		valueDomainModelExpected.setVdIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		
		ValueDomainModel valueDomainModelToReturn = new ValueDomainModel();
		valueDomainModelToReturn.setVdContextName("vdContextNameTest");
		valueDomainModelToReturn.setVdIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		
		Mockito.when(acRegistrationsDAO.getAcRegistrationByAcIdseq(Mockito.eq("AA557316-F69A-05DB-E040-BB89AD436C28"))).thenThrow(new EmptyResultDataAccessException("test exception", 1));
		Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"AA557316-F69A-05DB-E040-BB89AD436C28"}),any(RowMapper.class))).thenReturn(valueDomainModelToReturn);
		//MUT
		ValueDomainModel valueDomainModelReceived = valueDomainDAO.getValueDomainByIdseq("AA557316-F69A-05DB-E040-BB89AD436C28");
		Mockito.verify(jdbcTemplateMock).queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"AA557316-F69A-05DB-E040-BB89AD436C28"}),any(RowMapper.class));
		assertEquals(valueDomainModelExpected, valueDomainModelReceived);
		
	}
}
