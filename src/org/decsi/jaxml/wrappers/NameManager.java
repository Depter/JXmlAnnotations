package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
class NameManager {
    
    static NameManager getInstance(XmlChild child, XmlElement element) {
        return child.overrideElement()?
                getInstance(child) : getInstance(element);
    }
    
    static NameManager getInstance(XmlChild annotation) {
        String name = annotation.name();
        String prefix = annotation.nsPrefix();
        return new NameManager(name, prefix);
    }
    
    static NameManager getInstance(XmlElement annotation) {
        String name = annotation.name();
        String prefix = annotation.nsPrefix();
        return new NameManager(name, prefix);
    }
    
    static NameManager getInstance(XmlAttribute annotation) {
        String name = annotation.name();
        String prefix = annotation.nsPrefix();
        return new NameManager(name, prefix);
    }
    
    static NameManager getIsntance(XmlChildContainer annotation) {
        String name = annotation.name();
        String prefix = annotation.nsPrefix();
        return new NameManager(name, prefix);
    }
    
    private String name;
    private String nsPrefix;
    
    NameManager(String name, String nsPrefix) {
        this.name = escapeEmptyString(name);
        this.nsPrefix = escapeEmptyString(nsPrefix);
    }
    
    static String escapeEmptyString(String str) {
        if(str == null) return null;
        if(str.trim().length() == 0) return null;
        return str;
    }
    
    void checkName(AbstractWrapper wrapper) {
        if(name == null)
            throw illegalNameException(wrapper);
        if(nsPrefix!= null && !Character.isLetter(nsPrefix.charAt(0)))
            throw illegalPrefixCharException(wrapper);
        if(!Character.isLetter(name.charAt(0)))
            throw illegalPrefixCharException(wrapper);
    }
    
    private IllegalArgumentException illegalNameException(AbstractWrapper wrapper) {
        String msg = "Annotation %s for '%s' in class %s defines an illegal element name!";
        msg = String.format(msg, wrapper.getAnnotationName(),
                        wrapper.getElementName(), wrapper.getClassName());
        return new IllegalArgumentException(msg);
    }
    
    private IllegalArgumentException illegalPrefixCharException(AbstractWrapper wrapper) {
        String msg = "Prefix '%s' for '%s' for element '%s' in class %s is illegal!";
        msg = String.format(msg, nsPrefix, wrapper.getAnnotationName(), 
                    wrapper.getElementName(), wrapper.getClassName());
        return new IllegalArgumentException(msg);
    }
    
    private IllegalArgumentException illegalNameCharException(AbstractWrapper wrapper) {
        String msg = "Name '%s' for '%s' for element '%s' in class %s is illegal!";
        msg = String.format(msg, nsPrefix, wrapper.getAnnotationName(), 
                    wrapper.getElementName(), wrapper.getClassName());
        return new IllegalArgumentException(msg);
    }
    
    String getName() {
        return name;
    }
    
    String getNsPrefix() {
        return nsPrefix;
    }
    
    String getQualifiedName() {
        String qName = nsPrefix==null? "" : nsPrefix+":";
        return qName + name;
    }
}
