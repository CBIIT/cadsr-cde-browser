MERGE INTO sbrext.tool_options_view_ext s
   USING (SELECT 'CDEBrowser' AS tool_name,
                 'NCI_METATHESAURUS_URL' AS property,
                 'http://ncim.nci.nih.gov/ncimbrowser'
                                                                     AS VALUE,
                 'URL for EVS NCI_METATHESAURUS_URL.' AS description
            FROM DUAL
          UNION
          SELECT 'CDEBrowser' AS tool_name,
                 'NCI_TERMINOLOGY_SERVER_URL' AS property,
                 'http://ncit.nci.nih.gov/ncitbrowser/start.jsf'
                                                                     AS VALUE,
                 'URL for NCI_TERMINOLOGY_SERVER_URL.' AS description
            FROM DUAL			
          UNION                    
          SELECT 'EVSBrowser' AS tool_name,
                 'URL' AS property,
                 'http://ncit.nci.nih.gov/ncitbrowser/'
                                                                     AS VALUE,
                 'URL for EVS/Term Browser.' AS description
            FROM DUAL
          UNION
          SELECT 'EVSBrowser' AS tool_name,
                 'CONCEPT.DETAILS.URL' AS property,
                 'http://ncit.nci.nih.gov/ncitbrowser/start.jsf'
                                                                     AS VALUE,
                 'URL for EVS/Term Browser.' AS description
            FROM DUAL
          UNION
          SELECT 'UMLBrowser' AS tool_name,
                 'NCI_METATHESAURUS_URL' AS property,
                 'http://ncim.nci.nih.gov/ncimbrowser'
                                                                     AS VALUE,
                 'URL for EVS NCI_METATHESAURUS_URL.' AS description
            FROM DUAL			
          UNION
          SELECT 'UMLBrowser' AS tool_name,
                 'NCI_TERMINOLOGY_SERVER_URL' AS property,
                 'http://ncit.nci.nih.gov/ncitbrowser/start.jsf'
                                                                     AS VALUE,
                 'URL for NCI_TERMINOLOGY_SERVER_URL.' AS description
            FROM DUAL ) t
   ON (s.tool_name = t.tool_name AND s.property = t.property)
   WHEN MATCHED THEN
      UPDATE
         SET s.VALUE = t.VALUE, s.description = t.description
   WHEN NOT MATCHED THEN
      INSERT (tool_name, property, VALUE, description, locale)
      VALUES (t.tool_name, t.property, t.VALUE, t.description, 'US');
/*
    Commit changes.
*/
COMMIT ;
