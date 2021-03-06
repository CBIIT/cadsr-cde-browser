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

package gov.nih.nci.ncicb.cadsr.common.servicelocator;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ejb.ServiceLocatorImpl;
import gov.nih.nci.ncicb.cadsr.common.util.ObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for ServiceLocators.
 * ServiceLocator instatiation shoud be delegated to this classl
 * 
 */
public class ServiceLocatorFactory 
{
  protected static Log log = LogFactory.getLog(ServiceLocatorFactory.class.getName());
  
  public ServiceLocatorFactory()
  {
  }
 public static ServiceLocator getEJBLocator()
  {
    return new ServiceLocatorImpl();
  }

  public static ServiceLocator getLocator(String locatorClassName)
  {
      ServiceLocator locator =null;
      try {
        
        if(log.isDebugEnabled())
          log.debug("Instatiating ServiceLocator = "+locatorClassName);
        locator= (ServiceLocator)ObjectFactory.createObect(locatorClassName);
      }
      catch (Exception ex) {
        throw new ServiceLocatorException(
          "Unable to Create specified ServiceLocator implementation for "+locatorClassName, ex);
      }
      return locator;
  }   


}