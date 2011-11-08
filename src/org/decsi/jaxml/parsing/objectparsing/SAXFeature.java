package org.decsi.jaxml.parsing.objectparsing;

/**
 *
 * @author Peter Decsi
 */
public enum SAXFeature {

    /**
     * <b>TRUE</b>
     * Prefixes will be stripped off the element and attribute names 
     * and replaced with the corresponding namespace URIs.
     * 
     * <p>By default, the two will simply be concatenated, but the 
     * namespace-sep core property allows the application to specify a 
     * delimiter string for separating the URI part and the local part.</p>
     * 
     * <p><b>FALSE</b>
     * Do not perform namespace processing.
     * </p>
     */
    NAMESPACE("http://xml.org/sax/features/namespaces", true, false),
    
    /**
     * <b>TRUE</b>
     * Report the original prefixed names and attributes used for 
     * namespace declarations.
     * 
     * <p><b>FALSE</b>
     * Do not report attributes used for Namespace declarations, and 
     * optionally do not report original prefixed names.
     * </p>
     */
    NAMESPACE_PREFIX("http://xml.org/sax/features/namespace-prefixes", false, false),
    
    /**
     * <b>TRUE</b>
     * Validate the document and report validity errors.
     * 
     * <p><b>FALSE</b>
     * Do not report validity errors.
     * </p>
     */
    VALIDATION("http://xml.org/sax/features/validation", true, true);
    
    private String featureName;
    private boolean defaultValue;
    private boolean readOnly;
    
    private SAXFeature(String name, boolean defaultValue, boolean readOnly) {
        this.featureName = name;
        this.defaultValue = defaultValue;
        this.readOnly = readOnly;
    }
    
    public boolean getDefaultValue() {
        return defaultValue;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }
    
    public String getFeatureName() {
        return featureName;
    }
    
    public static SAXFeature getInstance(String name) {
        if(name == null) return null;
        for(SAXFeature feature : values())
            if(name.equalsIgnoreCase(feature.featureName))
                return feature;
        return null;
    } 
    
}
