package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 *
 * @author Peter Decsi
 */
public interface Wrapper<A extends Annotation, E extends AnnotatedElement> {
    
    public A getAnnotation();
    public E getAnnotatedElement();
}
