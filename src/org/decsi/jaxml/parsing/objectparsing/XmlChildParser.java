package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class XmlChildParser extends AbstractElementParser {
    
    protected XmlChildWrapper wrapper;
    protected AttributesImpl attributes;
    
    XmlChildParser(XmlChildWrapper wrapper, SAXEventDispatcher dispatcher, PrimitiveParser primitiveParser) {
        super(dispatcher, primitiveParser);
        this.wrapper = wrapper;
        initState();
    }
    
    private void initState() {
        initAttributes();
        addChildWrappers();
        addStringWrapper();
    }
    
    protected void initAttributes() {
        attributes = new AttributesImpl(dispatcher.getNamespaceManager(), 
                primitiveParser, wrapper.getAttributes());
    }
    
    private void addChildWrappers() {
        XmlElementWrapper elementWrapper = wrapper.getChildElementWrapper();
        if(elementWrapper != null)
            addChildWrappers(elementWrapper.getChildren());
    }
    
    private void addStringWrapper() {
        XmlStringValueWrapper stringWrapper = wrapper.getStringWrapper();
        if(stringWrapper != null)
            addStringContentWrapper(stringWrapper);
    }
    
    @Override
    public void parse(Object instance) throws SAXException {
        Object value = getValue(wrapper, instance);
        if(value == null)
            parseNull();
        else
            parseNotNull(value);
    }
    
    private void parseNull() throws SAXException {
        if(!wrapper.addIfNull()) return;
        startElement(null);
        endElement();
    }
    
    protected void startElement(Object value) throws SAXException {
        startPrefixMapping(wrapper.getNamespaces());
        attributes.setInstance(value);
        dispatcher.fireStartElement(wrapper, attributes);
    }
    
    protected void endElement() throws SAXException {
        dispatcher.fireEndElement(wrapper);
        endPrefixMapping(wrapper.getNamespaces());
    }
    
    private void parseNotNull(Object value) throws SAXException {
        startElement(value);
        parseChildren(value);
        endElement();
    }
}
