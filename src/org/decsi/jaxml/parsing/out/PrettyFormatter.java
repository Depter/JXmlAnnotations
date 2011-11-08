package org.decsi.jaxml.parsing.out;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
public class PrettyFormatter extends PlainFormatter{
    
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");
    private int indentLegth;
    private String currentIndent = "";
    
    private boolean firstInstruction = true;
    private boolean rootElement = true;
    
    public PrettyFormatter(int indentLength) {
        this.indentLegth = indentLength;
    }
    
    private String getIndent(int length) {
        String indent = "";
        for(int i=0;i<length;i++)
            indent+=" ";
        return indent;
    }

    @Override
    public void writeProcessingInstruction(Writer writer, String target, String data) throws IOException {
        String instruction = currentIndent + getInstructionAsString(target, data);
        if(firstInstruction) {
            instruction = LINE_SEPARATOR+instruction;
            firstInstruction = false;
        }
        writer.write(instruction);
    }
    
    
    @Override
    public void emptyElement(Writer writer, ElementDummy element) throws IOException {
        extraLineIfRoot(writer);
        String str = LINE_SEPARATOR+getElementBody(element);
        writer.write(str+" />");
    }
    
    private void extraLineIfRoot(Writer writer) throws IOException {
        if(rootElement) {
            writer.write(LINE_SEPARATOR);
            rootElement = false;
        }
    }
    
    private String getElementBody(ElementDummy element) {
        String str = currentIndent+ "<"+element.getQualifiedName();
        List<NsDeclaration> nss = element.getNsDeclarations();
        Attributes attributes = element.getAttributes();
        return appendNsAttributes(str, nss, attributes);
    }
    
    private String appendNsAttributes(String str, List<NsDeclaration> nss, Attributes attributes) {
        String aIndent = getIndent(str.length()+1);
        str = appendElementContent(str, getNsList(nss, aIndent));
        str = appendElementContent(str, getAttributeList(attributes, aIndent, !nss.isEmpty()));
        return str;
    }
    
    private String getNsList(List<NsDeclaration> nss, String aIndent) {
        String nsList = "";
        for(int i=0; i<nss.size(); i++)
            nsList+=(i>0? LINE_SEPARATOR+aIndent : "")+getNs(nss.get(i));
        return nsList;
    }
    
    private String getAttributeList(Attributes attributes, String aIndent, boolean hasNss) {
        String list = "";
        for(int i=0; i<attributes.getLength(); i++)
            list+=((i>0 || hasNss)? LINE_SEPARATOR+aIndent : "")+getAttribute(attributes, i);
       return list;
    }
    
    @Override
    public void startElement(Writer writer, ElementDummy element) throws IOException {
        extraLineIfRoot(writer);
        String str = LINE_SEPARATOR+getElementBody(element);
        writer.write(str+">");
        biggerIndent();
    }
    
    private void biggerIndent() {
        currentIndent += getIndent(indentLegth);
    }
    
    private void smallerIndent() {
        if(currentIndent.length()>=indentLegth)
            currentIndent = currentIndent.substring(indentLegth);
        else 
            currentIndent = "";
    }
    
    @Override
    public void endElement(Writer writer, ElementDummy element) throws IOException {
        smallerIndent();
        String tag = LINE_SEPARATOR+currentIndent+"</"+element.getQualifiedName()+">";
        writer.write(tag);
    }

    @Override
    public void stringContent(Writer writer, String content) throws IOException {
        biggerIndent();
        content = LINE_SEPARATOR+currentIndent+content;
        smallerIndent();
        writer.write(content);
    }
    
}
