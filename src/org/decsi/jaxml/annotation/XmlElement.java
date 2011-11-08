package org.decsi.jaxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to annotate classes, 
 * that can be represented as an xml tag.
 * 
 * <b>Name:</b>
 * This value is used as a tag name in the xml document
 * for elements of this object.
 * 
 * <b>nsPrefix:</b>
 * This value is used as the namespace prefix for
 * the xml-tag. The qualified name of the
 * xml-element will be 'nsPrefix:name' or
 * 'name' if nsPrefix is not set.
 * 
 * @author Peter Decsi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XmlElement {
    /**
     * This value is used as the tag name. 
     * 
     * Together with the nsPrefix they represent the qualified name of the tag.
     */
    String name();
    /**
     * This value is used as the namespace prefix for the xml tags.
     * 
     * Use {@link #NULL} to represent null values.
     */
    String nsPrefix() default NULL;
    
    /**
     * Via this values namespace declarations can be provided.
     * 
     * <p>The declarations should be provided in "prefix=uri" format.
     * One default uri can be provided, by simply writing the uri.</p>
     * 
     * Use {@link #NULL} to represent null values.
     */
    String[] namespaces() default NULL;
    
    /**
     * Use this constant instead of null values.
     */
    public static final String NULL = "";
}
