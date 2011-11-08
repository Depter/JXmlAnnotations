package org.decsi.jaxml.wrappers;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.wrappers.AnnotatedElementUtil.*;

/**
 *
 * @author Peter Decsi
 */
public class AnnotatedElementUtilTest {
    
    public AnnotatedElementUtilTest() {
    }

    @Test
    public void testGetName() throws Exception {
        Class c = Dummy.class;
        assertEquals(c.getCanonicalName(), getName(c));
        
        Field f = c.getDeclaredField("name");
        assertEquals("name", getName(f));
        
        Method m = c.getDeclaredMethod("getName");
        assertEquals("getName()", getName(m));
    }
    
    
    @Test
    public void testGetDeclaringClass() throws Exception {
        Class c = Dummy.class;
        assertEquals(c, getDeclaringClass(c));
        
        Field f = c.getDeclaredField("name");
        assertEquals(c, getDeclaringClass(f));
        
        Method m = c.getDeclaredMethod("getName");
        assertEquals(c, getDeclaringClass(m));
    }
    
    private static class Dummy {
        private String name;
        
        private void getName() {}
    }
}
