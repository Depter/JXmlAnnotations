package org.decsi.jaxml.wrappers;

import java.util.List;
import org.junit.Before;
import org.decsi.jaxml.annotation.XmlElement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class NamespaceBuilderTest {
    
    private NamespaceBuilder builder;
    
    public NamespaceBuilderTest() {
    }

    @Before
    public void setUp() {
        AbstractWrapper wrapper = new XmlElementWrapperImpl(Dummy.class);
        builder = new NamespaceBuilder(wrapper);
    }

    @Test
    public void testBuildNull() {
        List<XmlNamespaceWrapper> wrappers = builder.buildNamespaceWrappers(null);
        assertTrue(wrappers.isEmpty());
    }

    @Test
    public void testBuildEmpty() {
        List<XmlNamespaceWrapper> wrappers = builder.buildNamespaceWrappers(new String[]{""});
        assertTrue(wrappers.isEmpty());
    }
    
    @Test
    public void testBuild() {
        String[] nss = new String[]{"www.default.org", "geza=www.geza.org", "bela=www.bela.org"};
        List<XmlNamespaceWrapper> wrappers = builder.buildNamespaceWrappers(nss);
        assertEquals(3, wrappers.size());

        assertTrue(wrappers.get(0).isDefault());
        assertEquals(null, wrappers.get(0).getPrefix());
        assertEquals("www.default.org", wrappers.get(0).getURI());

        assertFalse(wrappers.get(1).isDefault());
        assertEquals("geza", wrappers.get(1).getPrefix());
        assertEquals("www.geza.org", wrappers.get(1).getURI());
        
        assertFalse(wrappers.get(2).isDefault());
        assertEquals("bela", wrappers.get(2).getPrefix());
        assertEquals("www.bela.org", wrappers.get(2).getURI());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTwoDefaultNsException() {
        String[] nss = new String[]{"bela", "geza"};
        builder.buildNamespaceWrappers(nss);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTwoNoURIException() {
        String[] nss = new String[]{"geza="};
        builder.buildNamespaceWrappers(nss);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTwoSamePrefixException() {
        String[] nss = new String[]{"geza=www.geza.org", "geza=www.bela.org"};
        builder.buildNamespaceWrappers(nss);
    }
    
    @XmlElement(name="dummy")
    private static class Dummy{}
}
