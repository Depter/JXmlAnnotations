package org.decsi.jaxml.parsing.objectparsing;

import org.xml.sax.Attributes;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventType;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEvent;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventQueue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.objectparsing.StringEscaperTest.compareChars;

/**
 *
 * @author Peter Decsi
 */
public class ObjectXmlReaderTest {
    
    private ObjectXmlReader reader;
    ContentHandlerEventQueue events;
    
    public ObjectXmlReaderTest() {
    }

    @Before
    public void setUp() {
        XMLReaderFactory factory = new XMLReaderFactory();
        reader = factory.createReader(XmlElementParserTest.Dummy.class);
        events = new ContentHandlerEventQueue();
        reader.setContentHandler(events);
        addProcessingInstruction();
    }
    
    private void addProcessingInstruction() {
        ProcessingInstruction instruction = new ProcessingInstruction("my-app");
        instruction.addData("value", "bela").addData("name", "geza");
        reader.addInstruction(instruction);
    }

    @Test
    public void testParse() throws Exception {
        reader.parse(new XmlElementParserTest.Dummy("bela"));
    
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_DOCUMENT, evt.getType());
    
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_PREFIX_MAPPING, evt.getType());
        assertEquals("b", evt.getPrefix());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("dummy", evt.getLocalName());
        assertEquals("b:dummy", evt.getqName());
        assertEquals("www.bela.org", evt.getUri());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("dummy", evt.getLocalName());
        assertEquals("b:dummy", evt.getqName());
        assertEquals("www.bela.org", evt.getUri());
        Attributes atts = evt.getAttributes();
        assertEquals(1, atts.getLength());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_PREFIX_MAPPING, evt.getType());
        assertEquals("b", evt.getPrefix());
        assertEquals("www.bela.org", evt.getUri());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.PROCESSING_INSTRUCTION, evt.getType());
        assertEquals("my-app", evt.getPrefix());
        assertEquals("value=\"bela\" name=\"geza\"", evt.getUri());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_DOCUMENT, evt.getType());
        
        assertEquals(null, events.popEvent());
            
    }
}
