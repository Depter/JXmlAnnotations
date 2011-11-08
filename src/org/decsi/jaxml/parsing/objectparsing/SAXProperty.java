package org.decsi.jaxml.parsing.objectparsing;

/**
 *
 * @author Peter Decsi
 */
public enum SAXProperty {
    
    /**
     * Defines the character, separating the name
     * from the prefix.
     */
    NAMESPACE_SEPARATOR("namespace-sep", String.class, false, ":");
    
    private String propertyName;
    private Class valueClass;
    private boolean isReadOnly;
    private Object defaultValue;
 
    private SAXProperty(String name, Class c, boolean readOnly, Object defaultValue) {
        this.propertyName = name;
        this.valueClass = c;
        this.isReadOnly = readOnly;
        this.defaultValue = defaultValue;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    public Class getValueClass() {
        return valueClass;
    }
    
    public boolean isReadOnly() {
        return isReadOnly;
    }
    
    public Object getDefaultValue() {
        return defaultValue;
    }
    
    public static SAXProperty getInstance(String propertyName) {
        for(SAXProperty property : values())
            if(property.propertyName.equalsIgnoreCase(propertyName))
                return property;
        return null;
    }
}
