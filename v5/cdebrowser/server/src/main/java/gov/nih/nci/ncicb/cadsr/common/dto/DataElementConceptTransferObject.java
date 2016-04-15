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

import gov.nih.nci.ncicb.cadsr.common.resource.DataElementConcept;
import gov.nih.nci.ncicb.cadsr.common.resource.ObjectClass;
import gov.nih.nci.ncicb.cadsr.common.resource.Property;


public class DataElementConceptTransferObject
  extends AdminComponentTransferObject implements DataElementConcept {

  protected String decIdseq;
	protected String conteIdseq;
	protected String cdIdseq;
	protected String proplName;
	protected String oclName;
	protected String objClassQualifier;
	protected String propertyQualifier;
	protected String changeNote;
	protected String objClassPrefName;
	protected String objClassContextName;
	protected String propertyPrefName;
	protected String propertyContextName;
	protected Float propertyVersion;
	protected Float objClassVersion;
	protected String conteName;
	protected String cdPrefName;
	protected String cdContextName;
	protected Float cdVersion;
  protected int cdPublicId;
  protected String objClassPublicId;
  protected String propertyPublicId;
  protected Property property;
  protected ObjectClass objectClass;
  
  
  public DataElementConceptTransferObject() {
    super();
  }

  public String getDecIdseq() {
    return decIdseq;
  }

  public void setDecIdseq(String aDecIdseq) {
    decIdseq = aDecIdseq;
    idseq = aDecIdseq;
  }

  public String getCdIdseq() {
		return cdIdseq;
	}

	public void setCdIdseq(String aCdIdseq) {
		cdIdseq = aCdIdseq;
	}

	public String getProplName() {
		return proplName;
	}

	public void setProplName(String aProplName) {
		proplName = aProplName;
	}

	public String getOclName() {
		return oclName;
	}

	public void setOclName(String aOclName) {
		oclName = aOclName;
	}

	public String getObjClassQualifier() {
		return objClassQualifier;
	}

	public void setObjClassQualifier(String aObjClassQualifier) {
		objClassQualifier = aObjClassQualifier;
	}

	public String getPropertyQualifier() {
		return propertyQualifier;
	}

	public void setPropertyQualifier(String aPropertyQualifier) {
		propertyQualifier = aPropertyQualifier;
	}

	public String getChangeNote() {
		return changeNote;
	}

	public void setChangeNote(String aChangeNote) {
		changeNote = aChangeNote;
	}
	public String getObjClassPrefName() {
		return objClassPrefName;
	}

	public String getObjClassContextName() {
		return objClassContextName;
	}
	public String getPropertyPrefName() {
		return propertyPrefName;
	}

	public String getPropertyContextName() {
		return propertyContextName;
	}
	public Float getObjClassVersion() {
		return objClassVersion;
	}
	public Float getPropertyVersion() {
		return propertyVersion;
	}
	public String getContextName() {
		return conteName;
	}
	public String getCDContextName() {
		return cdContextName;
	}
	public String getCDPrefName() {
		return cdPrefName;
	}
	public Float getCDVersion() {
		return cdVersion;
	}

  public void setCDPublicId(int cDPublicID)
  {
    this.cdPublicId = cDPublicID;
  }


  public int getCDPublicId()
  {
    return cdPublicId;
  }


  public void setObjClassPublicId(String objClassPublicId)
  {
    this.objClassPublicId = objClassPublicId;
  }


  public String getObjClassPublicId()
  {
    return objClassPublicId;
  }


  public void setPropertyPublicId(String propertyPublicId)
  {
    this.propertyPublicId = propertyPublicId;
  }


  public String getPropertyPublicId()
  {
    return propertyPublicId;
  }

   public Property getProperty()
   {
     return property;
   }
   public void setProperty(Property newProperty)
   {
     property=newProperty;
   }
   
   public ObjectClass getObjectClass()
   {
     return objectClass;
   }
   public void setObjectClass(ObjectClass newObjectClass)
   {
     objectClass= newObjectClass;
   }
}
