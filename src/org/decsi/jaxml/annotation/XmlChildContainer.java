package org.decsi.jaxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark fields of a class, 
 * that are a containers for child elements.
 * 
 * <p>The class, containing the annotated field should 
 * be annotated with {@link XmlElement XmlElement} 
 * annotation.</p>
 * 
 * <p>Currently supported values are Arrays and classes, that
 * implement the {@link java.util.Collection Collection} or
 * the {@link java.util.Map Map} interfaces.</p>
 * 
 * <p>After initializing the class the value of these fields
 * must not be null.</p>
 * 
 * <p>The container should hold primitives, primitive wrappers,
 * Strings and objects, that have {@link XmlStringValue XmlStringValue}
 * or {@link XmlElement XmlElement} annotations.</p>
 * 
 * <p>If the field contains a {@linkplain java.util.Map} then 
 * an {@linkplain XmlAttribute} annotation also must be present.
 * This annotation is used to add an attribute representing the
 * key value for the elements, representing the content of the Map.
 * </p>
 * 
 * @author Peter Decsi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlChildContainer {
    
    /**
     * This value is used as a tag name for the field.
     * 
     * <p>If {@linkplain #addOnlyContainedElements()} is
     * <tt>true</tt> then this value can be omitted, in
     * other cases it must have a value.</p>
     */
    String name() default XmlElement.NULL;
    
    /**
     * This value is used as a prefix name.
     */
    String nsPrefix() default XmlElement.NULL;

    /**
     * This attribute represents the order of the child elements
     * within their parent. 
     * 
     * <p>If {@linkplain #addOnlyContainedElements()} is
     * <tt>true</tt> then this index marks the index for
     * the child elements.</p>
     * 
     * <p>The index is 0 based. Value of less than <tt>0</tt> 
     * means no index is specified and the element goes to 
     * the end.</p>
     * 
     * <p>One positive index can be only used
     * for one element.</p>
     */
    int index() default -1;

    /**
     * If this value is <tt>true</tt>, an empty tag is always 
     * added for <tt>null</tt> values, otherwise if the fields 
     * value is <tt>null</tt>, no child element will be added to 
     * the xml representation.
     * 
     * <p>If {@linkplain #addOnlyContainedElements()} is <tt>true</tt>
     * then this value will be disregarded.</p>
     */
    boolean addIfNull() default true;
    
    /*
     * Indicates that only the elements of a collection
     * should be added to the parent element, the collection 
     * itself not.
     * 
     * This attribute is used when the annotated field
     * contains one of the supported collections.
     */
    boolean addOnlyContainedElements() default false;
    
    
    /*
     * If this value is <tt>true</tt>, an empty tag is always 
     * added for empty collections/maps/arrays, otherwise if 
     * no child element will be added to the xml representation.
     * 
     * <p>If {@linkplain #addOnlyContainedElements()} is <tt>true</tt>
     * then this value will be disregarded.</p>
     */
    boolean addIfEmpty() default true;
    
    /**
     * Identifies the class of keys if the container is a map.
     * 
     * <p>The values should be one of String, primitve wrappers or
     * a class, that has a field/method annotated with
     * {@link XmlStringValue XmlStringValue} annotation.</p>
     */
    Class keyClass() default Object.class;
    
    /**
     * Identifies the class of elements in the container.
     * 
     * <p> The value should be either a primitve, a 
     * wrapper for a primitive, String, a class that has 
     * a mebmer which is marked with {@linkplain XmlStringValue}
     * or a class that is annotated with {@linkplain XmlElement}.
     * </p>
     */
    Class elementClass();
    
    /**
     * List of additional attributes in "name=value" format;
     */
    String[] attributes() default XmlElement.NULL;
    
    /**
     * Via this values namespace declarations can be provided.
     * 
     * <p>The declarations should be provided in "prefix=uri" format.
     * One default uri can be provided, by simply writing the uri.</p>
     * 
     * Use {@link #NULL} to represent null values.
     */
    String[] namespaces() default XmlElement.NULL;
    
}
