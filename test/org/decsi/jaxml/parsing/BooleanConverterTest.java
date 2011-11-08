package org.decsi.jaxml.parsing;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class BooleanConverterTest {
    
    private BooleanConverter converter;
    
    public BooleanConverterTest() {
    }

    @Before
    public void setUp() {
        converter = new BooleanConverter();
    }

    @Test(expected=NullPointerException.class)
    public void testNullTrueConstructor() {
        new BooleanConverter(null, "0");
    }

    @Test(expected=NullPointerException.class)
    public void testNullFalseConstructor() {
        new BooleanConverter("1", null);
    }

    @Test
    public void testSetStringValueFor() {
        assertEquals("1", converter.getStirngValueFor(true));
        assertEquals("0", converter.getStirngValueFor(false));
    }
}
