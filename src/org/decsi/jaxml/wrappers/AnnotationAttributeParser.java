package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlAttribute.Type;

/**
 *
 * @author Peter Decsi
 */
class AnnotationAttributeParser {
    private Field field;
    
    private String str;
    private StringBuilder builder = new StringBuilder();
    private String nsPrefix = null;
    private String name = null;
    private String value = null;
    
    AnnotationAttributeParser(Field field) {
        this.field = field;
    }
    
    List<XmlAttributeWrapper> getAttributeWrappers(String[] strAttributes) {
        if(doesNotContainAttributes(strAttributes)) return new ArrayList<XmlAttributeWrapper>(0);
        return buildWrappers(strAttributes);
    }
    
    private boolean doesNotContainAttributes(String[] strAttributes) {
        if(strAttributes == null) return true;
        if(strAttributes.length==1 && strAttributes[0].length()==0)
            return true;
        return false;
    }
    
    private List<XmlAttributeWrapper> buildWrappers(String[] attributes) {
        List<XmlAttributeWrapper> wrappers = new ArrayList<XmlAttributeWrapper>();
        for(String strAttribute : attributes)
            addAttribute(wrappers, strAttribute);
        return wrappers;
    }
    
    private void addAttribute(List<XmlAttributeWrapper> wrappers, String strAttribute) {
        XmlAttributeWrapper attribute = getAttribute(strAttribute);
        for(XmlAttributeWrapper old : wrappers)
            if(old.getQualifiedName().equalsIgnoreCase(attribute.getQualifiedName()))
                throw attributeDeclaredTwiceException(attribute);
        wrappers.add(attribute);
    }
    
    private XmlAttributeWrapper getAttribute(String str) {
        parse(str);
        NameManager nm = new NameManager(name, nsPrefix);
        return new MyAttributeWrapper(field, nm, value);
    }    

    private void parse(String str) {
        this.str = str;
        initState();
        processString();
    }
    
    private void initState() {
        this.builder.setLength(0);
        this.nsPrefix = null;
        this.name = null;
        this.value = null;
    }
    
    private void processString() {
        int length = str.length();
        for(int i=0; i<length; i++)
            processChar(str.charAt(i));
        finishValues();
    }
    
    private void processChar(char c) {
        switch(c) {
            case ':':
                if(nsPrefix==null) createNsPrefix();
                else builder.append(c);
                break;
            case '=':
                if(name == null) createName();
                else builder.append(c);
                break;
            default:
                builder.append(c);
        }
    }
    
    private void createNsPrefix() {
        nsPrefix = builder.toString();
        builder.setLength(0);
    }
    
    private void createName() {
        name = builder.toString();
        builder.setLength(0);
    }
    
    private void finishValues() {
        if(name==null) createName();
        else createValue();
        if(nsPrefix != null && nsPrefix.trim().length()==0) nsPrefix = null;
        if(name != null && name.trim().length()==0) name = null; 
    }
    
    private void createValue() {
        value = builder.toString();
        if(value.length()==0) value=null;
    }
    
    private IllegalArgumentException attributeDeclaredTwiceException(XmlAttributeWrapper wrapper) {
        String msg = "Field '%s' in class %s defines a fix attribute with name '%s' twice!";
        msg = String.format(msg, field.getName(), 
                    field.getDeclaringClass().getCanonicalName(),
                    wrapper.getQualifiedName());
        return new IllegalArgumentException(msg);
    }
    
    private static class MyAttributeWrapper extends AbstractWrapper<XmlAttribute, Field> 
            implements XmlAttributeWrapper {
        
        private NameManager name;
        private String value;
        
        MyAttributeWrapper(Field field, NameManager name, String value) {
            super(field);
            this.name = name;
            name.checkName(this);
            this.value = value;
        }
        
        @Override
        protected String getAnnotationName() {
            return "Unknown";
        }
        
        @Override
        public boolean addIfDefault() {
            return true;
        }

        @Override
        public String getDefaultValue() {
            return null;
        }

        @Override
        public XmlAttribute getAnnotation() {
            return null;
        }

        @Override
        public Field getAnnotatedElement() {
            return super.getAnnotatedElement();
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
            return -1;
        }

        @Override
        public XmlStringValueWrapper getChildWrapper() {
            return null;
        }

        @Override
        public Type getType() {
            return XmlAttribute.Type.CDATA;
        }

        @Override
        public Object getPrimitiveValue(Object instance) {
            return value;
        }
    
        @Override
        public boolean trimInput() {
            return true;
        }
    }
}
