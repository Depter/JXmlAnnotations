package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlAttribute.Type;
import org.decsi.jaxml.parsing.PrimitiveUtil;
import org.decsi.jaxml.parsing.ReflectionUtil;

/**
 *
 * @author Peter Decsi
 */
class XmlAttributeWrapperImpl extends AbstractWrapper<XmlAttribute, Field> implements XmlAttributeWrapper{
    
    private NameManager name;
    private XmlStringValueWrapper valueWrapper;
    
    XmlAttributeWrapperImpl(Field field) {
        super(field);
        super.initState(XmlAttribute.class);
        initState();
    }
    
    private void initState() {
        createName();
        createValueWrapper();
    }
    
    private void createName() {
        name = NameManager.getInstance(super.getAnnotation());
        name.checkName(this);
    }
    
    private void createValueWrapper() {
        try {
            valueWrapper = XmlStringValueWrapperBuilder.getWrapperForClass(getAnnotatedElement().getType());
        } catch (IllegalArgumentException ex) {
            throw appendClass(ex);
        }
    }
    
    private IllegalArgumentException appendClass(IllegalArgumentException ex) {
        String msg = "Field: "+getElementName()+" in "+getClassName();
        msg += "\n"+ex.getMessage();
        return new IllegalArgumentException(msg);
    }
    
    @Override
    public boolean addIfDefault() {
        return super.getAnnotation().addIfDefault();
    }

    @Override
    public String getDefaultValue() {
        String defaultValue = super.getAnnotation().defaultValue();
        if(defaultValue==null || defaultValue.length()==0) 
            return null;
        return defaultValue;
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
    public XmlStringValueWrapper getChildWrapper() {
        return valueWrapper;
    }

    @Override
    public Type getType() {
        return super.getAnnotation().type();
    }

    @Override
    public Object getPrimitiveValue(Object instance) throws Exception {
        if(instance == null) return null;
        Object thisValue = ReflectionUtil.getFieldValue(getAnnotatedElement(), instance);
        return valueWrapper.getPrimitiveValue(thisValue);
    }

    @Override
    public boolean trimInput() {
        return true;
    }
}
