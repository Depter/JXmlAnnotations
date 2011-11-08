package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

/**
 *
 * @author Peter Decsi
 */
public class PrimitiveStringWrapper  implements XmlStringValueWrapper<Annotation, AccessibleObject> {
        
    private boolean trimInput;
    private Class clazz;
    
    PrimitiveStringWrapper(Class clazz, boolean trimInput) {
        this.trimInput = trimInput;
        this.clazz = clazz;
    }

    @Override
    public XmlStringValueWrapper getChildWrapper() {
        return null;
    }

    @Override
    public Object getPrimitiveValue(Object instance) throws Exception {
        return instance;
    }

    @Override
    public boolean trimInput() {
        return trimInput;
    }

    @Override
    public Annotation getAnnotation() {
        return null;
    }

    @Override
    public AccessibleObject getAnnotatedElement() {
        return null;
    }
    
    public Class getValueClass() {
        return clazz;
    }
}    