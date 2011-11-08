package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlChildContainer;

/**
 *
 * @author Peter Decsi
 */
public interface XmlChildContainerWrapper extends ChildWrapper<XmlChildContainer> {
    
    public boolean addIfEmpty();
    public boolean addOnlyContainedElements();
    public Class getElementClass();
    public Class getKeyClass();
    public String getQualifiedKeyName();
    public XmlChildWrapper getElementWrapper();
    //public XmlAttributeWrapper getKeyWrapper();
}
