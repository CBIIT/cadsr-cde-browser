/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.common;

import java.sql.Timestamp;

/**
 * Keeping this class name from the original code.
 */
public interface Audit
{
    public String getCreatedBy();
    public void setCreatedBy( String pCreatedBy );
    public Timestamp getDateCreated();
    public void setDateCreated( Timestamp pCreatedDate );
    public String getModifiedBy();
    public void setModifiedBy( String pModifiedBy );
    public Timestamp getDateModified();
    public void setDateModified( Timestamp pModifiedDate );
    public String toString();
}
