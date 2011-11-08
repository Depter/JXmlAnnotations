package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlStringValue;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.wrappers.XmlStringValueWrapperBuilder.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlStringValueWrapperBuilderTest {
    
    public XmlStringValueWrapperBuilderTest() {
    }

    @Test
    public void testGetFieldWrapperForClass() {
        XmlStringValueWrapper wrapper = getWrapperForClass(Dummy.class);
        assertEquals(null, wrapper.getChildWrapper().getChildWrapper());
    }

    @Test
    public void testGetMethodWrapperForClass() {
        XmlStringValueWrapper wrapper = getWrapperForClass(MethodDummy.class);
        assertEquals(null, wrapper.getChildWrapper().getChildWrapper());
    }

    @Test
    public void testGetCompositeWrapper() {
        XmlStringValueWrapper wrapper = getWrapperForClass(CompositeDummy.class);
        assertEquals(null, wrapper.getChildWrapper().getChildWrapper().getChildWrapper());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoAnnotation() {
        getWrapperForClass(NoAnnotationDummy.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoConstructor() {
        getWrapperForClass(NoConstructorDummy.class);
    }

    private static class Dummy {
        @XmlStringValue
        private String name = "bela";

        Dummy(String name) {this.name = name;}
    }
    
    private static class MethodDummy {
        private String name = "bela";
        
        MethodDummy(String name) {this.name = name;}
        
        @XmlStringValue
        public String getName() {return name;}
    }
    
    private static class CompositeDummy {
        @XmlStringValue
        private Dummy name = new Dummy("bela");
        
        CompositeDummy(Dummy name) {this.name = name;}
    }

    private static class NoAnnotationDummy {
        private String name = "bela";
        
        NoAnnotationDummy(String name) {this.name = name;}
    }

    private static class NoConstructorDummy {
        @XmlStringValue
        private String name = "bela";
    }    
}
