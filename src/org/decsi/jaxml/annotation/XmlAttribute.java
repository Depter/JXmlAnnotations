package org.decsi.jaxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to mark fields of a class, 
 * that can be added as an attribute for the tag,
 * representing the instance of the class.
 * 
 * <p>The class, containing the annotated field should 
 * be annotated with {@link XmlElement XmlElement} 
 * annotation.</p>
 * 
 * <p> The marked field should provide a value that is 
 * either a primitve, wrapper for a primitive, String
 * or a class that has a mebmer which is marked
 * with {@link XmlStringValue XmlStringValue}.</p>
 * 
 * <p>The next example we have two attributes (<tt>name</tt>,
 * and <tt>type</tt>). The <tt>name</tt> will be always added
 * but <tt>type</tt> is only when the value of field 
 * <tt>type != Type.NORMAL</tt>.
 * 
 * <pre>
 * {@code 
 *  
 *   @XmlElement(name="test")
 *   class MyAttributeClass {
 *      @XmlAttribute(name="name")
 *      private String name;
 * 
 *      @XmlAttribute(name="type", defaultValue=Type.NORMAL.getName())
 *      private Type type;
 *   }
 * 
 *   enum Type {
 *      NORMAL("normal"),
 *      ABNORMAL("abnormal");
 * 
 *      @XmlStringValue
 *      private String name;
 *      
 *      private Type(String name) {
 *          this.name = name;
 *      }
 * 
 *      public static Type getType(String name) {
 *          if(NORMAL.name.equalsIgnoreCase(name))
 *              return NORMAL;
 *          return ABNORMAL;
 *      }
 * 
 *      public String getName() {
 *          return name;
 *      }
 *   }
 * }
 * </pre>
 * @author Peter Decsi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlAttribute {
    /**
     * The name of the attribute.
     * 
     * <p>A call to <tt>name.trim()</tt> must not
     * result in an empty string.</p>
     */
    String name();
    
    /**
     * The prefix for the namespace.
     * 
     * Use {@link XmlElement.NULL} to indicate 
     * no prefix.
     */
    String nsPrefix() default XmlElement.NULL;
    
    /**
     * The default value of this attribute.
     * 
     * Use {@link XmlElement.NULL} or omit this 
     * property to indicate no default value.
     * 
     * <p>When parsing objects and a field has a null value, the
     * default value will be used as the value of the attribute.</p>
     * 
     * <p> When creating instances from xml and no attribute
     * found for the field, and the default value is
     * not <tt>null</tt> the default value will be used
     * to initialize the field.<.p>
     * </p>
     */
    String defaultValue() default XmlElement.NULL;
    
    /**
     * Signals wether the attribute should be added to the
     * element, if it's value is the default value.
     */
    boolean addIfDefault() default false;
    
    /**
     * This attribute represents the order of the attributes
     * within their parent. 
     * 
     * <p>The index is 0 based. Value of less than <tt>0</tt> 
     * means no index is specified and the attribute goes to 
     * the end.</p>
     * 
     * <p>One positive index can be only used
     * for one attribute.</p>
     */
    int index() default -1;
    
    /**
     * Sets the xml-attributettype.
     */
    Type type() default Type.CDATA;
    
    public static enum Type {
        CDATA, 
        ID, 
        IDREF, 
        IDREFS, 
        NMTOKEN, 
        NMTOKENS, 
        ENTITY, 
        ENTITIES, 
        NOTATION;
    }
}
