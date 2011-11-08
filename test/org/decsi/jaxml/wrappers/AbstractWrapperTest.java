package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.AbstractWrapper;
import org.junit.Before;
import org.decsi.jaxml.annotation.XmlElement;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class AbstractWrapperTest {
    
    private AbstractWrapper<XmlElement, Class> wrapper;
    
    public AbstractWrapperTest() {
    }

    @Before
    public void setUp() {
        wrapper = new AbstractWrapper<XmlElement, Class>(AnnotatedDummy.class);
        wrapper.initState(XmlElement.class);
    }

    @Test
    public void testGetAnnotation() {
        XmlElement an = wrapper.getAnnotation();
        assertEquals("bela", an.name());
        assertEquals("geza", an.nsPrefix());
    }
    
    @Test
    public void testGetElementName() {
        assertEquals(AnnotatedDummy.class.getCanonicalName(), wrapper.getElementName());
    }
    
    @Test
    public void testGetAnnotationName() {
        assertEquals(XmlElement.class.getCanonicalName(), wrapper.getAnnotationName());
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullAnnotation() {
        wrapper.initState(null);
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullElement() {
        wrapper = new AbstractWrapper<XmlElement, Class>(null);
    }

    
    @Test(expected=IllegalArgumentException.class)
    public void testAnnotationNotPresent() {
        AbstractWrapper w = new AbstractWrapper(NotAnnotatedDummy.class);
        w.initState(XmlElement.class);
    }

    @Test
    public void testGetAnnotatedElement() {
    }
    
    @XmlElement(name="bela", nsPrefix="geza")
    private static class AnnotatedDummy {}
    
    private static class NotAnnotatedDummy {}
}
