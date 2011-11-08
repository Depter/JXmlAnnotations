package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
class ChildAttributeFactory {
    
    private XmlElementWrapper elementWrapper;
    private XmlChildWrapper childWrapper;
    
    private List<XmlAttributeWrapper> attributes = new ArrayList<XmlAttributeWrapper>();
    
    ChildAttributeFactory(XmlChildWrapper childWrapper, XmlElementWrapper elementWrapper) {
        this.elementWrapper = elementWrapper;
        this.childWrapper = childWrapper;
    }
    
    List<XmlAttributeWrapper> getAttributes() {
        initAttributes();
        List<XmlAttributeWrapper> result = new ArrayList<XmlAttributeWrapper>(attributes);
        attributes.clear();
        return result;
    }
    
    private void initAttributes() {
        if(elementWrapper!=null && !childWrapper.overrideElement()) {
            initElementFirst();
        } else {
            initFixAttributesFirst();
        }
    }
    
    private void initElementFirst() {
        attributes = new ArrayList<XmlAttributeWrapper>(elementWrapper.getAttributes());
        addAttributeList(initAnnotationAttributes());
    }
    
    private void initFixAttributesFirst() {
            attributes = initAnnotationAttributes();
            if(elementWrapper!=null)
                addAttributeList(elementWrapper.getAttributes());
    }
    
    private List<XmlAttributeWrapper> initAnnotationAttributes() {
        Field field = childWrapper.getAnnotatedElement();
        AnnotationAttributeParser parser = new AnnotationAttributeParser(field);
        return parser.getAttributeWrappers(childWrapper.getAnnotation().attributes());
    }
    
    private void addAttributeList(List<XmlAttributeWrapper> newAttributes) {
        for(XmlAttributeWrapper attribute : newAttributes)
            if(!attributeExists(attribute))
                attributes.add(attribute);
    }
    
    private boolean attributeExists(XmlAttributeWrapper attribute) {
        String qName = attribute.getQualifiedName();
        for(XmlAttributeWrapper old : attributes)
            if(old.getQualifiedName().equalsIgnoreCase(qName))
                return true;
        return false;
    }
}
