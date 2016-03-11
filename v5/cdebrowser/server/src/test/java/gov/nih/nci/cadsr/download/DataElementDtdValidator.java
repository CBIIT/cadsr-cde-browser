package gov.nih.nci.cadsr.download;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * This is a class for manual tests to validate and parse XML document.
 * @author asafievan
 *
 */
public class DataElementDtdValidator {
	//This is a file which contains DTD XML directive
	public static String strFileName = "DataElements_new_dtd.xml";
	public static String strFileName2 = "DataElements_new_dtd2.xml";
	public static String strFileName1 = "DataElements_new.xml";

    public static void main(String[] args) {
    	parseXml(strFileName, true);
    	parseXml(strFileName2, true);
    	parseXml(strFileName1, false);
    }
    public static void parseXml(String strFileName, boolean validate) {
    	FileInputStream fis = null;
       	try {
	       	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    	dbf.setValidating(validate);
 			DocumentBuilder builder = dbf.newDocumentBuilder();
 			
 			builder.setEntityResolver(new EntityResolver() {
				@Override
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					 if (systemId.contains("DataElementList.dtd")) {
						 String fileName = ClassLoader.getSystemResource("DataElementList.dtd").getFile();
						 return new InputSource(new FileInputStream(fileName));
					 } else 
					 {
						 return null;
					 }
				}
 			});
 			
 			builder.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					 System.out.println("warning for document: " + exception);
					 throw exception;
				}

				@Override
				public void error(SAXParseException exception) throws SAXException {
					 System.out.println("error: " + exception);
					 throw exception;
				}

				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					 System.out.println("fatal: " + exception);
					 throw exception;
				}
 				
 			});
 			
 			String fileName1 = ClassLoader.getSystemResource(strFileName).getFile();
 			
 			fis = new FileInputStream(fileName1);
 			//Here we test the document has a right format
 			builder.parse(fis);
 			
 			System.out.println("Document is validated and parsed: " + strFileName);
		} 
       	catch (Exception e) {
       		//we come here is the document with DTD is not validated or not parsed
			e.printStackTrace();
		}
    	finally {
    		if (fis != null) {
    			try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
}
