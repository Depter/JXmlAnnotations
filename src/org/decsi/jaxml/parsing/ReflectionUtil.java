package org.decsi.jaxml.parsing;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
public class ReflectionUtil {
    private ReflectionUtil() {}
    
    public static List<Method> getAnnotatedMethods(Class c, Class... annotationClass) {
        List<Method> methods = new ArrayList<Method>();
        for(Method m : c.getDeclaredMethods())
            if(isAnnotationPresent(m, annotationClass))
                methods.add(m);
        return methods;
    }
    
    public static List<Field> getAnnotatedFields(Class c, Class... annotationClass) {
        List<Field> fields = new ArrayList<Field>();
        for(Field f : c.getDeclaredFields())
            if(isAnnotationPresent(f, annotationClass))
                fields.add(f);
        return fields;
    }
    
    private static boolean isAnnotationPresent(AccessibleObject member, Class[] annotationClasses) {
        for(Class c : annotationClasses)
            if(member.isAnnotationPresent(c))
                return true;
        return false;
    }
    
    public static Object getFieldValue(Field field, Object o) throws IllegalArgumentException, 
                                                                 IllegalAccessException {
        boolean accessible = field.isAccessible();
        try {
            if(!accessible) field.setAccessible(true);
            return field.get(o);
        } finally {
            if(!accessible) field.setAccessible(false);
        }
    }
    
    public static void setFieldValue(Field field, Object instance, Object value) throws IllegalArgumentException, 
                                                                                 IllegalAccessException {
        boolean accessible = field.isAccessible();
        try {
            if(!accessible) field.setAccessible(true);
            field.set(instance, value);
        } finally {
            if(!accessible) field.setAccessible(false);
        }
    }
    
    public static Object invokeMethod(Method method, Object instance, Object... parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean accessible = method.isAccessible();
        try {
            if(!accessible) method.setAccessible(true);
            return method.invoke(instance, parameters);
        } finally {
            if(!accessible) method.setAccessible(accessible);
        }
    }
    
    static <T> Constructor<T> getZeroParamConstructor(Class<T> c) {
        return getConstructor(c, new Class[0]);
    }
    
    public static <T> Constructor<T> getConstructor(Class<T> c, Class... paramTypes) {
        for(Constructor constructor : c.getDeclaredConstructors())
            if(Arrays.equals(constructor.getParameterTypes(), paramTypes))
                return constructor;
        return null;
    }
    
    static Method getStaticInstanceMethod(Class c) {
        return getStaticInstanceMethod(c, new Class[0]);
    }
    
    public static Method getStaticInstanceMethod(Class c, Class... paramTypes) {
        for(Method m : c.getDeclaredMethods())
            if(isStaticInstanceMethod(m, c) && needsParameters(m, paramTypes))
                return m;
        return null;
    }
    
    public static List<Method> getStaticInstanceMethods(Class c, Class... paramTypes) {
        List<Method> methods = new ArrayList<Method>();
        for(Method m : c.getDeclaredMethods())
            if(isStaticInstanceMethod(m, c) && needsParameters(m, paramTypes))
                methods.add(m);
        return methods;
    }
    
    private static boolean isStaticInstanceMethod(Method m, Class c) {
        if(!Modifier.isStatic(m.getModifiers())) return false;
        return c.equals(m.getReturnType());
    }
    
    private static boolean needsParameters(Method m, Class... paramTypes) {
        Class[] needed = m.getParameterTypes();
        return Arrays.equals(needed, paramTypes);
    }
}
