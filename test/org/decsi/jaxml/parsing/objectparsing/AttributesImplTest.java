package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.annotation.XmlElement;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class AttributesImplTest {
    
    private AttributesImpl attributes;
    
    public AttributesImplTest() {
    }

    @Before
    public void setUp() {
        NamespaceManager nsManager = getNsManager();
        PrimitiveParser pParser = new PrimitiveParser();
        List<XmlAttributeWrapper> wrappers = getWrappers();
        attributes = new AttributesImpl(nsManager, pParser, wrappers);
    }
    
    private NamespaceManager getNsManager() {
        NamespaceManager nsManager = NamespaceManagerTest.getNamespaceManager();
        nsManager.addNamespace(NamespaceManagerTest.getNsWrapper("b", "www.bela.org"));
        return nsManager;
    }
    
    private List<XmlAttributeWrapper> getWrappers() {
        XmlElementWrapper wrapper = 
                WrapperFactory.getElementWrapper(Dummy.class);
        return wrapper.getAttributes();
    }

    @Test
    public void testGetLength() {
        assertEquals(3, attributes.getLength());
        attributes.setInstance(new Dummy());
        assertEquals(2, attributes.getLength());
    }

    @Test
    public void testGetIndex_String_String() {
        assertEquals(0, attributes.getIndex("www.bela.org", "name"));
        assertEquals(1, attributes.getIndex(null, "id"));
        assertEquals(1, attributes.getIndex("", "id"));
    }

    @Test
    public void testGetIndex_String() {
        assertEquals(0, attributes.getIndex("b:name"));
        assertEquals(1, attributes.getIndex("id"));
    }

    @Test
    public void testGetURI() {
        assertEquals("www.bela.org", attributes.getURI(0));
        assertEquals("", attributes.getURI(1));
    }

    @Test
    public void testGetLocalName() {
        assertEquals("name", attributes.getLocalName(0));
        assertEquals("id", attributes.getLocalName(1));
    }

    @Test
    public void testGetQName() {
        assertEquals("b:name", attributes.getQName(0));
        assertEquals("id", attributes.getQName(1));
    }

    @Test
    public void testGetType_int() {
        assertEquals(XmlAttribute.Type.CDATA.name(), attributes.getType(0));
        assertEquals(XmlAttribute.Type.ID.name(), attributes.getType(1));
    }

    @Test
    public void testGetType_String_String() {
        assertEquals(XmlAttribute.Type.CDATA.name(), attributes.getType("www.bela.org", "name"));
        assertEquals(XmlAttribute.Type.ID.name(), attributes.getType("", "id"));
        assertEquals(XmlAttribute.Type.ID.name(), attributes.getType(null, "id"));
    }

    @Test
    public void testGetType_String() {
        assertEquals(XmlAttribute.Type.CDATA.name(), attributes.getType("b:name"));
        assertEquals(XmlAttribute.Type.ID.name(), attributes.getType("id"));
    }

    @Test
    public void testGetValue_int() {
        attributes.setInstance(new Dummy());
        assertEquals("bela", attributes.getValue(0));
        assertEquals("1", attributes.getValue(1));
    }

    @Test
    public void testGetValue_String_String() {
        attributes.setInstance(new Dummy());
        assertEquals("bela", attributes.getValue("www.bela.org", "name"));
        assertEquals("1", attributes.getValue("", "id"));
        assertEquals("1", attributes.getValue(null, "id"));
    }

    @Test
    public void testGetValue_String() {
        attributes.setInstance(new Dummy());
        assertEquals("bela", attributes.getValue("b:name"));
        assertEquals("1", attributes.getValue("id"));
    }
    
    @XmlElement(name="dummy")
    static class Dummy {
        
        @XmlAttribute(name="name", nsPrefix="b", index=0)
        private String name = "bela";
        
        @XmlAttribute(name="id", index=1, type= XmlAttribute.Type.ID)
        private int id = 1;
        
        @XmlAttribute(name="value", defaultValue="geza", addIfDefault=false)
        private String value = "geza";
    }
}
