package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.Field;
import org.decsi.jaxml.parsing.ReflectionUtil;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractNode implements ElementNode{
    
    private ElementNode parent;
    protected PrimitiveParser parser;
    protected Object instance;
    
    AbstractNode(ElementNode parent, PrimitiveParser parser) {
        this.parent = parent;
        this.parser = parser;
    }
    
    @Override
    public ElementNode getParent() {
        return parent;
    }
    
    @Override
    public Object getInstance() {
        return instance;
    }
    
    @Override
    public void endElement() throws SAXException {
    }
    
    protected void setFieldValueOnParent(Field field) throws SAXException {
        try {
            ReflectionUtil.setFieldValue(field, parent.getInstance(), instance);
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }
    
    @Override
    public void resetElement() {
        this.instance = null;
    }
    
}
