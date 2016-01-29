package gov.nih.nci.cadsr.common.util;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CDEBrowserParams
{
    private static Logger logger = LogManager.getLogger( CDEBrowserParams.class.getName() );
    //This is used monitor application mode
    public static String mode = "";

    String xmlDownloadDir = "";
    String xmlPaginationFlag = "no";
    String xmlFileMaxRecord;
    String treeURL = "";
    String evsSources = "";
    String showFormsAlphebetical = "no";
    String excludeTestContext = "no";
    String excludeTrainingContext = "no";
    String excludeWorkFlowStatuses = "";
    String excludeRegistrationStatuses = "";

    String curationToolUrl = "";
    String nciMetathesaurusUrl = "";
    String nciTerminologyServerUrl = "";
    String sentinelToolUrl = "";
    String adminToolUrl = "";
    String umlBrowserUrl = "";
    String regStatusCsTree = "";
    String csTypeRegStatus = "";
    String csTypeContainer = "container";
    String sentinelAPIUrl = "";
    String formBuilderUrl = "";
    String cdeBrowserUrl = "";
    String cdeBrowserReleaseNoteUrl = "";
    String objectCartUrl = "";
    String cadsrAPIUrl = "";
    String formBuilderHelpUrl = "";
    String cdebrowserHelpUrl = "";
    String privacyURL = "";
    String pcsUrl = "";

    String contextTest = "";
    String contextTraining = "";

    Map evsUrlMap = new HashMap();

    static CDEBrowserParams instance;

    /**
     * Read the resource bundle file
     * propFilename - the specified resource file (fn.properties) without the extension
     * (e.g., medsurv)
     */
    private CDEBrowserParams()
    {
    }

    public String getXMLDownloadDir()
    {
        return xmlDownloadDir;
    }

    public static CDEBrowserParams getInstance()
    {
        if( instance == null )
        {
            try
            {
                getDebugInstance();
                logger.debug( "Using debug properties file" );
                mode = "DEBUG MODE";
                return instance;
            } catch( NoSuchElementException nse )
            {
                logger.error( "Cannot find property" + nse );
                throw nse;
            } catch( Exception e )
            {
            }
            Properties properties = new Properties();// appServiceLocator.findCDEBrowserService().getApplicationProperties(Locale.US);
            instance = new CDEBrowserParams();
            instance.initAttributesFromProperties( properties );
            logger.debug( "Using database for properties" );
        }
        return instance;
    }

    public static void reloadInstance()
    {
        instance = null;
        getInstance();
    }

    public static CDEBrowserParams getToolInstance( String toolName )
    {
        Properties properties = new Properties();//appServiceLocator.findCDEBrowserService().getApplicationProperties(Locale.US, toolName);
        String browseurl = properties.getProperty( "CDEBrowser_URL" );
        if( browseurl.contains( "/CDEBrowser/" ) )
        {
            browseurl = browseurl.replace( "/CDEBrowser/", "" );
            properties.put( "CDEBrowser_URL", browseurl );
        }

        instance = new CDEBrowserParams();
        instance.initAttributesFromProperties( properties );

        return instance;
    }

    public static CDEBrowserParams getDebugInstance()
    {
        if( instance == null )
        {
            ResourceBundle b = ResourceBundle.getBundle( "cdebrowser", Locale.getDefault() );
            Properties properties = new Properties();

            for( Enumeration e = b.getKeys(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                if( key != null )
                {
                    logger.debug( " Get CDEBrowser.property = " + key );
                    properties.setProperty( key, b.getString( key ) );
                }
            }


            instance = new CDEBrowserParams();
            instance.initAttributesFromProperties( properties );
        }
        return instance;
    }

    public static void reloadInstance( String userName )
    {
        Properties properties = new Properties();//appServiceLocator.findCDEBrowserService().reloadApplicationProperties(Locale.US,userName);
        instance = new CDEBrowserParams();
        instance.initAttributesFromProperties( properties );
    }

    public String getTreeURL()
    {
        return treeURL;
    }

    public String getXMLPaginationFlag()
    {
        return xmlPaginationFlag;
    }

    public String getXMLFileMaxRecords()
    {
        return xmlFileMaxRecord;
    }

    public void setEvsUrlMap( Map evsUrlMap )
    {
        this.evsUrlMap = evsUrlMap;
    }

    public void setEvsUrlMap( Properties bundle, String evsSourcesArr )
    {
        try
        {
            String[] urls = tokenizeCSVList( evsSourcesArr );
            for( int i = 0; i < urls.length; i++ )
            {
                String key = urls[i];
                String value = bundle.getProperty( key );
                if( evsUrlMap == null )
                    evsUrlMap = new HashMap();
                evsUrlMap.put( key, value );
            }
        } catch( MissingResourceException mre )
        {
            logger.error( "Error getting init parameters, missing resource values" );
            logger.error( "EVS Url not mapped correctly" );
            logger.error( mre.getMessage(), mre );
            System.exit( -1 );
        } catch( Exception e )
        {
            logger.error( "Exception occurred", e );
            System.exit( -1 );
        }
    }

    public String[] tokenizeCSVList( String values )
    {
        String[] retVal = null;

        try
        {
            StringTokenizer st = new StringTokenizer( values, "," );
            int numberOfTokens = st.countTokens();
            retVal = new String[numberOfTokens];

            for( int i = 0; i < numberOfTokens; i++ )
            {
                retVal[i] = st.nextToken();
            }
        } catch( Exception e )
        {
            return new String[0];
        }

        return retVal;
    }


    public Map getEvsUrlMap()
    {
        return evsUrlMap;
    }

    public void setShowFormsAlphebetical( String showFormsAlphebetical )
    {
        this.showFormsAlphebetical = showFormsAlphebetical;
    }


    public String getShowFormsAlphebetical()
    {
        return showFormsAlphebetical;
    }


    public void setExcludeTestContext( String excludeTestContext )
    {
        this.excludeTestContext = excludeTestContext;
    }


    public String getExcludeTestContext()
    {
        return excludeTestContext;
    }

    public void setExcludeTrainingContext( String excludeTrainingContext )
    {
        this.excludeTrainingContext = excludeTrainingContext;
    }


    public String getExcludeTrainingContext()
    {
        return excludeTrainingContext;
    }

    public String getExcludeWorkFlowStatuses()
    {
        return excludeWorkFlowStatuses;
    }

    public void setExcludeWorkFlowStatuses( String excludeWorkFlowStatuses )
    {
        this.excludeWorkFlowStatuses = excludeWorkFlowStatuses;
    }

    public String getExcludeRegistrationStatuses()
    {
        return excludeRegistrationStatuses;
    }

    public void setExcludeRegistrationStatuses( String excludeRegistrationStatuses )
    {
        this.excludeRegistrationStatuses = excludeRegistrationStatuses;
    }


    public void setCurationToolUrl( String curationToolUrl )
    {
        this.curationToolUrl = curationToolUrl;
    }


    public String getCurationToolUrl()
    {
        return curationToolUrl;
    }


    public void setNciMetathesaurusUrl( String nciMetathesaurusUrl )
    {
        this.nciMetathesaurusUrl = nciMetathesaurusUrl;
    }


    public String getNciMetathesaurusUrl()
    {
        return nciMetathesaurusUrl;
    }


    public void setNciTerminologyServerUrl( String nciTerminologyServerUrl )
    {
        this.nciTerminologyServerUrl = nciTerminologyServerUrl;
    }


    public String getNciTerminologyServerUrl()
    {
        return nciTerminologyServerUrl;
    }


    public void setSentinelToolUrl( String sentinelToolUrl )
    {
        this.sentinelToolUrl = sentinelToolUrl;
    }


    public String getSentinelToolUrl()
    {
        return sentinelToolUrl;
    }


    public void setAdminToolUrl( String adminToolUrl )
    {
        this.adminToolUrl = adminToolUrl;
    }


    public String getAdminToolUrl()
    {
        return adminToolUrl;
    }

    public String getPrivacyURL()
    {
        return privacyURL;
    }

    public void setPrivacyURL( String privacyURL )
    {
        this.privacyURL = privacyURL;
    }

    private void initAttributesFromProperties( Properties properties )
    {
        // read the init parameters from the resource bundle
        int index = 0;
        try
        {

            xmlDownloadDir = properties.getProperty( "XML_DOWNLOAD_DIR" );
            index++;
            xmlPaginationFlag = properties.getProperty( "XML_PAGINATION_FLAG" );
            index++;
            xmlFileMaxRecord = properties.getProperty( "XML_FILE_MAX_RECORDS" );
            index++;
            treeURL = properties.getProperty( "TREE_URL" );
            index++;
            evsSources = properties.getProperty( "EVS_URL_SOURCES" );
            index++;
            setEvsUrlMap( properties, evsSources );
            showFormsAlphebetical = properties.getProperty( "SHOW_FORMS_ALPHEBETICAL" );
            index++;
            excludeTestContext = properties.getProperty( "EXCLUDE_TEST_CONTEXT_BY_DEFAULT" );
            index++;
            excludeTrainingContext = properties.getProperty( "EXCLUDE_TRAINING_CONTEXT_BY_DEFAULT" );
            index++;
            excludeWorkFlowStatuses = properties.getProperty( "EXCLUDE_WORKFLOW_BY_DEFAULT" );
            index++;
            excludeRegistrationStatuses = properties.getProperty( "EXCLUDE_REGISTRATION_BY_DEFAULT" );
            index++;
            //URL to Temporary Page for Admin Tool retrieved from Tools options table
            adminToolUrl = properties.getProperty( "AdminTool_URL" ); //"/CDEBrowser/common/adminRedirection.html";
            index++;
            curationToolUrl = properties.getProperty( "CURATION_URL" );
            index++;
            nciMetathesaurusUrl = properties.getProperty( "NCI_METATHESAURUS_URL" );
            index++;
            nciTerminologyServerUrl = properties.getProperty( "NCI_TERMINOLOGY_SERVER_URL" );
            index++;
            sentinelToolUrl = properties.getProperty( "SENTINEL_URL" );
            index++;
            regStatusCsTree = properties.getProperty( "CS_TYPE_REGISTRATION_STATUS" );
            index++;
            csTypeRegStatus = properties.getProperty( "REG_STATUS_CS_TREE" );
            index++;
            csTypeContainer = properties.getProperty( "CS_TYPE_CONTAINER" );
            index++;
            sentinelAPIUrl = properties.getProperty( "SENTINEL_URL" );
            index++;
            umlBrowserUrl = properties.getProperty( "UMLBrowser_URL" );
            index++;
            formBuilderUrl = properties.getProperty( "FormBuilder_URL" );
            index++;
            cdeBrowserUrl = properties.getProperty( "CDEBrowser_URL" );
            index++;
            objectCartUrl = properties.getProperty( "ObjectCartAPI_URL" );
            index++;
            cadsrAPIUrl = properties.getProperty( "CADSRAPI_URL" );
            index++;
            contextTest = properties.getProperty( "BROADCAST.EXCLUDE.CONTEXT.00.NAME" );
            index++;
            contextTraining = properties.getProperty( "BROADCAST.EXCLUDE.CONTEXT.01.NAME" );
            index++;
            formBuilderHelpUrl = properties.getProperty( "HELP.ROOT" );
            index++;
            privacyURL = properties.getProperty( "PRIVACY_URL" );
            index++;
            logger.info( "Loaded Properties" + properties );
            cdebrowserHelpUrl = properties.getProperty( "HELP.ROOT" );
            index++;
            pcsUrl = properties.getProperty( "PCS_URL" );
            index++;
            logger.info( "Loaded Properties" + properties );

        } catch( MissingResourceException mre )
        {
            logger.error( "Error getting init parameters, missing resource values" );
            logger.error( "Property missing index: " + index );
            logger.error( mre.getMessage(), mre );
        } catch( Exception e )
        {
            logger.error( "Exception occurred when loading properties", e );
        }
    }

    public String getRegStatusCsTree()
    {
        return regStatusCsTree;
    }

    public String getCsTypeRegStatus()
    {
        return csTypeRegStatus;
    }

    public String getCsTypeContainer()
    {
        return csTypeContainer;
    }

    public void setSentinelAPIUrl( String sentinelAPIUrl )
    {
        this.sentinelAPIUrl = sentinelAPIUrl;
    }

    public String getSentinelAPIUrl()
    {
        return sentinelAPIUrl;
    }

    public void setUmlBrowserUrl( String umlBrowserUrl )
    {
        this.umlBrowserUrl = umlBrowserUrl;
    }

    public String getUmlBrowserUrl()
    {
        return umlBrowserUrl;
    }

    public String getFormBuilderUrl()
    {
        return formBuilderUrl;
    }

    public void setFormBuilderUrl( String formBuilderUrl )
    {
        this.formBuilderUrl = formBuilderUrl;
    }

    public String getCdeBrowserUrl()
    {
        return cdeBrowserUrl;
    }

    public void setCdeBrowserUrl( String cdeBrowserUrl )
    {
        this.cdeBrowserUrl = cdeBrowserUrl;
    }


    public void setCdeReleaseNoteUrl( String newUrl )
    {
        cdeBrowserReleaseNoteUrl = newUrl;
    }

    public String getObjectCartUrl()
    {
        return ( objectCartUrl == null ) ? "" : objectCartUrl;
    }

    public void setObjectCartUrl( String objectCartUrl )
    {
        this.objectCartUrl = objectCartUrl;
    }

    public String getCadsrAPIUrl()
    {
        return ( cadsrAPIUrl == null ) ? "" : cadsrAPIUrl;
    }

    public void setCadsrAPIUrl( String cadsrAPIUrl )
    {
        this.cadsrAPIUrl = cadsrAPIUrl;
    }

    public String getContextTest()
    {
        return contextTest;
    }

    public String getContextTraining()
    {
        return contextTraining;
    }

    public String getFormBuilderHelpUrl()
    {
        return ( formBuilderHelpUrl == null ) ? "/help" : formBuilderHelpUrl;
    }

    public void setFormBuilderHelpUrl( String formBuilderHelpUrl )
    {
        this.formBuilderHelpUrl = formBuilderHelpUrl;
    }

    public String getCdeBrowserHelpUrl()
    {
//		return (cdebrowserHelpUrl == null) ? "/help" : cdebrowserHelpUrl;
        return "https://wiki.nci.nih.gov/display/caDSR/CDE+Browser+User+Guide";
    }

//	public String getCdeBrowserReleaseNoteUrl()
//	{
////		return cdeBrowserReleaseNoteUrl;
//		return "https://wiki.nci.nih.gov/display/caDSR/CDE+Browser+4.0.4+Release+Notes";
//	}

    public void setCdeBrowserHelpUrl( String cdebrowserHelpUrl )
    {
        this.cdebrowserHelpUrl = cdebrowserHelpUrl;
    }

    public String getPcsUrl()
    {
        return pcsUrl;
    }

    public void setPcsUrl( String pcsUrl )
    {
        this.pcsUrl = pcsUrl;
    }


}
