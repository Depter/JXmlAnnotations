package org.decsi.jaxml.parsing.out;

import java.util.List;
import org.junit.After;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
public class PlainFormatterTest {
    
    private PlainFormatter formatter;
    private StringWriter writer;
    
    public PlainFormatterTest() {
    }

    @Before
    public void setUp() {
        formatter = new PlainFormatter();
        writer = new StringWriter();
    }
    
    @After
    public void tearDown() throws IOException {
        writer.close();
        writer = null;
    }

    @Test
    public void testStartDocument() throws Exception {
        formatter.startDocument(writer);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", writer.toString());
    }

    @Test
    public void testEndDocument() {
        formatter.endDocument(writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testWriteProcessingInstruction() throws Exception {
        String target = "instruction";
        String data = "value1=\"value\" value2=\"value\"";
        formatter.writeProcessingInstruction(writer, target, data);
        assertEquals("<?"+target+" "+data+"?>", writer.toString());
    }

    @Test
    public void testEmptyElement() throws Exception {
    
    }

    @Test
    public void testStartElement() throws Exception {
    }

    @Test
    public void testEndElement() throws Exception {
    }

    @Test
    public void testStringContent() throws Exception {
    }
}
