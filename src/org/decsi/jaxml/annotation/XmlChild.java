package org.decsi.jaxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark fields of a class, 
 * that can be added as a child element for the class.
 * 
 * <p> The marked field should provide a value that is 
 * either a primitve, wrapper for a primitive, String,
 * a class that has a mebmer which is marked with 
 * {@link XmlStringValue XmlStringValue} or a class
 * that is annotated with {@link XmlElement XmlElement}.</p>
 * 
 * <p>In case the value is not an {@link XmlElement XmlElement}
 * this annotation should provide a name for the child-element.
 * In other cases the user can override the values for the
 * {@link XmlElement XmlElement} annotation.</p>
 * 
 * @author Peter Decsi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlChild {
    
    /**
     * This value is used as a tag name for the field.
     * 
     * <p>This value can be omitted in case the valuetype of 
     * the field is annotated with {@link XmlElement XmlElement}.
     * </p>
     * 
     * <p>If the value of the field is annotated with
     * {@link XmlElement XmlElement} the use of new
     * tag-name, namespace must be indicated by 
     * {@link #overrideElement() overrideElement()}.
     */
    String name() default XmlElement.NULL;
    
    /**
     * This value is used as a prefix name.
     * 
     * <p>This value can be omitted in case the valuetype of 
     * the field is annotated with {@link XmlElement XmlElement}.
     * </p>
     * 
     * <p>If the value of the field is annotated with
     * {@link XmlElement XmlElement} the use of new tag-name, 
     * namespace must be indicated by 
     * {@link #overrideElement() overrideElement()}.</p>
     */
    String nsPrefix() default XmlElement.NULL;

    /**
     * This attribute represents the order of the child elements
     * within their parent. 
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
     */
    boolean addIfNull() default true;
    
    /**
     * Indicates wether the values for this annotation
     * should owerride the values in the child's class,
     * if it is annotated with {@link XmlElement XmlElement}.
     * 
     * <p>If the value is not annotated with 
     * {@link XmlElement XmlElement} this vlaue is 
     * disregarded.</p>
     * 
     * <p>If the value is annotated {@link XmlElement XmlElement}
     * and the annotation does not provide a {@link #name() name},
     * then this value is automatically considered <tt>false</tt>.
     * </p>
     * 
     * <p> If there are attributes, defined here, and also
     * by the XmlElement, then the attribute also can be overriden
     * according to this value.
     * </p>
     */
    boolean overrideElement() default false;
    
    /**
     * List of additional attributes in "name=value" format;
     * 
     * <p>When the type of the field, annotated with this element is
     * annotated with {@linkplain XmlElement} and both annotations
     * declare an attribute with the same qualified name, the 
     * {@linkplain #overrideElement()} decides, which declaration will 
     * be used.</p>
     */
    String[] attributes() default XmlElement.NULL;
    
    /**
     * Via this values namespace declarations can be provided.
     * 
     * <p>The declarations should be provided in "prefix=uri" format.
     * One default uri can be provided, by simply writing the uri.</p>
     * 
     * <p>When the type of the field, annotated with this element is
     * annotated with {@linkplain XmlElement} and both annotations
     * declare the same prefix, the {@linkplain #overrideElement()}
     * decides, which declaration will be used.
     * </p>
     * 
     * Use {@link #NULL} to represent null values.
     */
    String[] namespaces() default XmlElement.NULL;
    
}
