package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class XmlStringChildNode extends ChildElementNode {

    private XmlChildWrapper wrapper;
    private ElementNode child;
    
    XmlStringChildNode(ElementNode parent, PrimitiveParser parser, XmlChildWrapper wrapper) {
        super(parent, parser);
        this.wrapper = wrapper;
        initChild();
    }
    
    private void initChild() {
        XmlStringValueWrapper stringWrapper = wrapper.getStringWrapper();
        if(stringWrapper == null) throw notStringChildException();
        child = new XmlStringValueNode(this, parser, wrapper.getValueClass(), stringWrapper);
    }
    
    private IllegalArgumentException notStringChildException() {
        String msg = "Wrapper %s is not a string container!";
        msg = String.format(msg, wrapper.toString());
        return new IllegalArgumentException(msg);
    }
    
    @Override
    public ElementNode createChildNode(String name, Attributes attributes) throws SAXException {
        return this;
    }

    @Override
    public void setString(String value) throws SAXException {
        child.setString(value);
        instance = child.getInstance();
    }

    @Override
    public boolean acceptsName(String qualifiedName) {
        return wrapper.getQualifiedName().equalsIgnoreCase(qualifiedName);
    }

    @Override
    public void setValue(Object object) throws SAXException {
        if(object==null) return;
        super.setFieldValueOnParent(wrapper.getAnnotatedElement());
    }
    
    @Override
    public void resetElement() {
        super.resetElement();
        child.resetElement();
    }

    @Override
    public boolean hasStringContent() {
        return true;
    }

    @Override
    public boolean trimStringContent() {
        return child.trimStringContent();
    }
}
