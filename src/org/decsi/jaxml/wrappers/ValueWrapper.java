package org.decsi.jaxml.wrappers;

import java.lang.reflect.AccessibleObject;

/**
 *
 * @author Peter Decsi
 */
public interface ValueWrapper<A extends AccessibleObject> {
    
    public Class getValueClass();
    
    public Object getValue(Object instance) throws Exception;
    
    public A getMember();
    
}
