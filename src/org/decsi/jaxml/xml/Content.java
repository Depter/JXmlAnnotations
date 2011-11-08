package org.decsi.jaxml.xml;

/**
 *
 * @author Peter Decsi
 */
public class Content {
    
    private String content;
    private Element parent;
    
    public Content() {}
    
    void setTextContent(String content) {
        this.content = content;
    }
    
    public String getTextContent() {
        return this.content;
    }
    
    public void setParent(Element parent) {
        this.parent = parent;
    }
    public Element getParent() {
        return parent;
    }
    
    public void removeFromParent() {
        if(parent==null) return;
        parent.removeContent(this);
        parent = null;
    }
}
