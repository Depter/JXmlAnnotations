package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.decsi.jaxml.parsing.PrimitiveUtil;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.PrimitiveStringWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class XmlStringValueNode extends AbstractNode {
    
    private XmlStringValueWrapper wrapper;
    private Class clazz;
    private ElementNode child;
    
    XmlStringValueNode(ElementNode parent, PrimitiveParser parser, Class c, XmlStringValueWrapper wrapper) {
        super(parent, parser);
        this.wrapper = wrapper;
        this.clazz = c;
        initChild();
    }
    
    private void initChild() {
        if(wrapper instanceof PrimitiveStringWrapper)
            child = new PrimitiveStringNode(this, parser, clazz);
        else
            child = new XmlStringValueNode(this, parser, getType(wrapper), wrapper.getChildWrapper());
    }
    
    private Class getType(XmlStringValueWrapper childWrapper) {
        AccessibleObject member = (AccessibleObject) childWrapper.getAnnotatedElement();
        if(member instanceof Field)
            return ((Field) member).getType();
        return ((Method) member).getReturnType();
    }

    @Override
    public ElementNode createChildNode(String name, Attributes attributes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setString(String value) throws SAXException {
        child.setString(value);
        try {
            instance = PrimitiveUtil.isPrimitive(clazz) ? child.getInstance():
                        createInstance(child.getInstance());
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }
    
    private Object createInstance(Object childInstance) throws Exception {
        Object[] params = new Object[]{childInstance};
        Class[] paramClass = new Class[]{getType(wrapper)};
        return InstanceFactory.newInstance(clazz, params, paramClass);
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
        if(child instanceof PrimitiveStringNode)
            return wrapper.trimInput();
        return child.trimStringContent();
    }
}
