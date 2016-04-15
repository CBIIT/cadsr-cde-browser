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

package gov.nih.nci.ncicb.cadsr.common.resource;

import java.util.List;

public interface Module extends FormElement,Orderable,Instructionable  {
  public String getModuleIdseq();
  public void setModuleIdseq(String idseq);

  public Form getForm();
  public void setForm(Form crf);

  public List<Question> getQuestions();
  public void setQuestions(List<Question> terms);

  public Object clone() throws CloneNotSupportedException ;

    public void setNumberOfRepeats(int repeats);

    public int getNumberOfRepeats();
}