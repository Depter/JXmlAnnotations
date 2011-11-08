package org.decsi.jaxml.parsing.objectparsing.util;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class ContentHandlerEventQueue implements ContentHandler {
    
    private List<ContentHandlerEvent> events = new ArrayList<ContentHandlerEvent>();
    
    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() throws SAXException {
        events.add(new ContentHandlerEvent(ContentHandlerEventType.START_DOCUMENT));
    }

    @Override
    public void endDocument() throws SAXException {
        events.add(new ContentHandlerEvent(ContentHandlerEventType.END_DOCUMENT));
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.START_PREFIX_MAPPING);
        event.setPrefixEvent(prefix, uri);
        events.add(event);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.END_PREFIX_MAPPING);
        event.setPrefixEvent(prefix, null);
        events.add(event);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.START_ELEMENT);
        event.setElementEvent(uri, localName, qName, atts);
        events.add(event);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.END_ELEMENT);
        event.setElementEvent(uri, localName, qName, null);
        events.add(event);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.CHARACTERS);
        char[] chrs = new char[length];
        System.arraycopy(ch, start, chrs, 0, length);
        event.setCharacters(chrs);
        events.add(event);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.IGNORABLE_CHARACTERS);
        char[] chrs = new char[length];
        System.arraycopy(ch, start, chrs, 0, length);
        event.setCharacters(chrs);
        events.add(event);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.PROCESSING_INSTRUCTION);
        event.setPrefixEvent(target, data);
        events.add(event);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        ContentHandlerEvent event = new ContentHandlerEvent(ContentHandlerEventType.SKIPPED_ENTITY);
        event.setPrefixEvent(name, null);
        events.add(event);
    }
    
    public ContentHandlerEvent popEvent() {
        int index = events.size()-1;
        return index<0? null : events.remove(index);
    }
}
