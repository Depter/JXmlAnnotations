package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEvent;
import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventType;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.objectparsing.StringEscaperTest.compareChars;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class XmlChildParserTest extends AbstractParserTesting {
    
    public XmlChildParserTest() {
    }

    @Override
    protected void createParser(Class c) throws Exception {
        XmlChildWrapper wrapper = getWrapper(c);
        super.parser = new XmlChildParser(wrapper, dispatcher, primitiveParser);
    }
    
    private XmlChildWrapper getWrapper(Class c) throws Exception {
        Field field = c.getDeclaredField("field");
        return WrapperFactory.getChildWrapper(field);
    }
    
    @Test
    public void testParseString() throws Exception {
        initParser(StringChild.class);
        parser.parse(new StringChild("bela"));
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("field", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("field", evt.getqName());
    }

    @Test
    public void testParseStringContent() throws Exception {
        initParser(StringContentChild.class);
        parser.parse(new StringContentChild(new StringChild("bela")));
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("field", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("field", evt.getqName());
    }   

    @Test
    public void testNsDeclaration() throws Exception {
        initParser(NsDeclarationChild.class);
        parser.parse(new NsDeclarationChild("bela"));
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_PREFIX_MAPPING, evt.getType());
        assertEquals("b", evt.getPrefix());
        
        evt = events.popEvent();    //End-element
        evt = events.popEvent();    //Characters
        evt = events.popEvent();    //start-element
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_PREFIX_MAPPING, evt.getType());
        assertEquals("b", evt.getPrefix());
        assertEquals("www.bela.org", evt.getUri());
    }   
    
    @Test
    public void testChildElement() throws Exception {
        initParser(ChildFieldDummy.class);
        parser.parse(new ChildFieldDummy());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("element", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("id", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("1".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("id", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("element", evt.getqName());
    }
    
    @Test
    public void testAddIfNull() throws Exception {
        initParser(AddIfNull.class);
        parser.parse(new AddIfNull());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        assertEquals(null, events.popEvent());
        
        initParser(DoNotAddIfNull.class);
        parser.parse(new DoNotAddIfNull());
        assertEquals(null, events.popEvent());
    }
    
    private static class StringChild {
        @XmlStringValue
        @XmlChild(name="field")
        private String field;

        public StringChild(String value) {
            this.field = value;
        }
    }
    
    private static class StringContentChild {
        
        @XmlChild(name="field")
        private StringChild field;

        StringContentChild(StringChild field) {
            this.field = field;
        }
    }
    
    private static class NsDeclarationChild {
        @XmlChild(name="field", namespaces={"b=www.bela.org"})
        private String field;

        public NsDeclarationChild(String value) {
            this.field = value;
        }
    }
    
    private static class ChildFieldDummy {
        @XmlChild
        private ChildElementDummy field = new ChildElementDummy();
    }
    
    @XmlElement(name="element")
    private static class ChildElementDummy {
        
        @XmlChild(name="name", index=0)
        private String name = "bela";
        @XmlChild(name="id", index=1)
        private int id = 1;
    }
    
    private static class AddIfNull {
        @XmlChild(name="name")
        private String field = null;
    }
    
    private static class DoNotAddIfNull {
        @XmlChild(name="name", addIfNull=false)
        private String field = null;
    }
}
