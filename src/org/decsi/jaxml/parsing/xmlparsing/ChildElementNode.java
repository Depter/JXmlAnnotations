package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
abstract class ChildElementNode extends AbstractNode{
    
    ChildElementNode(ElementNode parent, PrimitiveParser parser) {
        super(parent, parser);
    } 
    
    abstract protected boolean acceptsName(String qualifiedName);
    abstract protected void setValue(Object object) throws SAXException;
    
}
