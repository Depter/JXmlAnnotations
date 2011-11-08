package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.parsing.xmlparsing.XmlStringChildNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlStringChildNodeTest {
    
    private XmlStringChildNode node;
    
    public XmlStringChildNodeTest() {
    }

    @Test
    public void testCreateChildNode() throws Exception {
        initNode(StringContentDummy.class);
        assertEquals(node, node.createChildNode(null, null));
        initNode(DummyDummy.class);
        assertEquals(node, node.createChildNode(null, null));
    }

    @Test
    public void testSetString() throws Exception {
        initNode(StringContentDummy.class);
        node.setString("geza");
        String value = (String) node.getInstance();
        assertEquals("geza", value);
        
        initNode(DummyDummy.class);
        node.setString("bela");
        DummyString dummy = (DummyString) node.getInstance();
        assertEquals("bela", dummy.name);
    }

    @Test
    public void testAcceptsName() throws Exception {
        initNode(StringContentDummy.class);
        assertTrue(node.acceptsName("ns:name"));
        assertFalse(node.acceptsName("name"));
    }
    
    
    private void initNode(Class c) throws Exception {
        Field field = c.getDeclaredField("field");
        XmlChildWrapper wrapper = WrapperFactory.getChildWrapper(field);
        node = new XmlStringChildNode(null, new PrimitiveParser(), wrapper);
    }
    
    private static class StringContentDummy {
        
        @XmlChild(name="name", nsPrefix="ns")
        private String field;
    }
    
    private static class DummyDummy {
        
        @XmlChild(name="bela")
        private DummyString field;
    
    }
    
    private static class DummyString {
        
        @XmlStringValue
        private String name;

        DummyString(String name) {
            this.name = name;
        }
    }
}
