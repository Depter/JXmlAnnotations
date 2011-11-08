package org.decsi.jaxml.parsing.objectparsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.decsi.jaxml.wrappers.NamedWrapper;
import org.decsi.jaxml.wrappers.XmlNamespaceWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
class NamespaceManager {
    
    final static String EMPTY_STRING = "";
    
    private FeatureManager features;
    private PropertyManager properties;

    private List<XmlNamespaceWrapper> namespaces = new ArrayList<XmlNamespaceWrapper>();
    
    NamespaceManager() {
    }
    
    NamespaceManager(FeatureManager features, PropertyManager properties) {
        this.features = features;
        this.properties = properties;
    }
    
    String getLocalName(NamedWrapper wrapper) {
        return wrapper.getName();
    }
    
    String getURI(NamedWrapper wrapper) throws SAXException {
        String nsPrefix = wrapper.getNsPrefix();
        return getUriForNsPrefix(nsPrefix);
    }
    
    private String getUriForNsPrefix(String prefix) throws SAXException {
        for(XmlNamespaceWrapper ns : namespaces)
            if(samePrefix(prefix, ns))
                return ns.getURI();
        return getUriForNotDefinedPrefix(prefix);
    }
    
    private String getUriForNotDefinedPrefix(String prefix) throws SAXException {
        if(noPrefix(prefix)) return EMPTY_STRING;
        if(!features.isEnabled(SAXFeature.NAMESPACE)) return EMPTY_STRING;
        throw noUriDefinitionException(prefix);
    }
    
    private boolean samePrefix(String prefix, XmlNamespaceWrapper namespace) {
        if(namespace.isDefault())
            return noPrefix(prefix);
        return namespace.getPrefix().equalsIgnoreCase(prefix);
    }
    
    private boolean noPrefix(String prefix) {
        if(prefix == null) return true;
        return prefix.trim().length() == 0;
    }
    
    private SAXException noUriDefinitionException(String nsPrefix) {
        String msg = "URI not defined for '%s'!";
        msg = String.format(msg, nsPrefix);
        return new SAXException(msg);
    }
    
    String getQualifiedName(NamedWrapper wrapper) throws SAXException {
        String prefix = wrapper.getNsPrefix();
        String name = wrapper.getName();
        String separator = getPrefixSeparator();
        return noPrefix(prefix) ? name : prefix+separator+name;
    }
    
    private String getPrefixSeparator() {
        Object value = properties.getProperty(SAXProperty.NAMESPACE_SEPARATOR);
        return (String) value;
    }
    
    String getNsPrefixForUri(String uri) {
        for(XmlNamespaceWrapper namespace : namespaces)
            if(namespace.getURI().equalsIgnoreCase(uri))
                return namespace.getPrefix();
        return null;
    }    
    
    XmlNamespaceWrapper addNamespace(XmlNamespaceWrapper wrapper) {
        XmlNamespaceWrapper removed = removeNamespace(wrapper);
        namespaces.add(wrapper);
        return removed;
    }
    
    XmlNamespaceWrapper removeNamespace(XmlNamespaceWrapper wrapper) {
        String prefix = wrapper.getPrefix();
        Iterator<XmlNamespaceWrapper> it = namespaces.iterator();
        return removePrefixFromIterator(prefix, it);
    }
    
    private XmlNamespaceWrapper removePrefixFromIterator(String prefix, Iterator<XmlNamespaceWrapper> it) {
        while(it.hasNext()) {
            XmlNamespaceWrapper old = it.next();
            if(samePrefix(prefix, old)) {
                it.remove();
                return old;
            }
        }
        return null;
    }
    
    FeatureManager getFeatures() {
        return features;
    }
    
    PropertyManager getProperties() {
        return properties;
    }
}
