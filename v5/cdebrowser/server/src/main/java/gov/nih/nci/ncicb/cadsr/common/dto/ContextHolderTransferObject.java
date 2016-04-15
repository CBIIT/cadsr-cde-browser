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

package gov.nih.nci.ncicb.cadsr.common.dto;

import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.ContextHolder;

import javax.swing.tree.DefaultMutableTreeNode;

public class ContextHolderTransferObject implements ContextHolder
{
  private Context context;
  private DefaultMutableTreeNode node; 
  

  public void setContext(Context context)
  {
    this.context = context;
  }


  public Context getContext()
  {
    return context;
  }


  public void setNode(DefaultMutableTreeNode node)
  {
    this.node = node;
  }


  public DefaultMutableTreeNode getNode()
  {
    return node;
  }
}