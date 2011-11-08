package org.decsi.jaxml.parsing;

import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
public class AnnotationUtil {
    public final static String MAP_KEY_ATTRIBUTE = "mapkey";
    
    private AnnotationUtil(){}

    public static String escapeEmptyString(String str) {
        if(str == null) return null;
        if(XmlElement.NULL.equalsIgnoreCase(str.trim())) return null;
        return str;
    }
    
    public static String[] escapeEmtyArray(String[] str) {
        if(str==null) return new String[0];
        if(str.length==1 && str[0].length()==0) return new String[0];
        return str;
    }
    
    public static String getQualifiedName(Field field) {
        if(field.isAnnotationPresent(XmlChild.class))
            return getQualifiedNameForChild(field);
        if(field.isAnnotationPresent(XmlChildContainer.class))
            return getQualifiedNameForContainer(field);
        if(field.isAnnotationPresent(XmlAttribute.class))
            return getQualifiedName(field.getAnnotation(XmlAttribute.class));
        return null;
    }
    
    private static String getQualifiedNameForChild(Field field) {
        XmlChild childAn = field.getAnnotation(XmlChild.class);
        XmlElement elementAn = field.getType().getAnnotation(XmlElement.class);
        return getQualifiedName(childAn, elementAn);
    }
    
    private static String getQualifiedName(XmlChild childAn, XmlElement elementAn) {
        if(childAn.overrideElement())
            return getQualifiedName(childAn);
        return getQualifiedName(elementAn);
    }
    
    private static String getQualifiedNameForContainer(Field field) {
        XmlChildContainer container = field.getAnnotation(XmlChildContainer.class);
        return getQualifiedName(container);
    }
    
    public static String getQualifiedName(XmlElement annotation) {
        return getQualifiedName(annotation.name(), annotation.nsPrefix());
    }
    
    public static String getQualifiedName(XmlAttribute annotation) {
        String name = getQualifiedName(annotation.name(), annotation.nsPrefix());
        if(MAP_KEY_ATTRIBUTE.equalsIgnoreCase(name))
            throw new IllegalArgumentException("Attributename '"+MAP_KEY_ATTRIBUTE+"' is reserved for kayvalues from a map!");
        return name;
    }
    
    private static String getQualifiedName(String name, String prefix) {
        name = checkValidXmlName(name);
        prefix = escapeEmptyString(prefix);
        return prefix!=null? prefix+":"+name : name;
    }
    
    public static String checkValidXmlName(String name) {
        if(name == null || name.trim().length()==0) 
            throw illegalTagNameException(name);
        return name.trim();
    }
    
    private static IllegalArgumentException illegalTagNameException(String name) {
        String msg = "Name '%s' is not a valid name for an xml tag!";
        msg = String.format(msg, name);
        return new IllegalArgumentException(msg);
    }
    
    public static String getQualifiedName(XmlChild annotation) {
        return getQualifiedName(annotation.name(), annotation.nsPrefix());
    }
    
    public static String getQualifiedName(XmlChildContainer annotation) {
        return getQualifiedName(annotation.name(), annotation.nsPrefix());
    }
    
    public static int getIndex(Field field) {
        if(field.isAnnotationPresent(XmlChild.class))
            return field.getAnnotation(XmlChild.class).index();
        if(field.isAnnotationPresent(XmlChildContainer.class))
            return field.getAnnotation(XmlChildContainer.class).index();
        if(field.isAnnotationPresent(XmlAttribute.class))
            return field.getAnnotation(XmlAttribute.class).index();
        return -1;
    }
}
