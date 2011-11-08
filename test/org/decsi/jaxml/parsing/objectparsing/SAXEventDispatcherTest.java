package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEvent;
import org.xml.sax.SAXException;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventQueue;
import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventType;
import org.decsi.jaxml.wrappers.XmlNamespaceWrapper;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class SAXEventDispatcherTest {
    
    private SAXEventDispatcher dispatcher;
    private FeatureManager features;
    private ContentHandlerEventQueue contentHandler;
    
    public SAXEventDispatcherTest() {
    }

    @Before
    public void setUp() {
        dispatcher = new SAXEventDispatcher(getNsManager());
        addContentHandler();
    }
    
    private NamespaceManager getNsManager() {
        features = new FeatureManager();
        PropertyManager properties = new PropertyManager();
        return new NamespaceManager(features, properties);
    }
    
    private void addContentHandler() {
        contentHandler = new ContentHandlerEventQueue();
        dispatcher.setContentHandler(contentHandler);
    }

    @Test
    public void testStartDocument() throws SAXException {
        dispatcher.fireStartDocument();
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.START_DOCUMENT, evt.getType());
    }

    @Test
    public void testEndDocument() throws SAXException {
        dispatcher.fireEndDocument();
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.END_DOCUMENT, evt.getType());
    }

    @Test
    public void testStartPrefixMapping() throws SAXException {
        XmlNamespaceWrapperImpl ns = new XmlNamespaceWrapperImpl("bela", "www.bela.org");
        dispatcher.fireStartPrefixMapping(ns);
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.START_PREFIX_MAPPING, evt.getType());
        assertEquals("bela", evt.getPrefix());
        assertEquals("www.bela.org", evt.getUri());
    }

    @Test
    public void testEndPrefixMapping() throws SAXException {
        XmlNamespaceWrapperImpl ns = new XmlNamespaceWrapperImpl("bela", "www.bela.org");
        dispatcher.fireEndPrefixMapping(ns);
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.END_PREFIX_MAPPING, evt.getType());
        assertEquals("bela", evt.getPrefix());
        assertEquals(null, evt.getUri());
    }

    @Test(expected=SAXException.class)
    public void startElement_UndefinedPrefix() throws SAXException {
        NamespaceManagerTest.NameImpl name = new NamespaceManagerTest.NameImpl("bela", "prefix");
        dispatcher.fireStartElement(name, null);
    }

    @Test
    public void startElement_NoUriParsing() throws SAXException {
        features.setEnabled(SAXFeature.NAMESPACE, false);
        NamespaceManagerTest.NameImpl name = new NamespaceManagerTest.NameImpl("bela", "prefix");
        dispatcher.fireStartElement(name, null);
        
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("bela", evt.getLocalName());
        assertEquals("prefix:bela", evt.getqName());
        assertEquals("", evt.getUri());
    }

    @Test
    public void startElement() throws SAXException {
        XmlNamespaceWrapperImpl ns = new XmlNamespaceWrapperImpl("prefix", "www.bela.org");
        dispatcher.fireStartPrefixMapping(ns);
        NamespaceManagerTest.NameImpl name = new NamespaceManagerTest.NameImpl("bela", "prefix");
        dispatcher.fireStartElement(name, null);
        
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.START_ELEMENT, evt.getType());
        assertEquals("bela", evt.getLocalName());
        assertEquals("prefix:bela", evt.getqName());
        assertEquals("www.bela.org", evt.getUri());
    }

    @Test
    public void testEndElement() throws SAXException {
        NamespaceManagerTest.NameImpl name = new NamespaceManagerTest.NameImpl("bela", "");
        dispatcher.fireEndElement(name);
        
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.END_ELEMENT, evt.getType());
        assertEquals("bela", evt.getLocalName());
        assertEquals("bela", evt.getqName());
        assertEquals("", evt.getUri());
    }

    @Test
    public void testCharacters() throws SAXException {
        String str = "bela";
        dispatcher.fireCharacters(str);
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        StringEscaperTest.compareChars(str.toCharArray(), evt.getChrs());
        
        str = "bela < geza";
        dispatcher.fireCharacters(str);
        evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.CHARACTERS, evt.getType());
        StringEscaperTest.compareChars("bela &lt; geza".toCharArray(), evt.getChrs());
    }

    @Test
    public void testIgnorableWhitespace() throws SAXException {
        String str = "  ";
        dispatcher.fireIgnorableWhitespace(str);
        ContentHandlerEvent evt = contentHandler.popEvent();
        assertEquals(ContentHandlerEventType.IGNORABLE_CHARACTERS, evt.getType());
    }

    @Test(expected=SAXException.class)
    public void testIgnorableWhitespace_Excpetion() throws SAXException {
        String str = "  bela  ";
        dispatcher.fireIgnorableWhitespace(str);
    }
    
    private static class XmlNamespaceWrapperImpl implements XmlNamespaceWrapper {
        
        private String prefix;
        private String uri;
        
        XmlNamespaceWrapperImpl(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }
        
        @Override
        public boolean isDefault() {
            return prefix==null;
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        @Override
        public String getURI() {
            return uri;
        }
    }
}
