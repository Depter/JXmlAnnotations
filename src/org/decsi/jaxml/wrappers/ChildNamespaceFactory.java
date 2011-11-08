package org.decsi.jaxml.wrappers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
class ChildNamespaceFactory {
    
    private XmlElementWrapper elementWrapper;
    private XmlChildWrapper childWrapper;
    
    private List<XmlNamespaceWrapper> namespaces = new ArrayList<XmlNamespaceWrapper>();
    
    ChildNamespaceFactory(XmlChildWrapper childWrapper, XmlElementWrapper elementWrapper) {
        this.elementWrapper = elementWrapper;
        this.childWrapper = childWrapper;
    }
    
    List<XmlNamespaceWrapper> buildNamespaces() {
        initNamespaces();
        List<XmlNamespaceWrapper> result = new ArrayList<XmlNamespaceWrapper>(namespaces);
        namespaces.clear();
        return result;
    }
    
    private void initNamespaces() {
        if(elementWrapper!=null && !childWrapper.overrideElement())
            initNamespacesFromElement();
        else initNamespacesFromChild();
    }
    
    private void initNamespacesFromElement() {
        namespaces = new ArrayList<XmlNamespaceWrapper>(elementWrapper.getNamespaces());
        addNamespaces(getFixNamespaces());
    }
    
    private List<XmlNamespaceWrapper> getFixNamespaces() {
        String[] nss = childWrapper.getAnnotation().namespaces();
        NamespaceBuilder builder = new NamespaceBuilder(childWrapper);
        return builder.buildNamespaceWrappers(nss);
    }
    
    private void initNamespacesFromChild() {
        namespaces = getFixNamespaces();
        if(elementWrapper != null)
            addNamespaces(elementWrapper.getNamespaces());
    }
    
    private void addNamespaces(List<XmlNamespaceWrapper> nsWrappers) {
        for(XmlNamespaceWrapper nsWrapper : nsWrappers)
            if(!containsNamespace(nsWrapper))
                namespaces.add(nsWrapper);
    }
    
    private boolean containsNamespace(XmlNamespaceWrapper nsWrapper) {
        for(XmlNamespaceWrapper old : namespaces) {
            if(old.isDefault()) {
                if(nsWrapper.isDefault()) return true;
            } else {
                if(old.getPrefix().equalsIgnoreCase(nsWrapper.getPrefix()))
                    return true;
            }
        }
        return false;
    }
    
}
