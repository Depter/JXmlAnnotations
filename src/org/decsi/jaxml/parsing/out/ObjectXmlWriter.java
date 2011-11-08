package org.decsi.jaxml.parsing.out;

import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public interface ObjectXmlWriter {
    
    public void write(Object instance) throws SAXException;
}
