package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.decsi.jaxml.parsing.ReflectionUtil;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class MapXmlContainerNode extends AbstractXmlContainerNode{
    
    private List values = new ArrayList();
    private List keys = new ArrayList();
    private boolean containerCreated = false;
    private boolean firstChildCall = true;
    private KeyParser keyParser;
    private Object key;
    
    MapXmlContainerNode(ElementNode parent, PrimitiveParser parser, XmlChildContainerWrapper wrapper) {
        super(parent, parser, wrapper);
        initKeyParser();
    }
    
    private void initKeyParser() {
        XmlAttributeWrapper keyAttribute = getKeyWrapper();
        keyParser = new KeyParser(keyAttribute);
    }
    
    private XmlAttributeWrapper getKeyWrapper() {
        String keyName = wrapper.getQualifiedKeyName();
        for(XmlAttributeWrapper attribute : wrapper.getElementWrapper().getAttributes())
            if(attribute.getQualifiedName().equalsIgnoreCase(keyName))
                return attribute;
        throw noKeyAttributeFound();
    }
    
    private IllegalArgumentException noKeyAttributeFound() {
        String msg = "Key attribute in wrapper %s is not found!";
        msg = String.format(msg, wrapper.toString());
        return new IllegalArgumentException(msg);
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
        addPreviousChild();
        return createNewChildInstance(name, attributes);
    }
    
    private void addPreviousChild() {
        if(firstChildCall) return;
        addChild();
        resetChild();
    }
    
    private void addChild() {
        keys.add(key);
        values.add(child.getInstance());
    }
    
    private void resetChild() {
        key = null;
        child.resetElement();
    }
    
    private ElementNode createNewChildInstance(String name, Attributes attributes) throws SAXException {
        firstChildCall = false;
        keyParser.getKey(attributes);
        return child.createChildNode(name, attributes);
    }
    
    @Override
    protected void setValue(Object object) throws SAXException {
        if(object == null) return;
        if(!firstChildCall)
            addChild();
        fillObject(object);
    }
    
    protected void fillObject(Object object) throws SAXException {
        Map fieldValue = getFieldValue(object);
        if(fieldValue == null)
            throw fieldValueNotInitializedExeption();
        fillMap(fieldValue);
    }
    
    private Map getFieldValue(Object object) throws SAXException {
        try {
            return (Map) ReflectionUtil.getFieldValue(wrapper.getAnnotatedElement(), object);
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
    
    private void fillMap(Map map) {
        int length = values.size();
        for(int i=0; i<length; i++)
            map.put(keys.get(i), values.get(i));
    }
    
    @Override
    public void resetElement() {
        super.resetElement();
        values.clear();
    }
    
    private class KeyParser {
        
        private String keyName;
        private ElementNode node;
        
        KeyParser(XmlAttributeWrapper attribute) {
            this.keyName = attribute.getQualifiedName();
            initNode(attribute);
        }
        
        private void initNode(XmlAttributeWrapper attribute) {
                node = new XmlStringValueNode(null, parser, wrapper.getKeyClass(), attribute.getChildWrapper());
            //XmlStringValueWrapper stringWrapper = attribute.getChildWrapper();
            //if(stringWrapper.getChildWrapper() instanceof PrimitiveValueWrapper)
            //    node = new PrimitiveStringNode(null, parser, (PrimitiveValueWrapper)stringWrapper.getChildWrapper());
            //else
            //    node = new XmlStringValueNode(null, parser, wrapper.getKeyClass(), stringWrapper.getChildWrapper());
        }
        
        void getKey(Attributes attributes) throws SAXException {
            String value = attributes.getValue(keyName);
            node.setString(value);
            key = node.getInstance();
            node.resetElement();
        }
        
    }
}
