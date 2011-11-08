package org.decsi.jaxml.parsing.out;

import java.util.List;
import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
public class ElementDummyImpl implements ElementDummy {
    private String qName;
    private Attributes attributes;
    
    private List<NsDeclaration> nsDeclarations;

    ElementDummyImpl(List<NsDeclaration> nsDeclarations) {
        this.nsDeclarations = nsDeclarations;
    }
    
    void setState(String qName, Attributes attributes) {
        this.qName = qName;
        escpaName();
        this.attributes = attributes;
    }
    
    private void escpaName() {
        if(qName==null) return;
        qName = qName.trim();
        if(qName.length()==0) qName = null;
    }
    
    @Override
    public String getQualifiedName() {
        return qName;
    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public List<NsDeclaration> getNsDeclarations() {
        return nsDeclarations;
    }
}
