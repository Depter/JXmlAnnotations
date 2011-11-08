package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.decsi.jaxml.parsing.ReflectionUtil;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class CollectionXmlContainerNode extends AbstractXmlContainerNode {
    
    protected List values = new ArrayList();
    private boolean containerCreated = false;
    private boolean firstChildCall = true;
    
    CollectionXmlContainerNode(ElementNode parent, PrimitiveParser parser, XmlChildContainerWrapper wrapper) {
        super(parent, parser, wrapper);
    }

    @Override
    public ElementNode createChildNode(String name, Attributes attributes) throws SAXException {
        if(!containerCreated &&
           !wrapper.addOnlyContainedElements()) {
            containerCreated = true;
            return this;
        } else {
            return createNodeFromChild(name, attributes);
        }
    }
    
    private ElementNode createNodeFromChild(String name, Attributes attributes) throws SAXException {
        if(!firstChildCall) {
            values.add(child.getInstance());
            child.resetElement();
        }
        firstChildCall = false;
        return child.createChildNode(name, attributes);
    }

    @Override
    public void setValue(Object object) throws SAXException {
        if(object == null) return;
        if(!firstChildCall)
            values.add(child.getInstance());
        fillObject(object);
    }
    
    protected void fillObject(Object object) throws SAXException {
        Collection fieldValue = getFieldValue(object);
        if(fieldValue == null)
            throw fieldValueNotInitializedExeption();
        fieldValue.addAll(values);
    }
    
    private Collection getFieldValue(Object object) throws SAXException {
        try {
            return (Collection) ReflectionUtil.getFieldValue(wrapper.getAnnotatedElement(), object);
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }    
    
    private SAXException fieldValueNotInitializedExeption() {
        Field field = wrapper.getAnnotatedElement();
        String msg = "Field '%s' in class %s is null, thus it can not be filled from xml!";
        msg = String.format(msg, field.getName(), field.getDeclaringClass().getCanonicalName());
        return new SAXException(msg);
    }
    
    
    @Override
    public void resetElement() {
        super.resetElement();
        values.clear();
    }
}
