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

package gov.nih.nci.ncicb.cadsr.common.persistence.dao;

import java.util.Collection;
import java.util.Map;
import java.util.List;

import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueMeaning;

public interface ValueDomainDAO extends AdminComponentDAO {
  /**
   * Gets all Value Domains and their Valid Values
   *
   * @param <b>vdIdseqs</b> list of Value Domain Idseq.
   *
   * @return <b>Map</b> Map of Value Domain objects each having
   *   list of Valid Value objects (key: vd idseq, value: vv list)
   *   
   */
  public Map getPermissibleValues(Collection vdIdseqs);
  
  public ValueDomain getValueDomainById(String vdId);
  
  public ValueMeaning getValueMeaning(String shortMeaning);
  
  public ValueMeaning getValueMeaningById(Integer vmId);
}
