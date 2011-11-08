package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEvent;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventQueue;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventType;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.objectparsing.StringEscaperTest.compareChars;

/**
 *
 * @author Peter Decsi
 */
public class XmlStringValueParserTest {
    
    private XmlStringValueParser parser;
    private PrimitiveParser pParser;
    private ContentHandlerEventQueue events;
    
    public XmlStringValueParserTest() {
    }

    private void initParser(Class c) {
        XmlStringValueWrapper wrapper = WrapperFactory.getStringValueWrapper(c);
        SAXEventDispatcher dispatcher = getDispatcher();
        pParser = new PrimitiveParser();
        parser = new XmlStringValueParser(wrapper, pParser, dispatcher);
    }
    
    private SAXEventDispatcher getDispatcher() {
        NamespaceManager namespaceManager = NamespaceManagerTest.getNamespaceManager();
        SAXEventDispatcher dispathcer = new SAXEventDispatcher(namespaceManager);
        events = new ContentHandlerEventQueue();
        dispathcer.setContentHandler(events);
        return dispathcer;
    }
    
    @Test
    public void testParseString() throws Exception {
        initParser(StringDummy.class);
        parser.parse(new StringDummy("bela"));
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
    }
    
    @Test
    public void testParseByte() throws Exception {
        initParser(ByteDummy.class);
        parser.parse(new ByteDummy((byte) 1));
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("1".toCharArray(), evt.getChrs());
    }
    
    @Test
    public void testParseBoolean() throws Exception {
        initParser(BooleanDummy.class);
        parser.parse(new BooleanDummy(true));
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("true".toCharArray(), evt.getChrs());
        
        parser.parse(new BooleanDummy(false));
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("false".toCharArray(), evt.getChrs());
    
        pParser.setFalseValue("0");
        pParser.setTrueValue("1");
        parser.parse(new BooleanDummy(true));
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("1".toCharArray(), evt.getChrs());
        
        parser.parse(new BooleanDummy(false));
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("0".toCharArray(), evt.getChrs());
    }
    
    @Test
    public void testParseComposite() throws Exception {
        initParser(CompositDummy.class);
        parser.parse(new CompositDummy(new StringDummy("bela")));
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
    }
    
    @Test
    public void testParseNull() throws Exception {
        initParser(CompositDummy.class);
        parser.parse(new CompositDummy(null));
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.IGNORABLE_CHARACTERS, evt.getType());
        compareChars("".toCharArray(), evt.getChrs());
        
        parser.parse(null);
        evt = events.popEvent();
        assertEquals(null, evt);
    }
    
    private static class StringDummy {
        @XmlStringValue
        private String name;

        StringDummy(String name) {
            this.name = name;
        }
    }
    
    private static class ByteDummy {
        @XmlStringValue
        private byte value;

        ByteDummy(byte value) {
            this.value = value;
        }
    }
    
    private static class BooleanDummy {
        @XmlStringValue
        private boolean value;

        BooleanDummy(boolean value) {
            this.value = value;
        }
    }
    
    private static class CompositDummy {
        @XmlStringValue
        private StringDummy name;

        CompositDummy(StringDummy name) {
            this.name = name;
        }
    }
    
}
