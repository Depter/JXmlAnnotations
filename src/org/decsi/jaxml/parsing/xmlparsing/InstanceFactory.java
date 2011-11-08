package org.decsi.jaxml.parsing.xmlparsing;

import java.util.List;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import org.decsi.jaxml.annotation.XmlConstructor;
import org.xml.sax.SAXException;
import static org.decsi.jaxml.parsing.ReflectionUtil.*;

/**
 *
 * @author Peter Decsi
 */
class InstanceFactory {
    
    private final static Comparator<Method> METHOD_COMPARATOR = new MethodComparator();
    
    private static boolean constructorFound = false;
    private static boolean instanceMethodFound = false;
    
    static <T> T newInstance(Class<T> c) throws Exception {
        return newInstance(c, new Object[0], new Class[0]);
    }
    
    static <T> T newInstance(Class<T> c, Object[] arguments, Class[] paramTypes) throws Exception {
        resetState();
        T instance = createInstance(c, arguments, paramTypes);
        return checkState(c, instance);
    }
    
    private static <T> T createInstance(Class<T> c, Object[] arguments, Class[] paramTypes) throws Exception {
        Constructor<T> constructor = getConstructor(c, paramTypes);
        List<Method> methods = getStaticInstanceMethods(c, paramTypes);
        
        if(constructor != null && constructor.isAnnotationPresent(XmlConstructor.class)) {
            constructorFound = true;
            return useConstructor(constructor, arguments);
        } else if(!methods.isEmpty()){
            instanceMethodFound = true;
            return newInstanceFromStaticMethod(c, methods, arguments); 
        } else if(constructor!=null) {
            constructorFound = true;
            return useConstructor(constructor, arguments);
        } else {
            return null;
        }
    }
    
    private static void resetState() {
        constructorFound = false;
        instanceMethodFound = false;
    }
    
    private static <T> T newInstanceFromConstructor(Class<T> c, Object[] arguments, Class[] paramTypes) throws Exception {
        Constructor<T> constructor = getConstructor(c, paramTypes);
        if(constructor == null) return null;
        constructorFound = true;
        return useConstructor(constructor, arguments);
    }
   
    private static <T> T useConstructor(Constructor<T> constructor, Object[] arguments) throws Exception {
        boolean isAccessible = setAccessible(constructor);
        T instance = constructor.newInstance(arguments);
        if(!isAccessible) constructor.setAccessible(isAccessible);
        return instance;
    }
    
    private static boolean setAccessible(AccessibleObject ao) {
        boolean isAccessible = ao.isAccessible();
        if(!isAccessible) ao.setAccessible(true);
        return isAccessible;
    }
    
    private static <T> T newInstanceFromStaticMethod(Class<T> c, List<Method> methods, Object[] arguments) throws Exception {
        Collections.sort(methods, METHOD_COMPARATOR);
        Method m = methods.get(0);
        return (T) useInstanceMethod(m, arguments);
    }
   
    private static Object useInstanceMethod(Method method, Object[] arguments) throws Exception {
        boolean isAccessible = setAccessible(method);
        Object instance = method.invoke(null, arguments);
        if(!isAccessible) method.setAccessible(isAccessible);
        return instance;
    }
    
    private static <T> T checkState(Class c, T value) throws SAXException {
        if(!constructorFound && !instanceMethodFound)
            throwNoNullConstructorError(c);
        clearState();
        return value;
    }
    
    private static void clearState() {
        constructorFound = false;
        instanceMethodFound = false;
    }
    
    private static void throwNoNullConstructorError(Class c) throws SAXException {
        String msg = "Class %s does not have a constructor or instance method with the requested parameters!";
        msg = String.format(msg, c.getCanonicalName());
        throw new SAXException(msg);
    }     
    
    private static class MethodComparator implements Comparator<Method> {

        @Override
        public int compare(Method o1, Method o2) {
            if(o1==null) return o2==null? 0 : 1;
            if(o2==null) return -1;
            return compareNotNull(o1, o2);
        }
        
        private int compareNotNull(Method o1, Method o2) {
            if(o1.isAnnotationPresent(XmlConstructor.class))
                return o2.isAnnotationPresent(XmlConstructor.class)? compareName(o1, o2) : -1;
            return o2.isAnnotationPresent(XmlConstructor.class)? 1 : compareName(o1, o2);
        }
        
        private int compareName(Method o1, Method o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    
    }
}
