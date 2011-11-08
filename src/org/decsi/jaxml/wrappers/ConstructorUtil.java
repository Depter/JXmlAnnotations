package org.decsi.jaxml.wrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 *
 * @author Peter Decsi
 */
class ConstructorUtil {
    
    private ConstructorUtil() {}
    
    static boolean isConstructable(Class c, Class... parameters) {
        if(notStaticInnerClass(c)) return false;
        return getInstanceMethod(c, parameters) != null ||
               getConstructor(c, parameters) != null;
    }
    
    private static boolean notStaticInnerClass(Class c) {
        if(!c.isMemberClass()) return false;
        return !Modifier.isStatic(c.getModifiers());
    }
    
    static Constructor getConstructor(Class c, Class... parameters) {
        try {
            return c.getDeclaredConstructor(parameters);
        } catch (Exception ex) {
            return null;
        }
    }
    
    static Method getInstanceMethod(Class c, Class... parameters) {
        for(Method m : c.getDeclaredMethods())
            if(isStaticInstanceMethod(m, parameters))
                return m;
        return null;
    }
    
    private static boolean isStaticInstanceMethod(Method m, Class... parameters) {
        if(!Modifier.isStatic(m.getModifiers())) return false;
        if(!m.getReturnType().equals(m.getDeclaringClass())) return false;
        return Arrays.equals(m.getParameterTypes(), parameters);
    }
}
