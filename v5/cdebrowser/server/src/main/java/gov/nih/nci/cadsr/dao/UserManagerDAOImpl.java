package gov.nih.nci.cadsr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import oracle.jdbc.OracleTypes;

@Component("userManagerDAO")
public class UserManagerDAOImpl extends AbstractDAOOperations implements UserManagerDAO {
	private Logger logger = LogManager.getLogger(UserManagerDAOImpl.class.getName());

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall jdbcCall;

	public UserManagerDAOImpl() {
		super();
	}

	@Autowired
	public UserManagerDAOImpl(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = getJdbcTemplate();
		jdbcCall = new SimpleJdbcCall(dataSource);
	}

	@Override
	public boolean validUser(String userName, String password) {
		Boolean validUser = false;
		String sql = "SELECT ENABLED_IND from USER_ACCOUNTS_VIEW where UA_NAME like UPPER(?)";

		// Check if the user account is enabled
		validUser = query(sql, userName, Boolean.class);

		return validUser.booleanValue();
	}

	@Override
	public List<String> getContextsForAllRoles(String username, String acType) {
		jdbcCall.withCatalogName("cadsr_security_util").withProcedureName("get_contexts_list")
				.withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("p_ua_name", Types.VARCHAR),
						new SqlParameter("p_actl_name", Types.VARCHAR),
						new SqlOutParameter("p_contexts", OracleTypes.CURSOR, new UserContextMapper()));

		Map<String, String> params = new HashMap<String, String>();
		params.put("p_ua_name", username);
		params.put("p_actl_name", acType);

		Map<String, Object> out = jdbcCall.execute(params);
		List<UserConext> ucontextList = (List<UserConext>) out.get("p_contexts");

		List<String> roleList = new ArrayList<String>();

		for (UserConext uc : ucontextList) {
			String roleName = uc.getBusinessRole();
			if (StringUtils.isNotEmpty(roleName) && !roleList.contains(roleName))
				roleList.add(roleName);
		}
		return roleList;
	}

	@Override
	public int getCadsrLockoutProperties() {
		String sql = "select COUNT(*) from sbrext.tool_options_view_ext where tool_name = 'caDSR' and property in ('LOCKOUT.TIMER', 'LOCKOUT.THRESHOLD')";

		int cnt = getInt(sql);

		return cnt;
	}

	@Override
	public void getConnection(String username, String password) throws SQLException {
		Connection conn = this.getDataSource().getConnection(username, password);
		// The above will have issues with Tomcat DBCP, use the below statement instead.
		//Connection conn = this.getDataSource().getConnection();
		conn.close();
	}
	
	/*
	 * author santhanamv
	 * Method to authenticate login credentials, to overcome the issue with getConnection(username, password) method and Tomcat DBCP
	 * This method retrieves the jdbcUrl from context.xml via controller
	 */
	public void authenticateUser(String username, String password, String jdbcUrl) throws SQLException {
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", username);
	    connectionProps.put("password", password);		
	    Connection conn = DriverManager.getConnection(jdbcUrl, connectionProps);
		conn.close();
	}

	@Override
	public int incLock(String username) {
		String sql = "update sbrext.users_lockout_view set LOCKOUT_COUNT = LOCKOUT_COUNT + 1, VALIDATION_TIME = SYSDATE where ua_name = ?";

		int updCnt = getJdbcTemplate().update(sql, new Object[] { username });

		return updCnt;
	}

	@Override
	public int insertLock(String username) {
		String sql = "insert into sbrext.users_lockout_view (ua_name, LOCKOUT_COUNT, VALIDATION_TIME) values (?, 1, SYSDATE)";

		int insCnt = getJdbcTemplate().update(sql, new Object[] { username });

		return insCnt;
	}

	@Override
	public int resetLock(String username) {
		String sql = "update sbrext.users_lockout_view set LOCKOUT_COUNT = 0, VALIDATION_TIME = SYSDATE where ua_name = ?";

		int updCnt = getJdbcTemplate().update(sql, new Object[] { username });

		return updCnt;
	}

	private class UserContextMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserConext ucontext = new UserConext();
			ucontext.setBusinessRole(rs.getString(3));
			ucontext.setContextIdSeq(rs.getString(1));
			ucontext.setContextName(rs.getString(2));
			return ucontext;
		}
	}

	public String getOrganization(String username) {
		try {
			String organization = jdbcTemplate.queryForObject(
					"select name from organizations where org_idseq = (select org_idseq from USER_ACCOUNTS where ua_name = ?)",
					new Object[] { username }, String.class);
			return organization;
		}
		catch (Exception e) {
			logger.debug("getOrganization exception for a user:" + username +  ' ' + e);
			return null;
		}
	}

	private class UserConext {
		private String contextIdSeq;
		private String contextName;
		private String businessRole;

		public String getContextIdSeq() {
			return contextIdSeq;
		}

		public void setContextIdSeq(String contextIdSeq) {
			this.contextIdSeq = contextIdSeq;
		}

		public String getContextName() {
			return contextName;
		}

		public void setContextName(String contextName) {
			this.contextName = contextName;
		}

		public String getBusinessRole() {
			return businessRole;
		}

		public void setBusinessRole(String businessRole) {
			this.businessRole = businessRole;
		}

	}

}
