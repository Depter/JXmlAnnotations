package org.decsi.jaxml.parsing.out;

import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Peter Decsi
 */
public interface XmlFormatter {

    public void startDocument(Writer writer) throws IOException;
    public void endDocument(Writer writer) throws IOException;

    public void writeProcessingInstruction(Writer writer, String target, String data) throws IOException;
    
    public void emptyElement(Writer writer, ElementDummy element) throws IOException;
    public void startElement(Writer writer, ElementDummy element) throws IOException;
    public void endElement(Writer writer, ElementDummy element) throws IOException;

    public void stringContent(Writer writer, String cotnent) throws IOException;
    
}
