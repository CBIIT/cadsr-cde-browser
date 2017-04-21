/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
/**
 * @author asafievan
 *
 */
public class ProgramAreaDAOImplTest {
	@Mock
	JdbcTemplate jdbcTemplateMock = mock(JdbcTemplate.class);
	ProgramAreaDAOImpl programAreaDAOImpl;
	public static String PAL_SQL = " SELECT * FROM program_areas_lov_view";
	@Before
	public void setUp() throws Exception {
		programAreaDAOImpl = new ProgramAreaDAOImpl(jdbcTemplateMock);
	}

	@After
	public void tearDown() throws Exception {
		Mockito.reset(jdbcTemplateMock);
	}

	@Test
	public void testGetAllProgramAreasEmptyTwice() {
		List<ProgramAreaModel> expected = new ArrayList<>();
		Mockito.when(jdbcTemplateMock.query(Mockito.eq(PAL_SQL) ,any(RowMapper.class))).thenReturn(expected);
		List<ProgramAreaModel> received = programAreaDAOImpl.getAllProgramAreas();
		assertEquals(received, expected);
		Mockito.verify(jdbcTemplateMock, times(2)).query(Mockito.eq(PAL_SQL), any(RowMapper.class));
	}
	
	@Test
	public void testGetAllProgramAreasOneException() {
		List<ProgramAreaModel> expected = new ArrayList<>();
		Mockito.when(jdbcTemplateMock.query(Mockito.eq(PAL_SQL) ,any(RowMapper.class))).thenThrow(new RuntimeException("Test Exception")).thenReturn(expected);
		List<ProgramAreaModel> received = programAreaDAOImpl.getAllProgramAreas();
		assertEquals(received, expected);
		Mockito.verify(jdbcTemplateMock, times(3)).query(Mockito.eq(PAL_SQL), any(RowMapper.class));
	}
	@Test(expected=RuntimeException.class)
	public void testGetAllProgramAreasTwoExceptions() {
		List<ProgramAreaModel> expected = new ArrayList<>();
		Mockito.when(jdbcTemplateMock.query(Mockito.eq(PAL_SQL) ,any(RowMapper.class))).thenThrow(new RuntimeException("Test Exception"))
			.thenThrow(new RuntimeException("Test Exception2"));
		programAreaDAOImpl.getAllProgramAreas();
		Mockito.verify(jdbcTemplateMock).query(Mockito.eq(PAL_SQL), any(RowMapper.class));
	}
	@Test
	public void testGetAllProgramAreasSuccess() {
		List<ProgramAreaModel> expected = new ArrayList<>();
		ProgramAreaModel palModel = new ProgramAreaModel();
		palModel.setPalName("testpalName");
		expected.add(palModel);
		Mockito.when(jdbcTemplateMock.query(Mockito.eq(PAL_SQL) ,any(RowMapper.class))).thenReturn(expected);
		List<ProgramAreaModel> received = programAreaDAOImpl.getAllProgramAreas();
		assertEquals(received, expected);
		Mockito.verify(jdbcTemplateMock).query(Mockito.eq(PAL_SQL), any(RowMapper.class));
	}
	@Test
	public void testGetAllProgramAreasOneException2() {
		List<ProgramAreaModel> expected = new ArrayList<>();
		Mockito.when(jdbcTemplateMock.query(Mockito.eq(PAL_SQL) ,any(RowMapper.class))).thenReturn(expected).thenThrow(new RuntimeException("Test Exception")).thenReturn(expected);
		List<ProgramAreaModel> received = programAreaDAOImpl.getAllProgramAreas();
		assertEquals(received, expected);
		Mockito.verify(jdbcTemplateMock, times(3)).query(Mockito.eq(PAL_SQL), any(RowMapper.class));
	}
	@Test(expected=RuntimeException.class)
	public void testGetAllProgramAreasTwoExceptions3() {
		List<ProgramAreaModel> expected = new ArrayList<>();
		Mockito.when(jdbcTemplateMock.query(Mockito.eq(PAL_SQL) ,any(RowMapper.class))).thenReturn(expected).thenThrow(new RuntimeException("Test Exception"))
			.thenThrow(new RuntimeException("Test Exception3"));
		programAreaDAOImpl.getAllProgramAreas();
		Mockito.verify(jdbcTemplateMock, times(3)).query(Mockito.eq(PAL_SQL), any(RowMapper.class));
	}
}
