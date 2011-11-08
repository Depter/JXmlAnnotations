package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlStringValue;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.wrappers.XmlStringValueWrapperBuilder.getWrapperForClass;

/**
 *
 * @author Peter Decsi
 */
public class XmlStringValueWrapperImplTest {
    
    private XmlStringValueWrapper wrapper;
    
    public XmlStringValueWrapperImplTest() {
    }

    @Test
    public void testGetChildWrapper() {
        wrapper = getWrapperForClass(StringDummy.class);
        assertEquals(null, wrapper.getChildWrapper().getChildWrapper());
        
        wrapper = getWrapperForClass(CompositeDummy.class);
        assertEquals(null, wrapper.getChildWrapper().getChildWrapper().getChildWrapper());
    }

    @Test
    public void testTrimInput() {
        wrapper = getWrapperForClass(StringDummy.class);
        assertTrue(wrapper.trimInput());
        wrapper = getWrapperForClass(NotTrimDummy.class);
        assertFalse(wrapper.trimInput());
        wrapper = getWrapperForClass(CompositeDummy.class);
        assertTrue(wrapper.trimInput());
        wrapper = getWrapperForClass(NotTrimComposite.class);
        assertTrue(wrapper.trimInput());
    }

    @Test
    public void testGetPrimitiveValue() throws Exception {
        wrapper = getWrapperForClass(StringDummy.class);
        assertEquals(null, wrapper.getPrimitiveValue(null));
        assertEquals("geza", wrapper.getPrimitiveValue(new StringDummy("geza")));
        
        wrapper = getWrapperForClass(CompositeDummy.class);
        assertEquals(null, wrapper.getPrimitiveValue(null));
        CompositeDummy instance = CompositeDummy.create(new StringDummy("geza"));
        assertEquals("geza", wrapper.getPrimitiveValue(instance));
        
        wrapper = getWrapperForClass(MethodDummy.class);
        assertEquals(null, wrapper.getPrimitiveValue(null));
        assertEquals("bela", wrapper.getPrimitiveValue(new MethodDummy("bela")));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testVoidMethodException() {
        wrapper = getWrapperForClass(VoidMethodDummy.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParameterMethodException() {
        wrapper = getWrapperForClass(ParameterMethodDummy.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNonStaticInnerClass() {
        wrapper = getWrapperForClass(OutterDummy.InnerDummy.class);
    }
    
    private static class StringDummy {
        @XmlStringValue
        private String name = "bela";
        public StringDummy(String name) {
            this.name = name;
        }
    }
    
    private static class NotTrimDummy {
        @XmlStringValue(trim=false)
        private String name = "bela";
        public NotTrimDummy(String name) {
            this.name = name;
        }
    }
    
    private static class CompositeDummy {
        
        private static CompositeDummy create(StringDummy name) {
            CompositeDummy d = new CompositeDummy();
            d.name = name;
            return d;
        }
        
        @XmlStringValue
        private StringDummy name = new StringDummy("bela");
    }
    
    private static class NotTrimComposite {
        
        private static NotTrimComposite create(StringDummy name) {
            NotTrimComposite d = new NotTrimComposite();
            d.name = name;
            return d;
        }
        
        @XmlStringValue(trim=false)
        private StringDummy name = new StringDummy("bela");
    }
    
    private static class MethodDummy {
        
        private String name;

        MethodDummy(String name) {
            this.name = name;
        }
        
        @XmlStringValue
        public String getName() {
            return name;
        }
    }
    
    private static class VoidMethodDummy {

        VoidMethodDummy(String name) {}
        
        @XmlStringValue
        public void getName() {
        }
    }
    
    private static class ParameterMethodDummy {

        ParameterMethodDummy(String name) {}
        
        @XmlStringValue
        public String getName(int id) {
            return null;
        }
    }
    
    private static class OutterDummy{
        
        private class InnerDummy {
            
            @XmlStringValue
            private String name;

            public InnerDummy(String name) {
                this.name = name;
            }
        }
    }
}
