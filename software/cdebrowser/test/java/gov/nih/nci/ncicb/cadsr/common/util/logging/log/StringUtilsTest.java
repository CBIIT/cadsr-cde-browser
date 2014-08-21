package gov.nih.nci.ncicb.cadsr.common.util.logging.log;

import static org.junit.Assert.*;
import gov.nih.nci.ncicb.cadsr.common.util.StringUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StringUtilsTest {
	
	@Before
    public void setUp() {
        System.out.println("@Before - setUp");
    }
 
    @After
    public void tearDown() {
        System.out.println("@After - tearDown");
    }
    
    @Test
    public void testConversionOfSpecialCharsToUnicodeCharsForExcelDownload() {
		String specialChars = "&#8322; &#945; &#946; &#947; &#948; &#178; &#176; &#9702; &#181; &#955; &#411; &#8805; &#8804; &#177; &#954; &#8495; &#922;";
		
		String unicodeChars = StringUtils.updateDataForSpecialCharacters(specialChars);
		
		String expectedUnicodeChars = "\u2082 \u03B1 \u03B2 \u03B3 \u03B4 \u00B2 \u00B0 \u00B0 \u00B5 \u03BB \u03BB \u2265 \u2264 \u00B1 \u03BA \u212F \u03BA";
		
		assertTrue(unicodeChars.equals(expectedUnicodeChars));
	}
    
    @Test
    public void testConversionOfSpecialCharsToUnicodeCharsForXmlDownload() {
		String specialChars = "&amp;#8322; &amp;#945; &amp;#946; &amp;#947; &amp;#948; &amp;#178; &amp;#176; &amp;#9702; &amp;#181; &amp;#955; &amp;#411; &amp;#8805; &amp;#8804; &amp;#177; &amp;#954; &amp;#8495; &amp;#922; &lt; &gt; &amp;#8225;";
		
		String unicodeChars = StringUtils.updateXMLDataForSpecialCharacters(specialChars);
		
		String expectedUnicodeChars = "\u2082 \u03B1 \u03B2 \u03B3 \u03B4 \u00B2 \u00B0 \u00B0 \u00B5 \u03BB \u03BB \u2265 \u2264 \u00B1 \u03BA \u212F \u03BA &#60; &#62; \u2021";
		
		assertTrue(unicodeChars.equals(expectedUnicodeChars));
	}

}
