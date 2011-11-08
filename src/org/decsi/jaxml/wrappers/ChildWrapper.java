package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
public interface ChildWrapper<A extends Annotation>
        extends Wrapper<A, Field>, NamespaceOwner<A, Field>, 
                ValueWrapper<Field>, IndexedWrapper {
    
    public boolean addIfNull();
    public List<XmlAttributeWrapper> getAttributes();
    
    /**
     * Returns true, if this child is the child of it's
     * parent element with the qualified name.
     */
    public boolean isChildOfParent(String qualifiedName);
}
