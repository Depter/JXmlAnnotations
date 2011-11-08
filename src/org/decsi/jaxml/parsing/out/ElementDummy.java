package org.decsi.jaxml.parsing.out;

import java.util.List;
import org.xml.sax.Attributes;

/**
 *
 * @author Peter Decsi
 */
public interface ElementDummy {
    public String getQualifiedName();
    public List<NsDeclaration> getNsDeclarations();
    public Attributes getAttributes();
}
