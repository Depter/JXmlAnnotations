package org.decsi.jaxml.wrappers;

/**
 *
 * @author Peter Decsi
 */
class XmlNamespaceWrapperImpl implements XmlNamespaceWrapper{
    
    private String prefix;
    private String uri;
    
    XmlNamespaceWrapperImpl(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }
    
    @Override
    public boolean isDefault() {
        return prefix == null;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getURI() {
        return uri;
    }
    
}
