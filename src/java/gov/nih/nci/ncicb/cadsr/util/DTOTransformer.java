package gov.nih.nci.ncicb.cadsr.util;

import gov.nih.nci.ncicb.cadsr.dto.FormValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.ValidValueTransferObject;
import gov.nih.nci.ncicb.cadsr.dto.bc4j.BC4JDataElementTransferObject;
import gov.nih.nci.ncicb.cadsr.persistence.bc4j.ValidValuesValueObject;
import gov.nih.nci.ncicb.cadsr.dto.DataElementTransferObject;
import gov.nih.nci.ncicb.cadsr.resource.Form;
import gov.nih.nci.ncicb.cadsr.resource.FormValidValue;
import gov.nih.nci.ncicb.cadsr.resource.Module;
import gov.nih.nci.ncicb.cadsr.resource.Question;
import gov.nih.nci.ncicb.cadsr.resource.ValidValue;
import gov.nih.nci.ncicb.cadsr.resource.ValueDomain;

import java.util.*;


public class DTOTransformer {
  /**
   * Transformer method to convert ValidValue to FormValidValue
   *
   * @param validValue a <code>ValidValue</code> value
   *
   * @return a <code>FormValidValue</code> value
   */
  public static FormValidValue toFormValidValue(
    ValidValue validValue,
    Question question) {
    FormValidValue fvv = new FormValidValueTransferObject();
    fvv.setVpIdseq(validValue.getVpIdseq());
    fvv.setLongName(validValue.getShortMeaningValue());
    fvv.setPreferredDefinition(validValue.getShortMeaning());
    fvv.setContext(question.getModule().getForm().getContext());
    fvv.setAslName(question.getModule().getForm().getAslName());
    fvv.setVpIdseq(validValue.getVpIdseq());
    fvv.setVersion(question.getModule().getForm().getVersion());
    fvv.setShortMeaning(validValue.getShortMeaning());
    fvv.setQuestion(question);

    return fvv;
  }

  /**
   * Transform a list of validValues to a list of FormValidValues. <br/
   * > Will throw a ClassCastException is list is not of ValidValues
   *
   * @param validValues a <code>List&lt;ValidValue&gt;</code> value
   *
   * @return a <code>List</code> value
   */
  public static List toFormValidValueList(
    List validValues,
    Question question) {
    List newValidValues = new ArrayList();

    for (Iterator it = validValues.iterator(); it.hasNext();) {
      ValidValue vv = (ValidValue) it.next();
      FormValidValue fvv = toFormValidValue(vv, question);
      newValidValues.add(fvv);
    }

    return newValidValues;
  }

  public static DataElementTransferObject toDataElement(BC4JDataElementTransferObject bc4jDE) {
    DataElementTransferObject de = new DataElementTransferObject();
    de.setDeIdseq(bc4jDE.getDeIdseq());
    de.setPreferredName(bc4jDE.getPreferredName());
    de.setPublicId(bc4jDE.getPublicId());
    de.setLongName(bc4jDE.getLongName());
    de.setPreferredDefinition(bc4jDE.getPreferredDefinition());
    ValueDomain vd = bc4jDE.getValueDomain();
    //List values = toPermissibleValueList(vd);
    //vd.setValidValues(values);
    de.setValueDomain(vd);
    de.setContext(bc4jDE.getContext());
    de.setLongCDEName(bc4jDE.getLongCDEName());
    de.setAslName(bc4jDE.getAslName());
    de.setVersion(bc4jDE.getVersion());
    de.setContextName(bc4jDE.getContextName());
    de.setUsingContexts(bc4jDE.getUsingContexts());
    de.setRegistrationStatus(bc4jDE.getRegistrationStatus());

    return de;
  }

  public static ValidValueTransferObject toPermissibleValue (ValidValueTransferObject bc4jVV) {
    ValidValueTransferObject value = new ValidValueTransferObject ();
    value.setDescription(bc4jVV.getDescription());
    value.setShortMeaning(bc4jVV.getShortMeaning());
    value.setShortMeaningDescription(bc4jVV.getShortMeaningDescription());
    value.setShortMeaningValue(bc4jVV.getShortMeaningValue());
    value.setVdIdseq(bc4jVV.getVdIdseq());
    value.setVpIdseq(bc4jVV.getVpIdseq());
    return value;
  }

  public static List toPermissibleValueList(ValueDomain vd) {
    ArrayList newValues = new ArrayList (11);
    List oldValues = vd.getValidValues();
    if (oldValues != null) {
      Iterator it = oldValues.iterator();
      while (it.hasNext()) {
       newValues.add(toPermissibleValue((ValidValueTransferObject)it.next()));
      }
    }
    return newValues;
  }
}
