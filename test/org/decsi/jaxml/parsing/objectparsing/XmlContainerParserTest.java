package org.decsi.jaxml.parsing.objectparsing;

import org.xml.sax.Attributes;
import org.decsi.jaxml.annotation.XmlAttribute;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEvent;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventType;
import org.decsi.jaxml.testclasses.ContactBook;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.objectparsing.StringEscaperTest.compareChars;

/**
 *
 * @author Peter Decsi
 */
public class XmlContainerParserTest extends AbstractParserTesting {
    
    public XmlContainerParserTest() {
    }

    @Override
    protected void createParser(Class c) throws Exception {
        XmlChildContainerWrapper wrapper = getWrapper(c);
        super.parser = new XmlContainerParser(wrapper, dispatcher, primitiveParser);
    }
    
    private XmlChildContainerWrapper getWrapper(Class c) throws Exception {
        Field field = c.getDeclaredField("field");
        return WrapperFactory.getChildContainerWrapper(field);
    }

    @Test
    public void testContactBook() throws Exception {
        Field field = ContactBook.class.getDeclaredField("contacts");
        XmlChildContainerWrapper wrapper = WrapperFactory.getChildContainerWrapper(field);
        primitiveParser = new PrimitiveParser();
        dispatcher = getDispatcher();
        parser = new XmlContainerParser(wrapper, dispatcher, primitiveParser);
        
        parser.parse(ContactBook.createTestInstance());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("contacts", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("contact", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("email", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("jucus@bela.org".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("email", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("person", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("person", evt.getqName());
        Attributes attributes = evt.getAttributes();
        assertEquals(4, attributes.getLength());
        assertEquals("id", attributes.getQName(0));
        assertEquals("3", attributes.getValue(0));
        assertEquals("age", attributes.getQName(1));
        assertEquals("55", attributes.getValue(1));
        assertEquals("gender", attributes.getQName(2));
        assertEquals("woman", attributes.getValue(2));
        assertEquals("name", attributes.getQName(3));
        assertEquals("jucus", attributes.getValue(3));
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("contact", evt.getqName());
        attributes = evt.getAttributes();
        assertEquals(1, attributes.getLength());
        assertEquals("id", attributes.getQName(0));
        assertEquals("2", attributes.getValue(0));
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("contact", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("email", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("geza@bela.org".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("email", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("person", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("person", evt.getqName());
        attributes = evt.getAttributes();
        assertEquals(4, attributes.getLength());
        assertEquals("id", attributes.getQName(0));
        assertEquals("2", attributes.getValue(0));
        assertEquals("age", attributes.getQName(1));
        assertEquals("25", attributes.getValue(1));
        assertEquals("gender", attributes.getQName(2));
        assertEquals("man", attributes.getValue(2));
        assertEquals("name", attributes.getQName(3));
        assertEquals("geza", attributes.getValue(3));
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("contact", evt.getqName());
        attributes = evt.getAttributes();
        assertEquals(1, attributes.getLength());
        assertEquals("id", attributes.getQName(0));
        assertEquals("1", attributes.getValue(0));
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("contacts", evt.getqName());        
    
        assertEquals(null, events.popEvent());
    }
    
    @Test
    public void testParseArray() throws Exception {
        initParser(ArrayContainer.class);
        parser.parse(new ArrayContainer());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("geza".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
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
        assertEquals("names", evt.getqName());
        
        assertEquals(null, events.popEvent());
    }

    @Test
    public void testParseList() throws Exception {
        initParser(ListContainer.class);
        parser.parse(new ListContainer());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("geza".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
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
        assertEquals("names", evt.getqName());
        
        assertEquals(null, events.popEvent());
    }

    @Test
    public void testParseMap() throws Exception {
        initParser(MapContainer.class);
        parser.parse(new MapContainer());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("geza".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        assertEquals("container-key", evt.getAttributes().getQName(0));
        assertEquals("geza-key", evt.getAttributes().getValue(0));
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        assertEquals("container-key", evt.getAttributes().getQName(0));
        assertEquals("bela-key", evt.getAttributes().getValue(0));
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        assertEquals(null, events.popEvent());
    }

    @Test
    public void testAddOnlyElements() throws Exception {
        initParser(OnlyElements.class);
        parser.parse(new OnlyElements());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("geza".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        compareChars("bela".toCharArray(), evt.getChrs());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("name", evt.getqName());
        
        assertEquals(null, events.popEvent());
    }
    
    @Test
    public void testAddIfEmpty() throws Exception {
        initParser(AddIfEmpty.class);
        parser.parse(new AddIfEmpty());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        initParser(DoNotAddIfEmpty.class);
        parser.parse(new DoNotAddIfEmpty());
        assertEquals(null, events.popEvent());
    }
    
    @Test
    public void testAddIfNull() throws Exception {
        initParser(AddIfNull.class);
        parser.parse(new AddIfNull());
        
        ContentHandlerEvent evt = events.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        evt = events.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("names", evt.getqName());
        
        initParser(DoNotAddIfNull.class);
        parser.parse(new DoNotAddIfNull());
        assertEquals(null, events.popEvent());
    }
    
    private static class ArrayContainer {
        @XmlChildContainer(name="names", elementClass=String.class)
        @XmlChild(name="name")
        private String[] field = new String[]{"bela", "geza"};
    }
    
    private static class ListContainer {
        @XmlChildContainer(name="names", elementClass=String.class)
        @XmlChild(name="name")
        private List<String> field = new ArrayList<String>(2);
        {
            field.add("bela");
            field.add("geza");
        }
    }
    
    private static class MapContainer {   
        @XmlChildContainer(name="names", elementClass=String.class, keyClass=String.class)
        @XmlChild(name="name")
        @XmlAttribute(name="container-key")
        private Map<String, String> field = new HashMap<String, String>(2);
        {
            field.put("bela-key", "bela");
            field.put("geza-key", "geza");
        }
    }
    
    private static class OnlyElements {
        @XmlChildContainer(name="names", elementClass=String.class,
                addOnlyContainedElements=true)
        @XmlChild(name="name")
        private String[] field = new String[]{"bela", "geza"};
    }
    
    private static class AddIfEmpty {
        @XmlChildContainer(name="names", elementClass=String.class)
        @XmlChild(name="name")
        private String[] field = new String[0];
    }
    
    private static class DoNotAddIfEmpty {
        @XmlChildContainer(name="names", elementClass=String.class,
                addIfEmpty=false)
        @XmlChild(name="name")
        private String[] field = new String[0];
    }
    
    private static class AddIfNull {
        @XmlChildContainer(name="names", elementClass=String.class)
        @XmlChild(name="name")
        private String[] field = null;
    }
    
    private static class DoNotAddIfNull {
        @XmlChildContainer(name="names", elementClass=String.class,
                addIfNull=false)
        @XmlChild(name="name")
        private String[] field = null;
    }
    
}
