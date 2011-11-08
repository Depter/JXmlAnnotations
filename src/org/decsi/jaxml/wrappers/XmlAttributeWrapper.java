package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlAttribute;

/**
 *
 * @author Peter Decsi
 */
public interface XmlAttributeWrapper 
    extends NamedWrapper<XmlAttribute, Field>, 
            IndexedWrapper, 
            XmlStringValueWrapper<XmlAttribute, Field> {
    
    public boolean addIfDefault();
    public String getDefaultValue();
    
    public XmlAttribute.Type getType();
}
