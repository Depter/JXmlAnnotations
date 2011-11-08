package org.decsi.jaxml.parsing.xmlparsing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
interface ElementNode {
    
    public ElementNode getParent();
    public ElementNode createChildNode(String name, Attributes attributes) throws SAXException;
    
    public void setString(String value) throws SAXException;
    public Object getInstance();
    
    public void endElement() throws SAXException;
    public void resetElement();
    
    public boolean hasStringContent();
    public boolean trimStringContent();
}
