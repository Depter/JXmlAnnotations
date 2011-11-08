package org.decsi.jaxml.parsing.xmlparsing;

import org.junit.Before;
import org.decsi.jaxml.annotation.XmlElement;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.parsing.objectparsing.PrimitiveParser;
import org.decsi.jaxml.wrappers.WrapperFactory;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import org.decsi.jaxml.wrappers.XmlElementWrapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class AttributeBuilderTest {
    
    private AttributeBuilder builder;
    private List<XmlAttributeWrapper> attributes;
    private Dummy dummy;
    
    public AttributeBuilderTest() {
    }
    
    @Before
    public void setUp() {
        dummy = new Dummy();
        initAttributes();
        builder = new AttributeBuilder(dummy, attributes, new PrimitiveParser());
    }
    
    private void initAttributes() {
        XmlElementWrapper wrapper = WrapperFactory.getElementWrapper(Dummy.class);
        attributes = wrapper.getAttributes();
    }

    @Test
    public void testSetAttribute() throws Exception {
        builder.setAttribute("name", "geza");
        assertEquals("geza", dummy.name);
        
        builder.setAttribute("id", "1");
        assertEquals(1, dummy.id.id);
        
        builder.setAttribute("str", null);
        assertEquals("bela", dummy.strDummy.str);
        
        builder.initDefaultAttributes();
        assertEquals("jucus", dummy.defaultValue);
    }
    
    @XmlElement(name="dummy")
    private static class Dummy {
        @XmlAttribute(name="name")
        private String name;
        
        @XmlAttribute(name="id")
        private IdDummy id;
        
        @XmlAttribute(name="str", defaultValue="bela")
        private StringDummy strDummy;
        
        @XmlAttribute(name="default", defaultValue="jucus")
        private String defaultValue;
    }
    
    private static class IdDummy {
        
        @XmlStringValue
        private int id = 2;

        IdDummy(int id) {
            this.id = id;
        }
    }
    
    private static class StringDummy {
        
        @XmlStringValue
        private String str;

        StringDummy(String str) {
            this.str = str;
        }
    }
}
