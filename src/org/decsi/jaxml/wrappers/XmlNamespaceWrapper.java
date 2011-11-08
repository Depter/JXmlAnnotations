package org.decsi.jaxml.wrappers;

/**
 *
 * @author Peter Decsi
 */
public interface XmlNamespaceWrapper {
    
    public boolean isDefault();
    public String getPrefix();
    public String getURI();
}
