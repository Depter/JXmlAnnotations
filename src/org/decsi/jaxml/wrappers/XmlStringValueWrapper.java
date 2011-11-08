package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

/**
 *
 * @author Peter Decsi
 */
public interface XmlStringValueWrapper<A extends Annotation, E extends AccessibleObject> 
        extends //ValueWrapper<A>, 
        Wrapper<A, E> {
    
    public XmlStringValueWrapper getChildWrapper();
    public Object getPrimitiveValue(Object instance) throws Exception;
    public boolean trimInput();
}
