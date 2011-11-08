package org.decsi.jaxml.parsing.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.ChildWrapper;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import static org.decsi.jaxml.parsing.xmlparsing.AbstractXmlContainerNode.getContainerNode;

/**
 *
 * @author Peter Decsi
 */
class XmlElementNode extends AbstractNode {

    private XmlElementWrapper wrapper;
    private AbstractNode stringNode;
    private List<ChildElementNode> children;
    private String elementName;
    
    XmlElementNode(ElementNode parent, PrimitiveParser parser, 
            XmlElementWrapper wrapper) {
        super(parent, parser);
        this.wrapper = wrapper;
        this.elementName = wrapper.getQualifiedName();
        initChildren();
    }
    
    XmlElementNode(ElementNode parent, PrimitiveParser parser, 
            XmlChildWrapper wrapper) {
        super(parent, parser);
        this.wrapper = wrapper.getChildElementWrapper();
        this.elementName = wrapper.getQualifiedName();
        initChildren();
    }
    
    private void initChildren() {
        initStringNode();
        if(stringNode==null) 
            initChildNodes();
    }
    
    private void initStringNode() {
        XmlStringValueWrapper stringWrapper = wrapper.getStringWrapper();
        if(stringWrapper==null) return;
        stringNode = new XmlStringValueNode(this, parser, wrapper.getAnnotatedElement(), stringWrapper); 
    }
    
    
    private void initChildNodes() {
        children = new ArrayList<ChildElementNode>();
        for(ChildWrapper child : wrapper.getChildren())
            children.add(createChildNode(child));
    }
    
    private ChildElementNode createChildNode(ChildWrapper child) {
        if(child instanceof XmlChildWrapper) {
            return getSimpleChildNode((XmlChildWrapper) child);
        } else {
            return getContainerNode(this, parser, (XmlChildContainerWrapper) child);
        }
    }
    
    private ChildElementNode getSimpleChildNode(XmlChildWrapper cw) {
        if(cw.getStringWrapper() == null)
            return new XmlChildNode(this, parser, cw);
        return new XmlStringChildNode(this, parser, cw);
    }
    
    @Override
    public ElementNode createChildNode(String name, Attributes attributes) throws SAXException {
        if(instance == null && 
            elementName.equalsIgnoreCase(name)) {
            initObject(attributes);
            return this;
        } else {
            return createChildFromChildren(name, attributes);
        } 
    }
    
    private void initObject(Attributes attributes) throws SAXException {
        createEmptyInstance();
        if(attributes!=null)
            fillAttributes(attributes);
    }
    
    private void createEmptyInstance() throws SAXException {
        try {
            instance = InstanceFactory.newInstance(wrapper.getAnnotatedElement());
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }
    
    private void fillAttributes(Attributes attributes) throws SAXException {
        AttributeBuilder builder = new AttributeBuilder(instance, wrapper.getAttributes(), parser);
        for(int i=0; i<attributes.getLength(); i++)
            builder.setAttribute(attributes.getQName(i), attributes.getValue(i));
        builder.initDefaultAttributes();
    }
    
    private ElementNode createChildFromChildren(String name, Attributes attributes) throws SAXException {
        for(ChildElementNode node : children)
            if(node.acceptsName(name))
                return node.createChildNode(name, attributes);
        throw noChildForNameException(name);
    }
    
    private SAXException noChildForNameException(String name) {
        String msg = "No child found for name '%s'!";
        msg = String.format(msg, name);
        return new SAXException(msg);
    }
    
    @Override
    public void setString(String value) throws SAXException {
        if(stringNode == null)
            throw new SAXException("Wrapper "+wrapper+" does not allows string content!");
        stringNode.setString(value);
    }
    
    @Override
    public void endElement() throws SAXException {
        setStringValue();
        setChildValues();
    }
    
    private void setStringValue() throws SAXException {
        if(stringNode == null) return;
        if(instance!=null && stringNode.getInstance()==null) return;
        this.instance = stringNode.getInstance();
    }
    
    private void setChildValues() throws SAXException {
        if(children == null) return;
        for(ChildElementNode child : children)
            child.setValue(instance);
    }
    
    @Override
    public void resetElement() {
        super.resetElement();
        if(stringNode != null) stringNode.resetElement();
        resetChildren();
    }
    
    private void resetChildren() {
        if(children==null) return;
        for(ChildElementNode child : children)
            child.resetElement();
    }

    @Override
    public boolean hasStringContent() {
        return stringNode!=null;
    }

    @Override
    public boolean trimStringContent() {
        if(stringNode==null) return false;
        return stringNode.trimStringContent();
    }
}
