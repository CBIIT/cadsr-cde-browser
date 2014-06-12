/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.common.persistence.dao.jdbc;

import gov.nih.nci.ncicb.cadsr.common.dto.CSITransferObject;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.ValueDomainDAO;
import gov.nih.nci.ncicb.cadsr.common.dto.ValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValueMeaningTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ValueDomainTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ReferenceDocumentTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.dto.ConceptDerivationRuleTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ContextTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DefinitionTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.DesignationTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.ClassSchemeItem;
import gov.nih.nci.ncicb.cadsr.common.resource.Classification;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.Definition;
import gov.nih.nci.ncicb.cadsr.common.resource.ValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueMeaning;
import gov.nih.nci.ncicb.cadsr.common.resource.ReferenceDocument;
import gov.nih.nci.ncicb.cadsr.common.util.StringUtils;
import org.springframework.jdbc.object.MappingSqlQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.sql.DataSource;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocator;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.SimpleServiceLocator;

import java.sql.Types;

import java.util.Iterator;

import org.springframework.jdbc.core.SqlParameter;


public class JDBCValueDomainDAO extends JDBCAdminComponentDAO implements ValueDomainDAO {
  public JDBCValueDomainDAO(ServiceLocator locator) {
    super(locator);
  }

  /**
   * Gets all Value Domains and their Valid Values
   *
   * @param <b>vdIdseqs</b> list of Value Domain Idseq.
   *
   * @return <b>Map</b> Map of Value Domain objects each having
   *   list of Valid Value objects (key: vd idseq, value: vv list).
   *   For non-enummerated VD, vd_idseq with empty list of VV should be returned.
   *   
   */
  public Map getPermissibleValues(Collection vdIdseqs){

    if (vdIdseqs == null) {
      return new HashMap();
    }
    
    if (vdIdseqs.size() == 0) {
      return new HashMap();
    }

		Iterator vdIdseqIterator = vdIdseqs.iterator();
    String whereString = "";
    Map vvMap = new HashMap();

    while(vdIdseqIterator.hasNext()) {

      // put every vd_idseq with empty list into the map
      List vvList = new ArrayList();
      String vdomainIdSeq = (String) vdIdseqIterator.next();
      // vd_idseq is the key and vvList is the value (list of valid values)
      vvMap.put(vdomainIdSeq, vvList);

      if (StringUtils.doesValueExist(whereString)){
        whereString = whereString + " or VD_IDSEQ = '" + vdomainIdSeq + "'";
      }
      else {
        whereString = whereString + " where VD_IDSEQ = '" + vdomainIdSeq + "'";
      }
    }
    
    PermissibleValueQuery query = new PermissibleValueQuery(vvMap);
    query.setDataSource(getDataSource());
    query.setSql(whereString, "");

    // query.execute returns map of n number of records retrieved from db
    query.execute();  
    Map records = query.getValidValueMap();
    return records;
  }

    public ValueDomain getValueDomainById(String vdId){
        ValueDomainQuery query = new ValueDomainQuery(getDataSource());
        query.setSql();

        //value domain fully populated but concept is not populated        
        List ret = query.getValueDomainById(vdId); 
        if (ret!=null && !ret.isEmpty()){
            return (ValueDomain)ret.get(0);
        }
        return null;
    }
    
    public ValueMeaning getValueMeaning(String shortMeaning){
        ValueMeaningQuery vmQuery = new ValueMeaningQuery(getDataSource());
        vmQuery.setSql();
        ValueMeaning vm = vmQuery.getValueMeaning(shortMeaning);        
        vm.setDefinitions(getDefinitions(vm.getIdseq()));
        vm.setDesignations(getDesignations(vm.getIdseq(), null));  
        //Added for 4.0 release
        vm.setContext(getContext(vm.getIdseq()));
        return vm;
    }
    
    /**
     * Inner class to get value meanign
     * 
     */
    class ValueMeaningQuery extends MappingSqlQuery {
    
    	ValueMeaningQuery(DataSource ds) {
        super();
        this.setDataSource(ds);
      }

      public void setSql() {
       // String sql = " SELECT * from sbr.value_meanings where short_meaning = ?" ;
        String sql = " SELECT * from sbr.value_meanings_view where long_name = ?" ;
        setSql(sql);
        declareParameter(new SqlParameter("short_meaning", Types.VARCHAR));
        compile();
      }
      
      protected Object mapRow(
        ResultSet rs,
        int rownum) throws SQLException {

        // reaches to this point for each record retrieved.
        ValueMeaning vm = new ValueMeaningTransferObject();
        vm.setLongName(rs.getString("long_name"));
        vm.setIdseq(rs.getString("vm_idseq"));
        vm.setPreferredDefinition(rs.getString("preferred_definition"));
        //Added for 4.0 release
        vm.setPublicId(new Integer(rs.getString("vm_id")).intValue());
        vm.setVersion(new Float(rs.getString("version")).floatValue());             
        vm.setAslName(rs.getString("asl_name"));
        //
        return vm;
      }
      
      public ValueMeaning getValueMeaning(String shortMeaning){
          Object[] obj =
            new Object[] { shortMeaning };
          List ret =  execute(obj);
          if (ret!=null){
              return (ValueMeaning)(ret.get(0));
          }else{
              return null;
          }
      }
    }
    
    public ValueMeaning getValueMeaningById(Integer vmId){
        ValueMeaningByIdQuery vmQuery = new ValueMeaningByIdQuery(getDataSource());
        vmQuery.setSql();
        ValueMeaning vm = vmQuery.getValueMeaning(vmId);        
        vm.setDefinitions(getDefinitions(vm.getIdseq()));
        vm.setDesignations(getDesignations(vm.getIdseq(), null));  
        //Added for 4.0 release
        vm.setContext(getContext(vm.getIdseq()));
        return vm;
    }    
    
    /**
     * Inner class to get value meanign
     * 
     */
    class ValueMeaningByIdQuery extends ValueMeaningQuery {
    
    	ValueMeaningByIdQuery(DataSource ds) {
        super(ds);
      }

      public void setSql() {
       // String sql = " SELECT * from sbr.value_meanings where short_meaning = ?" ;
        String sql = " SELECT * from sbr.value_meanings_view where vm_id = ?" ;
        setSql(sql);
        declareParameter(new SqlParameter("vm_id", Types.INTEGER));
        compile();
      }
      
      public ValueMeaning getValueMeaning(Integer vmId){
          Object[] obj =
            new Object[] { vmId };
          List ret =  execute(obj);
          if (ret!=null){
              return (ValueMeaning)(ret.get(0));
          }else{
              return null;
          }
      }
      
    }
    
  /**
   * Inner class to get all valid values for each value domain
   * 
   */
  class ValueDomainQuery extends MappingSqlQuery {
  
    // vd_idseq is the key and vvList is the value (list of valid values)
    ValueDomainQuery(DataSource ds) {
      super();
      this.setDataSource(ds);
    }

    public void setSql() {
      String sql = " SELECT VD_IDSEQ, LONG_NAME, VERSION, VD_ID, ASL_NAME, UOML_NAME, MAX_LENGTH_NUM, MIN_LENGTH_NUM, DECIMAL_PLACE, HIGH_VALUE_NUM, LOW_VALUE_NUM, CONDR_IDSEQ, FORML_NAME, DTL_NAME from SBR.VALUE_DOMAINS_VIEW where VD_IDSEQ=?" ;
      setSql(sql);
      declareParameter(new SqlParameter("VD_IDSEQ", Types.VARCHAR));
      compile();
    }  

    
    protected Object mapRow(
      ResultSet rs,
      int rownum) throws SQLException {

      // reaches to this point for each record retrieved.
      String vdomainIdSeq = rs.getString(1);  // VD_IDSEQ
      
      ValueDomain vd = new ValueDomainTransferObject();
      vd.setVdIdseq(vdomainIdSeq);
      vd.setLongName(rs.getString(2)); 
      vd.setVersion(rs.getFloat(3));
      vd.setPublicId(rs.getInt(4));
      vd.setAslName(rs.getString(5));
      vd.setUnitOfMeasure(rs.getString(6));
      vd.setMaxLength(rs.getString(7));
      vd.setMinLength(rs.getString(8));
      vd.setDecimalPlace(rs.getString(9));
      vd.setHighValue(rs.getString(10));
      vd.setLowValue(rs.getString(11));
      vd.setDisplayFormat(rs.getString(13));
      vd.setDatatype(rs.getString(14));
      ConceptDerivationRule cdr = 
        new ConceptDerivationRuleTransferObject();
      cdr.setIdseq(rs.getString(12));
        
      vd.setConceptDerivationRule(cdr);
      return vd;
    }
    
    
    public List getValueDomainById(String vdId){
        Object[] obj =
          new Object[] {
        vdId
          };

       return execute(obj);

        
    }
  }


    /**
     * Inner class to get all valid values for each value domain
     * 
     */
    class PermissibleValueQuery extends MappingSqlQuery {
    
      // vd_idseq is the key and vvList is the value (list of valid values)
      private Map vvMap = null;
      
      PermissibleValueQuery(Map vvvMap) {
        super();
        this.vvMap = vvvMap;
      }

      public void setSql(String whereString, String dummy) {
        super.setSql("SELECT * FROM SBREXT.FB_VD_VV_VIEW " + whereString +"order by upper( PV_VALUE ) ");
      }

      public Map getValidValueMap(){
        return vvMap;
      }
      
      protected Object mapRow(
        ResultSet rs,
        int rownum) throws SQLException {

        // reaches to this point for each record retrieved.
        String vdomainIdSeq = rs.getString(1);  // VD_IDSEQ
        
        ValidValueTransferObject vvto = new ValidValueTransferObject();
        vvto.setShortMeaningValue(rs.getString(3)); // PV_VALUE
        vvto.setShortMeaning(rs.getString(4));  // PV_SHORT_MEANING
        vvto.setShortMeaningDescription(rs.getString(5)); // PV_MEANING_DESCRIPTION
        vvto.setVpIdseq(rs.getString(6)); //VP_IDSEQ
        
        ValueMeaning vm = new ValueMeaningTransferObject();
        vm.setLongName(vvto.getShortMeaning());
        vm.setPreferredDefinition(rs.getString("VM_DESCRIPTION"));
        String vmIdSeq = rs.getString("VM_IDSEQ");
        vm.setIdseq(vmIdSeq);
        vm.setPublicId(rs.getInt("VM_ID"));
        vm.setVersion(rs.getFloat("VM_VERSION"));
        
        //set designations and definitions
        vm.setDefinitions(getDefinitions(vmIdSeq));
        vm.setDesignations(getDesignations(vmIdSeq, null));
                
        vvto.setValueMeaning(vm);
        
        List vvList = (List) vvMap.get(vdomainIdSeq);
        vvList.add(vvto);
        vvMap.put(vdomainIdSeq, vvList);
        return vvMap;
      }
    }
    
    




  public static void main(String[] args) {
    ServiceLocator locator = new SimpleServiceLocator();

    JDBCValueDomainDAO vdTest = new JDBCValueDomainDAO(locator);
    Collection vdIdseqList = new ArrayList();
    vdIdseqList.add("29A8FB24-0AB1-11D6-A42F-0010A4C1E842");
    //vdIdseqList.add("99BA9DC8-209C-4E69-E034-080020C9C0E0");
    // not existing vd_idseq
    vdIdseqList.add("99BA9DC8-209C-4E69-E034-080020C9C0E"); 

    JDBCValueDomainDAO vddao = new JDBCValueDomainDAO(locator);
    //Map vdMap = vddao.getPermissibleValues(null);
    Map vdMap = vddao.getPermissibleValues(vdIdseqList);
    System.out.println(vdMap);
  }
}
