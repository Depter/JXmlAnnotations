package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import java.util.List;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.parsing.PrimitiveUtil;

/**
 *
 * @author Peter Decsi
 */
class XmlChildWrapperImpl extends AbstractWrapper<XmlChild, Field> 
    implements XmlChildWrapper {
    
    private ValueWrapper<Field> valueWrapper;
    private XmlElementWrapper xmlChildWrapper;
    private XmlStringValueWrapper stringChildWrapper;
    private NameManager name;
    private List<XmlAttributeWrapper> attributes;
    private List<XmlNamespaceWrapper> namespaces;
    
    XmlChildWrapperImpl(Field field) {
        super(field);
        super.initState(XmlChild.class);
        initState();
    }
    
    XmlChildWrapperImpl(Field field, XmlChild annotation) {
        super(field, annotation);
        initState();
    }
    
    private void initState() {
        valueWrapper = ValueWrapperImpl.getFieldWrapper(getAnnotatedElement());
        inintXmlChildWrapper();
        if(xmlChildWrapper==null) initStringChildWrapper();
        initName();
        initAttributes();
        initNamespaces();
    }
    
    private void inintXmlChildWrapper() {
        Class childClass = valueWrapper.getValueClass();
        if(childClass.isAnnotationPresent(XmlElement.class))
            xmlChildWrapper = new XmlElementWrapperImpl(childClass);
    }
    
    private void initStringChildWrapper() {
        try {
            stringChildWrapper = XmlStringValueWrapperBuilder.getWrapperForClass(getAnnotatedElement().getType());
        } catch (IllegalArgumentException ex) {
            throw noStringSource(ex);
        }
    }
    
    private IllegalArgumentException noStringSource(IllegalArgumentException ex) {
        String msg = "Field '%s' in class %s is annotated with XmlChild annotation "+
                     "but it's value is neither a class annotated with XmlElement or a valid string source!";
        msg = String.format(msg, getElementName(), getClassName());
        msg += "\n"+ex.getMessage();
        return new IllegalArgumentException(msg);
    }
    
    private void initName() {
        if(xmlChildWrapper != null) {
            name = NameManager.getInstance(getAnnotation(), xmlChildWrapper.getAnnotation());
        } else {
            name = NameManager.getInstance(getAnnotation());
        }
    }
    
    private void initAttributes() {
        ChildAttributeFactory factory = new ChildAttributeFactory(this, xmlChildWrapper);
        attributes = factory.getAttributes();
    }
    
    private void initNamespaces() {
        ChildNamespaceFactory factory = new ChildNamespaceFactory(this, xmlChildWrapper);
        namespaces = factory.buildNamespaces();
    }
    
    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public String getNsPrefix() {
        return name.getNsPrefix();
    }

    @Override
    public String getQualifiedName() {
        return name.getQualifiedName();
    }

    @Override
    public int getIndex() {
        return super.getAnnotation().index();
    }

    @Override
    public Class getValueClass() {
        return valueWrapper.getValueClass();
    }

    @Override
    public Object getValue(Object instance) throws Exception {
        return valueWrapper.getValue(instance);
    }

    @Override
    public boolean overrideElement() {
        return super.getAnnotation().overrideElement();
    }

    @Override
    public boolean addIfNull() {
        return super.getAnnotation().addIfNull();
    }

    @Override
    public XmlElementWrapper getChildElementWrapper() {
        return xmlChildWrapper;
    }

    @Override
    public XmlStringValueWrapper getStringWrapper() {
        return stringChildWrapper;
    }

    @Override
    public List<XmlAttributeWrapper> getAttributes() {
        return attributes;
    }

    @Override
    public List<XmlNamespaceWrapper> getNamespaces() {
        return namespaces;
    }

    @Override
    public boolean isChildOfParent(String qualifiedName) {
        return getQualifiedName().equalsIgnoreCase(qualifiedName);
    }

    @Override
    public Field getMember() {
        return getAnnotatedElement();
    }
}
