package org.decsi.jaxml.wrappers;

import java.util.List;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
public interface XmlElementWrapper extends NamespaceOwner<XmlElement, Class> {
    
    public List<XmlAttributeWrapper> getAttributes();
    public List<ChildWrapper> getChildren();
    public XmlStringValueWrapper getStringWrapper();
}
