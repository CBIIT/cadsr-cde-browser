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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
	
	ValueDomainDAOImpl valueDomainDAO;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		valueDomainDAO = new ValueDomainDAOImpl(jdbcTemplateMock);
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
	public void testGetValueDomainByIdseq() {
		ValueDomainModel valueDomainModelExpected = new ValueDomainModel();
		valueDomainModelExpected.setVdContextName("vdContextNameTest");
		
		ValueDomainModel valueDomainModelToReturn = new ValueDomainModel();
		valueDomainModelToReturn.setVdContextName("vdContextNameTest");
		
		Mockito.when(jdbcTemplateMock.queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"vdIdseqTest"}),any(RowMapper.class))).thenReturn(valueDomainModelToReturn);
		//MUT
		ValueDomainModel valueDomainModelReceived = valueDomainDAO.getValueDomainByIdseq("vdIdseqTest");
		Mockito.verify(jdbcTemplateMock).queryForObject(Mockito.anyString(), Mockito.eq(new Object[]{"vdIdseqTest"}),any(RowMapper.class));
		assertEquals(valueDomainModelExpected, valueDomainModelReceived);
		
	}

}
