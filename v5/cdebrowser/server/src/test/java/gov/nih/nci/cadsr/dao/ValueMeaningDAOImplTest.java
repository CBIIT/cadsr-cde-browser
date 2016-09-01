package gov.nih.nci.cadsr.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.common.util.UnitTestCommon;
import gov.nih.nci.cadsr.dao.model.AlternateDefinitionUiModel;
import gov.nih.nci.cadsr.dao.model.AlternateNameUiModel;
import gov.nih.nci.cadsr.dao.model.ValueMeaningUiModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class ValueMeaningDAOImplTest
{
    private DataSource mockDataSource = mock( DataSource.class );
    private ValueMeaningDAOImpl valueMeaningDAO;
    private int cdeId = 2179601;
    private float version = 1.0F;
    private String expectedSql0 = "SELECT DISTINCT  value_meanings.* FROM sbr.permissible_values,sbr.vd_pvs,sbr.value_meanings  WHERE sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq AND sbr.vd_pvs.vd_idseq = pvIdseq AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq";
    private String expectedSql1 = "SELECT sbr.vd_pvs.pv_idseq AS pvIdseq, sbr.value_meanings.long_name AS pvMeaning, sbr.value_meanings.vm_id AS vmPublicId, sbr.value_meanings.version AS vmVersion, sbr.value_meanings.vm_idseq AS vmIdseq FROM sbr.permissible_values, sbr.vd_pvs, sbr.value_meanings, sbr.data_elements WHERE sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq AND sbr.data_elements.cde_id = CDE_ID AND sbr.data_elements.version = VERSION_NUMBER AND sbr.vd_pvs.vd_idseq = sbr.data_elements.vd_idseq AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq";
    private UnitTestCommon unitTestCommon;

    @Mock
    private AlternateDefinitionDAOImpl alternateDefinitionDAO;

    @Mock
    private AlternateNameDAOImpl alternateNameDAO;

/*
    @Mock
    private ValueMeaningDAOImpl valueMeaningDAO;
*/

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Before
    public void setUp() throws Exception
    {
        unitTestCommon = new UnitTestCommon();
        valueMeaningDAO = new ValueMeaningDAOImpl( mockDataSource );
        expectedSql1 = expectedSql1.replace( "CDE_ID", Integer.toString( cdeId ) );
        expectedSql1 = expectedSql1.replace( "VERSION_NUMBER", Float.toString( version ) );

    }

    @Test
    public void test0() throws Exception
    {
        String sql = valueMeaningDAO.pvIdseqQueryBuilder( "pvIdseq" );
        assertEquals( cleanup( expectedSql0 ), cleanup( sql ) );
    }

    @Test
    public void test1() throws Exception
    {
        String sql = valueMeaningDAO.uiCdeIdAndVersionQueryBuilder( cdeId, version );
        assertEquals( cleanup( expectedSql1 ), cleanup( sql ) );
    }

/*
    TODO this test is incomplete/broken
    @Test
    public void test2() throws Exception
    {
        when( alternateNameDAO.getUiAlternateNamesByAcIdseq( anyString() ) ).thenReturn( initAlternateNames() );
        when( alternateDefinitionDAO.getUiAlternateDefinitionsByAcIdseq( anyString() ) ).thenReturn( initalternateDefinitions() );
        when( valueMeaningDAO.getUiValueMeaningsByCdeIdAndVersion( "123", "456" ) ).thenReturn( initValueMeanings() );

        ValueMeaningDAOImpl valueMeaningDAO = new ValueMeaningDAOImpl( mockDataSource );
        valueMeaningDAO.setAlternateDefinitionDAO( alternateDefinitionDAO );
        valueMeaningDAO.setAlternateNameDAO( alternateNameDAO );

        List<ValueMeaningUiModel> valueMeaningUiModelList = valueMeaningDAO.getUiValueMeaningsByCdeIdAndVersion( cdeId, version );

        // Add the AlternateNames and AlternateDefinitions
        valueMeaningUiModelList = valueMeaningDAO.addAltNamesAndDefinitions( valueMeaningUiModelList );
        System.out.println( "valueMeaningUiModelList: " + valueMeaningUiModelList);

        //assertEquals(  );

    }
*/

    private ArrayList<AlternateNameUiModel> initAlternateNames()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( unitTestCommon.getTestDataDir() + "/src/test/java/gov/nih/nci/cadsr/dao/ValueMeaningTest1.data" );
        } catch( IOException e )
        {
            System.err.println( "Error: " + e.getMessage() );
        }
        return gson.fromJson( json, new TypeToken<ArrayList<AlternateNameUiModel>>()
        {
        }.getType() );
    }

    private ArrayList<AlternateDefinitionUiModel> initalternateDefinitions()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( unitTestCommon.getTestDataDir() + "/src/test/java/gov/nih/nci/cadsr/dao/ValueMeaningTest2.data" );
        } catch( IOException e )
        {
            System.err.println( "Error: " + e.getMessage() );
        }
        return gson.fromJson( json, new TypeToken<ArrayList<AlternateDefinitionUiModel>>()
        {
        }.getType() );
    }


    private ArrayList<ValueMeaningUiModel> initValueMeanings()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( unitTestCommon.getTestDataDir() + "/src/test/java/gov/nih/nci/cadsr/dao/ValueMeaningTest3.data" );
        } catch( IOException e )
        {
            System.err.println( "Error: " + e.getMessage() );
        }
        return gson.fromJson( json, new TypeToken<ArrayList<ValueMeaningUiModel>>()
        {
        }.getType() );
    }


    private String cleanup( String s )
    {
        return s.replaceAll( "\\s\\s*", " " ).replaceAll( "\\s*,\\s*", ", " ).replaceAll( "\\s*\\)\\s*", ")" ).replaceAll( "\\s*\\(\\s*", "(" ).toUpperCase().trim();
    }

}
