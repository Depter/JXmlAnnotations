package org.decsi.jaxml.xml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
public class Element extends NamedContent {
    
    private List<Content> contents = new ArrayList<Content>();
    private List<Attribute> attributes = new ArrayList<Attribute>();
    
    public Element(String name, String nsPrefix) {
        super(name, nsPrefix);
    }
    
    public int attributeCount() {
        return attributes.size();
    }
    
    public List<Attribute> getAttributes() {
        return new ArrayList<Attribute>(attributes);
    }
    
    public void addAttribute(Attribute attribute) {
        if(attribute!=null) {
            attribute.removeFromParent();
            attribute.setParent(this);
            attributes.add(attribute);
        }
    }
    public void removeAttrribute(Attribute attribute) {
        if(attribute == null) return;
        if(attributes.remove(attribute))
            attribute.setParent(this);
    }
    
    public Attribute getAttribute(String qualifiedName) {
        for(Attribute a : attributes)
            if(a.getQualifiedName().equalsIgnoreCase(qualifiedName))
                return a;
        return null;
    }
    
    public Content addStringContent(String str) {
        if(str==null) return null;
        Content c = new Content();
        c.setTextContent(str);
        addContent(c);
        return c;
    }
    
    public void addContent(Content content) {
        if(content != null) {
            content.removeFromParent();
            content.setParent(this);
            contents.add(content);
        }
    }
    
    public void removeContent(Content content) {
        if(content == null) return;
        if(content instanceof Attribute) {
            removeAttrribute((Attribute) content);
        } else {
            if(contents.remove(content))
                content.setParent(null);
        }
    }
    
    public int getContentCount() {
        return contents.size();
    }
    
    public List<Content> getContents() {
        return new ArrayList<Content>(contents);
    }
    
    public int getChildCount() {
        int count = 0;
        for(Content c : contents)
            if(c instanceof Element)
                count++;
        return count;
    }
    public Element getChildAt(int index) {
        int count = 0;
        for(Content c : contents)
            if(c instanceof Element)
                if(count++ == index)
                    return (Element) c;
        throw new IndexOutOfBoundsException(
                String.format("Index %d is out of bounds [0,%d]", index, getChildCount()));
    }

    public List<Element> getChildren() {
        List<Element> children = new ArrayList<Element>(contents.size());
        for(Content c : contents)
            if(c instanceof Element)
                children.add((Element) c);
        return children;
    }
    
    @Override
    public String getTextContent() {
        StringBuilder builder = new StringBuilder();
        for(Content c : contents)
            appendContent(builder, c);
        return builder.length()==0? null : builder.toString();
    }
    
    private void appendContent(StringBuilder builder, Content c) {
        if(c == null) return;
        else if(c instanceof Element) 
            builder.append(((Element)c).getFullTextContent());
        else builder.append(c.getTextContent());
    }
    
    public String getFullTextContent() {
        StringBuilder builder = new StringBuilder('<').append(getQualifiedName());
        appendAttributes(builder);
        appendContent(builder);
        return builder.toString();
    } 
    private void appendAttributes(StringBuilder builder) {
        for(Attribute a : attributes)
            builder.append(' ').append(a.getTextContent());
    }
    private void appendContent(StringBuilder builder) {
        if(contents.isEmpty()) 
            builder.append("/>");
        for(Content content : contents) 
            builder.append(content.getTextContent());
        if(!contents.isEmpty()) 
            builder.append("</").append(getQualifiedName()).append('>');
    }
    
}
