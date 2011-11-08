package org.decsi.jaxml.parsing.objectparsing;

import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
interface ObjectParser {
    
    void parse(Object instance) throws SAXException;
}
