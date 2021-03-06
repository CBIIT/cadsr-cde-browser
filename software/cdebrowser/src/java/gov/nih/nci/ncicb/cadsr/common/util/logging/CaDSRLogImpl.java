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

/**
 * CaDSRLogImpl
 *
 * This class implements the Log interface.  It is a wrapper for the commons Log
 * class to allow the application to be independent of any underlying logging
 * package.  It also includes code guards to verify that logging should be 
 * performed before incurring the overhead of the logging method call 
 *
 * @release 3.0
 * @author: <a href=mailto:jane.jiang@oracle.com>Jane Jiang</a>
 * @date: 8/16/2005
 * @version: $Id: CaDSRLogImpl.java,v 1.1 2009-02-13 18:09:23 davet Exp $
 */
 
package gov.nih.nci.ncicb.cadsr.common.util.logging;

public class CaDSRLogImpl implements Log {

org.apache.commons.logging.Log logger;

   private CaDSRLogImpl() {
   }
   
   public CaDSRLogImpl (java.lang.String name) {
      super();
      logger = org.apache.commons.logging.LogFactory.getLog(name);
   }
    public boolean isDebugEnabled() {
       return logger.isDebugEnabled();
    }

    public void trace(Object message) {
       if (logger.isTraceEnabled())
         logger.trace(message);
    }
    
    public void trace(Object message, Throwable t) {
       if (logger.isTraceEnabled())
          logger.trace(message, t);
    }
    
    public void info(Object message) {
       if (logger.isInfoEnabled()) 
          logger.info(message);
    }
    public void info(Object message, Throwable t) {
       if (logger.isInfoEnabled())
          logger.info(message, t);
    }

    public void debug(Object message) {
       if (logger.isDebugEnabled()) 
          logger.debug(message);
    }
    public void debug(Object message, Throwable t) {
       if (logger.isDebugEnabled())
          logger.debug(message, t);
    }

    public void warn(Object message) {
       if (logger.isWarnEnabled())
          logger.warn(message);
    }
    public void warn(Object message, Throwable t) {
       if (logger.isWarnEnabled()) 
          logger.warn(message, t);
    }

    public void error(Object message) {
       if (logger.isErrorEnabled()) {
          logger.error(message);
       }
    }
    public void error(Object message, Throwable t) {
       if (logger.isErrorEnabled()) {
          logger.error(message, t);
       }
    }

    public void fatal(Object message) {
       if (logger.isFatalEnabled()) {
          logger.fatal(message);
       }
    }
    
    public void fatal(Object message, Throwable t) {
       if (logger.isFatalEnabled()) {
          logger.fatal(message, t);
       }
    }
   
}