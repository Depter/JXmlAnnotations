package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import static org.decsi.jaxml.wrappers.AnnotatedElementUtil.*;

/**
 *
 * @author Peter Decsi
 */
class AbstractWrapper<A extends Annotation, E extends AnnotatedElement>
        implements Wrapper<A, E> {
    
    private A annotation;
    private E element;

    AbstractWrapper(E element) {
        if(element == null)
            throw new NullPointerException("Annotated element is null!");
        this.element = element;
    }

    AbstractWrapper(E element, A annotation) {
        this(element);
        if(annotation == null)
            throw new NullPointerException("Annotation is null!");
        this.annotation = annotation;
    }
    
    protected void initState(Class<A> annotationClass) {
        if(annotationClass == null)
            throw new NullPointerException("AnnotationClass is null!");
        checkAnnotationIsPresent(annotationClass);
        this.annotation = element.getAnnotation(annotationClass);
    }
    
    protected void checkAnnotationIsPresent(Class annotationClass) {
        if(!element.isAnnotationPresent(annotationClass))
            throw annotationMissingException(annotationClass);
    }
    
    private IllegalArgumentException annotationMissingException(Class annotationClass) {
        String msg = "%s in %s is not annotated with %s";
        msg = String.format(msg, getName(element), getDeclaringClass(element), 
                annotationClass.getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    @Override
    public A getAnnotation() {
        return annotation;
    }

    @Override
    public E getAnnotatedElement() {
        return element;
    }
    
    protected String getElementName() {
        return getName(element);
    }
    
    protected String getClassName() {
        return getDeclaringClass(element).getCanonicalName();
    }
    
    protected String getAnnotationName() {
        return annotation.annotationType().getName();
    }
}
