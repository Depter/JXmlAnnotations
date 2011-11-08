package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import org.decsi.jaxml.annotation.XmlStringValue;
import static org.decsi.jaxml.parsing.PrimitiveUtil.isPrimitive;
import static org.decsi.jaxml.parsing.ReflectionUtil.*;

/**
 *
 * @author Peter Decsi
 */
class XmlStringValueWrapperImpl<E extends AccessibleObject> 
            implements XmlStringValueWrapper<Annotation, E> {
    
    private XmlStringValueWrapper child;
    private Annotation annotation;
    private E member;

    XmlStringValueWrapperImpl(E member, Annotation annotation) {
        this.member = member;
        checkMember();
        this.annotation = annotation;
        initChild();
    }
    
    private void checkMember() {
        if(member instanceof Field) return;
        Method m = (Method) member;
        if(void.class.equals(m.getReturnType())) throw voidMemberTypeException(m);
        if(m.getParameterTypes().length > 0) throw parameterMethodException(m);
    }
    
    private IllegalArgumentException voidMemberTypeException(Method m) {
        String msg = "Method '%s' in class %s is annotated with XmlStirngValue but it's retunr type is void!";
        msg = String.format(msg, m.getName(), m.getDeclaringClass().getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    private IllegalArgumentException parameterMethodException(Method m) {
        String msg = "Method '%s' in class %s is annotated with XmlStirngValue but it has parameters!";
        msg = String.format(msg, m.getName(), m.getDeclaringClass().getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    private void initChild() {
        Class type = getMemberType();
        this.child = XmlStringValueWrapperBuilder.getWrapperForClass(type);
    }
    
    private Class getMemberType() {
        if(member instanceof Field)
            return ((Field) member).getType();
        return ((Method) member).getReturnType(); 
    }
    
    @Override
    public XmlStringValueWrapper getChildWrapper() {
        return child;
    }

    @Override
    public boolean trimInput() {
        if(!(child instanceof PrimitiveStringWrapper))
            return child.trimInput();
        return (annotation instanceof XmlStringValue)? 
            ((XmlStringValue) annotation).trim() : true;
    }

    @Override
    public Object getPrimitiveValue(Object instance) throws Exception {
        if(instance==null) return null;
        if(isPrimitive(instance.getClass()))
            return instance;
        Object thisValue = getThisValue(instance);
        return child==null? thisValue : child.getPrimitiveValue(thisValue);
    }
    
    private Object getThisValue(Object instance) throws Exception {
        if(member instanceof Field)
            return getFieldValue((Field) member, instance);
        return invokeMethod((Method) member, instance);
    }

    @Override
    public XmlStringValue getAnnotation() {
        return (XmlStringValue) annotation;
    }

    @Override
    public E getAnnotatedElement() {
        return member;
    }
}
