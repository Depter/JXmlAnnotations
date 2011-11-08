package org.decsi.jaxml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field or method of classes, that can 
 * provide a stirng value.
 * 
 * <p>There can be only one element marked with this 
 * annotation in a class. The marked element or
 * method should provide a value that is either
 * a primitve, wrapper for a primitive, String
 * or a class that has a mebmer which is marked
 * with this annotation.</p>
 * 
 * <p>The class should have a static instance method
 * or a constructor, that accepts the value, it returns
 * as a string value. In case both a constructor and a
 * static instance method is present, the static instance 
 * method will be used.</p>
 * 
 * <p>
 * The following examples are both valid classes, and 
 * they can be used to set the string content for 
 * xml-elements or the value of xml-attributes.
 * <pre>
 * {@code 
 *  class MyStringValue {
 *      public static MyStringValue getInstance(String name) {
 *          MyStringValue value = new MyStringValue();
 *          value.name = name;
 *          return value;
 *      }
 *  
 *      @XmlStringValue
 *      private String name;
 *  } 
 * 
 *  class MyObjectValue {
 *    @XmlStringValue
 *    private MyStringValue value;
 * 
 *    MyObjectValue(MyStringValue value) {
 *       this.value = value;
 *    }
 *   }
 * }
 * </pre>
 * 
 * @author Peter Decsi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface XmlStringValue {
    
    public boolean trim() default true;
}
