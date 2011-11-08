package org.decsi.jaxml.parsing.objectparsing;

import java.util.ArrayList;
import java.util.List;
import org.decsi.jaxml.wrappers.ChildWrapper;
import org.decsi.jaxml.wrappers.ValueWrapper;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.wrappers.XmlNamespaceWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractElementParser implements ObjectParser{
    
    protected SAXEventDispatcher dispatcher;
    protected List<ObjectParser> children = new ArrayList<ObjectParser>();
    private List<XmlNamespaceWrapper> removedNamespaces = new ArrayList<XmlNamespaceWrapper>();
    protected PrimitiveParser primitiveParser;
    
    AbstractElementParser(SAXEventDispatcher dispatcher, PrimitiveParser primitiveParser) {
        this.dispatcher = dispatcher;
        this.primitiveParser = primitiveParser;
    }

    protected void addChildWrappers(List<ChildWrapper> childWrappers) {
        for(ChildWrapper w : childWrappers) {
            if(w instanceof XmlChildWrapper) addChildWrapper((XmlChildWrapper) w);
            else addContainerWrapper((XmlChildContainerWrapper) w);
        }
    }
    
    private void addChildWrapper(XmlChildWrapper child) {
        XmlChildParser parser = new XmlChildParser(child, dispatcher, primitiveParser);
        children.add(parser);
    }
    
    private void addContainerWrapper(XmlChildContainerWrapper child) {
        XmlContainerParser parser = new XmlContainerParser(child, dispatcher, primitiveParser); 
        children.add(parser);
    }
    
    protected void addStringContentWrapper(XmlStringValueWrapper stringWrapper) {
        if(stringWrapper == null) return;
        children.add(new XmlStringValueParser(stringWrapper, primitiveParser, dispatcher));
    }
    
    protected void parseChildren(Object instance) throws SAXException {
        for(ObjectParser parser : children)
            parser.parse(instance);
    }
    
    protected Object getValue(ValueWrapper valueWrapper, Object instance) throws SAXException {
        try {
            return valueWrapper.getValue(instance);
        } catch (Exception ex) {
            String msg = "ValueWrapper '%s' is unable to query value from '%s'!";
            msg = String.format(msg, valueWrapper, instance);
            throw new SAXException(msg, ex);
        }
    }    
    
    protected void startPrefixMapping(List<XmlNamespaceWrapper> namespaces) throws SAXException {
        for(XmlNamespaceWrapper namespace : namespaces)
            startPrefixMapping(namespace);
    }
    
    private void startPrefixMapping(XmlNamespaceWrapper namespace) throws SAXException {
        XmlNamespaceWrapper removed = dispatcher.fireStartPrefixMapping(namespace);
        if(removed != null) 
            removedNamespaces.add(removed);
    }
    
    protected void endPrefixMapping(List<XmlNamespaceWrapper> namespaces) throws SAXException {
        for(XmlNamespaceWrapper namespace : namespaces)
            dispatcher.fireEndPrefixMapping(namespace);
        addRemovedNamespaces();
     }
    
    private void addRemovedNamespaces() {
        NamespaceManager namespaceManager = dispatcher.getNamespaceManager();
        for(XmlNamespaceWrapper namespace : removedNamespaces)
            namespaceManager.addNamespace(namespace);
        removedNamespaces.clear();
    }
}
