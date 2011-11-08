package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.decsi.jaxml.parsing.ReflectionUtil;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class AttributeBuilder {
    
    private Object instance;
    private List<XmlAttributeWrapper> attributes;
    private PrimitiveParser parser;

    public AttributeBuilder(Object instance, List<XmlAttributeWrapper> attributes, PrimitiveParser parser) {
        this.instance = instance;
        this.attributes = new ArrayList<XmlAttributeWrapper>(attributes);
        this.parser = parser;
    }
    
    
    void setAttribute(String name, String value) throws SAXException {
        XmlAttributeWrapper wrapper = getAttribute(name);
        if(wrapper == null) return;
        setAttributeValue(wrapper, value);
    }
    
    private XmlAttributeWrapper getAttribute(String name) {
        Iterator<XmlAttributeWrapper> it = attributes.iterator();
        while(it.hasNext()) {
            XmlAttributeWrapper w = it.next();
            if(w.getQualifiedName().equalsIgnoreCase(name)) {
                it.remove();
                return w;
            }
        }
        return null;
    }
    
    private void setAttributeValue(XmlAttributeWrapper attribute, String value) throws SAXException {
        value = getValue(value, attribute.getDefaultValue());
        if(value == null) return;
        setFieldValue(attribute, value);
    }
    
    
    private String getValue(String value, String defaultValue) {
        if(isNull(value))
            return isNull(defaultValue)? null : defaultValue;
        return value;
    }
    
    private boolean isNull(String value) {
        if(value==null)
            return true;
        return value.length()==0;
    }
    
    private void setFieldValue(XmlAttributeWrapper wrapper, String value) throws SAXException {
        XmlStringValueNode node = getStringNode(wrapper);
        node.setString(value);
        setFieldValue(wrapper.getAnnotatedElement(), node.getInstance());
    }
    
    private XmlStringValueNode getStringNode(XmlAttributeWrapper wrapper) {
        return new XmlStringValueNode(null, parser, wrapper.getAnnotatedElement().getType(), wrapper.getChildWrapper()); 
    }
    
    private void setFieldValue(Field field, Object value) throws SAXException {
        if(value == null) return;
        try {
            ReflectionUtil.setFieldValue(field, instance, value);
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }
    
    void initDefaultAttributes() throws SAXException {
        for(XmlAttributeWrapper attribute : new ArrayList<XmlAttributeWrapper>(attributes))
            if(!isNull(attribute.getDefaultValue()))
                setAttribute(attribute.getQualifiedName(), attribute.getDefaultValue());
    }
}
