package gov.nih.nci.cadsr.dao.operation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class LookupDataQueryBuilderTest
{
	private static final String contextIdSeq = "F6117C06-C689-F9FD-E040-BB89AD432E40";
	private static final String csOrCsCsi = "LABS";
	private static final String protocolForm = "FORM";
	
	private static final String csContextQuery = "AND c.conte_idseq = ?";
	private static final String csCsiQuery = "AND (UPPER(csv.cs_long_name) like UPPER(?) OR UPPER(csv.csi_name) like UPPER(?))";
	
	private static final String protocolContextQuery = "AND c.conte_idseq = ?";
	private static final String formQuery = " AND (UPPER(ffv.protocol_long_name) like UPPER(?) OR UPPER(ffv.long_name) like UPPER(?))";
	
	@Configuration
	static class LookupDataServiceTestContextConfiguration
	{
		@Bean
		public LookupDataQueryBuilder lookupDataQueryBuilder() {
			return new LookupDataQueryBuilder();
		}
	}
	
	@Autowired
	private LookupDataQueryBuilder lookupDataQueryBuilder;
	
	@Test
	public void testBuildCSContextLookupQuery()
	{
		String sql = lookupDataQueryBuilder.buildCSLookupQuery(contextIdSeq, null);
		
		Assert.isTrue(StringUtils.contains(sql, csContextQuery));
	}

	@Test
	public void testBuildCSCsiLookupQuery()
	{
		String sql = lookupDataQueryBuilder.buildCSLookupQuery(null, csOrCsCsi);
		
		Assert.isTrue(StringUtils.contains(sql, csCsiQuery));
	}
	
	@Test
	public void testBuildProtocolContextLookupQuery()
	{
		String sql = lookupDataQueryBuilder.buildProtocolLookupQuery(contextIdSeq, null);
		
		Assert.isTrue(StringUtils.contains(sql, protocolContextQuery));
		
	}
	
	@Test
	public void testBuildProtocolLookupQuery()
	{
		String sql = lookupDataQueryBuilder.buildProtocolLookupQuery(null, protocolForm );
		System.out.println (sql);
		Assert.isTrue(StringUtils.contains(sql, formQuery));
		
	}
	
	

}
