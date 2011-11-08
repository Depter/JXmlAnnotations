package org.decsi.jaxml.xml;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class NamedContentTest {
    
    private final static String NAME = "content";
    private NamedContent content;
    
    public NamedContentTest() {
    }

    @Before
    public void setUp() {
        content = new NamedContent(NAME, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullNameConstructor() {
        new NamedContent(null, NAME);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyNameConstructor() {
        new NamedContent("  ", NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullNameSet() {
        content.setName(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyNameSet() {
        content.setName("  ");
    }
    
    @Test
    public void testSetName() {
        assertEquals(NAME, content.getName());
        
        content.setName("bela");
        assertEquals("bela", content.getName());
    }

    @Test
    public void testGetNsPrefix() {
        assertEquals(null, content.getNsPrefix());
        
        content.setNsPrefix("bela");
        assertEquals("bela", content.getNsPrefix());
        
        content.setNsPrefix("  ");
        assertEquals(null, content.getNsPrefix());
        
        content.setNsPrefix(null);
        assertEquals(null, content.getNsPrefix());
    }

    @Test
    public void testGetQualifiedName() {
        assertEquals(NAME, content.getQualifiedName());
        content.setName("geza");
        content.setNsPrefix("bela");
        assertEquals("bela:geza", content.getQualifiedName());
    }

    @Test
    public void testEquals() {
        NamedContent c = new NamedContent(NAME, null);
        assertTrue(content.equals(c));
        
        content.setNsPrefix("bela");
        assertFalse(content.equals(c));
        
        c.setNsPrefix("bela");
        assertTrue(content.equals(c));        
    }
    
}
