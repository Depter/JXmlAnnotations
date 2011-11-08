package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class PrimitiveStringNode extends AbstractNode {
    
    private String trueValue;
    //private XmlStringValueWrapper wrapper;
    private Class valueClass;
    
    PrimitiveStringNode(AbstractNode parent, PrimitiveParser parser, Class clazz) {
        super(parent, parser);
        this.valueClass = clazz;
        this.trueValue = parser.getBooleanValue(true);
    }

    @Override
    public ElementNode createChildNode(String name, Attributes attributes) {
        throw new UnsupportedOperationException("PrimitiveStringElementNode can not create child nodes!");
    }

    @Override
    public void setString(String value) throws SAXException {
        if(isNull(value)) 
            value=null;
        try {
            if(String.class.equals(valueClass)) instance=value;
            else if(isByte(valueClass)) 
                instance = value==null? Byte.valueOf((byte)0) : Byte.valueOf(value.trim());
            else if(isShort(valueClass)) 
                instance = value==null? Short.valueOf((short)0) : Short.valueOf(value.trim());
            else if(isInt(valueClass)) 
                instance = value==null? Integer.valueOf((short)0) : Integer.valueOf(value.trim());
            else if(isLong(valueClass)) 
                instance = value==null? Long.valueOf(0) : Long.valueOf(value.trim());
            else if(isFloat(valueClass)) 
                instance = value==null? Float.valueOf(0) : Float.valueOf(value.trim());
            else if(isDouble(valueClass)) 
                instance = value==null? Double.valueOf(0) : Double.valueOf(value.trim());
            else if(isBoolean(valueClass)) 
                initBoolean(value.trim());
            else if(isCharacter(valueClass)) 
                initCharacter(value.trim());
            else throw notPrimitiveClassException(valueClass);
        } catch (Exception ex) {
            throw new SAXException(ex.getMessage());
        }
        
    }
    
    private boolean isNull(String value) {
        return value==null || value.length()==0;
    }
    
    private boolean isByte(Class c) {
        return byte.class.equals(c) || Byte.class.equals(c);
    }
    
    private boolean isShort(Class c) {
        return short.class.equals(c) || Short.class.equals(c);
    }
    
    private boolean isInt(Class c) {
        return int.class.equals(c) || Integer.class.equals(c);
    }
    
    private boolean isLong(Class c) {
        return long.class.equals(c) || Long.class.equals(c);
    }
    
    private boolean isFloat(Class c) {
        return float.class.equals(c) || Float.class.equals(c);
    }
    
    private boolean isDouble(Class c) {
        return double.class.equals(c) || Double.class.equals(c);
    }
    
    private boolean isBoolean(Class c) {
        return boolean.class.equals(c) || Boolean.class.equals(c);
    }
    
    private boolean isCharacter(Class c) {
        return char.class.equals(c) || Character.class.equals(c);
    }
    
    private void initBoolean(String value) {
        instance = trueValue.equalsIgnoreCase(value);
    }
    
    private void initCharacter(String value) throws SAXException {
        if(value==null) {
            instance = Character.valueOf((char)0);
        } else {
            if(value.length() > 1)
                throw new SAXException(value+" is not a character!");
            instance = Character.valueOf(value.charAt(0));
        }
    }

    private SAXException notPrimitiveClassException(Class c) {
        String msg = "Class %s is not a primitive or prmiitve wrapper!";
        msg = String.format(msg, c.getCanonicalName());
        return new SAXException(msg);
    }

    @Override
    public boolean hasStringContent() {
        return true;
    }

    @Override
    public boolean trimStringContent() {
        return true;
    }
}
