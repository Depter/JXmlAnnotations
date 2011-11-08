package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.wrappers.XmlElementWrapper;
import java.util.List;
import java.util.ArrayList;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class CollectionXmlContainerNodeTest {
    
    private ElementNode node;
    
    public CollectionXmlContainerNodeTest() {
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
        List<DummyElement> values = dummy.values;
        assertEquals(2, values.size());
        assertEquals("geza", values.get(0).name);
        assertEquals("bela", values.get(1).name);
    }

    @Test
    public void testFillObject_NoContainerElement() throws Exception {
        initNode(NoContainerAddedDummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("value", null);
        node.setString("geza");
        node.endElement();
        node = node.getParent();
        node = node.createChildNode("value", null);
        node.setString("bela");
        node.endElement();
        node = node.getParent();
        node.endElement();
        
        NoContainerAddedDummy dummy = (NoContainerAddedDummy) node.getInstance();
        List<DummyElement> values = dummy.values;
        assertEquals(2, values.size());
        assertEquals("geza", values.get(0).name);
        assertEquals("bela", values.get(1).name);
    }

    @Test(expected=SAXException.class)
    public void testFillObject_NotInitializedContainer() throws Exception {
        initNode(NotInitializedDummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("value", null);
        node.setString("geza");
        node.endElement();
        node = node.getParent();
        node = node.createChildNode("value", null);
        node.setString("bela");
        node.endElement();
        node = node.getParent();
        node.endElement();
    }
    
    private void initNode(Class c) throws Exception {
        XmlElementWrapper wrapper = WrapperFactory.getElementWrapper(c);
        PrimitiveParser parser = new PrimitiveParser();
        node = new XmlElementNode(node, parser, wrapper);
    }
    
    @XmlElement(name="dummy")
    private static class Dummy {
        
        @XmlChildContainer(name="values", elementClass=DummyElement.class)
        private List<DummyElement> values = new ArrayList<DummyElement>();
    }
    
    @XmlElement(name="value")
    static class DummyElement {
    
        @XmlStringValue
        private String name;

        DummyElement() {
        }

        DummyElement(String name) {
            this.name = name;
        }
        
        String getName() {
            return name;
        }
    }
    
    @XmlElement(name="dummy")
    private static class NoContainerAddedDummy {
        
        @XmlChildContainer(name="values", elementClass=DummyElement.class, addOnlyContainedElements=true)
        private List<DummyElement> values = new ArrayList<DummyElement>();
    }
    
    
    @XmlElement(name="dummy")
    private static class NotInitializedDummy {
        
        @XmlChildContainer(name="values", elementClass=DummyElement.class, addOnlyContainedElements=true)
        private List<DummyElement> values;
    }
}
