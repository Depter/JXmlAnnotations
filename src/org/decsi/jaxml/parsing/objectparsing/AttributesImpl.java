package org.decsi.jaxml.parsing.objectparsing;

import java.util.ArrayList;
import java.util.List;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import static org.decsi.jaxml.parsing.objectparsing.NamespaceManager.EMPTY_STRING;

/**
 *
 * @author Peter Decsi
 */
class AttributesImpl implements Attributes {
    
    private NamespaceManager nameUtil;
    private PrimitiveParser primitiveParser;
    private List<XmlAttributeWrapper> wrappers;
    private List<XmlAttributeWrapper> addedWrappers = new ArrayList<XmlAttributeWrapper>();
    private Object instance;
    
    AttributesImpl(NamespaceManager nameUtil, PrimitiveParser primitiveParser, List<XmlAttributeWrapper> wrappers) {
        this.nameUtil = nameUtil;
        this.primitiveParser = primitiveParser;
        this.wrappers = wrappers;
        initAddedWrappers();
    }
    
    void setInstance(Object instance) {
        this.instance = instance;
        initAddedWrappers();
    }
    
    Object getInstance() {
        return instance;
    }
    
    private void initAddedWrappers() {
        addedWrappers.clear();
        for(XmlAttributeWrapper wrapper : wrappers) 
            if(wrapperShouldBeAdded(wrapper))
                addedWrappers.add(wrapper);
    }
    
    private boolean wrapperShouldBeAdded(XmlAttributeWrapper wrapper) {
        if(isDefaultValue(wrapper))
            return wrapper.addIfDefault();
        return true;
    }
    
    private boolean isDefaultValue(XmlAttributeWrapper wrapper) {
        String value = getValue(wrapper);
        String defaultValue = wrapper.getDefaultValue();
        if(defaultValue == null) return value==null;
        return defaultValue.equals(value);
    }
    
    @Override
    public int getLength() {
        return addedWrappers.size();
    }

    @Override
    public int getIndex(String uri, String localName) {
        String qName = getQualifiedName(uri, localName);
        return getIndex(qName);
    }

    private String getQualifiedName(String uri, String localName) {
        String prefix = nameUtil.getNsPrefixForUri(uri);
        if(prefix==null)
            return localName;
        return prefix+":"+localName;
    }
    
    @Override
    public int getIndex(String qName) {
        for(int i=0; i<addedWrappers.size(); i++)
            if(addedWrappers.get(i).getQualifiedName().equalsIgnoreCase(qName))
                return i;
        return -1;
    }

    @Override
    public String getURI(int index) {
        if(outOfRange(index)) return null;
        XmlAttributeWrapper wrapper = addedWrappers.get(index);
        return getURI(wrapper);
    }

    private boolean outOfRange(int index) {
        if(index<0) return true;
        if(index >= addedWrappers.size()) return true;
        return false;
    }
    
    private String getURI(XmlAttributeWrapper wrapper) {
        String prefix = wrapper.getNsPrefix();
        if(prefix==null || prefix.trim().length()==0)
            return EMPTY_STRING;
        return getUriFromNameUtil(wrapper);
    }
    
    private String getUriFromNameUtil(XmlAttributeWrapper wrapper) {
        try {
            return nameUtil.getURI(wrapper);
        } catch (SAXException ex) {
            return EMPTY_STRING;
        }
    }
    
    @Override
    public String getLocalName(int index) {
        if(outOfRange(index)) return null;
        return nameUtil.getLocalName(addedWrappers.get(index));
    }

    @Override
    public String getQName(int index) {
        if(outOfRange(index)) return null;
        XmlAttributeWrapper wrapper = addedWrappers.get(index);
        try{return nameUtil.getQualifiedName(wrapper);}
        catch (SAXException ex) {return nameUtil.getLocalName(wrapper);}
    }

    @Override
    public String getType(int index) {
        if(outOfRange(index)) return null;
        return addedWrappers.get(index).getType().name();
    }

    @Override
    public String getType(String uri, String localName) {
        return getType(getIndex(uri, localName));
    }

    @Override
    public String getType(String qName) {
        return getType(getIndex(qName));
    }

    @Override
    public String getValue(int index) {
        if(outOfRange(index)) return null;
        return getValue(addedWrappers.get(index));
    }
    
    private String getValue(XmlAttributeWrapper wrapper) {
        Object value = getValueWithoutException(wrapper);
        return primitiveParser.getStringValue(value);
    }
    
    protected Object getValueWithoutException(XmlAttributeWrapper wrapper) {
        try{
            return wrapper.getPrimitiveValue(instance);
        } catch(Exception ex) {
            return null;
        }
    }

    @Override
    public String getValue(String uri, String localName) {
        return getValue(getIndex(uri, localName));
    }

    @Override
    public String getValue(String qName) {
        return getValue(getIndex(qName));
    }
    
}
