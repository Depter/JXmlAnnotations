package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class XmlChildNode extends ChildElementNode{

    private XmlChildWrapper wrapper;
    private ElementNode child;
    
    XmlChildNode(ElementNode parent, PrimitiveParser parser, XmlChildWrapper wrapper) {
        super(parent, parser);
        this.wrapper = wrapper;
        initChild();
    }
    
    private void initChild() {
        XmlElementWrapper elementWrapper = wrapper.getChildElementWrapper();
        if(elementWrapper==null) throw notElementChild();
        child = new XmlElementNode(super.getParent(), parser, wrapper);
    }
    
    private IllegalArgumentException notElementChild() {
        String msg = "Wrapper %s do not wrapes any elements!";
        msg = String.format(msg, wrapper.toString());
        return new IllegalArgumentException(msg);
    }
    
    @Override
    public ElementNode createChildNode(String name, Attributes attributes) throws SAXException {
        return child.createChildNode(name, attributes);
    }

    @Override
    public void setString(String value) throws SAXException {
        String msg = "Wrapper %s is not a string wrapper!";
        msg = String.format(msg, wrapper.toString());
        throw new SAXException(msg);
    }

    @Override
    public boolean acceptsName(String qualifiedName) {
        return wrapper.getQualifiedName().equalsIgnoreCase(qualifiedName);
    }
    
    @Override
    public Object getInstance() {
        return child.getInstance();
    }

    @Override
    public void setValue(Object object) throws SAXException {
        if(object == null) return;
        instance = child.getInstance();
        super.setFieldValueOnParent(wrapper.getAnnotatedElement());
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
        return false;
    }
}
