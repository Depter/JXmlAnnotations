package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlChildContainer;

/**
 *
 * @author Peter Decsi
 */
class ChildBuilder extends AbstractContentBuilder<ChildWrapper> {

    @Override
    protected boolean isContent(Field field) {
        return isContainer(field) || isChild(field);
    }

    private boolean isContainer(Field field) {
        return field.isAnnotationPresent(XmlChildContainer.class);
    }
    
    private boolean isChild(Field field) {
        return !isContainer(field) &&
                field.isAnnotationPresent(XmlChild.class);
    }
    
    @Override
    protected ChildWrapper buildContent(Field field) {
        if(isContainer(field))
            return new XmlChildContainerWrapperImpl(field);
        return new XmlChildWrapperImpl(field);
    }

    @Override
    protected IllegalArgumentException indexExistsException(ChildWrapper old, ChildWrapper wrapper) {
        Field oldField = (Field) old.getAnnotatedElement();
        String msg = "Index %d is used for '%s' as well as '%s' in class %s!";
        msg = String.format(msg, old.getIndex(), oldField.getName(), 
                ((Field) wrapper.getAnnotatedElement()).getName(), 
                oldField.getDeclaringClass().getCanonicalName());
        return new IllegalArgumentException(msg);
    }

    @Override
    protected IllegalArgumentException nameExistsException(ChildWrapper old, ChildWrapper wrapper) {
        Field oldField = (Field) old.getAnnotatedElement();
        String msg = "Name '%s' is used for '%s' as well as '%s' in class %s!";
        msg = String.format(msg, old.getName(), oldField.getName(), 
                ((Field) wrapper.getAnnotatedElement()).getName(), 
                oldField.getDeclaringClass().getCanonicalName());
        return new IllegalArgumentException(msg);
    }    
}
