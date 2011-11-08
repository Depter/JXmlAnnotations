package org.decsi.jaxml.xml;

/**
 *
 * @author Peter Decsi
 */
public class Attribute extends NamedContent {
    
    public static Attribute fromContent(String content) {
        String nsPrefix = getNsPrefixFromContent(content);
        String name = getNameFromContent(content);
        String value = getValueFromContent(content);
        return new Attribute(name, nsPrefix, value);
    }
    
    private static String getNsPrefixFromContent(String content) {
        int index = content.indexOf(':');
        if(index == -1) return null;
        return content.substring(0, index);
    }
    
    private static String getNameFromContent(String content) {
        int fIndex = content.indexOf(':')+1;
        int lIndex = content.indexOf('=');
        return lIndex==-1? content.substring(fIndex) : content.substring(fIndex, lIndex);
    }
    
    private static String getValueFromContent(String content) {
        int fIndex = content.indexOf('=')+1;
        if(fIndex==-1) return null;
        return removeApostrofs(content.substring(fIndex).trim());
    }
    
    private static String removeApostrofs(String content) {
        if(content.length()==0) return null;
        int fIndex = content.indexOf('"')+1;
        int lIndex = content.lastIndexOf('"');
        content = content.substring(fIndex==-1?0:fIndex, lIndex==-1? content.length() : lIndex);
        return content.length()==0? null : content;
    }
    
    private String value;
    
    public Attribute(String name) {
        this(name, null, null);
    }
    public Attribute(String name, String value) {
        this(name, null, value);
    }
    public Attribute(String name, String prefix, String value) {
        super(name, prefix);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String getTextContent() {
        String txtValue = value==null? "" : value;
        return String.format("%s=\"%s\"", getQualifiedName(), txtValue);
    }
}
