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

package gov.nih.nci.ncicb.cadsr.common.dto.bc4j;

import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.common.dto.ConceptDerivationRuleTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ConceptTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ContextTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.RepresentationTransferObject;
import gov.nih.nci.ncicb.cadsr.common.persistence.bc4j.RepresentationViewObjRowImpl;
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.resource.Representation;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;
import java.util.List;

import java.sql.Date;
import java.sql.SQLException;
import java.io.Serializable;
import gov.nih.nci.ncicb.cadsr.common.dto.base.AdminComponentTransferObject;
import gov.nih.nci.ncicb.cadsr.common.persistence.bc4j.ComponentConceptsExtImpl;
import gov.nih.nci.ncicb.cadsr.common.persistence.bc4j.ConDerivationRulesExtImpl;
import gov.nih.nci.ncicb.cadsr.common.persistence.bc4j.ContextsViewRowImpl;
import gov.nih.nci.ncicb.cadsr.common.persistence.bc4j.ValueDomainsViewRowImpl;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.AbstractDAOFactory;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.ConceptDAO;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocator;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocatorFactory;

import oracle.jbo.domain.Number;
import java.util.List;

public class BC4JValueDomainTransferObject extends AdminComponentTransferObject
                            implements ValueDomain, Serializable {
	protected String vdIdseq;
	protected String datatype;
	protected String uom;
	protected String dispFormat;
	protected String maxLength;
	protected String minLength;
	protected String highVal;
	protected String lowVal;
	protected String charSet;
	protected String decimalPlace;
	protected String cdPrefName;
	protected String cdContextName;
	protected Float cdVersion;
        protected String repPrefName;
        protected String repContextName;
        protected int repPublicId;
        protected Float repVersion;
  protected int cdPublicId;
	protected String vdType;
  protected List validValues;
  protected Representation representation;
  protected ConceptDerivationRule conceptDerivationRule;



	public BC4JValueDomainTransferObject() {
    idseq = vdIdseq;
	}

	public BC4JValueDomainTransferObject(ValueDomainsViewRowImpl vdViewRowImpl)
		throws SQLException {
		preferredDefinition = vdViewRowImpl.getPreferredDefinition();
		preferredName = vdViewRowImpl.getPreferredName();
		longName = vdViewRowImpl.getLongName();
		createdBy = vdViewRowImpl.getCreatedBy();
		//createdDate = (Date)vdViewRowImpl.getDateCreated().getData();
		modifiedBy = vdViewRowImpl.getModifiedBy();
		//modifiedDate = (Date)(vdViewRowImpl.getDateModified().getData());
		aslName = vdViewRowImpl.getAslName();
		version = new Float(vdViewRowImpl.getVersion().floatValue());
		deletedInd = checkForNull(vdViewRowImpl.getDeletedInd());
		latestVerInd = vdViewRowImpl.getLatestVersionInd();
		vdIdseq = vdViewRowImpl.getVdIdseq().trim();
        idseq = vdIdseq;
		datatype = vdViewRowImpl.getDtlName().trim();
		uom = checkForNull(vdViewRowImpl.getUomlName());
		dispFormat = checkForNull(vdViewRowImpl.getFormlName());
		maxLength = checkForNull(vdViewRowImpl.getMaxLengthNum());
		minLength = checkForNull(vdViewRowImpl.getMinLengthNum());
		highVal = checkForNull(vdViewRowImpl.getHighValueNum());
		lowVal = checkForNull(vdViewRowImpl.getLowValueNum());
		charSet = checkForNull(vdViewRowImpl.getCharSetName());
		decimalPlace = checkForNull(vdViewRowImpl.getDecimalPlace());
		vdType = vdViewRowImpl.getVdTypeFlag();
		cdPrefName = vdViewRowImpl.getCDPrefName();
		cdContextName = vdViewRowImpl.getCDContextName();
		cdVersion = new Float(vdViewRowImpl.getCDVersion().floatValue());
        cdPublicId = vdViewRowImpl.getCDPublicId().intValue();

            String cdrIdseq = vdViewRowImpl.getCondrIdseq();
             if(cdrIdseq!=null)
              {
                   conceptDerivationRule  = new ConceptDerivationRuleTransferObject();
                  conceptDerivationRule.setIdseq(cdrIdseq);
                 }

            if (vdViewRowImpl.getVdId() != null)
                  publicId = vdViewRowImpl.getVdId().intValue();
            origin = checkForNull(vdViewRowImpl.getOrigin());
           RepresentationViewObjRowImpl repImpl=  (RepresentationViewObjRowImpl)vdViewRowImpl.getRepresentationViewObj();
           if(repImpl!=null)
           {
               Representation rep = new RepresentationTransferObject();
               rep.setLongName(repImpl.getLongName());
               rep.setPreferredName(repImpl.getPreferredName());
               rep.setIdseq(repImpl.getRepIdseq().trim());
               rep.setPublicId(repImpl.getRepId().intValue());

               ContextsViewRowImpl conImpl = (ContextsViewRowImpl) repImpl.getContextsRow();
               Context repContext = new ContextTransferObject();
               repContext.setName(conImpl.getName());
               rep.setContext(repContext);
               ServiceLocator locator =
               ServiceLocatorFactory.getLocator(CaDSRConstants.CDEBROWSER_SERVICE_LOCATOR_CLASSNAME);

               AbstractDAOFactory daoFactory = AbstractDAOFactory.getDAOFactory(locator);
               ConceptDAO conDAO = daoFactory.getConceptDAO();
               {
                ConceptDerivationRule repRule;
                repRule = conDAO.getRepresentationDerivationRuleForVD(vdViewRowImpl.getVdIdseq());
                rep.setConceptDerivationRule(repRule);
                }

               Number repVersion = repImpl.getVersion();
               rep.setVersion(repVersion.floatValue());
               setRepresentation(rep);
    }


	}

	public String getVdIdseq() {
		return vdIdseq;
	}

	public void setVdIdseq(String pVdIdseq) {
		vdIdseq = pVdIdseq;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDisplayFormat() {
		return dispFormat;
	}

	public void setDisplayFormat(String dispFormat) {
		this.dispFormat = dispFormat;
	}

	public String getUnitOfMeasure() {
		return uom;
	}
	public void setUnitOfMeasure(String uom) {
		this.uom = uom;
	}

	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getMinLength() {
		return minLength;
	}
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}
	public String getHighValue() {
		return highVal;
	}
	public void setHighValue(String highVal) {
		this.highVal = highVal;
	}

	public String getLowValue() {
		return lowVal;
	}
	public void setLowValue(String lowVal) {
		this.lowVal = lowVal;
	}

	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getDecimalPlace() {
		return decimalPlace;
	}
	public void setDecimalPlace(String dp) {
		this.decimalPlace = dp;
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

	public String getVDType() {
		if ("E".equals(vdType))
			return "Enumerated";
		else if ("N".equals(vdType))
			return "Non Enumerated";
		else
			return vdType;
	}

	public void setVDType(String type) {
		vdType = type;
	}

   public List getValidValues()
   {
     return validValues;
   }

   public void setValidValues(List validValues)
   {
     this.validValues = validValues;
   }
   public Representation getRepresentation()
   {
     return representation;
   }
   public void setRepresentation(Representation newRepresentation)
   {
     representation=newRepresentation;
   }

   public ConceptDerivationRule getConceptDerivationRule()
   {
     return conceptDerivationRule;
   }
   public void setConceptDerivationRule(ConceptDerivationRule rule)
   {
     conceptDerivationRule = rule;
   }


  public void setCDPublicId(int cdPublicId)
  {
    this.cdPublicId = cdPublicId;
  }


  public int getCDPublicId()
  {
    return cdPublicId;
  }

    public String getRepresentationPrefName() {
            return repPrefName;
    }

    public String getRepresentationContextName() {
            return repContextName;
    }
    public Float getRepresentationVersion() {
            return repVersion;
    }
}