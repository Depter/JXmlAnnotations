package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 *
 * @author Peter Decsi
 */
public interface NamedWrapper<A extends Annotation, E extends AnnotatedElement> extends Wrapper<A, E> {
    
    public String getName();
    public String getNsPrefix();
    public String getQualifiedName();
}
