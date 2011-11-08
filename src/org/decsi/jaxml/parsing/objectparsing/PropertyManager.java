package org.decsi.jaxml.parsing.objectparsing;

import java.util.EnumMap;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author Peter Decsi
 */
class PropertyManager {
    
    private EnumMap<SAXProperty, Object> properties = new EnumMap<SAXProperty, Object>(SAXProperty.class);
    {
        for(SAXProperty property : SAXProperty.values())
            properties.put(property, property.getDefaultValue());
    }

    PropertyManager() {
    }
    
    Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXProperty property = getPropertyForName(name);
        return properties.get(property);
    }
    
    private SAXProperty getPropertyForName(String name) throws SAXNotSupportedException {
        SAXProperty property = SAXProperty.getInstance(name);
        if(property == null)
            throw new SAXNotSupportedException(name);
        return property;
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXProperty property = getPropertyForName(name);
        if(property.isReadOnly()) 
            throw new SAXNotRecognizedException(name);
        properties.put(property, value);
    }
    
    Object getProperty(SAXProperty property) {
        return properties.get(property);
    }
}
