package gov.nih.nci.cadsr.dao.operation;

import org.apache.commons.lang3.StringUtils;

public class LookupDataQueryBuilder 
{
	
	private static final String protocolLookupSql = "SELECT c.pal_name programAreaPalName, c.conte_idseq contextIdSeq, " + 
						 "c.name contextName, ffv.proto_idseq protocolIdSeq, ffv.protocol_long_name protocolLongName, " +
						 "ffv.qc_idseq formIdSeq, ffv.long_name formLongName " +
						 "FROM sbrext.fb_forms_view ffv, sbr.contexts c " +
						 "WHERE ffv.conte_idseq = c.conte_idseq AND latest_version_ind = 'Yes' AND ffv.proto_idseq IS NOT NULL ";
	
	private static final String csLookupSql = "SELECT DISTINCT c.pal_name programAreaPalName, c.conte_idseq contextIdSeq, c.name contextName, " +
						  "csv.cs_idseq csIdSeq, csv.cs_long_name csLongName, csv.cs_csi_idseq csCsiIdSeq, csv.csi_name csCsiName, " +
						  "csv.csi_level csiLevel, csv.parent_csi_idseq parentCsiIdSeq " +
						  "FROM sbrext.br_cs_csi_hier_view_ext csv, sbr.contexts c " +
						  "WHERE csv.cs_asl_name = 'RELEASED' AND csv.cstl_name != 'Publishing' AND csv.cs_conte_idseq = c.conte_idseq " +
						  "AND csv.csi_level <= 2 ";

	public String buildProtocolLookupQuery(String contexIdSeq, String protocolOrForm)
	{
		StringBuffer sql = new StringBuffer(protocolLookupSql);
		
		if (StringUtils.isNotBlank(contexIdSeq))
			sql.append(" AND c.conte_idseq = '" + contexIdSeq + "' ");
		else if (StringUtils.isNotBlank(protocolOrForm))
			sql.append(" AND (Upper(ffv.protocol_long_name) like '%" + StringUtils.upperCase(protocolOrForm) + "%' OR " +
							   "Upper(ffv.long_name) like '%" + StringUtils.upperCase(protocolOrForm) + "%') ");
		
		sql.append(" ORDER BY c.pal_name, c.conte_idseq, UPPER(ffv.protocol_long_name), UPPER(ffv.long_name)");
		
		return sql.toString();
	}
	
	public String buildCSLookupQuery(String contexIdSeq, String csOrCsCsi)
	{
    	//Limiting the data to 2 levels of CSIs to match up with the left context menu tree.
    	//Can be changed later based on client feedback.
		StringBuffer sql = new StringBuffer(csLookupSql);
		
		if (StringUtils.isNotBlank(contexIdSeq))
			sql.append(" AND c.conte_idseq = '" + contexIdSeq + "' ");
		else if (StringUtils.isNotBlank(csOrCsCsi))
			sql.append(" AND (Upper(csv.cs_long_name) like '%" + StringUtils.upperCase(csOrCsCsi) + "%' OR " +
						"Upper(csv.csi_name) like '%" + StringUtils.upperCase(csOrCsCsi) + "%') ");
		
		sql.append(" ORDER BY c.pal_name, c.conte_idseq, UPPER(csv.cs_long_name), UPPER(csv.csi_name)");
		
		return sql.toString();
	}
	
}
