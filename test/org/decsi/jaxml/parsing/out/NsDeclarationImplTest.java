package org.decsi.jaxml.parsing.out;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class NsDeclarationImplTest {
    
    private NsDeclarationImpl ns;
    
    public NsDeclarationImplTest() {
    }

    @Test
    public void testGetPrefix() {
        ns = new NsDeclarationImpl("ns", "www.bela.org");
        assertEquals("ns", ns.getPrefix());
        
        ns = new NsDeclarationImpl(null, "www.bela.org");
        assertEquals(null, ns.getPrefix());
        
        ns = new NsDeclarationImpl(" ", "www.bela.org");
        assertEquals(null, ns.getPrefix());
        
        ns = new NsDeclarationImpl(" ns ", "www.bela.org");
        assertEquals("ns", ns.getPrefix());
    }

    @Test
    public void testGetURI() {
        ns = new NsDeclarationImpl("ns", "www.bela.org");
        assertEquals("www.bela.org", ns.getURI());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNoUriException_Null() {
        new NsDeclarationImpl("ns", null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNoUriException_Empty() {
        new NsDeclarationImpl("ns", "  ");
    }
}
