package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.xmlparsing.CollectionXmlContainerNodeTest.DummyElement;

/**
 *
 * @author Peter Decsi
 */
public class ArrayXmlContainerNodeTest {
    
    private ElementNode node;
    
    public ArrayXmlContainerNodeTest() {
    }

    @Test
    public void testFillObject() throws Exception {
        initNode(Dummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("values", null);
        node = node.createChildNode("value", null);
        node.setString("geza");
        node.endElement();
        node = node.getParent();
        node = node.createChildNode("value", null);
        node.setString("bela");
        node.endElement();
        node = node.getParent();
        node.endElement();
        node = node.getParent();
        node.endElement();
        
        Dummy dummy = (Dummy) node.getInstance();
        DummyElement[] values = dummy.values;
        assertEquals(2, values.length);
        assertEquals("geza", values[0].getName());
        assertEquals("bela", values[1].getName());
    }

    @Test
    public void testFillObject_IntDummy() throws Exception {
        initNode(IntegerDummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("values", null);
        node = node.createChildNode("value", null);
        node.setString("12");
        node.endElement();
        node = node.getParent();
        node = node.createChildNode("value", null);
        node.setString("31");
        node.endElement();
        node = node.getParent();
        node.endElement();
        node = node.getParent();
        node.endElement();
        
        IntegerDummy dummy = (IntegerDummy) node.getInstance();
        int[] values = dummy.values;
        assertEquals(2, values.length);
        assertEquals(12, values[0]);
        assertEquals(31, values[1]);
    }

    @Test
    public void testFillObject_NoContainerDummy() throws Exception {
        initNode(NoContainerDummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("value", null);
        node.setString("12");
        node.endElement();
        node = node.getParent();
        node = node.createChildNode("value", null);
        node.setString("31");
        node.endElement();
        node = node.getParent();
        node.endElement();
        
        NoContainerDummy dummy = (NoContainerDummy) node.getInstance();
        int[] values = dummy.values;
        assertEquals(2, values.length);
        assertEquals(12, values[0]);
        assertEquals(31, values[1]);
    }
    
    private void initNode(Class c) throws Exception {
        XmlElementWrapper wrapper = WrapperFactory.getElementWrapper(c);
        PrimitiveParser parser = new PrimitiveParser();
        node = new XmlElementNode(node, parser, wrapper);
    }
    
    @XmlElement(name="dummy")
    private static class Dummy {
        
        @XmlChildContainer(name="values", elementClass=DummyElement.class)
        private DummyElement[] values;
    }
    
    
    @XmlElement(name="dummy")
    private static class IntegerDummy {
        
        @XmlChildContainer(name="values", elementClass=int.class)
        @XmlChild(name="value")
        private int[] values;
    }    
    
    @XmlElement(name="dummy")
    private static class NoContainerDummy {
        
        @XmlChildContainer(name="values", elementClass=int.class, addOnlyContainedElements=true)
        @XmlChild(name="value")
        private int[] values;
    }    
    
}
