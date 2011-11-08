package org.decsi.jaxml.parsing.xmlparsing;

import java.util.List;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.decsi.jaxml.wrappers.WrapperFactory;
import java.util.Map;
import java.util.HashMap;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.xmlparsing.CollectionXmlContainerNodeTest.DummyElement;

/**
 *
 * @author Peter Decsi
 */
public class MapXmlContainerNodeTest {
    
    private ElementNode node;
    private AttributeImpl attributes = new AttributeImpl();
    
    public MapXmlContainerNodeTest() {
    }

    @Test
    public void testCreateChildNode() throws Exception {
        initNode(Dummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("values", null);
        attributes.clear().putValue("key", "2");
        node = node.createChildNode("value", attributes);
        node.setString("geza");
        node.endElement();
        node = node.getParent();
        attributes.clear().putValue("key", "4");
        node = node.createChildNode("value", attributes);
        node.setString("bela");
        node.endElement();
        node = node.getParent();
        node.endElement();
        node = node.getParent();
        node.endElement();
        
        Dummy dummy = (Dummy) node.getInstance();
        Map<Integer, DummyElement> values = dummy.values;
        assertEquals(2, values.size());
        assertEquals("geza", values.get(2).getName());
        assertEquals("bela", values.get(4).getName());
    }

    @Test
    public void testCreateChildNode_StringKey() throws Exception {
        initNode(StringKeyDummy.class);
        node = node.createChildNode("dummy", null);
        node = node.createChildNode("values", null);
        attributes.clear().putValue("key", "key-1");
        node = node.createChildNode("value", attributes);
        node.setString("geza");
        node.endElement();
        node = node.getParent();
        attributes.clear().putValue("key", "key-4");
        node = node.createChildNode("value", attributes);
        node.setString("bela");
        node.endElement();
        node = node.getParent();
        node.endElement();
        node = node.getParent();
        node.endElement();
        
        StringKeyDummy dummy = (StringKeyDummy) node.getInstance();
        Map<StringKey, DummyElement> values = dummy.values;
        assertEquals(2, values.size());
        assertEquals("geza", values.get(new StringKey("key-1")).getName());
        assertEquals("bela", values.get(new StringKey("key-4")).getName());
    }
    
    
    private void initNode(Class c) throws Exception {
        XmlElementWrapper wrapper = WrapperFactory.getElementWrapper(c);
        PrimitiveParser parser = new PrimitiveParser();
        node = new XmlElementNode(node, parser, wrapper);
    }
    
    @XmlElement(name="dummy")
    private static class Dummy {
        
        @XmlChildContainer(name="values", elementClass=DummyElement.class, keyClass=Integer.class)
        @XmlAttribute(name="key")
        private Map<Integer, DummyElement> values = new HashMap<Integer, DummyElement>();
    }
    
    @XmlElement(name="dummy")
    private static class StringKeyDummy {
        
        @XmlChildContainer(name="values", elementClass=DummyElement.class, keyClass=StringKey.class)
        @XmlAttribute(name="key")
        private Map<StringKey, DummyElement> values = new HashMap<StringKey, DummyElement>();
    }
    
    private static class StringKey {
        
        @XmlStringValue
        private String value;

        StringKey(String value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(Object o) {
            if(o==null || !(o instanceof StringKey)) return false;
            StringKey k = (StringKey) o;
            return value==null? k.value==null : value.equalsIgnoreCase(value);
        }
        
        @Override
        public int hashCode() {
            return value==null? 0 : value.toLowerCase().hashCode();
        }
    }
    
    private static class AttributeImpl implements Attributes {
        
        private HashMap<String, String> values = new HashMap<String, String>();
        private List<String> names = new ArrayList<String>();
        
        AttributeImpl putValue(String qName, String value) {
            names.add(qName);
            values.put(qName, value);
            return this;
        }
        
        AttributeImpl clear() {
            names.clear();
            values.clear();
            return this;
        }
        
        @Override
        public int getLength() {
            return names.size();
        }

        @Override
        public String getURI(int index) {
            return "";
        }

        @Override
        public String getLocalName(int index) {
            return "";
        }

        @Override
        public String getQName(int index) {
            return names.get(index);
        }

        @Override
        public String getType(int index) {
            return "CDATA";
        }

        @Override
        public String getType(String uri, String localName) {
            return "CDATA";
        }

        @Override
        public String getType(String qName) {
            return "CDATA";
        }

        @Override
        public String getValue(int index) {
            String name = names.get(index);
            return values.get(name);
        }

        @Override
        public int getIndex(String uri, String localName) {
            return -1;
        }

        @Override
        public int getIndex(String qName) {
            return names.indexOf(qName);
        }

        @Override
        public String getValue(String uri, String localName) {
            return null;
        }

        @Override
        public String getValue(String qName) {
            return values.get(qName);
        }
    
    }
    
}
