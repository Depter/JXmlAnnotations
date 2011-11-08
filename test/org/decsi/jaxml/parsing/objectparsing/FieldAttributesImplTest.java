package org.decsi.jaxml.parsing.objectparsing;

import java.lang.reflect.Field;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import java.util.List;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlChildWrapper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class FieldAttributesImplTest {
    
    private AttributesImpl attributes;
    
    public FieldAttributesImplTest() {
    }
    
    public void initAttributes(Class c) throws Exception {
        NamespaceManager nsManager = getNsManager();
        PrimitiveParser pParser = new PrimitiveParser();
        List<XmlAttributeWrapper> wrappers = getWrappers(c);
        attributes = new AttributesImpl(nsManager, pParser, wrappers);
    }
    
    private NamespaceManager getNsManager() {
        NamespaceManager nsManager = NamespaceManagerTest.getNamespaceManager();
        nsManager.addNamespace(NamespaceManagerTest.getNsWrapper("b", "www.bela.org"));
        return nsManager;
    }
    
    private List<XmlAttributeWrapper> getWrappers(Class c) throws Exception {
        Field field = c.getDeclaredField("field");
        XmlChildWrapper wrapper = 
                WrapperFactory.getChildWrapper(field);
        return wrapper.getAttributes();
    }

    @Test
    public void testGetLength() throws Exception {
        initAttributes(Overridden.class);
        assertEquals(4, attributes.getLength());
        initAttributes(NotOverridden.class);
        assertEquals(4, attributes.getLength());
    }

    @Test
    public void testGetIndex_String_String() throws Exception {
        initAttributes(Overridden.class);
        assertEquals(0, attributes.getIndex(null, "fix"));
        assertEquals(1, attributes.getIndex("www.bela.org", "name"));
        assertEquals(2, attributes.getIndex(null, "id"));
        
        initAttributes(NotOverridden.class);
        assertEquals(0, attributes.getIndex("www.bela.org", "name"));
        assertEquals(1, attributes.getIndex(null, "id"));
        assertEquals(2, attributes.getIndex(null, "value"));
        assertEquals(3, attributes.getIndex(null, "fix"));
    }

    @Test
    public void testGetValue_int() throws Exception {
        initAttributes(Overridden.class);
        attributes.setInstance(new Overridden().field);
        assertEquals("10", attributes.getValue(0));
        assertEquals("geza", attributes.getValue(1));
        assertEquals("1", attributes.getValue(2));
        
        initAttributes(NotOverridden.class);
        attributes.setInstance(new NotOverridden().field);
        assertEquals("bela", attributes.getValue(0));
        assertEquals("1", attributes.getValue(1));
        assertEquals("10", attributes.getValue(2));
    }
    
    private static class Overridden {
        @XmlChild(attributes={"fix=10", "b:name=geza"}, overrideElement=true)
        private AttributesImplTest.Dummy field = new AttributesImplTest.Dummy();
    }
    
    private static class NotOverridden {
        @XmlChild(attributes={"fix=10", "b:name=geza"})
        private AttributesImplTest.Dummy field = new AttributesImplTest.Dummy();
    }
}
