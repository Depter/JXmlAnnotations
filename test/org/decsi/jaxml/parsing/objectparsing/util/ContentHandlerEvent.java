package org.decsi.jaxml.parsing.objectparsing.util;

import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
public class ContentHandlerEvent {

    private ContentHandlerEventType type;
    private String prefix;
    private String uri;
    private String localName;
    private String qName;
    private Attributes attributes;
    private char[] chrs;
    
    public ContentHandlerEvent(ContentHandlerEventType type) {
        this.type = type;
    }
    
    void setPrefixEvent(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }
    
    void setElementEvent(String uri, String localName, String qName, Attributes atts) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
        if(atts!=null)
            this.attributes = new AttributesUtil(atts);
    }
    
    void setCharacters(char[] chrs) {
        this.chrs = chrs;
    }
    
    public ContentHandlerEventType getType() {
        return type;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public char[] getChrs() {
        return chrs;
    }

    public String getLocalName() {
        return localName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getqName() {
        return qName;
    }

    public String getUri() {
        return uri;
    }
}
