package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
public interface NamespaceOwner<A extends Annotation, E extends AnnotatedElement> 
            extends NamedWrapper<A, E> {
    
    public List<XmlNamespaceWrapper> getNamespaces();
}
