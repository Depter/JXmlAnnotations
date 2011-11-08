package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.ConstructorUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class ConstructorUtilTest {
    
    public ConstructorUtilTest() {
    }

    @Test
    public void testIsConstructable() {
        assertTrue(ConstructorUtil.isConstructable(Dummy.class));
        assertTrue(ConstructorUtil.isConstructable(Dummy.class, String.class, int.class));
        assertFalse(ConstructorUtil.isConstructable(Dummy.class, int.class, String.class));
        assertFalse(ConstructorUtil.isConstructable(Dummy.InnerDummy.class));

        assertTrue(ConstructorUtil.isConstructable(StaticDummy.class));
        assertTrue(ConstructorUtil.isConstructable(StaticDummy.class, String.class, int.class));
        assertFalse(ConstructorUtil.isConstructable(StaticDummy.class, int.class, String.class));
    }
    
    private static class Dummy {
        private Dummy() {}
        private Dummy(String name, int id) {}
        private class InnerDummy{}
    }
    
    private static class StaticDummy {
        private static StaticDummy instance() {
            return null;
        }
        
        private static StaticDummy instance(String name, int id) {
            return null;
        }
    }
}
