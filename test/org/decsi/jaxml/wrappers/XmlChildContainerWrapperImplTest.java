package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlChild;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.testclasses.ContactBook;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlChildContainerWrapperImplTest {
    
    private XmlChildContainerWrapper wrapper;
    
    public XmlChildContainerWrapperImplTest() {
    }

    @Test
    public void testGetName() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertEquals("ns", wrapper.getNsPrefix());
        assertEquals("bela", wrapper.getName());
        assertEquals("ns:bela", wrapper.getQualifiedName());

        assertEquals("ns", wrapper.getElementWrapper().getNsPrefix());
        assertEquals("bela", wrapper.getElementWrapper().getName());
        assertEquals("ns:bela", wrapper.getElementWrapper().getQualifiedName());
    }
    
    @Test
    public void testNoKeyWrapperIfNotMap() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertEquals(null, wrapper.getQualifiedKeyName());
    }
    
    @Test
    public void testAccesptsCollections() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertEquals("ns:bela", wrapper.getQualifiedName());
        initWrapper(ArrayDummy.class);
        assertEquals("names", wrapper.getQualifiedName());
        initWrapper(MapDummy.class);
        assertEquals("names", wrapper.getQualifiedName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNotCollectionsException() throws Exception {
        initWrapper(NotCollectionDummy.class);
    }

    @Test
    public void testGetIndex() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertEquals(-1, wrapper.getIndex());
        
        initWrapper(IndexDummy.class);
        assertEquals(1, wrapper.getIndex());
        assertEquals(-1, wrapper.getElementWrapper().getIndex());
    }

    @Test
    public void testAttributeName() throws Exception {
        initWrapper(MapDummy.class);
        List<XmlAttributeWrapper> attributes = wrapper.getElementWrapper().getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("key", attributes.get(0).getQualifiedName());
        
        initWrapper(IndexDummy.class);
        attributes = wrapper.getElementWrapper().getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("name-key", attributes.get(0).getQualifiedName());
    }

    @Test
    public void testAddIfNull() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertTrue(wrapper.addIfNull());
        assertTrue(wrapper.getElementWrapper().addIfNull());
        
        initWrapper(AddIfNullDummy.class);
        assertFalse(wrapper.addIfNull());
        assertFalse(wrapper.getElementWrapper().addIfNull());
    }

    @Test
    public void testGetAttributes() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertEquals(0, wrapper.getAttributes().size());
        
        initWrapper(AttributeDummy.class);
        List<XmlAttributeWrapper> attributes = wrapper.getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("id", attributes.get(0).getQualifiedName());
        assertEquals("1", attributes.get(0).getPrimitiveValue(null));
        assertEquals("type", attributes.get(1).getQualifiedName());
        assertEquals("bela", attributes.get(1).getPrimitiveValue(null));
        
        attributes = wrapper.getElementWrapper().getAttributes();
        assertEquals(2, attributes.size());
        assertEquals("id", attributes.get(0).getQualifiedName());
        assertEquals("2", attributes.get(0).getPrimitiveValue(null));
        
        initWrapper(MapDummy.class);
        attributes = wrapper.getElementWrapper().getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("key", attributes.get(0).getQualifiedName());
    }
    
    @Test
    public void testContactBook() throws Exception {
        Field  field = ContactBook.class.getDeclaredField("contacts");
        wrapper = WrapperFactory.getChildContainerWrapper(field);
        List<XmlAttributeWrapper> attributes = wrapper.getElementWrapper().getAttributes();
        assertEquals(1, attributes.size());
        
        attributes = wrapper.getElementWrapper().getChildElementWrapper().getAttributes();
        assertEquals(1, attributes.size());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testOverridenKeyAttributeException() throws Exception {
        initWrapper(OverridenKeyAttributeDummy.class);
    }
    
    @Test
    public void testGetValueClass() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertEquals(List.class, wrapper.getValueClass());
        assertEquals(String.class, wrapper.getElementWrapper().getValueClass());
        
        initWrapper(ArrayDummy.class);
        assertEquals(String[].class, wrapper.getValueClass());
        assertEquals(String.class, wrapper.getElementWrapper().getValueClass());
        
        initWrapper(MapDummy.class);
        assertEquals(Map.class, wrapper.getValueClass());
        assertEquals(String.class, wrapper.getElementWrapper().getValueClass());
        assertEquals(String.class, wrapper.getKeyClass());
    
        initWrapper(ValueClassDummy.class);
        assertEquals(Map.class, wrapper.getValueClass());
        assertEquals(StringElementDummy.class, wrapper.getElementWrapper().getValueClass());
        assertEquals(StringDummy.class, wrapper.getKeyClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalElementTypeException() throws Exception {
        initWrapper(IllegalElementTypeDummy.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalKeyTypeException() throws Exception {
        initWrapper(IllegalKeyTypeDummy.class);
    }

    @Test
    public void testAddIfEmpty() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertTrue(wrapper.addIfEmpty());
        initWrapper(AddIfEmptyDummy.class);
        assertFalse(wrapper.addIfEmpty());
    }

    @Test
    public void testAddOnlyContainedElements() throws Exception {
        initWrapper(SimpleNameDummy.class);
        assertFalse(wrapper.addOnlyContainedElements());
        initWrapper(OnlyContainedElementsDummy.class);
        assertTrue(wrapper.addOnlyContainedElements());
    }
    
    private void initWrapper(Class c) throws Exception {
        Field f = c.getDeclaredField("name");
        wrapper = new XmlChildContainerWrapperImpl(f);
    }
    
    private static class SimpleNameDummy {
        @XmlChildContainer(name="bela", nsPrefix="ns", elementClass=String.class)
        private List<String> name;
    }
    
    private static class ArrayDummy {
        @XmlChildContainer(name="names", elementClass=String.class)
        private String[] name;
    }
    
    private static class MapDummy {
        @XmlChildContainer(name="names", 
                elementClass=String.class, keyClass=String.class)
        private Map<String, String> name;
    }
    
    private static class NotCollectionDummy {
        @XmlChildContainer(name="names", elementClass=String.class)
        private String name;
    }
    
    private static class IndexDummy {
        @XmlChildContainer(name="names", index=1, 
                elementClass=String.class, keyClass=String.class)
        @XmlChild(name="name", index=2)
        @XmlAttribute(name="name-key", index=3)
        private Map<String, String> name;
    }
    
    private static class AddIfNullDummy {
        @XmlChildContainer(name="names", addIfNull=false, 
                elementClass=String.class, keyClass=String.class)
        @XmlChild(name="name", addIfNull=false)
        @XmlAttribute(name="name-key", defaultValue="def-key")
        private Map<String, String> name;
    }
    
    private static class AttributeDummy {
        @XmlChildContainer(name="names", attributes={"id=1", "type=bela"}, 
                elementClass=String.class, keyClass=String.class)
        @XmlChild(name="name", addIfNull=false,
                attributes={"id=2"})
        private Map<StringDummy, StringDummy> name;
    }
    
    private static class ValueClassDummy {
        @XmlChildContainer(name="names", 
                elementClass=StringElementDummy.class, keyClass=StringDummy.class)
        private Map<StringDummy, StringElementDummy> name;
    }

    private static class OverridenKeyAttributeDummy {
        @XmlChildContainer(name="names", 
                elementClass=StringElementDummy.class, keyClass=StringDummy.class)
        @XmlChild(attributes={"key=1"})
        @XmlAttribute(name="key")
        private Map<StringDummy, StringElementDummy> name;
    }

    private static class StringDummy {
        @XmlStringValue
        private String name;
        StringDummy(String name) {
            this.name = name;
        }
    }
    
    @XmlElement(name="name")
    private static class StringElementDummy {}
    
    private static class IllegalElementTypeDummy {
        @XmlChildContainer(name="names", elementClass=Object.class)
        private List<Object> name;
    }
    
    private static class IllegalKeyTypeDummy {
        @XmlChildContainer(name="names",
                elementClass=String.class, keyClass=Object.class)
        private Map<Object, String> name;
    }
    
    private static class AddIfEmptyDummy {
        @XmlChildContainer(name="names", elementClass=String.class, addIfEmpty=false)
        private List<String> name;
    }
    
    private static class OnlyContainedElementsDummy {
        @XmlChildContainer(name="names", 
                elementClass=String.class, 
                addOnlyContainedElements=true)
        private List<String> name;
    }
}
