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
public class AttributeTest {
    
    private final static String NAME = "myname";
    private Attribute attribute;
    
    public AttributeTest() {
    }

    @Before
    public void setUp() {
        attribute = new Attribute(NAME);
    }

    @Test
    public void testFromContent() {
        String txtContent = "ns:name=\"value\"";
        attribute = Attribute.fromContent(txtContent);
        assertEquals("ns", attribute.getNsPrefix());
        assertEquals("name", attribute.getName());
        assertEquals("value", attribute.getValue());

        txtContent = "name=\"value\"";
        attribute = Attribute.fromContent(txtContent);
        assertEquals(null, attribute.getNsPrefix());
        assertEquals("name", attribute.getName());
        assertEquals("value", attribute.getValue());

        txtContent = "name=\"\"";
        attribute = Attribute.fromContent(txtContent);
        assertEquals(null, attribute.getNsPrefix());
        assertEquals("name", attribute.getName());
        assertEquals(null, attribute.getValue());
    }

    @Test
    public void testGetValue() {
        assertEquals(null, attribute.getValue());
        
        attribute.setValue("bela");
        assertEquals("bela", attribute.getValue());
        
        attribute.setValue("  ");
        assertEquals("  ", attribute.getValue());
        
        attribute.setValue(null);
        assertEquals(null, attribute.getValue());
    }


    @Test
    public void testGetTextContent() {
        assertEquals(NAME+"=\"\"", attribute.getTextContent());
        
        String value = "bela";
        attribute.setValue(value);
        assertEquals(NAME+"=\""+value+"\"", attribute.getTextContent());
        
        String prefix = "dp";
        attribute.setNsPrefix(prefix);
        assertEquals(prefix+":"+NAME+"=\""+value+"\"", attribute.getTextContent());
        
    }
}
