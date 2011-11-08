package org.decsi.jaxml.wrappers;

import java.util.List;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
class XmlElementWrapperImpl extends AbstractWrapper<XmlElement, Class> 
                            implements XmlElementWrapper {
    
    private final static String PARAMLESS_CONSTRUCTOR = "Class %s is annotated with XmlElement, but it "+
        "does not have a parameterless constructor or static instance method, it is a non-static class!";
    private final static String STRING_CONTENT_CONSTRUCTOR = "Class %s is annotated with XmlElement, it has "+
        "a member annotated with XmlStringValue but it does not have a constructor or static instance method "+
        "that accepts valuetype of the member, or it is a non-static inner class!";
    
    private NameManager name;
    private List<XmlAttributeWrapper> attributes;
    private List<XmlNamespaceWrapper> namespaces;
    private List<ChildWrapper> children;
    private XmlStringValueWrapper stringWrapper;
    
    XmlElementWrapperImpl(Class c) {
        super(c);
        super.initState(XmlElement.class);
        this.initState();
    }
    
    private void initState() {
        initName();
        initNamespaces();
        initContent();
        checkConstructable();
    }
    
    private void initName() {
        name = NameManager.getInstance(super.getAnnotation());
        name.checkName(this);
    }
    
    private void initNamespaces() {
        String[] nsArray = getAnnotation().namespaces();
        NamespaceBuilder builder = new NamespaceBuilder(this);
        namespaces = builder.buildNamespaceWrappers(nsArray);
    }
    
    private void initContent() {
        Class c = getAnnotatedElement();
        attributes = new AttributeBuilder().buildContent(c);
        children = new ChildBuilder().buildContent(c);
        initStringWrapper(c);
        checkContent();
    }
    
    private void initStringWrapper(Class c) {
        if(XmlStringValueWrapperBuilder.getAnnotatedMembers(c).isEmpty())
            return;
        stringWrapper = XmlStringValueWrapperBuilder.getWrapperForClass(c);
    }
    
    private void checkContent() {
        if(!(stringWrapper!=null && children.size()>0)) return;
        String msg = "Class %s contains a member annotated with XmlStringValue, and Fields annotated with XmlChild\n or it constains no default constructor or instance method!";
        msg = String.format(msg, getAnnotatedElement().getCanonicalName());
        throw new IllegalArgumentException(msg);
    }
    
    private void checkConstructable() {
        Class c = super.getAnnotatedElement();
        if(!isConstructable(c))
            throw notConstructable();
    }
    
    private boolean isConstructable(Class c) {
        if(!ConstructorUtil.isConstructable(c))
            return false;
        return true;
    }
    
    private IllegalArgumentException notConstructable() {
        String msg = stringWrapper!=null? STRING_CONTENT_CONSTRUCTOR : PARAMLESS_CONSTRUCTOR;
        msg = String.format(msg, super.getAnnotatedElement().getCanonicalName());
        return new IllegalArgumentException(msg);
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
    public List<XmlAttributeWrapper> getAttributes() {
        return attributes;
    }

    @Override
    public List<ChildWrapper> getChildren() {
        return children;
    }
    
    @Override
    public XmlStringValueWrapper getStringWrapper() {
        return stringWrapper;
    }

    @Override
    public List<XmlNamespaceWrapper> getNamespaces() {
        return namespaces;
    }
}
