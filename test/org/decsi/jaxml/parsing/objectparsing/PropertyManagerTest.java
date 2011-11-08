package org.decsi.jaxml.parsing.objectparsing;

import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXNotRecognizedException;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class PropertyManagerTest {
    
    private PropertyManager properties;
    
    public PropertyManagerTest() {
    }

    @Before
    public void setUp() {
        properties = new PropertyManager();
    }

    @Test
    public void testGetProperty() throws Exception {
        String name = SAXProperty.NAMESPACE_SEPARATOR.getPropertyName();
        String separator = (String) properties.getProperty(name);
        assertEquals(":", separator);
    }

    @Test
    public void testSetProperty_NotRecognized() throws Exception {
        String name = SAXProperty.NAMESPACE_SEPARATOR.getPropertyName();
        properties.setProperty(name, "-");
        assertEquals("-", properties.getProperty(name));
    }

    @Test(expected=SAXNotSupportedException.class)
    public void testSetProperty_NotSupported() throws Exception {
        String name = "bela";
        properties.setProperty(name, "-");
    }    
}
