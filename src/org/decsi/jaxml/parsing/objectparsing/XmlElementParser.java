package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class XmlElementParser extends AbstractElementParser {
    
    private XmlElementWrapper wrapper;
    private AttributesImpl attributes;
    
    XmlElementParser(Class c, SAXEventDispatcher dispatcher, PrimitiveParser primitiveParser) {
        super(dispatcher, primitiveParser);
        wrapper = WrapperFactory.getElementWrapper(c);
        initState();
    }
    
    private void initState() {
        this.attributes = new AttributesImpl(dispatcher.getNamespaceManager(), 
                primitiveParser, wrapper.getAttributes());
        super.addChildWrappers(wrapper.getChildren());
        super.addStringContentWrapper(wrapper.getStringWrapper());
    }
    
    @Override
    public void parse(Object instance) throws SAXException {
        if(instance == null) return;
        dispatchStart(instance);
        parseChildren(instance);
        dispatchEnd();
    }
    
    private void dispatchStart(Object instance) throws SAXException {
        startPrefixMapping(wrapper.getNamespaces());
        attributes.setInstance(instance);
        dispatcher.fireStartElement(wrapper, attributes);
    }
    
    private void dispatchEnd() throws SAXException {
        dispatcher.fireEndElement(wrapper);
        endPrefixMapping(wrapper.getNamespaces());
    }
}
