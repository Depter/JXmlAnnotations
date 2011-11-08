package org.decsi.jaxml.parsing.out;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
public class PlainFormatter implements XmlFormatter{
    
    private final static String FIRST_ROW = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final static String INSTRUCTION_FORMAT = "<?%s %s?>";
    private final static String EMPTY_ELEMENT = "<%s %s %s />";
    private final static String ELEMENT = "<%s %s>";
    private final static String ELEMENT_END = "</%s>";
    protected final static String NS_PREFIX = "xmlns";
    protected final static String PREFIX_SEPARATOR = ":";
    protected final static String ATTRIBUTE_FORMAT = "%s=\"%s\"";

    @Override
    public void startDocument(Writer writer) throws IOException {
        writer.write(FIRST_ROW);
    }

    @Override
    public void endDocument(Writer writer) {
    }

    @Override
    public void writeProcessingInstruction(Writer writer, String target, String data) throws IOException {
        String instruction = getInstructionAsString(target, data);
        writer.write(instruction);
    }
    
    protected String getInstructionAsString(String target, String data) {
        if(data==null)
            data="";
        return String.format(INSTRUCTION_FORMAT, target, data);
    }
    
    @Override
    public void emptyElement(Writer writer, ElementDummy element) throws IOException {
        String str = "<"+element.getQualifiedName();
        str = appendElementContent(str, getNsList(element.getNsDeclarations()));
        str = appendElementContent(str, getAttributeList(element.getAttributes()));
        writer.write(str+" />");
    }
    
    protected String getNsList(List<NsDeclaration> nss) {
        String nsList = "";
        for(int i=0; i<nss.size(); i++)
            nsList+=(i>0? " " : "")+getNs(nss.get(i));
        return nsList;
    }
    
    protected String getNs(NsDeclaration ns) {
        String prefix = ns.getPrefix();
        if(prefix==null || prefix.trim().length()==0)
            return NS_PREFIX+"=\""+ns.getURI()+"\"";
        return NS_PREFIX+PREFIX_SEPARATOR+prefix+"=\""+ns.getURI()+"\"";
    }
    
    protected String getAttributeList(Attributes attributes) {
        String list = "";
        for(int i=0; i<attributes.getLength(); i++)
            list+=(i>0? " " : "")+getAttribute(attributes, i);
       return list;
    }
    
    protected String getAttribute(Attributes attributes, int index) {
        String qName = attributes.getQName(index);
        String value = attributes.getValue(index);
        return String.format(ATTRIBUTE_FORMAT, qName, value);
    }
    
    @Override
    public void startElement(Writer writer, ElementDummy element) throws IOException {
        String str = "<"+element.getQualifiedName();
        str = appendElementContent(str, getNsList(element.getNsDeclarations()));
        str = appendElementContent(str, getAttributeList(element.getAttributes()));
        writer.write(str+">");
    }
    
    protected String appendElementContent(String element, String content) {
        if(content == null || content.trim().length()==0)
            return element;
        return element+" "+content;
    }

    @Override
    public void endElement(Writer writer, ElementDummy element) throws IOException {
        String qName = element.getQualifiedName();
        writer.write(String.format(ELEMENT_END, qName));
    }

    @Override
    public void stringContent(Writer writer, String cotnent) throws IOException {
        writer.write(cotnent);
    }
    
}
