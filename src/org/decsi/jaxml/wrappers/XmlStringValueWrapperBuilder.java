package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.PrimitiveUtil;
import static org.decsi.jaxml.parsing.ReflectionUtil.*;

/**
 *
 * @author Peter Decsi
 */
class XmlStringValueWrapperBuilder {
    
    private XmlStringValueWrapperBuilder() {}
    
    static XmlStringValueWrapper getWrapperForClass(Class c) {
        if(PrimitiveUtil.isPrimitive(c))
            return new PrimitiveStringWrapper(c, true);
        return getWrapperForNotPRimitiveClass(c);
    }
    
    private static XmlStringValueWrapper getWrapperForNotPRimitiveClass(Class c) {
        AccessibleObject member = getAnnotatedMember(c);
        checkConstructable(c, member);
        return new XmlStringValueWrapperImpl(member, member.getAnnotation(XmlStringValue.class));
    }
    
    private static AccessibleObject getAnnotatedMember(Class c) {
        List<AccessibleObject> members = getAnnotatedMembers(c);
        checkMembersSize(members, c);
        return members.get(0);
    }
    
    static List<AccessibleObject> getAnnotatedMembers(Class c) {
        List<AccessibleObject> members = new ArrayList<AccessibleObject>();
        members.addAll(getAnnotatedFields(c, XmlStringValue.class));
        members.addAll(getAnnotatedMethods(c, XmlStringValue.class));
        return members;
    }
    
    private static void checkMembersSize(List<AccessibleObject> members, Class c) {
        if(members.isEmpty())
            throw noAnnotatedMember(c);
        if(members.size() > 1)
            throw tooManyAnnotations(c);
    }
    
    private static IllegalArgumentException tooManyAnnotations(Class c) {
        String msg = "Class %s contains more than one member, annotated with XmlStringValue annotation!";
        msg = String.format(msg, c.getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    private static IllegalArgumentException noAnnotatedMember(Class c) {
        String msg = "Class %s contains no fields or methods, annotated with XmlStringValue!";
        msg = String.format(msg, c.getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    private static void checkConstructable(Class c, AccessibleObject member) {
        Class paramType = (member instanceof Field)?
                ((Field) member).getType() : ((Method) member).getReturnType();
        if(!ConstructorUtil.isConstructable(c, paramType))
            throw notCosntructable(c, paramType);
        if(c.isMemberClass() && !Modifier.isStatic(c.getModifiers()))
            throw nonStaticInnerClassException(c);
    }
    
    private static IllegalArgumentException notCosntructable(Class c, Class paramType) {
        String msg = "Class %s contains no fields or methods, annotated with XmlStringValue but it does "+
                     "not contain any constructor or static factory method that accepts the value (%s) if the member!";
        msg = String.format(msg, c.getCanonicalName(), paramType.getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    private static IllegalArgumentException nonStaticInnerClassException(Class c) {
        String msg = "Class %s is a non-static inner class, thus it can not be initiated!";
        msg = String.format(msg, c.getCanonicalName());
        return new IllegalArgumentException(msg);
    }
}
