package org.decsi.jaxml.parsing.objectparsing;

import org.xml.sax.Attributes;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEvent;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.objectparsing.StringEscaperTest.compareChars;

/**
 *
 * @author Peter Decsi
 */
public class XmlElementParserTest extends AbstractParserTesting{
    
    public XmlElementParserTest() {
    }


    @Override
    protected void createParser(Class c) throws Exception {
        parser = new XmlElementParser(c, dispatcher, primitiveParser);
    }

    @Test
    public void testParse() throws Exception {
        initParser(Dummy.class);
        parser.parse(new Dummy("dummy value"));
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_PREFIX_MAPPING, evt.getType());
        assertEquals("b", evt.getPrefix());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("dummy", evt.getLocalName());
        assertEquals("b:dummy", evt.getqName());
        assertEquals("www.bela.org", evt.getUri());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("dummy value".toCharArray(), evt.getChrs());
        
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
        
        assertEquals(null, events.popEvent());
    }
    
    @XmlElement(name="dummy", nsPrefix="b", namespaces={"b=www.bela.org"})
    public static class Dummy {
        
        @XmlAttribute(name="name", defaultValue="bela", addIfDefault=false)
        private String name = "bela";
        
        @XmlAttribute(name="id", type=XmlAttribute.Type.ID)
        private int id = 1;
        
        @XmlStringValue
        private String value;
        
        public Dummy(String value) {
            this.value = value;
        }
        
        public Dummy() {}
    }
}
