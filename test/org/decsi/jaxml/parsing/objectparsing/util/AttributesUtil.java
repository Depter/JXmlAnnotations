package org.decsi.jaxml.parsing.objectparsing.util;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
class AttributesUtil implements Attributes{
    
    private List<AttributeDummy> dummies = new ArrayList<AttributeDummy>();
    
    AttributesUtil(Attributes attributes) {
        for(int i=0; i<attributes.getLength(); i++)
            dummies.add(new AttributeDummy(attributes, i));
    }
    
    @Override
    public int getLength() {
        return dummies.size();
    }

    @Override
    public String getURI(int index) {
        return dummies.get(index).uri;
    }

    @Override
    public String getLocalName(int index) {
        return dummies.get(index).name;
    }

    @Override
    public String getQName(int index) {
        return dummies.get(index).qName;
    }

    @Override
    public String getType(int index) {
        return dummies.get(index).type;
    }

    @Override
    public String getValue(int index) {
        return dummies.get(index).value;
    }

    @Override
    public int getIndex(String uri, String localName) {
        for(int i=0; i<dummies.size(); i++)
            if(!dummies.get(i).name.equalsIgnoreCase(localName) &&
                uriEquals(dummies.get(i).uri, uri))
                return i;
        return -1;
    }
    
    private boolean uriEquals(String dUri, String uri) {
        if(dUri==null) return uri==null;
        return dUri.equalsIgnoreCase(uri);
    }

    @Override
    public int getIndex(String qName) {
        for(int i=0; i<dummies.size(); i++)
            if(dummies.get(i).qName.equalsIgnoreCase(qName))
                return i;
        return -1;
    }

    @Override
    public String getType(String uri, String localName) {
        int index = getIndex(uri, localName);
        return getType(index);
    }

    @Override
    public String getType(String qName) {
        int index = getIndex(qName);
        return getType(index);
    }

    @Override
    public String getValue(String uri, String localName) {
        int index = getIndex(uri, localName);
        return getValue(index);
    }

    @Override
    public String getValue(String qName) {
        int index = getIndex(qName);
        return getValue(index);
    }
    
    private static class AttributeDummy {
        
        private String uri;
        private String name;
        private String qName;
        private String value;
        private String type;
        
        AttributeDummy(Attributes attributes, int index) {
            uri = attributes.getURI(index);
            name = attributes.getLocalName(index);
            qName = attributes.getQName(index);
            value = attributes.getValue(index);
            type = attributes.getType(index);
        }
    }
}
