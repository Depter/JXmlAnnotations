package org.decsi.jaxml.parsing.xmlparsing;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class NamespaceMapTest {
    
    private NamespaceMap nss = new NamespaceMap();
    
    public NamespaceMapTest() {
    }

    @Test
    public void testGetPrefixForUri() {
        assertEquals(null, nss.getPrefixForUri("www.bela.org"));
        nss.addNamespace("", "www.bela.org");
        assertEquals(null, nss.getPrefixForUri("www.bela.org"));
        nss.addNamespace("ns", "www.bela.org");
        assertEquals("ns", nss.getPrefixForUri("www.bela.org"));
    }

    @Test
    public void testAddRemoveNamespace() {
        nss.addNamespace("ns", "www.bela.org");
        assertEquals("www.bela.org", nss.getUriForPrefix("ns"));
        nss.addNamespace(null, "www.geza.org");
        assertEquals("www.geza.org", nss.getUriForPrefix(null));
        nss.addNamespace(null, "www.jucus.org");
        assertEquals("www.jucus.org", nss.getUriForPrefix(null));
        nss.removeNamespace(null);
        assertEquals("www.geza.org", nss.getUriForPrefix(null));
        nss.removeNamespace("ns");
        assertEquals("", nss.getUriForPrefix("ns"));
    }
}
