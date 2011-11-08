package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.decsi.jaxml.wrappers.AnnotationAttributeParser;
import org.junit.Before;
import java.util.List;
import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class AnnotationAttributeParserTest {
    
    AnnotationAttributeParser parser;
    
    public AnnotationAttributeParserTest() {
    }

    @Before
    public void setUp() throws Exception {
        Field f = Dummy.class.getDeclaredField("name");
        parser = new AnnotationAttributeParser(f);
    }
    
    @Test
    public void testNullAttribute() {
        String[] attributes = null;
        List<XmlAttributeWrapper> wrappers = parser.getAttributeWrappers(attributes);
        assertEquals(0, wrappers.size());
    }
    
    @Test
    public void testEmptyAttribute() {
        String[] attributes = new String[] {""};
        List<XmlAttributeWrapper> wrappers = parser.getAttributeWrappers(attributes);
        assertEquals(0, wrappers.size());
    }
    
    @Test
    public void testAttributes() throws Exception {
        String[] attributes = new String[] {"name=bela", "ns:id=1", "onlyName"};
        List<XmlAttributeWrapper> wrappers = parser.getAttributeWrappers(attributes);
        assertEquals(3, wrappers.size());
        
        XmlAttributeWrapper wrapper = wrappers.get(0);
        assertTrue(wrapper.addIfDefault());
        assertEquals(Dummy.class.getDeclaredField("name"), wrapper.getAnnotatedElement());
        assertEquals(null, wrapper.getAnnotation());
        assertEquals(null, wrapper.getChildWrapper());
        assertEquals(null, wrapper.getDefaultValue());
        assertEquals(-1, wrapper.getIndex());
        assertEquals("name", wrapper.getQualifiedName());
        assertEquals("bela", wrapper.getPrimitiveValue(null));
        
        wrapper = wrappers.get(1);
        assertEquals("ns:id", wrapper.getQualifiedName());
        assertEquals("1", wrapper.getPrimitiveValue(null));
        
        wrapper = wrappers.get(2);
        assertEquals("onlyName", wrapper.getQualifiedName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNoNameAttribute() {
        String[] attributes = new String[] {"=bela"};
        parser.getAttributeWrappers(attributes);
    }
    
    private static class Dummy {
        
        private String name;
    }
}
