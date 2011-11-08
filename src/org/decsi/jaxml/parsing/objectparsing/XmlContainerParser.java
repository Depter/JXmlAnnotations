package org.decsi.jaxml.parsing.objectparsing;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.decsi.jaxml.wrappers.XmlChildContainerWrapper;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.decsi.jaxml.wrappers.XmlStringValueWrapper;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class XmlContainerParser extends AbstractElementParser {
    
    private XmlChildContainerWrapper containerWrapper;
    private XmlChildWrapper elementWrapper;
    private AttributesImpl containerAttributes;
    private ContainerElementParser elementParser;
    
    private Object[] array;
    private Collection collection;
    private Map map;
    
    XmlContainerParser(XmlChildContainerWrapper wrapper, SAXEventDispatcher dispatcher, PrimitiveParser primitiveParser) {
        super(dispatcher, primitiveParser);
        this.containerWrapper = wrapper;
        this.elementWrapper = wrapper.getElementWrapper();
        initState();
    }
    
    private void initState() {
        containerAttributes = new AttributesImpl(dispatcher.getNamespaceManager(), 
                primitiveParser, containerWrapper.getAttributes());
        elementParser = new ContainerElementParser();
    }
    
    @Override
    public void parse(Object instance) throws SAXException {
        getValue(instance);
        parseValue();
        setValue(null, null, null);
    }
    
    private void getValue(Object instance) throws SAXException {
        Object value = super.getValue(containerWrapper, instance);
        if(value == null) {
            setValue(null, null, null);
        } else if(Map.class.isAssignableFrom(value.getClass())) {
            setValue(null, (Map) value, null);
        } else if(Collection.class.isAssignableFrom(value.getClass())) {
            setValue((Collection) value, null, null);
        } else if(value.getClass().isArray()) {
            setValue(null, null, (Object[]) value);
        }
    }
    
    private void setValue(Collection collection, Map map, Object[] array) {
        this.collection = collection;
        this.array = array;
        this.map = map;
    }
    
    private void parseValue() throws SAXException {
        if(isNullValue()) parseNullValue();
        else parseNonNullValue();
    }
    
    private boolean isNullValue() {
        return collection==null && map==null && array==null;
    }
    
    private void parseNullValue() throws SAXException {
        if(!containerWrapper.addIfNull() || 
           !containerWrapper.addIfEmpty() || 
           containerWrapper.addOnlyContainedElements()) return;
        parseEmptyTag();
    }
    
    private void parseEmptyTag() throws SAXException {
        startContainerElement();
        endContainerElement();
    }
    
    private void startContainerElement() throws SAXException {
        startPrefixMapping(containerWrapper.getNamespaces());
        dispatcher.fireStartElement(containerWrapper, containerAttributes);
    }
    
    private void endContainerElement() throws SAXException {
        dispatcher.fireEndElement(containerWrapper);
        endPrefixMapping(containerWrapper.getNamespaces());
    }
    
    private void parseNonNullValue() throws SAXException {
        if(isEmpty()) parseEmptyValue();
        else parseNotEmptyValue();
    }
    
    private boolean isEmpty() {
        if(collection!=null) return collection.isEmpty();
        if(map!=null) return map.isEmpty();
        return array.length==0;
    }
    
    private void parseEmptyValue() throws SAXException {
        if(!containerWrapper.addIfEmpty() || 
           containerWrapper.addOnlyContainedElements()) return;
        parseEmptyTag();
    }
    
    private void parseNotEmptyValue() throws SAXException {
        boolean addContainer = !containerWrapper.addOnlyContainedElements();
        if(addContainer) 
            startContainerElement();
        parseChildren();
        if(addContainer)
            endContainerElement();
    }
    
    private void parseChildren() throws SAXException {
        Iterator it = getChildIterator();
        while(it.hasNext())
            elementParser.parse(it.next());
    }
    
    private Iterator getChildIterator() {
        if(collection!=null) return collection.iterator();
        if(array!=null) return new ArrayIterator(array);
        return map.keySet().iterator();
    }
    
    private class ContainerElementParser extends AbstractElementParser {
    
        private ContainerElementAttributes attributes;
        
        ContainerElementParser() {
            super(XmlContainerParser.this.dispatcher, 
                  XmlContainerParser.this.primitiveParser);
            initState();
        }

        private void initState() {
            initAttributes();
            addChildWrappers();
            addStringContentWrapper(elementWrapper.getStringWrapper());
        }

        protected void initAttributes() {
            attributes = new ContainerElementAttributes(
                        elementWrapper.getAttributes(),
                        containerWrapper.getQualifiedKeyName());
        }

        private void addChildWrappers() {
            XmlElementWrapper childWrapper = elementWrapper.getChildElementWrapper();
            if(childWrapper == null) return;
            addChildWrappers(childWrapper.getChildren());
        }

        @Override
        public void parse(Object instance) throws SAXException {
            Object value = getValue(instance);
            initAttributes(value, instance);
            parseValue(value);
        }
        
        private Object getValue(Object instance) {
            if(map==null)
                return instance;
            return map.get(instance);
        }
        
        private void initAttributes(Object value, Object keyValue) {
            attributes.key = keyValue;
            attributes.setInstance(value);
        }
        
        private void parseValue(Object value) throws SAXException {
            if(value == null) 
                parseNull();
            else 
                parseNotNull(value);
        }
        
        private void parseNull() throws SAXException {
            if(!elementWrapper.addIfNull()) return;
            dispatcher.fireStartElement(elementWrapper, attributes);
            dispatcher.fireEndElement(elementWrapper);        
        }

        private void parseNotNull(Object value) throws SAXException {
            dispatcher.fireStartElement(elementWrapper, attributes);
            parseChildren(value);
            dispatcher.fireEndElement(elementWrapper);
        }      
    }
    
    private class ContainerElementAttributes extends AttributesImpl {
        
        private Object key;
        
        ContainerElementAttributes(List<XmlAttributeWrapper> wrappers, String keyName) {
            super(XmlContainerParser.this.dispatcher.getNamespaceManager(), 
                  XmlContainerParser.this.primitiveParser,
                  wrappers);
        }
        
        @Override
        protected Object getValueWithoutException(XmlAttributeWrapper attribute) {
            Object usedInstance = getUsedInstance(attribute);
            XmlStringValueWrapper stringWrapper = attribute.getChildWrapper();
            try{
                return attribute.getPrimitiveValue(usedInstance);
                //return stringWrapper.getPrimitiveValue(usedInstance);
            } catch(Exception ex) {
                return null;
            }
        }
        
        private Object getUsedInstance(XmlAttributeWrapper attribute) {
            return isMapKeyAttribute(attribute)? 
                    key : super.getInstance();
        }
        
        private boolean isMapKeyAttribute(XmlAttributeWrapper attribute) {
            String keyName = containerWrapper.getQualifiedKeyName();
            String qName = attribute.getQualifiedName();
            return qName.equalsIgnoreCase(keyName);
        }
    }
    
    private static class ArrayIterator implements Iterator {
        
        private final Object[] array;
        private int index = 0;
        
        ArrayIterator(Object[] array) {
            this.array = array;
        }
        
        @Override
        public boolean hasNext() {
            return array!=null && index<array.length;
        }

        @Override
        public Object next() {
            return array[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    } 
}
