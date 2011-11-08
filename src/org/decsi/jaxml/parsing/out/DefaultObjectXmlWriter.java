package org.decsi.jaxml.parsing.out;

import java.io.IOException;
import org.decsi.jaxml.parsing.objectparsing.ObjectXmlReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
        
/**
 *
 * @author Peter Decsi
 */
public class DefaultObjectXmlWriter implements ObjectXmlWriter{
    
    private final static Attributes END_ATTRIBUTES = new NullAttributes();
    
    private Writer writer;
    private XmlFormatter formatter;
    private ObjectXmlReader xmlReader;
    
    private List<NsDeclaration> nss = new ArrayList<NsDeclaration>();
    private ElementDummyImpl element = new ElementDummyImpl(nss);
    boolean lastEventWasStart;
    
    public DefaultObjectXmlWriter(ObjectXmlReader objectReader, Writer writer) {
        this.xmlReader = objectReader;
        this.xmlReader.setContentHandler(new Printer());
        this.writer = writer;
        this.formatter = new PlainFormatter();
    }
    
    
    public DefaultObjectXmlWriter(ObjectXmlReader objectReader, Writer writer, XmlFormatter formatter) {
        this.xmlReader = objectReader;
        this.xmlReader.setContentHandler(new Printer());
        this.writer = writer;
        this.formatter = formatter;
    }

    @Override
    public void write(Object instance) throws SAXException {
        xmlReader.parse(instance);
    }
    
    private class Printer implements ContentHandler {
    
        @Override
        public void setDocumentLocator(Locator locator) {
        }

        @Override
        public void startDocument() throws SAXException {
            try {
                formatter.startDocument(writer);
            } catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        @Override
        public void endDocument() throws SAXException {
            try {
                formatter.endDocument(writer);
            } catch (IOException ex) {
                throw new SAXException(ex);
            } finally {
                clearState();
            }
        }

        private void clearState() {
            writer = null;
            formatter = null;
            element = null;
            nss = null;
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            nss.add(new NsDeclarationImpl(prefix, uri));
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if(lastEventWasStart)
                writeStartElement();
            lastEventWasStart = true;
            element.setState(qName, atts);
        }

        private void writeStartElement() throws SAXException {
            try {
                formatter.startElement(writer, element);
            } catch (IOException ex) {
                throw new SAXException(ex);
            } finally {
                nss.clear();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(lastEventWasStart) {
                writeEmptyElement();
            } else {
                element.setState(qName, END_ATTRIBUTES);
                writeElementEnd();
            }
            lastEventWasStart = false;
        }

        private void writeEmptyElement() throws SAXException {
            try {
                formatter.emptyElement(writer, element);
            } catch (IOException ex) {
                throw new SAXException(ex);
            } finally {
                nss.clear();
            }
        }

        private void writeElementEnd() throws SAXException {
            try {
                formatter.endElement(writer, element);
            } catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            writeStartElement();
            lastEventWasStart = false;
            writeStringContent(ch, start, length);
        }

        private void writeStringContent(char[] ch, int start, int length) throws SAXException {
            char[] content = new char[length];
            System.arraycopy(ch, start, content, 0, length);
            writeString(String.copyValueOf(content));
        }

        private void writeString(String value) throws SAXException {
            try {
                formatter.stringContent(writer, value);
            } catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            try {
                formatter.writeProcessingInstruction(writer, target, data);
            } catch (IOException ex) {
                throw new SAXException(ex);
            }
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
        }
    }
    
    private static class NullAttributes implements Attributes {

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public String getURI(int index) {
            return null;
        }

        @Override
        public String getLocalName(int index) {
            return null;
        }

        @Override
        public String getQName(int index) {
            return null;
        }

        @Override
        public String getType(int index) {
            return null;
        }

        @Override
        public String getValue(int index) {
            return null;
        }

        @Override
        public int getIndex(String uri, String localName) {
            return -1;
        }

        @Override
        public int getIndex(String qName) {
            return -1;
        }

        @Override
        public String getType(String uri, String localName) {
            return null;
        }

        @Override
        public String getType(String qName) {
            return null;
        }

        @Override
        public String getValue(String uri, String localName) {
            return null;
        }

        @Override
        public String getValue(String qName) {
            return null;
        }
    } 
}
