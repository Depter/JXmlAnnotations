package org.decsi.jaxml.parsing.xmlparsing;

import java.lang.reflect.Array;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class ArrayXmlContainerNode extends CollectionXmlContainerNode {
    
    private Class elementClass;
    
    ArrayXmlContainerNode(ElementNode parent, PrimitiveParser parser, XmlChildContainerWrapper wrapper) {
        super(parent, parser, wrapper);
        this.elementClass = wrapper.getElementClass();
    }
    
    @Override
    protected void fillObject(Object object) throws SAXException {
        instance = createArray();
        super.setFieldValueOnParent(wrapper.getAnnotatedElement());
        instance=null;
    }
    
    private Object createArray() {
        int length = values.size();
        Object array = Array.newInstance(elementClass, length);
        fillArrayObject(array);
        return array;
    }
    
    private void fillArrayObject(Object array) {
        Class c = wrapper.getElementClass();
        int length = values.size();
        for(int i=0; i<length; i++)
            addValue(array, values.get(i), i);
            
    }
    
    private void addValue(Object array, Object value, int index) {
        if(byte.class.equals(elementClass))
            Array.setByte(array, index, value==null? 0 : (Byte) value);
        else if(byte.class.equals(elementClass))
            Array.setShort(array, index, value==null? 0 : (Short) value);
        else if(byte.class.equals(elementClass))
            Array.setInt(array, index, value==null? 0 : (Integer) value);
        else if(byte.class.equals(elementClass))
            Array.setLong(array, index, value==null? 0 : (Long) value);
        else if(byte.class.equals(elementClass))
            Array.setFloat(array, index, value==null? 0 : (Float) value);
        else if(byte.class.equals(elementClass))
            Array.setDouble(array, index, value==null? 0 : (Double) value);
        else if(byte.class.equals(elementClass))
            Array.setBoolean(array, index, value==null? false : (Boolean) value);
        else if(byte.class.equals(elementClass))
            Array.setChar(array, index, value==null? 0 : (Character) value);
        else
            Array.set(array, index, value);
    }
}
