package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class XmlStringValueParser extends AbstractElementParser {
    
    private XmlStringValueWrapper wrapper;
    
    XmlStringValueParser(XmlStringValueWrapper wrapper, PrimitiveParser primitiveParser, SAXEventDispatcher dispatcher) {
        super(dispatcher, primitiveParser);
        this.wrapper = wrapper;
    }
    
    @Override
    public void parse(Object instance) throws SAXException {
        if(instance == null) return;
        Object value = getValue(instance);
        String stringValue = primitiveParser.getStringValue(value);
        dispatchString(stringValue);
    }
    
    protected Object getValue(Object instance) throws SAXException {
        try {
            return wrapper.getPrimitiveValue(instance);
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }
    
    private void dispatchString(String value) throws SAXException {
        if(value == null) return;
        if(value.trim().length()==0) dispatcher.fireIgnorableWhitespace(value);
        else dispatcher.fireCharacters(value);
    }
    
}
