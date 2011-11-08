package org.decsi.jaxml.wrappers;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;

/**
 *
 * @author Peter Decsi
 */
class AnnotatedElementUtil {
    
    private AnnotatedElementUtil() {}
    
    static String getName(AnnotatedElement element) {
        if(element == null) return null;
        if(element instanceof Class) 
            return ((Class) element).getCanonicalName();
        if(element instanceof Field) 
            return ((Field) element).getName();
        if(element instanceof Method)
            return ((Method) element).getName()+"()";
        return null;
    }
    
    static Class getDeclaringClass(AnnotatedElement element) {
        if(element == null) return null;
        if(element instanceof Class) 
            return (Class) element;
        if(element instanceof Member) 
            return ((Member) element).getDeclaringClass();
        return null;
    }
}
