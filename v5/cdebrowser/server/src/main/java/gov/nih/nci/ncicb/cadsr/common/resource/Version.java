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

public class Version{
   private String id; //uniquely identify an Admin Component
   private Float versionNumber;
   private String changeNote;
   private boolean latestVersionIndicator = false;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setChangeNote(String changeNote) {
        this.changeNote = changeNote;
    }

    public String getChangeNote() {
        return changeNote;
    }

    public void setLatestVersionIndicator(boolean latestVersionIndicator) {
        this.latestVersionIndicator = latestVersionIndicator;
    }

    public boolean isLatestVersionIndicator() {
        return latestVersionIndicator;
    }
}
