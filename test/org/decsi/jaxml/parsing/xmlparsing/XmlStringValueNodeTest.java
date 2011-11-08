package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlStringValueNodeTest {
    
    private XmlStringValueNode node;
    
    public XmlStringValueNodeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSetString_DummyString() throws Exception {
        initNode(DummyString.class);
        node.setString("Bela");
        DummyString instance = (DummyString) node.getInstance();
        assertEquals("Bela", instance.name);
        
        initNode(DummyString.class);
        node.setString(null);
        instance = (DummyString) node.getInstance();
        assertEquals(null, instance.name);
    }

    @Test
    public void testSetString_DummyDummyString() throws Exception {
        initNode(DummyDummyString.class);
        node.setString("Bela");
        DummyDummyString instance = (DummyDummyString) node.getInstance();
        assertEquals("Bela", instance.dummy.name);
        
        initNode(DummyDummyString.class);
        node.setString(null);
        instance = (DummyDummyString) node.getInstance();
        assertEquals(null, instance.dummy.name);
    }
    
    private void initNode(Class c) {
        XmlStringValueWrapper wrapper = WrapperFactory.getStringValueWrapper(c);
        node = new XmlStringValueNode(null, new PrimitiveParser(), c, wrapper);
    }
    
    private static class DummyString {
        
        @XmlStringValue
        private String name;
        
        private DummyString(String name) {
            this.name = name;
        }
    }
    
    private static class DummyDummyString {
        
        private static DummyDummyString getIsntance(DummyString dummy) {
            DummyDummyString d = new DummyDummyString();
            d.dummy =dummy;
            return d;
        }
        
        @XmlStringValue
        private DummyString dummy;
        
    }
}
