package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import org.junit.Before;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlAttributeWrapperImplTest {
    
    private XmlAttributeWrapper wrapper;
    
    public XmlAttributeWrapperImplTest() {
    }

    @Before
    public void setUp() throws Exception{
        wrapper = getWrapper(Dummy.class);
    }

    private XmlAttributeWrapper getWrapper(Class c) throws NoSuchFieldException {
        Field field = c.getDeclaredField("name");
        return new XmlAttributeWrapperImpl(field);
    }
    
    @Test
    public void testAddIfDefault() throws NoSuchFieldException {
        assertFalse(wrapper.addIfDefault());
        
        wrapper = getWrapper(AddIfDefaultDummy.class);
        assertTrue(wrapper.addIfDefault());
    }

    @Test
    public void testGetDefaultValue() throws NoSuchFieldException {
        assertEquals(null, wrapper.getDefaultValue());
        
        wrapper = getWrapper(EmptyDefaultDummy.class);
        assertEquals("  ", wrapper.getDefaultValue());
        
        wrapper = getWrapper(DefaultValueDummy.class);
        assertEquals("bela", wrapper.getDefaultValue());
    }

    @Test
    public void testGetIndex() throws NoSuchFieldException {
        assertEquals(-1, wrapper.getIndex());
        
        wrapper = getWrapper(IndexDummy.class);
        assertEquals(2, wrapper.getIndex());
    }

    @Test
    public void testChildWrapper() throws Exception {
        Dummy d = new Dummy();
        d.name = new NameDummy("bela");
        assertEquals("bela", wrapper.getPrimitiveValue(d));
    }
    
    private static class Dummy {
        @XmlAttribute(name="name")
        private NameDummy name;
    }
    
    private static class NameDummy {
        
        @XmlStringValue
        private String name;

        NameDummy(String name) {
            this.name = name;
        }
    }
    
    private static class IndexDummy {
        @XmlAttribute(name="name", index=2)
        private String name;
    }
    
    private static class AddIfDefaultDummy {
        @XmlAttribute(name="name", addIfDefault=true)
        private String name;
    }
    
    private static class EmptyDefaultDummy {
        @XmlAttribute(name="name", defaultValue="  ")
        private String name;
    }
    
    private static class DefaultValueDummy {
        @XmlAttribute(name="name", defaultValue="bela")
        private String name;
    }
}
