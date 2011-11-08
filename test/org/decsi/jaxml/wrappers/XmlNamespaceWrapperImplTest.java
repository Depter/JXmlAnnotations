package org.decsi.jaxml.wrappers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlNamespaceWrapperImplTest {
    
    private XmlNamespaceWrapper defaultWrapper;
    private XmlNamespaceWrapper wrapper;
    
    public XmlNamespaceWrapperImplTest() {
    }

    @Before
    public void setUp() {
        defaultWrapper = new XmlNamespaceWrapperImpl(null, "bela");
        wrapper = new XmlNamespaceWrapperImpl("prefix", "geza");
    }

    @Test
    public void testIsDefault() {
        assertTrue(defaultWrapper.isDefault());
        assertFalse(wrapper.isDefault());
    }

    @Test
    public void testGetPrefix() {
        assertEquals(null, defaultWrapper.getPrefix());
        assertEquals("prefix", wrapper.getPrefix());
    }

    @Test
    public void testGetURI() {
        assertEquals("bela", defaultWrapper.getURI());
        assertEquals("geza", wrapper.getURI());
    }
}
