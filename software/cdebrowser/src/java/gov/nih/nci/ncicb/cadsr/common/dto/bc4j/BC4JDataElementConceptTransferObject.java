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

import gov.nih.nci.ncicb.cadsr.common.dto.ContextTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.ObjectClassTransferObject;
import gov.nih.nci.ncicb.cadsr.common.dto.PropertyTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElementConcept;
import gov.nih.nci.ncicb.cadsr.common.resource.ObjectClass;
import gov.nih.nci.ncicb.cadsr.common.resource.Property;

import java.sql.Date;
import java.sql.SQLException;
import java.io.Serializable;

import gov.nih.nci.ncicb.cadsr.common.dto.base.AdminComponentTransferObject;
import gov.nih.nci.ncicb.cadsr.common.persistence.bc4j.DataElementConceptsViewRowImpl;
import oracle.jbo.domain.Number;

public class BC4JDataElementConceptTransferObject extends AdminComponentTransferObject
        implements DataElementConcept, Serializable {
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
    protected int ocPublicId;
    protected int propPublicId;
    protected String objClassPublicId;
    protected String propertyPublicId;
    protected Property property;
    protected ObjectClass objectClass;


    public BC4JDataElementConceptTransferObject() {
        super();
        idseq = decIdseq;
    }

    public BC4JDataElementConceptTransferObject(DataElementConceptsViewRowImpl dataElementConceptsViewRowImpl)
            throws SQLException {

        decIdseq = dataElementConceptsViewRowImpl.getDecIdseq().trim();
        idseq = decIdseq;
        cdIdseq = dataElementConceptsViewRowImpl.getCdIdseq().trim();


        changeNote = checkForNull(dataElementConceptsViewRowImpl.getChangeNote());
        preferredDefinition =
                dataElementConceptsViewRowImpl.getPreferredDefinition();
        preferredName = dataElementConceptsViewRowImpl.getPreferredName();
        longName = dataElementConceptsViewRowImpl.getLongName();
        createdBy = dataElementConceptsViewRowImpl.getCreatedBy();
        //createdDate = (Date)dataElementConceptsViewRowImpl.getDateCreated().getData();
        modifiedBy = dataElementConceptsViewRowImpl.getModifiedBy();
        //modifiedDate = (Date)(dataElementConceptsViewRowImpl.getDateModified().getData());
        aslName = dataElementConceptsViewRowImpl.getAslName();
        version =
                new Float(dataElementConceptsViewRowImpl.getVersion().floatValue());
        deletedInd = dataElementConceptsViewRowImpl.getDeletedInd();
        latestVerInd = dataElementConceptsViewRowImpl.getLatestVersionInd();

        //ObjectClass Info
        String objectClassPrefName = dataElementConceptsViewRowImpl.getObjectClassPrefName();
        if (objectClassPrefName != null && !objectClassPrefName.equals("")) {
            ObjectClass objClass = new ObjectClassTransferObject();
            objClass.setIdseq(dataElementConceptsViewRowImpl.getOcIdseq());
            objClass.setLongName(checkForNull(dataElementConceptsViewRowImpl.getObjectClassLongName()));
            String objClassPrefName =
                    checkForNull(objectClassPrefName);
            objClass.setPreferredName(objClassPrefName);
            Number objClassPublicId = dataElementConceptsViewRowImpl.getObjectClassPublicId();
            if (objClassPublicId != null) {
                objClass.setPublicId(objClassPublicId.intValue());
            }
            String objClassContextName =
                    checkForNull(dataElementConceptsViewRowImpl.getObjectClassContext());
            Context objContext = new ContextTransferObject();
            objContext.setName(objClassContextName);
            objClass.setContext(objContext);
            Float objClassVersion = null;
            if (dataElementConceptsViewRowImpl.getObjectClassVersion() != null)
                objClassVersion =
                        new Float(
                                dataElementConceptsViewRowImpl.getObjectClassVersion().floatValue());
            else
                objClassVersion = new Float(0.00f);
            objClass.setVersion(objClassVersion);
            String oclName = checkForNull(dataElementConceptsViewRowImpl.getOclName());
            objClass.setName(oclName);
            String objClassQualifier =
                    checkForNull(dataElementConceptsViewRowImpl.getObjClassQualifier());
            objClass.setQualifier(objClassQualifier);
            setObjectClass(objClass);
        }

        //Property Info
        String propPrefName = dataElementConceptsViewRowImpl.getPropertyPrefName();
        if (propPrefName != null && !propPrefName.equals("")) {
            Property prop = new PropertyTransferObject();
            String propertyPrefName =
                    checkForNull(propPrefName);
            prop.setPreferredName(propertyPrefName);
            prop.setLongName(checkForNull(dataElementConceptsViewRowImpl.getPropertyLongName()));
            Number propPublicId = dataElementConceptsViewRowImpl.getPropertyPublicId();
            if (propPublicId != null) {
                prop.setPublicId(propPublicId.intValue());
            }

            String propertyContextName =
                    checkForNull(dataElementConceptsViewRowImpl.getPropertyContextName());
            Context propContext = new ContextTransferObject();
            propContext.setName(propertyContextName);
            prop.setContext(propContext);
            String proplName = checkForNull(dataElementConceptsViewRowImpl.getProplName());
            prop.setName(proplName);
            Float propertyVersion = null;
            if (dataElementConceptsViewRowImpl.getPropertyVersion() != null)
                propertyVersion =
                        new Float(
                                dataElementConceptsViewRowImpl.getPropertyVersion().floatValue());
            else
                propertyVersion = new Float(0.00f);
            prop.setVersion(propertyVersion);
            String propertyQualifier =
                    checkForNull(dataElementConceptsViewRowImpl.getPropertyQualifier());
            prop.setQualifier(propertyQualifier);
            setProperty(prop);
        }

        conteName = dataElementConceptsViewRowImpl.getContextName();
        cdPrefName = dataElementConceptsViewRowImpl.getCDPrefName();
        cdContextName = dataElementConceptsViewRowImpl.getCDContextName();
        cdPublicId = dataElementConceptsViewRowImpl.getCDPublicId().intValue();
        cdVersion =
                new Float(dataElementConceptsViewRowImpl.getCDVersion().floatValue());
        if (dataElementConceptsViewRowImpl.getDecId() != null) {
            publicId = dataElementConceptsViewRowImpl.getDecId().intValue();
        }

        origin = checkForNull(dataElementConceptsViewRowImpl.getOrigin());
    }

    public String getDecIdseq() {
        return decIdseq;
    }

    public void setDecIdseq(String aDecIdseq) {
        decIdseq = aDecIdseq;
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

    public void setCDPublicId(int cDPublicID) {
        this.cdPublicId = cDPublicID;
    }


    public int getCDPublicId() {
        return cdPublicId;
    }


    public void setObjClassPublicId(String objClassPublicId) {
        this.objClassPublicId = objClassPublicId;
    }


    public String getObjClassPublicId() {
        return objClassPublicId;
    }


    public void setPropertyPublicId(String propertyPublicId) {
        this.propertyPublicId = propertyPublicId;
    }


    public String getPropertyPublicId() {
        return propertyPublicId;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property newProperty) {
        property = newProperty;
    }

    public ObjectClass getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(ObjectClass newObjectClass) {
        objectClass = newObjectClass;
    }
}
