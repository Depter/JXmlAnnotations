package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlChild;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlElementWrapperImplTest {
    
    private XmlElementWrapper wrapper;
    
    public XmlElementWrapperImplTest() {
    }

    @Test
    public void testGetName() {
        wrapper = new XmlElementWrapperImpl(NameDummy.class);
        assertEquals("bela", wrapper.getName());
        assertEquals("geza", wrapper.getNsPrefix());
        assertEquals("geza:bela", wrapper.getQualifiedName());
    }
    
    @Test
    public void testNamespaces() {
        wrapper = new XmlElementWrapperImpl(NameDummy.class);
        List<XmlNamespaceWrapper> wrappers = wrapper.getNamespaces();
        assertTrue(wrappers.isEmpty());
        
        wrapper = new XmlElementWrapperImpl(NamespaceDummy.class);
        wrappers = wrapper.getNamespaces();
        assertEquals(2, wrappers.size());
        assertFalse(wrappers.get(0).isDefault());
        assertEquals("bela", wrappers.get(0).getPrefix());
        assertEquals("www.bela.org", wrappers.get(0).getURI());
        assertTrue(wrappers.get(1).isDefault());
        assertEquals(null, wrappers.get(1).getPrefix());
        assertEquals("www.default.org", wrappers.get(1).getURI());
    }

    @Test
    public void testGetAttributes() {
        wrapper = new XmlElementWrapperImpl(NameDummy.class);
        assertTrue(wrapper.getAttributes().isEmpty());
        
        wrapper = new XmlElementWrapperImpl(AttributeDummy.class);
        List<XmlAttributeWrapper> attributes = wrapper.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("bela", attributes.get(0).getQualifiedName());
        assertEquals("id", attributes.get(1).getQualifiedName());
    }

    @Test
    public void testGetChildren() {
        wrapper = new XmlElementWrapperImpl(NameDummy.class);
        assertTrue(wrapper.getChildren().isEmpty());
        
        wrapper = new XmlElementWrapperImpl(ChildrenDummy.class);
        List<ChildWrapper> children = wrapper.getChildren();
        assertEquals(2, children.size());
        assertEquals("bela", children.get(0).getQualifiedName());
        assertEquals("id", children.get(1).getQualifiedName());
        
        XmlChildWrapper child = (XmlChildWrapper) children.get(0);
        wrapper = child.getChildElementWrapper();
        assertEquals("geza:bela", wrapper.getQualifiedName());
    }

    @Test
    public void testGetStringWrapper() {
        wrapper = new XmlElementWrapperImpl(NameDummy.class);
        assertEquals(null, wrapper.getStringWrapper());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testChildrenAndString() {
        new XmlElementWrapperImpl(ChildrenAndStringDummy.class);
    }
    
    @XmlElement(name="bela", nsPrefix="geza")
    private static class NameDummy{}
    
    @XmlElement(name="bela", namespaces={"bela=www.bela.org", "www.default.org"})
    private static class NamespaceDummy {}
    
    @XmlElement(name="bela", nsPrefix="geza")
    private static class AttributeDummy {
        
        @XmlAttribute(name="id")
        private int id = 1;
        
        @XmlAttribute(name="bela")
        private String name = "bela";
    }
    
    
    @XmlElement(name="bela", nsPrefix="geza")
    private static class ChildrenDummy {
        
        @XmlChild(name="id")
        private int id = 1;
        
        @XmlChild(name="bela", overrideElement=true)
        private StringDummy name;
    }    
    
    @XmlElement(name="bela", nsPrefix="geza")
    private static class StringDummy {
        @XmlStringValue
        private String name = "bela";

        public StringDummy(String name) {this.name = name;}
        public StringDummy() {}
    }
    
    @XmlElement(name="bela", nsPrefix="geza")
    private static class ChildrenAndStringDummy {
        
        @XmlChild(name="id")
        private int id = 1;
        
        @XmlStringValue
        private String name = "bela";

        public ChildrenAndStringDummy(String name) {this.name = name;}
    }
}
