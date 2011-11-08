package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.junit.Test;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class PrimitiveStringElementNodeTest {
    
    private static PrimitiveParser parser = new PrimitiveParser();
    private PrimitiveStringNode node;
    
    public PrimitiveStringElementNodeTest() {
    }

    @Test
    public void testSetString_Byte() throws Exception {
        initNode(byte.class);
        node.setString("1");
        assertTrue(node.getInstance() instanceof Byte);
        assertEquals(new Byte((byte) 1), node.getInstance());
        
        initNode(Byte.class);
        node.setString(" 1 ");
        assertTrue(node.getInstance() instanceof Byte);
        assertEquals(new Byte((byte) 1), node.getInstance());
    }
    
    private void initNode(Class c) {
        node = new PrimitiveStringNode(null, parser, c);
    }

    @Test
    public void testSetString_Short() throws Exception {
        initNode(short.class);
        node.setString("1");
        assertTrue(node.getInstance() instanceof Short);
        assertEquals(new Short((short) 1), node.getInstance());
        
        initNode(Short.class);
        node.setString(" 1 ");
        assertTrue(node.getInstance() instanceof Short);
        assertEquals(new Short((short) 1), node.getInstance());
    }

    @Test
    public void testSetString_Integer() throws Exception {
        initNode(int.class);
        node.setString("1");
        assertTrue(node.getInstance() instanceof Integer);
        assertEquals(new Integer(1), node.getInstance());
        
        initNode(Integer.class);
        node.setString(" 1 ");
        assertTrue(node.getInstance() instanceof Integer);
        assertEquals(new Integer(1), node.getInstance());
    }

    @Test
    public void testSetString_Long() throws Exception {
        initNode(long.class);
        node.setString("1");
        assertTrue(node.getInstance() instanceof Long);
        assertEquals(new Long(1), node.getInstance());
        
        initNode(Long.class);
        node.setString(" 1 ");
        assertTrue(node.getInstance() instanceof Long);
        assertEquals(new Long(1), node.getInstance());
    }

    @Test
    public void testSetString_Float() throws Exception {
        initNode(float.class);
        node.setString("2.34");
        assertTrue(node.getInstance() instanceof Float);
        assertEquals(new Float(2.34f), node.getInstance());
        
        initNode(Float.class);
        node.setString(" 2.34 ");
        assertTrue(node.getInstance() instanceof Float);
        assertEquals(new Float(2.34f), node.getInstance());
    }

    @Test
    public void testSetString_Double() throws Exception {
        initNode(double.class);
        node.setString("2.34");
        assertTrue(node.getInstance() instanceof Double);
        assertEquals(new Double(2.34), node.getInstance());
        
        initNode(Double.class);
        node.setString(" 2.34 ");
        assertTrue(node.getInstance() instanceof Double);
        assertEquals(new Double(2.34), node.getInstance());
    }

    @Test
    public void testSetString_Boolean() throws Exception {
        initNode(boolean.class);
        node.setString("true");
        assertTrue(node.getInstance() instanceof Boolean);
        assertEquals(Boolean.TRUE, node.getInstance());
        node.setString("false");
        assertTrue(node.getInstance() instanceof Boolean);
        assertEquals(Boolean.FALSE, node.getInstance());
        node.setString("bela");
        assertTrue(node.getInstance() instanceof Boolean);
        assertEquals(Boolean.FALSE, node.getInstance());
        
        initNode(Boolean.class);
        node.setString(" true ");
        assertTrue(node.getInstance() instanceof Boolean);
        assertEquals(Boolean.TRUE, node.getInstance());
    }

    @Test
    public void testSetString_Character() throws Exception {
        initNode(char.class);
        node.setString("c");
        assertTrue(node.getInstance() instanceof Character);
        assertEquals(new Character('c'), node.getInstance());
        
        initNode(Character.class);
        node.setString(" c ");
        assertTrue(node.getInstance() instanceof Character);
        assertEquals(new Character('c'), node.getInstance());
    }

    @Test(expected=SAXException.class)
    public void testSetString_CharacterException() throws Exception {
        initNode(char.class);
        node.setString("cc");
    }

    @Test
    public void testSetString_String() throws Exception {
        initNode(String.class);
        node.setString("bela");
        assertTrue(node.getInstance() instanceof String);
        assertEquals("bela", node.getInstance());
        
        initNode(String.class);
        node.setString(" bela ");
        assertTrue(node.getInstance() instanceof String);
        assertEquals(" bela ", node.getInstance());
    }

    @Test(expected=SAXException.class)
    public void testSetString_ClassException() throws Exception {
        initNode(Object.class);
        node.setString("cc");
    }    
}
