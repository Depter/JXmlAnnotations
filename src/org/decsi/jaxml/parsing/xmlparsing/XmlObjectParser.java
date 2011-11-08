package org.decsi.jaxml.parsing.xmlparsing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class XmlObjectParser<T> {
    
    private SAXParser parser;
    private ObjectContentHandler contentHandler;
    
    XmlObjectParser(SAXParser parser, ObjectContentHandler contentHandler) {
        this.parser = parser;
        this.contentHandler = contentHandler;
    }
    
    public T build(File file) throws SAXException, IOException {
        parser.parse(file, contentHandler);
        return getValue();
    }
    
    private T getValue() {
        Object value = contentHandler.getValue();
        contentHandler.reset();
        return value==null? null : (T) value;
    }
    
    public T build(InputStream is) throws SAXException, IOException {
        parser.parse(is, contentHandler);
        return getValue();
    }
    
    public T build(String uri) throws SAXException, IOException {
        parser.parse(uri, contentHandler);
        return getValue();
    }
    
}
