package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChildContainer;

/**
 *
 * @author Peter Decsi
 */
class AttributeBuilder extends AbstractContentBuilder<XmlAttributeWrapper> {
    
    @Override
    protected boolean isContent(Field field) {
        if(!field.isAnnotationPresent(XmlAttribute.class)) return false;
        if(field.isAnnotationPresent(XmlChildContainer.class)) return false;
        return true;
    }

    @Override
    protected XmlAttributeWrapper buildContent(Field field) {
        return new XmlAttributeWrapperImpl(field);
    }

    @Override
    protected IllegalArgumentException indexExistsException(XmlAttributeWrapper old, XmlAttributeWrapper wrapper) {
        Field oldField = old.getAnnotatedElement();
        String msg = "Index %d is used for '%s' as well as '%s' in class %s!";
        msg = String.format(msg, old.getIndex(), oldField.getName(), 
                wrapper.getAnnotatedElement().getName(), 
                oldField.getDeclaringClass().getCanonicalName());
        return new IllegalArgumentException(msg);
    }

    @Override
    protected IllegalArgumentException nameExistsException(XmlAttributeWrapper old, XmlAttributeWrapper wrapper) {
        Field oldField = old.getAnnotatedElement();
        String msg = "Name '%s' is used for '%s' as well as '%s' in class %s!";
        msg = String.format(msg, old.getQualifiedName(), oldField.getName(),
                wrapper.getAnnotatedElement().getName(), 
                oldField.getDeclaringClass().getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
}
