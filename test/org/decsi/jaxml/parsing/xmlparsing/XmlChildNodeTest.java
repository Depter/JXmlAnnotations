package org.decsi.jaxml.parsing.xmlparsing;

import org.junit.Before;
import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlChildNodeTest {
    
    private XmlChildNode node;
    
    public XmlChildNodeTest() {
    }
    
    @Before
    public void setUp() throws Exception {
        Field field = Dummy.class.getDeclaredField("name");
        XmlChildWrapper wrapper = WrapperFactory.getChildWrapper(field);
        node = new XmlChildNode(null, new PrimitiveParser(), wrapper);
    }

    @Test
    public void testCreateChildNode() throws Exception {
        ElementNode child = node.createChildNode("name", null);
        child.createChildNode("name", null).setString("bela");
        child.endElement();
        ValueDummy value = (ValueDummy) node.getInstance();
        assertEquals("bela", value.name);
    }

    @Test(expected=SAXException.class)
    public void testSetString() throws Exception {
        node.setString("bela");
    }

    @Test
    public void testAcceptsName() {
        assertTrue(node.acceptsName("name"));
        assertFalse(node.acceptsName("name-element"));
    }
    
    @XmlElement(name="dummy")
    private static class Dummy {
        
        @XmlChild(name="name", overrideElement=true)
        private ValueDummy name;
    }
    
    @XmlElement(name="name-element")
    private static class ValueDummy {
        
        @XmlChild(name="name")
        private String name;
    }
}
