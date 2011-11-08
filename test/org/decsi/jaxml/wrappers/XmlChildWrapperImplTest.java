package org.decsi.jaxml.wrappers;

import java.util.List;
import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlChildWrapperImplTest {
    
    private XmlChildWrapper wrapper;
    
    public XmlChildWrapperImplTest() {
    }

    @Test
    public void testGetIndex() throws Exception {
        initWrapper(IndexDummy.class);
        assertEquals(2, wrapper.getIndex());
    }

    @Test
    public void testGetValueClass() throws Exception {
        initWrapper(IndexDummy.class);
        assertEquals(String.class, wrapper.getValueClass());
        
        initWrapper(NotOverridenDummy.class);
        assertEquals(DummyElement.class, wrapper.getValueClass());
    }

    @Test
    public void testGetNamespaces() throws Exception {
        initWrapper(NamespaceDummy.class);
        List<XmlNamespaceWrapper> nss = wrapper.getNamespaces();
        assertEquals(1, nss.size());
        assertFalse(nss.get(0).isDefault());
        assertEquals("bela", nss.get(0).getPrefix());
        assertEquals("www.bela.org", nss.get(0).getURI());
    }
    
    @Test
    public void testDoNotOverrideElement() throws Exception {
        initWrapper(NotOverridenDummy.class);
        assertEquals("dummy", wrapper.getQualifiedName());
        List<XmlAttributeWrapper> attributes = wrapper.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("id", attributes.get(0).getQualifiedName());
        assertEquals("name", attributes.get(1).getQualifiedName());
        assertEquals("geza", attributes.get(1).getPrimitiveValue(new DummyElement()));
        
        
        List<XmlNamespaceWrapper> nss = wrapper.getNamespaces();
        assertEquals(1, nss.size());
        assertEquals("bela", nss.get(0).getPrefix());
        assertEquals("www.bela.org", nss.get(0).getURI());        
    }

    @Test
    public void testOverrideElement() throws Exception {
        initWrapper(OverridenDummy.class);
        assertEquals("name", wrapper.getQualifiedName());
        List<XmlAttributeWrapper> attributes = wrapper.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("name", attributes.get(0).getQualifiedName());
        assertEquals("bela", attributes.get(0).getPrimitiveValue(null));
        assertEquals("id", attributes.get(1).getQualifiedName());
        
        attributes = wrapper.getChildElementWrapper().getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("id", attributes.get(0).getQualifiedName());
        assertEquals("name", attributes.get(1).getQualifiedName());
        assertEquals("geza", attributes.get(1).getPrimitiveValue(new DummyElement()));
        
        List<XmlNamespaceWrapper> nss = wrapper.getNamespaces();
        assertEquals(1, nss.size());
        assertEquals("bela", nss.get(0).getPrefix());
        assertEquals("www.geza.org", nss.get(0).getURI());
        
        nss = wrapper.getChildElementWrapper().getNamespaces();
        assertEquals(1, nss.size());
        assertEquals("bela", nss.get(0).getPrefix());
        assertEquals("www.bela.org", nss.get(0).getURI());
    }

    @Test
    public void testAddIfNull() throws Exception {
        initWrapper(IndexDummy.class);
        assertTrue(wrapper.addIfNull());
        
        initWrapper(NotAddedIfNullDummy.class);
        assertFalse(wrapper.addIfNull());
    }

    @Test
    public void testGetChildElementWrapper() throws Exception {
        initWrapper(OverridenDummy.class);
        assertEquals("name", wrapper.getQualifiedName());
        assertEquals(null, wrapper.getStringWrapper());
        
        XmlElementWrapper child = wrapper.getChildElementWrapper();
        assertEquals("dummy", child.getQualifiedName());
    }

    @Test
    public void testGetStringWrapper() throws Exception {
        initWrapper(IndexDummy.class);
        assertEquals(null, wrapper.getChildElementWrapper());
        XmlStringValueWrapper child = wrapper.getStringWrapper();
        assertTrue(child.trimInput());
    }

    @Test
    public void testGetAnnotatedAttributes() throws Exception {
        initWrapper(AttributedDummy.class);
        List<XmlAttributeWrapper> attributes = wrapper.getAttributes();
        
        assertEquals(1, attributes.size());
        XmlAttributeWrapper attribute = attributes.get(0);
        assertEquals("id", attribute.getQualifiedName());
        assertEquals("1", attribute.getPrimitiveValue(null));
    }
    
    private void initWrapper(Class c) throws Exception {
        Field f = c.getDeclaredField("name");
        wrapper = new XmlChildWrapperImpl(f);
    }
    
    private static class IndexDummy {
        @XmlChild(name="name", index=2)
        private String name = "bela";
    }
    
    private static class NamespaceDummy {
        @XmlChild(name="name", namespaces={"bela=www.bela.org"})
        private String name = "bela";
    }
    
    private static class NotOverridenDummy {
        @XmlChild(name="name")
        private DummyElement name;
    }
    
    private static class OverridenDummy {
        @XmlChild(name="name", overrideElement=true, 
                attributes={"name=bela"},
                namespaces={"bela=www.geza.org"})
        private DummyElement name;
    }
    
    @XmlElement(name="dummy", namespaces={"bela=www.bela.org"})
    private static class DummyElement {
        @XmlAttribute(name="name")
        private String name = "geza";
        
        @XmlAttribute(name="id")
        private int id = 1;
    }
    
    private static class NotAddedIfNullDummy {
        @XmlChild(name="name", addIfNull=false)
        private String name = "bela";
    }
    
    private static class CompositeStringDummy {
        @XmlChild(name="name")
        private StringDummy name;
    }
    
    private static class StringDummy {
        @XmlStringValue
        private String name;

        StringDummy(String name) {
            this.name = name;
        }
    }
    
    private static class AttributedDummy {
        @XmlChild(name="name", attributes={"id=1"})
        private String name = "bela";
    }
}
