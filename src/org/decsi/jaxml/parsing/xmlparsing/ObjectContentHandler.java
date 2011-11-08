package org.decsi.jaxml.parsing.xmlparsing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Peter Decsi
 */
class ObjectContentHandler extends DefaultHandler {
    
    private NamespaceMap nss = new NamespaceMap();
    private ElementNode root;
    private ElementNode node;
    private boolean hasStringContent = false;
    private StringBuilder strBuilder = new StringBuilder();
    
    ObjectContentHandler(ElementNode root) {
        this.node = root;
        this.root = root;
    }
    
    void reset() {
        root.resetElement();
        node = root;
    }
    
    Object getValue() {
        return root.getInstance();
    }
    
    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        nss.addNamespace(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        nss.removeNamespace(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String name = getQualifiedName(uri, localName, qName);
        node = node.createChildNode(name, atts);
    }
    
    private String getQualifiedName(String uri, String localName, String qName) {
        if(qName != null && qName.trim().length()>0)
            return qName;
        String prefix = nss.getPrefixForUri(uri);
        return prefix==null? localName : prefix+":"+localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        addStringContent();
        node.endElement();
        node = node.getParent();
    }
    
    private void addStringContent() throws SAXException {
        if(!hasStringContent) return;
        String content = getStringContent();
        node.setString(content);
        resetStrBuilder();
    }
    
    private String getStringContent() {
        String content = strBuilder.toString();
        return node.trimStringContent()?
                content.trim() : content;
    }
    
    private void resetStrBuilder() {
        strBuilder.setLength(0);
        hasStringContent = false;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(!node.hasStringContent())
            return;
        hasStringContent = true;
        strBuilder.append(ch, start, length);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        char[] chs = ("&"+name+";").toCharArray();
        this.characters(chs, 0, chs.length);
    }
    
}
