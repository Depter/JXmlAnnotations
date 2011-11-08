package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;

/**
 *
 * @author Peter Decsi
 */
public class WrapperFactory {
    
    private WrapperFactory(){}
    
    public static XmlElementWrapper getElementWrapper(Class c) {
        return new XmlElementWrapperImpl(c);
    }
    
    public static XmlAttributeWrapper getAttributeWrapper(Field field) {
        return new XmlAttributeWrapperImpl(field);
    }
    
    public static XmlChildWrapper getChildWrapper(Field field) {
        return new XmlChildWrapperImpl(field);
    }
    
    public static XmlChildContainerWrapper getChildContainerWrapper(Field field) {
        return new XmlChildContainerWrapperImpl(field);
    }
    
    public static XmlStringValueWrapper getStringValueWrapper(Class c) {
        return XmlStringValueWrapperBuilder.getWrapperForClass(c);
    }
}
