package org.decsi.jaxml.wrappers;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import static org.decsi.jaxml.wrappers.AnnotatedElementUtil.*;

/**
 *
 * @author Peter Decsi
 */
class NamespaceBuilder {
    private AnnotatedElement element;
    
    NamespaceBuilder(Wrapper wrapper) {
        this.element = wrapper.getAnnotatedElement();
    }
  
    List<XmlNamespaceWrapper> buildNamespaceWrappers(String[] namespaces) {
        if(doesNotContainNamespaces(namespaces)) return new ArrayList<XmlNamespaceWrapper>(0);
        return buildWrappers(namespaces);
    }
    
    private boolean doesNotContainNamespaces(String[] namespaces) {
        if(namespaces == null) return true;
        if(namespaces.length==1 && namespaces[0].length()==0)
            return true;
        return false;
    }
    
    private List<XmlNamespaceWrapper> buildWrappers(String[] namespaces) {
        List<XmlNamespaceWrapper> wrappers = new ArrayList<XmlNamespaceWrapper>();
        for(String namespace : namespaces)
            addNamespace(wrappers, namespace);
        return wrappers;
    }
    
    private void addNamespace(List<XmlNamespaceWrapper> wrappers, String namespace) {
        XmlNamespaceWrapper wrapper = getWrapper(namespace);
        checkWrapper(wrappers, wrapper);
        wrappers.add(wrapper);
    }
    
    private XmlNamespaceWrapper getWrapper(String namespace) {
        String prefix = getPrefix(namespace);
        String uri = getUri(prefix, namespace);
        if(prefix !=null && prefix.trim().length()==0) prefix = null;
        return new XmlNamespaceWrapperImpl(prefix, uri);
    }
    
    private String getPrefix(String namespace) {
        int index = namespace.indexOf('=');
        if(index < 0) return null;
        return namespace.substring(0, index);
    }
    
    private String getUri(String prefix, String namespace) {
        int begin = prefix==null? 0 : prefix.length()+1;
        String uri = namespace.substring(begin).trim();
        if(uri.length() != 0) return uri;
        throw noURIException(prefix);
    }
    
    private IllegalArgumentException noURIException(String prefix) {
        String msg = "Element '%s' in class %s contains a namespace declaration without an URI for prefix '%s'!";
        msg = String.format(msg, getName(element), getDeclaringClass(element).getCanonicalName(), prefix);
        return new IllegalArgumentException(msg);
    }
    
    private void checkWrapper(List<XmlNamespaceWrapper> wrappers, XmlNamespaceWrapper wrapper) {
        for(XmlNamespaceWrapper old : wrappers) {
            if(old.isDefault()) {
                if(wrapper.isDefault()) throw twoDefaultWrapperException();
            } else {
                if(old.getPrefix().equalsIgnoreCase(wrapper.getPrefix()))
                    throw prefixAlreadyUsedException(old.getPrefix());
            }
        }
    }
    
    private IllegalArgumentException twoDefaultWrapperException() {
        String msg = "Element '%s' in class %s contains two default namespace declaration!";
        msg = String.format(msg, getName(element), getDeclaringClass(element).getCanonicalName());
        return new IllegalArgumentException(msg);
    }
    
    private IllegalArgumentException prefixAlreadyUsedException(String prefix) {
        String msg = "Element '%s' in class %s contains two namespace declaration with prefix '%s'!";
        msg = String.format(msg, getName(element), getDeclaringClass(element).getCanonicalName(), prefix);
        return new IllegalArgumentException(msg);
    }
}
