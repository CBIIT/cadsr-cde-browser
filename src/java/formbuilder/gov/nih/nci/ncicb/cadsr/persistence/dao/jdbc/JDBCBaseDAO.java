package gov.nih.nci.ncicb.cadsr.persistence.dao.jdbc;

import gov.nih.nci.ncicb.cadsr.persistence.PersistenceConstants;
import gov.nih.nci.ncicb.cadsr.persistence.dao.BaseDAO;
import gov.nih.nci.ncicb.cadsr.persistence.dao.ConnectionException;
import gov.nih.nci.ncicb.cadsr.persistence.dao.DAOCreateException;
import gov.nih.nci.ncicb.cadsr.security.oc4j.BaseUserManager;
import gov.nih.nci.ncicb.cadsr.servicelocator.ServiceLocator;
import gov.nih.nci.ncicb.cadsr.servicelocator.SimpleServiceLocator;
import gov.nih.nci.ncicb.cadsr.util.StringUtils;
import gov.nih.nci.ncicb.cadsr.exception.DMLException;

import org.apache.commons.logging.LogFactory;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.object.SqlUpdate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.sql.DataSource;


public class JDBCBaseDAO extends BaseDAO implements PersistenceConstants {
  public JDBCBaseDAO(ServiceLocator locator) {
    super(locator);
    log = LogFactory.getLog(JDBCBaseDAO.class.getName());
  }

  public DataSource getDataSource(String key) {
    if (getServiceLocator() != null) {
      return getServiceLocator().getDataSource(key);
    }

    return null;
  }

  public DataSource getDataSource() {
    DataSource ds = null;

    if (getServiceLocator() != null) {
      ds = getServiceLocator().getDataSource(getDataSourceKey());
    }

    if (log.isDebugEnabled()) {
      log.debug("Return DataSource =  " + ds);
    }
    return ds;
  }

  public String getDataSourceKey() {
    return DATASOURCE_LOCATION_KEY;
  }

  /**
   * Check if the user has a create privilege on the administered component
   * within the context
   *
   * @param username corresponds to the login user name.
   * @param acType corresponds to the administered component type
   * @param conteIdseq corresponds to the context id seq
   *
   * @return <b>boolean</b> true indicates that user has an create privilege
   *         and false when the user has not
   */
  public boolean hasCreate(
    String username,
    String acType,
    String conteIdseq) {
    HasCreateQuery hasCreate = new HasCreateQuery(this.getDataSource());
    String retValue = hasCreate.execute(username, acType, conteIdseq);

    return StringUtils.toBoolean(retValue);
  }

  /**
   * Check if the user has a delete privilege on the administered component
   *
   * @param username corresponds to the login user name.
   * @param acIdseq corresponds to the administered component id seq
   *
   * @return <b>boolean</b> true indicates that user has an delete privilege
   *         and false when the user has not
   */
  public boolean hasDelete(
    String username,
    String acIdseq) {
    HasDeleteQuery hasDelete = new HasDeleteQuery(this.getDataSource());
    String retValue = hasDelete.execute(username, acIdseq);

    return StringUtils.toBoolean(retValue);
  }

  /**
   * Check if the user has an update privilege on the administered component
   *
   * @param username corresponds to the login user name.
   * @param acIdseq corresponds to the administered component id seq
   *
   * @return <b>boolean</b> true indicates that user has an update privilege
   *         and false when the user has not
   */
  public boolean hasUpdate(
    String username,
    String acIdseq) {
    HasUpdateQuery hasUpdate = new HasUpdateQuery(this.getDataSource());
    String retValue = hasUpdate.execute(username, acIdseq);

    return StringUtils.toBoolean(retValue);
  }

  /**
   * Utility method to get global unique identifier used as a primary key in
   * all caDSR tables
   *
   * @return <b>String</b> global unique identifier (length 36)
   */
  public String generateGUID() {
    String guid = null;
    GUIDGenerator gen = new GUIDGenerator(this.getDataSource());
    guid = gen.getGUID();

    return guid;
  }

  /**
   * Utility method to derive preferred name using the long name.
   *
   * @param <b>longName</b> Long name of the admin component
   *
   * @return <b>String</b> Derived preferred name
   */
  public String generatePreferredName(String longName) {
    String prefName = null;
    PreferredNameGenerator gen =
      new PreferredNameGenerator(this.getDataSource());
    prefName = gen.getPreferredName(longName);

    return prefName;
  }

  /**
   * Utility method to update the display order of the target record 
   * (Module, Question, Valid Value, and Instructions) with the new display order.
   * The display order of the record which has the new display order will be 
   * updated to have the original display order of the target record. The display
   * orders are swapped.
   *
   * @param <b>targetRecordId<b> corresponds to the target record whose 
   *        display order is to be updated
   * @param <b>newDisplayOrder<b> corresponds to the new display order
   * @param <b>relationshipName<b> corresponds to the relationship of
   *        the parent and the child records (for example, "FORM_MODULE")
   *
   * @return <b>int</b> 1 if the display order swapping is successful
   *
   * @throws <b>DMLException</b>
   */
  public int swapDisplayOrder(
    String targetRecordId, String relationshipName,
    int newDisplayOrder) throws DMLException {

    // first, get the original display order of the target record which to be 
    // updated with the new display order
    GetParentIdseq query = new GetParentIdseq();
    query.setDataSource(getDataSource());
    query.setSql();
    //List result = (List)query.execute(targetRecordId, relationshipName);
    List result = 
      (List)query.execute(new Object[] {targetRecordId, relationshipName});
    if (result.size() <= 0){
      throw new DMLException("No matching target record found whose " +
        "display order is to be updated.");
    }
    Map rec = (Map)(result.get(0));
    String qrIdseq = (String) rec.get("QR_IDSEQ");
    String pQcIdseq = (String) rec.get("P_QC_IDSEQ");
    int originalDisplayOrder = 
      Integer.parseInt(rec.get("DISPLAY_ORDER").toString());

    // next, update the display order of the record which has the new
    // display order with the original display order of the target record
    UpdateSwappedRecDispOrder updateRec1 = 
      new UpdateSwappedRecDispOrder(getDataSource());
    int updateCount1 = 
      updateRec1.executeUpdate (originalDisplayOrder, pQcIdseq, newDisplayOrder); 

    // finally, update the display order of the target record with 
    // the new display order
    UpdateRecDispOrder updateRec2 = new UpdateRecDispOrder(getDataSource());
    int updateCount2 = 
      updateRec2.executeUpdate (newDisplayOrder, qrIdseq); 
     
    return 1;  // success
  }

  public static void main(String[] args) {
    ServiceLocator locator = new SimpleServiceLocator();

    //JDBCDAOFactory factory = (JDBCDAOFactory)new JDBCDAOFactory().getDAOFactory(locator);
    JDBCBaseDAO test = new JDBCBaseDAO(locator);

    /*boolean res;
       res = test.hasDelete("SBREX", "B046971C-6A89-5F47-E034-0003BA0B1A09");
       System.out.println("\n*****Delete Result 1: " + res);
       res = test.hasDelete("SBREXT", "99BA9DC8-2099-4E69-E034-080020C9C0E0");
       System.out.println("\n*****Delete Result 2: " + res);
       res =
         test.hasCreate(
           "SBREX", "CONCEPTUALDOM", "29A8FB18-0AB1-11D6-A42F-0010A4C1E842");
       System.out.println("\n*****Create Result 1: " + res);
       res =
         test.hasCreate(
           "SBREXT", "CONCEPTUALDOMAIN", "29A8FB18-0AB1-11D6-A42F-0010A4C1E842");
       System.out.println("\n*****Create Result 2: " + res);
       res = test.hasUpdate("SBREX", "29A8FB18-0AB1-11D6-A42F-0010A4C1E842");
       System.out.println("\n*****Update Result 1: " + res);
       res = test.hasUpdate("SBREXT", "29A8FB18-0AB1-11D6-A42F-0010A4C1E842");
       System.out.println("\n*****Update Result 2: " + res);
    System.out.println("Preferred Name: " + test.generatePreferredName("my long name test test"));
    */
    try {
      test.swapDisplayOrder("D458E178-32A5-7522-E034-0003BA0B1A09", "FORM_MODULE", 6);
    }
    catch (DMLException e) {
      System.out.println("Failed to find the target record to update its display order");
    }
  }

  /**
   * Inner class that checks if the user has a create privilege on the
   * administered component within the context
   */
  private class HasCreateQuery extends StoredProcedure {
    public HasCreateQuery(DataSource ds) {
      super(ds, "cadsr_security_util.has_create_privilege");
      setFunction(true);
      declareParameter(new SqlOutParameter("returnValue", Types.VARCHAR));
      declareParameter(new SqlParameter("p_ua_name", Types.VARCHAR));
      declareParameter(new SqlParameter("p_actl_name", Types.VARCHAR));
      declareParameter(new SqlParameter("p_conte_idseq", Types.VARCHAR));
      compile();
    }

    public String execute(
      String username,
      String acType,
      String conteIdseq) {
      Map in = new HashMap();
      in.put("p_ua_name", username);
      in.put("p_actl_name", acType);
      in.put("p_conte_idseq", conteIdseq);

      Map out = execute(in);
      String retValue = (String) out.get("returnValue");

      return retValue;
    }
  }

  /**
   * Inner class that checks if the user has a delete privilege on the
   * administered component
   */
  private class HasDeleteQuery extends StoredProcedure {
    public HasDeleteQuery(DataSource ds) {
      super(ds, "cadsr_security_util.has_delete_privilege");
      setFunction(true);
      declareParameter(new SqlOutParameter("returnValue", Types.VARCHAR));
      declareParameter(new SqlParameter("p_ua_name", Types.VARCHAR));
      declareParameter(new SqlParameter("p_ac_idseq", Types.VARCHAR));
      compile();
    }

    public String execute(
      String username,
      String acIdseq) {
      Map in = new HashMap();
      in.put("p_ua_name", username);
      in.put("p_ac_idseq", acIdseq);

      Map out = execute(in);
      String retValue = (String) out.get("returnValue");

      return retValue;
    }
  }

  /**
   * Inner class that checks if the user has an update privilege on the
   * administered component
   */
  private class HasUpdateQuery extends StoredProcedure {
    public HasUpdateQuery(DataSource ds) {
      super(ds, "cadsr_security_util.has_update_privilege");
      setFunction(true);
      declareParameter(new SqlOutParameter("returnValue", Types.VARCHAR));
      declareParameter(new SqlParameter("p_ua_name", Types.VARCHAR));
      declareParameter(new SqlParameter("p_ac_idseq", Types.VARCHAR));
      compile();
    }

    public String execute(
      String username,
      String acIdseq) {
      Map in = new HashMap();
      in.put("p_ua_name", username);
      in.put("p_ac_idseq", acIdseq);

      Map out = execute(in);
      String retValue = (String) out.get("returnValue");

      return retValue;
    }
  }

  /**
   * Inner class to get global unique identifier
   */
  private class GUIDGenerator extends StoredProcedure {
    public GUIDGenerator(DataSource ds) {
      super(ds, PersistenceConstants.IDSEQ_GENERATOR);
      setFunction(true);
      declareParameter(new SqlOutParameter("returnValue", Types.VARCHAR));
      compile();
    }

    public String getGUID() {
      Map in = new HashMap();
      Map out = execute(in);
      String retValue = (String) out.get("returnValue");

      return retValue;
    }
  }

  /**
   * Inner class to get preferred name
   */
  private class PreferredNameGenerator extends StoredProcedure {
    public PreferredNameGenerator(DataSource ds) {
      super(ds, "set_name.set_qc_name");
      setFunction(true);
      declareParameter(new SqlOutParameter("returnValue", Types.VARCHAR));
      declareParameter(new SqlParameter("name", Types.VARCHAR));
      compile();
    }

    public String getPreferredName(String longName) {
      Map in = new HashMap();
      in.put("name", longName);

      Map out = execute(in);
      String retValue = (String) out.get("returnValue");

      return retValue;
    }
  }

  /**
   * Inner class to get the primary key, parent idseq, and display order 
   * of the target record.
   */
  private class GetParentIdseq extends MappingSqlQuery {
    GetParentIdseq() {
      super();
    }

    public void setSql() {
      super.setSql("select QR_IDSEQ, P_QC_IDSEQ, DISPLAY_ORDER from QC_RECS_EXT " +
        " where C_QC_IDSEQ = ? and RL_NAME = ? ");
      declareParameter(new SqlParameter("C_QC_IDSEQ", Types.VARCHAR));
      declareParameter(new SqlParameter("RL_NAME", Types.VARCHAR));
    }

    protected Object mapRow(
      ResultSet rs,
      int rownum) throws SQLException {

      Map out = new HashMap();
      out.put("QR_IDSEQ", rs.getString(1));  
      out.put("P_QC_IDSEQ", rs.getString(2));  
      out.put("DISPLAY_ORDER", new Integer(rs.getString(3)));  
      return out;
    }
  }

  /**
   * Inner class to update the display order of the record which has the new
   * display order with the original display order of the target record
   */
  private class UpdateSwappedRecDispOrder extends SqlUpdate {
    public UpdateSwappedRecDispOrder(DataSource ds) {
      String updateSql = 
      " update qc_recs_ext set display_order = ? where p_qc_idseq = ? and " + 
      " display_order = ? ";
      this.setDataSource(ds);
      this.setSql(updateSql);
      declareParameter(new SqlParameter("original_display_order", Types.INTEGER));
      declareParameter(new SqlParameter("p_qc_idseq", Types.VARCHAR));
      declareParameter(new SqlParameter("new_display_order", Types.INTEGER));
      compile();
    }
    protected int executeUpdate (int originalDisplayOrder, String pQcIdseq, 
      int newDisplayOrder) 
    {
      Object [] obj = 
        new Object[]
          {new Integer(originalDisplayOrder), 
           pQcIdseq,
           new Integer(newDisplayOrder)
          };
      
	    int res = update(obj);
      return res;
    }
  }

  /**
   * Inner class to update the display order of the target record with the
   * new display order.
   */
  private class UpdateRecDispOrder extends SqlUpdate {
    public UpdateRecDispOrder(DataSource ds) {
      String updateSql = 
      " update qc_recs_ext set display_order = ? where qr_idseq = ? ";
      this.setDataSource(ds);
      this.setSql(updateSql);
      declareParameter(new SqlParameter("new_display_order", Types.INTEGER));
      declareParameter(new SqlParameter("qr_idseq", Types.VARCHAR));
      compile();
    }
    protected int executeUpdate (int displayOrder, String qrIdseq)
    {
      Object [] obj = 
        new Object[]
          {new Integer(displayOrder), 
           qrIdseq
          };
      
	    int res = update(obj);
      return res;
    }
  }
}
