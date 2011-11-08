package org.decsi.jaxml.xml;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class ContentTest {
    
    private Content content;
    
    public ContentTest() {
    }

    @Before
    public void setUp() {
        content = new Content();
    }

    @Test
    public void testSetTextContent() {
        assertEquals(null, content.getTextContent());
        content.setTextContent("bela");
        assertEquals("bela", content.getTextContent());
        content.setTextContent(null);
        assertEquals(null, content.getTextContent());
    }

    @Test
    public void testSetParent() {
        assertEquals(null, content.getParent());

        Element expected = new Element("bela", null);
        content.setParent(expected);
        assertEquals(expected, content.getParent());
        
        content.setParent(null);
        assertEquals(null, content.getParent());
    }

}
