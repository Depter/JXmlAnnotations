package org.decsi.jaxml.wrappers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Peter Decsi
 */
abstract class ValueWrapperImpl<A extends AccessibleObject> implements ValueWrapper<A> {
    
    static ValueWrapper<Field> getFieldWrapper(Field field) {
        checkNonNullObject(field);
        return new FieldValueWrapper(field);
    }
    
    static ValueWrapper<Method> getMethodWrapper(Method method) {
        checkNonNullObject(method);
        return new MethodValueWrapper(method);
    }
    
    private static void checkNonNullObject(AccessibleObject object) {
        if(object == null)
            throw new NullPointerException("AccessibleObject is null!");
    }
    
    private Class valueClass;
    private A accessibleObject;
    private boolean isAccessible;
    
    private ValueWrapperImpl(A accessibleObject, Class valueClass) {
        this.accessibleObject = accessibleObject;
        this.isAccessible = accessibleObject.isAccessible();
        this.valueClass = valueClass;
        checkVoidClass();
    }
    
    private void checkVoidClass() {
        if(void.class.equals(valueClass)) {
            String msg = "Element '%s' can not represent a value, because it's type is void!";
            msg = String.format(msg, accessibleObject.toString());
            throw new IllegalArgumentException(msg);
        }    
    }
    
    @Override
    public Class getValueClass() {
        return valueClass;
    }

    @Override
    public Object getValue(Object instance) throws Exception {
        setAccessible();
        try {
            return getAccessibleValue(instance);
        } finally {
            resetAccessible();
        }
    }
    
    @Override
    public A getMember() {
        return accessibleObject;
    }
    
    private void setAccessible() {
        if(isAccessible) return;
        accessibleObject.setAccessible(true);
    }
    
    protected abstract Object getAccessibleValue(Object instance) throws Exception;

    private void resetAccessible() {
        if(isAccessible) return;
        accessibleObject.setAccessible(false);
    }
    
    private static class FieldValueWrapper extends ValueWrapperImpl<Field> {
        
        FieldValueWrapper(Field field) {
            super(field, field.getType());
        }
        
        @Override
        protected Object getAccessibleValue(Object instance) throws Exception {
            return super.accessibleObject.get(instance);
        }
    } 
    
    private static class MethodValueWrapper extends ValueWrapperImpl<Method> {
        
        MethodValueWrapper(Method method) {
            super(method, method.getReturnType());
            checkNoParams();
        }
        
        private void checkNoParams() {
            Class[] params = super.accessibleObject.getParameterTypes();
            if(params == null || params.length==0) return;
            throw hasParametersException();
        }
        
        private IllegalArgumentException hasParametersException() {
            String msg = "Method '%s()' in class %s must not have any parameters!";
            msg = String.format(msg, super.accessibleObject.getName(), 
                    super.accessibleObject.getDeclaringClass().getCanonicalName());
            return new IllegalArgumentException(msg);
        }
        
        @Override
        protected Object getAccessibleValue(Object instance) throws Exception {
            return super.accessibleObject.invoke(instance);
        }
    }
}
