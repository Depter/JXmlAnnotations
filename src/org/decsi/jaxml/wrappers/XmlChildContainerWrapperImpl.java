package org.decsi.jaxml.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlAttribute.Type;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlElement;
import org.decsi.jaxml.parsing.PrimitiveUtil;

/**
 *
 * @author Peter Decsi
 */
class XmlChildContainerWrapperImpl extends AbstractWrapper<XmlChildContainer, Field>
                                          implements XmlChildContainerWrapper{
    
    private final static String DEFAULT_KEY_NAME = "key";
    private final static String DEFAULT_KEY_PREFIX = null;
    
    private Field collectionField;
    private NameManager name;
    private List<XmlAttributeWrapper> fixAttributes;
    private List<XmlNamespaceWrapper> namespaces;
    private ValueWrapper<Field> valueWrapper;
    private XmlChildWrapper elementWrapper;
    private XmlAttributeWrapper keyWrapper;
    
    XmlChildContainerWrapperImpl(Field field) {
        super(field);
        super.initState(XmlChildContainer.class);
        this.collectionField = field;
        initState();
    }
    
    private void initState() {
        valueWrapper = ValueWrapperImpl.getFieldWrapper(getAnnotatedElement());
        checkValueIsCollection();
        initName();
        initNamespaces();
        initAttributes();
        initKeyWrapper();
        initElementWrapper();
    }
    
    private void checkValueIsCollection() {
        Class c = valueWrapper.getValueClass();
        if(c.isArray() || Map.class.isAssignableFrom(c) || 
           Collection.class.isAssignableFrom(c)) return;
        throw notCollectionTypeException();
    }
    
    private IllegalArgumentException notCollectionTypeException() {
        String msg = "Field '%s' in class %s is annotated with XmlChildContainer "+
                     "annotation, but it's type is not an array, map or collection!";
        msg = String.format(msg, collectionField.getName(), getClassName());
        return new IllegalArgumentException(msg);
    }
    
    private void initName() {
        XmlChildContainer annotation = getAnnotation();
        name = NameManager.getIsntance(annotation);
        if(!annotation.addOnlyContainedElements())
            name.checkName(this);
    }
    
    private void initNamespaces() {
        String[] nss = getAnnotation().namespaces();
        NamespaceBuilder builder = new NamespaceBuilder(this);
        namespaces = builder.buildNamespaceWrappers(nss);
    }
    
    private void initAttributes() {
        AnnotationAttributeParser parser = new AnnotationAttributeParser(getAnnotatedElement());
        String[] strAttributes = getAnnotation().attributes();
        fixAttributes = parser.getAttributeWrappers(strAttributes);
    }

    private void initElementWrapper() {
        XmlChild annotation = getElementAnnotation();
        elementWrapper = new ElementWrapper(annotation, getAnnotation().elementClass());
    }
    
    private XmlChild getElementAnnotation() {
        if(collectionField.isAnnotationPresent(XmlChild.class))
            return collectionField.getAnnotation(XmlChild.class);
        return new DummyXmlChildAnnotation();
    }
    
    private boolean isMap() {
        Class c = valueWrapper.getValueClass();
        return (Map.class.isAssignableFrom(c));
    }
    
    private void initKeyWrapper() {
        if(isMap()) 
            keyWrapper = new KeyWrapper(getKeyAnnotation(), getKeyClass());
    }
    
    private XmlAttribute getKeyAnnotation() {
        if(collectionField.isAnnotationPresent(XmlAttribute.class))
            return collectionField.getAnnotation(XmlAttribute.class);
        return new DummyXmlAttributeAnnotation();
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
        return getAnnotation().index();
    }

    @Override
    public boolean addIfNull() {
        return super.getAnnotation().addIfNull();
    }

    @Override
    public List<XmlAttributeWrapper> getAttributes() {
        return fixAttributes;
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
    public boolean addIfEmpty() {
        return super.getAnnotation().addIfEmpty();
    }

    @Override
    public boolean addOnlyContainedElements() {
        return super.getAnnotation().addOnlyContainedElements();
    }
    
    @Override
    public Class getElementClass() {
        return super.getAnnotation().elementClass();
    }

    @Override
    public Class getKeyClass() {
        return super.getAnnotation().keyClass();
    }
    
    @Override
    public String getQualifiedKeyName() {
        if(keyWrapper == null)
            return null;
        return keyWrapper.getQualifiedName();
    }
    
    @Override
    public XmlChildWrapper getElementWrapper() {
        return elementWrapper;
    }

    @Override
    public List<XmlNamespaceWrapper> getNamespaces() {
        return namespaces;
    }

    @Override
    public boolean isChildOfParent(String qualifiedName) {
        if(!addOnlyContainedElements())
            return getQualifiedName().equalsIgnoreCase(qualifiedName);
        return elementWrapper.isChildOfParent(qualifiedName);
    }

    @Override
    public Field getMember() {
        return getAnnotatedElement();
    }
    
    private class DummyXmlChildAnnotation implements XmlChild {
        @Override
        public String name() {
            return getAnnotation().name();
        }
        @Override
        public String nsPrefix() {
            return getAnnotation().nsPrefix();
        }
        @Override
        public int index() {
            return -1;
        }
        @Override
        public boolean addIfNull() {
            return getAnnotation().addIfNull();
        }
        @Override
        public boolean overrideElement() {
            return false;
        }
        @Override
        public String[] attributes() {
            return new String[]{""};
        }

        @Override
        public String[] namespaces() {
            return new String[]{""};
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return XmlChild.class;
        }
    }
    
    private class DummyXmlAttributeAnnotation implements XmlAttribute {
        @Override
        public String name() {
            return DEFAULT_KEY_NAME;
        }
        @Override
        public String nsPrefix() {
            return DEFAULT_KEY_PREFIX;
        }
        @Override
        public String defaultValue() {
            return null;
        }
        @Override
        public boolean addIfDefault() {
            return true;
        }
        @Override
        public int index() {
            return -1;
        }
        @Override
        public Class<? extends Annotation> annotationType() {
            return XmlAttribute.class;
        }

        @Override
        public Type type() {
            return Type.CDATA;
        }
    }
    
    private class MyAbstractWrapper implements ValueWrapper<Field> {
        
        private NameManager name;
        private Class clazz;
        
        MyAbstractWrapper(NameManager name, Class clazz) {
            this.name = name;
            this.clazz = clazz;
        }
        
        public String getName() {
            return name.getName();
        }

        public String getNsPrefix() {
            return name.getNsPrefix();
        }

        public String getQualifiedName() {
            return name.getQualifiedName();
        }        

        public Field getAnnotatedElement() {
            return collectionField;
        }

        @Override
        public Class getValueClass() {
            return clazz;
        }

        public int getIndex() {
            return -1;
        }

        @Override
        public Object getValue(Object instance) {
            return instance;
        }

        @Override
        public Field getMember() {
            return collectionField;
        }
    }
    
    private static NameManager getNameManager(XmlChild annotation, Class elementClass) {
        XmlElement element = (XmlElement) elementClass.getAnnotation(XmlElement.class);
        if(element != null)
            return NameManager.getInstance(annotation, element);
        return NameManager.getInstance(annotation);
    }

    private class ElementWrapper extends MyAbstractWrapper implements XmlChildWrapper {
        
        private XmlChild annotation;
        private List<XmlNamespaceWrapper> namespaces;
        private List<XmlAttributeWrapper> attributes;
        private XmlElementWrapper xmlChildWrapper;
        private XmlStringValueWrapper stringValueWrapper;
        
        ElementWrapper(XmlChild annotation, Class elementClass) {
            super(getNameManager(annotation, elementClass), elementClass);
            this.annotation = annotation;
            initState();
        }
        
        
        private void initState() {
            initXmlChildWrapper();
            initNamespaces();
            initAttributes();
            if(xmlChildWrapper==null) initStringChildWrapper();
        }
        
        private void initAttributes() {
            ChildAttributeFactory factory = new ChildAttributeFactory(this, xmlChildWrapper);
            attributes = factory.getAttributes();
            addKeyAttribute();
        }
        
        private void initNamespaces() {
            ChildNamespaceFactory factory = new ChildNamespaceFactory(this, xmlChildWrapper);
            namespaces = factory.buildNamespaces();
        }
        
        private void addKeyAttribute() {
            if(!isMap()) return;
            checkNameNotUsed(keyWrapper.getQualifiedName());
            attributes.add(keyWrapper);
        }
        
        private void checkNameNotUsed(String qName) {
            for(XmlAttributeWrapper attribute : attributes)
                if(attribute.getQualifiedName().equalsIgnoreCase(qName))
                    throw keyNameExists(attribute);
        }
        
        private IllegalArgumentException keyNameExists(XmlAttributeWrapper attribute) {
            Field field = attribute.getAnnotatedElement();
            String msg = "Field '%s' in class %s has the same qualified-name ('%s') as the key attribute!";
            msg = String.format(msg, field.getName(),
                    field.getDeclaringClass().getCanonicalName(),
                    attribute.getQualifiedName());
            return new IllegalArgumentException(msg);
        }
        
        private void initXmlChildWrapper() {
            if(super.clazz.isAnnotationPresent(XmlElement.class))
                xmlChildWrapper = new XmlElementWrapperImpl(super.clazz);
        }
        
        private void initStringChildWrapper() {
            try {
                stringValueWrapper = XmlStringValueWrapperBuilder.getWrapperForClass(getElementClass());
            } catch (IllegalArgumentException ex) {
                throw invalidTypeException();
            }
        }
        
        private IllegalArgumentException invalidTypeException() {
            String msg = "The Container field '%s' in class %s should contain "+
                         "primitives, primitive wrappers, strings, classes "+
                         "that are annotated with XmlElement annotation or "+
                         "classes that can be converted to an xml string!";
            msg = String.format(msg, collectionField.getName(),
                    collectionField.getDeclaringClass().getCanonicalName());
            return new IllegalArgumentException(msg);
        }
        
        @Override
        public boolean overrideElement() {
            return annotation.overrideElement();
        }

        @Override
        public XmlElementWrapper getChildElementWrapper() {
            return xmlChildWrapper;
        }

        @Override
        public XmlStringValueWrapper getStringWrapper() {
            return stringValueWrapper;
        }

        @Override
        public boolean addIfNull() {
            return annotation.addIfNull();
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
        public XmlChild getAnnotation() {
            return annotation;
        }

        @Override
        public boolean isChildOfParent(String qualifiedName) {
            return getQualifiedName().equalsIgnoreCase(qualifiedName);
        }
    }
    
    private class KeyWrapper extends MyAbstractWrapper implements XmlAttributeWrapper {
        
        private XmlAttribute annotation;
        private XmlStringValueWrapper childWrapper;
        
        KeyWrapper(XmlAttribute annotation, Class keyClass) {
            super(NameManager.getInstance(annotation), keyClass);
            this.annotation = annotation;
            initStringValueWrapper();
        }
        
        private void initStringValueWrapper() {
            try {
                childWrapper = XmlStringValueWrapperBuilder.getWrapperForClass(getKeyClass());
            } catch (IllegalArgumentException ex) {
                throw invalidTypeException();
            }
        }
        
        private IllegalArgumentException invalidTypeException() {
            String msg = "The Container field '%s' in class %s should contain "+
                         "keys that are primitives, primitive wrappers, strings, "+
                         "classes that are annotated with XmlElement annotation "+
                         "or classes that can be converted to an xml string!";
            msg = String.format(msg, collectionField.getName(),
                    collectionField.getDeclaringClass().getCanonicalName());
            return new IllegalArgumentException(msg);
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
            return annotation;
        }

        @Override
        public XmlStringValueWrapper getChildWrapper() {
            return childWrapper;
        }

        @Override
        public Type getType() {
            return annotation.type();
        }

        @Override
        public Object getPrimitiveValue(Object instance) throws Exception {
            return childWrapper.getPrimitiveValue(instance);
        }
        
        @Override
        public boolean trimInput() {
            return true;
        }
    }
}
