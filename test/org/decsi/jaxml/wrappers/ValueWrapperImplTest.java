package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.ValueWrapperImpl;
import org.decsi.jaxml.wrappers.ValueWrapper;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class ValueWrapperImplTest {
    
    public ValueWrapperImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFieldWrapper() throws Exception {
        Field f = Dummy.class.getDeclaredField("name");
        ValueWrapper<Field> wrapper = ValueWrapperImpl.getFieldWrapper(f);
        
        assertEquals(String.class, wrapper.getValueClass());
        assertEquals("bela", wrapper.getValue(new Dummy()));
    }

    @Test
    public void testMethoddWrapper() throws Exception {
        Method m = Dummy.class.getDeclaredMethod("getName");
        ValueWrapper<Method> wrapper = ValueWrapperImpl.getMethodWrapper(m);
        
        assertEquals(String.class, wrapper.getValueClass());
        assertEquals("geza", wrapper.getValue(new Dummy()));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testVoidMethodWrapper() throws Exception {
        Method m = Dummy.class.getDeclaredMethod("reverseName");
        ValueWrapper<Method> wrapper = ValueWrapperImpl.getMethodWrapper(m);
    }

    private static class Dummy {
        
        private String name = "bela";
        
        private String getName() {
            return "geza";
        }
        
        private void reverseName(){}
    }
}
