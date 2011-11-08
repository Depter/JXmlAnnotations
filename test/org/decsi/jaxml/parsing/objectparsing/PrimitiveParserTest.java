package org.decsi.jaxml.parsing.objectparsing;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class PrimitiveParserTest {
    
    private final static String TRUE = "true";
    private final static String FALSE = "false";
    
    private PrimitiveParser parser;
    
    public PrimitiveParserTest() {
    }

    @Before
    public void setUp() {
        parser = new PrimitiveParser();
        parser.setFalseValue(FALSE);
        parser.setTrueValue(TRUE);
    }

    @Test
    public void testParseBoolean() {
        assertEquals(TRUE, parser.getStringValue(true));
        assertEquals(TRUE, parser.getStringValue(Boolean.TRUE));
        assertEquals(FALSE, parser.getStringValue(false));
        assertEquals(FALSE, parser.getStringValue(Boolean.FALSE));
    }
    
    @Test
    public void testByte() {
        byte value = (byte) 2;
        assertEquals("2", parser.getStringValue(value));
    }
    
    @Test
    public void testShort() {
        short value = (short) 4;
        assertEquals("4", parser.getStringValue(value));
    }
    
    @Test
    public void testInteger() {
        int value = 11;
        assertEquals("11", parser.getStringValue(value));
    }
    
    @Test
    public void testLong() {
        long value = 200000000000L;
        assertEquals("200000000000", parser.getStringValue(value));
    }
    
    @Test
    public void testFloat() {
        float value = -2.35f;
        assertEquals("-2.35", parser.getStringValue(value));
    }
    
    @Test
    public void testDouble() {
        double value = 4.12;
        assertEquals("4.12", parser.getStringValue(value));
    }
    
    @Test
    public void testCharacter() {
        char value = 'b';
        assertEquals("b", parser.getStringValue(value));
    }
    
    @Test
    public void testString() {
        String value = "bela";
        assertEquals("bela", parser.getStringValue(value));
    }
    
    @Test
    public void testNull() {
        String value = null;
        assertEquals("", parser.getStringValue(value));
    }
}
