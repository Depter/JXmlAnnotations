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
public class ElementTest {
    
    private final static String NAME = "element";
    private Element element;
    
    public ElementTest() {
    }

    @Before
    public void setUp() {
        element = new Element(NAME, null);
    }

    @Test
    public void testAddAttribute() {
        fail("Not implemented yet!");
    }

    @Test
    public void testRemoveAttrribute() {
        fail("Not implemented yet!");
    }

    @Test
    public void testAddStringContent() {
        fail("Not implemented yet!");
    }

    @Test
    public void testAddContent() {
        fail("Not implemented yet!");
    }

    @Test
    public void testRemoveContent() {
        fail("Not implemented yet!");
    }

    @Test
    public void testGetTextContent() {
        fail("Not implemented yet!");
    }
}
