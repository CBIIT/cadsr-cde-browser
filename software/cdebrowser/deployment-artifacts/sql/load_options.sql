/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L*/

WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK;

SET SCAN OFF;

/*
   Database values owned by CDEBrowser
*/      
MERGE INTO sbrext.tool_options_view_ext s
   USING (SELECT 'CDEBrowser' AS tool_name, 'HELP.ROOT' AS property,
                    'https://cdebrowser.nci.nih.gov/help' AS value,
                 'Help URL for CDEBrowser.' AS description
            FROM DUAL) t
   ON (s.tool_name = t.tool_name AND s.property = t.property)
   WHEN MATCHED THEN
      UPDATE
         SET s.value = t.value, s.description = t.description
   WHEN NOT MATCHED THEN
      INSERT (tool_name, property, value, description, locale)
      VALUES (t.tool_name, t.property, t.value, t.description, 'US');

/*
    Database values used by FreeStyle Search 
*/
MERGE INTO sbrext.tool_options_view_ext s
   USING (SELECT 'CDEBrowser' AS tool_name, 'VIEWDE.URL' AS property,
                 'search'||CHR(63)||'dataElementDetails=9'||CHR(38)||'cdeId='||CHR(36)||'PID'||CHR(36)||CHR(38)||'version='||CHR(36)||'VERS'||CHR(36)||CHR(38)||'PageId=DataElementsGroup'||CHR(38)||'queryDE=yes'||CHR(38)||'FirstTimer=yes' AS value,
                 'DataElements Details URL for CDEBrowser.' AS description
            FROM DUAL) t
   ON (s.tool_name = t.tool_name AND s.property = t.property)
   WHEN MATCHED THEN
      UPDATE
         SET s.value = t.value, s.description = t.description
   WHEN NOT MATCHED THEN
      INSERT (tool_name, property, value, description, locale)
      VALUES (t.tool_name, t.property, t.value, t.description, 'US');
	
/*
   Database values owned by CDEBrowser
*/
/*MERGE INTO sbrext.tool_options_view_ext s
   USING (SELECT 'CDEBrowser' AS tool_name, 'URL' AS property,
                    'https://cdebrowser.nci.nih.gov/CDEBrowser/' AS value,
                 'URL for CDEBrowser.' AS description
            FROM DUAL) t
   ON (s.tool_name = t.tool_name AND s.property = t.property)
   WHEN MATCHED THEN
      UPDATE
         SET s.value = t.value, s.description = t.description
   WHEN NOT MATCHED THEN
      INSERT (tool_name, property, value, description, locale)
      VALUES (t.tool_name, t.property, t.value, t.description, 'US');*/

/*
   EVS code url update
*/
/* MERGE INTO sbrext.tool_options_view_ext s
   USING (SELECT 'CDEBrowser' AS tool_name, 'GO_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=GO&ontology_node_id=' AS value,
                 'URL for EVS GO code.' AS description
            FROM DUAL	     	    
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'LOINC_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=LOINC&ontology_node_id=' AS value,
                 'URL for EVS LOINC code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'MEDDRA_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=MedDRA&ontology_node_id=' AS value,
                 'URL for EVS MEDDRA code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'NCI_CONCEPT_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=NCI%20Thesaurus&ontology_node_id=' AS value,
                 'URL for EVS NCI_CONCEPT_CODE code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'NCI_META_CUI' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=NCI%20MetaThesaurus&ontology_node_id=' AS value,
                 'URL for EVS NCI_META_CUI code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'NCI_MO_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=MGED&ontology_node_id=' AS value,
                 'URL for EVS GO code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'UMLS_CUI' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=NCI%20MetaThesaurus&ontology_node_id=' AS value,
                 'URL for EVS UMLS_CUI code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'UWD_VA_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=UWD_VA&ontology_node_id=' AS value,
                 'URL for EVS UWD_VA_CODE code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'VA_NDF_CODE' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/ontology_visualize.xhtml?ontology_display_name=VA_NDFRT&ontology_node_id=' AS value,
                 'URL for EVS VA_NDF_CODE code.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'NCI_METATHESAURUS_URL' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/quick_search.xhtml' AS value,
                 'URL for EVS NCI_METATHESAURUS_URL.' AS description
            FROM DUAL
	    UNION
          SELECT 'CDEBrowser' AS tool_name, 'NCI_TERMINOLOGY_SERVER_URL' AS property,
                    'http://bioportal.nci.nih.gov/ncbo/faces/pages/advanced_search.xhtml' AS value,
                 'URL for NCI_TERMINOLOGY_SERVER_URL.' AS description
            FROM DUAL) t
   ON (s.tool_name = t.tool_name AND s.property = t.property)
   WHEN MATCHED THEN
      UPDATE
         SET s.value = t.value, s.description = t.description
   WHEN NOT MATCHED THEN
      INSERT (tool_name, property, value, description, locale)
      VALUES (t.tool_name, t.property, t.value, t.description, 'US');
*/
      
/*
    Commit changes.
*/
commit;

exit