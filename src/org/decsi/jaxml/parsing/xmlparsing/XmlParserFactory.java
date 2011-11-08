package org.decsi.jaxml.parsing.xmlparsing;

import javax.xml.parsers.SAXParser;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class XmlParserFactory<T> {

    private ElementNode node;
    private PrimitiveParser parser = new PrimitiveParser();
    private boolean isCreated = false;
            
    public XmlParserFactory(Class<T> c) {
        XmlElementWrapper wrapper = WrapperFactory.getElementWrapper(c);
        node = new XmlElementNode(null, parser, wrapper);
    }
    
    public void setBooleanValue(boolean value, String strValue) {
        checkState();
        if(value) parser.setTrueValue(strValue);
        else parser.setFalseValue(strValue);
    }
    
    private void checkState() {
        if(isCreated)
            throw new IllegalStateException("ObjectXmlReader is already built!");
    }
    
    public XmlObjectParser<T> build(SAXParser parser) throws SAXException {
        checkState();
        isCreated = true;
        ObjectContentHandler ch = new ObjectContentHandler(node);
        parser.getXMLReader().setContentHandler(ch);
        return new XmlObjectParser<T>(parser, ch);
    }
}
