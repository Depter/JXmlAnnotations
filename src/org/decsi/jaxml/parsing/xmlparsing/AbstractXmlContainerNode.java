package org.decsi.jaxml.parsing.xmlparsing;

import java.util.Collection;
import java.util.Map;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractXmlContainerNode extends ChildElementNode{
    
    static ChildElementNode getContainerNode(ElementNode parent, PrimitiveParser parser, XmlChildContainerWrapper wrapper) {
        Class c = wrapper.getValueClass();
        if(Collection.class.isAssignableFrom(c))
            return new CollectionXmlContainerNode(parent, parser, wrapper);
        if(Map.class.isAssignableFrom(c))
            return new MapXmlContainerNode(parent, parser, wrapper);
        if(c.isArray())
            return new ArrayXmlContainerNode(parent, parser, wrapper);
        throw notContainerClassException(c);
    }
    
    private static IllegalArgumentException notContainerClassException(Class c) {
        String msg = "Class %s is not a container class! It must be an instance of Collection, Map or array!";
        msg = String.format(msg, c.getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    protected XmlChildContainerWrapper wrapper;
    protected ChildElementNode child;
    
    AbstractXmlContainerNode(ElementNode parent, PrimitiveParser parser, XmlChildContainerWrapper wrapper) {
        super(parent, parser);
        this.wrapper = wrapper;
        initChildNode();
    }
    
    private void initChildNode() {
        ElementNode childParent = getChildParent();
        XmlChildWrapper childWrapper = wrapper.getElementWrapper();
        createChildNode(childWrapper, childParent);
    }
    
    private ElementNode getChildParent() {
        if(wrapper.addOnlyContainedElements())
            return getParent();
        return this;
    }
    
    private void createChildNode(XmlChildWrapper childWrapper, ElementNode parentNode) {
        if(childWrapper.getStringWrapper() == null) {
            child = new XmlChildNode(parentNode, parser, childWrapper);
        } else {
            child = new XmlStringChildNode(parentNode, parser, childWrapper);
        }
    }

    @Override
    public void setString(String value) throws SAXException {
        String msg = "Wrapper '%s' is a container wrapper and can not hold stirng content!";
        msg = String.format(msg, wrapper.toString());
        throw new SAXException(msg);
    }

    @Override
    public boolean acceptsName(String qualifiedName) {
        if(!wrapper.addOnlyContainedElements() && instance==null)
            return wrapper.getQualifiedName().equalsIgnoreCase(qualifiedName);
        return child.acceptsName(qualifiedName);
    }
    
    @Override
    public void resetElement() {
        super.resetElement();
        child.resetElement();
    }
    
    @Override
    public boolean hasStringContent() {
        return false;
    }

    @Override
    public boolean trimStringContent() {
        return true;
    }    
}
