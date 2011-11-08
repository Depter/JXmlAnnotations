package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.annotation.XmlChild;

/**
 *
 * @author Peter Decsi
 */
public interface XmlChildWrapper extends ChildWrapper<XmlChild> {
    
    public boolean overrideElement();
    public XmlElementWrapper getChildElementWrapper();
    public XmlStringValueWrapper getStringWrapper();
}
