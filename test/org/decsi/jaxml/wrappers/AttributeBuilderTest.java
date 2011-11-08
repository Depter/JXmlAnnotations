package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.AttributeBuilder;
import org.decsi.jaxml.wrappers.XmlAttributeWrapper;
import java.util.Map;
import org.junit.Before;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class AttributeBuilderTest {
    
    private AttributeBuilder builder;
    
    public AttributeBuilderTest() {
    }

    @Before
    public void setUp() {
        builder = new AttributeBuilder();
    }

    @Test
    public void testBuild() {
        List<XmlAttributeWrapper> attributes = builder.buildContent(Dummy.class);
        assertEquals(4, attributes.size());
        
        assertEquals("name", attributes.get(0).getQualifiedName());
        assertEquals("id", attributes.get(1).getQualifiedName());
        assertEquals("post-code", attributes.get(2).getQualifiedName());
        assertEquals("state", attributes.get(3).getQualifiedName());
    }

    @Test
    public void testExcludeContainer() {
        List<XmlAttributeWrapper> attributes = builder.buildContent(ContainerDummy.class);
        assertEquals(1, attributes.size());
        assertEquals("id", attributes.get(0).getQualifiedName());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSameIndexException() {
        builder.buildContent(SameIndexDummy.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSameNameException() {
        builder.buildContent(SameNameDummy.class);
    }
    
    private static class Dummy {
        @XmlAttribute(name="id", index=2)
        private int id = 1;
        
        @XmlAttribute(name="name", index=1)
        private String name = "bela";
        
        @XmlAttribute(name="state")
        private String state = "geza";
        
        @XmlAttribute(name="post-code")
        private int postCode = 1000;
    }
    
    private static class ContainerDummy {
        @XmlAttribute(name="id")
        private int id = 1;
        
        @XmlChildContainer(name="names", 
                elementClass=String.class, keyClass=String.class)
        @XmlAttribute(name="key")
        private Map<String, String> names;
    }
    
    private static class SameIndexDummy {
        @XmlAttribute(name="id", index=1)
        private int id = 1;
        
        @XmlAttribute(name="name", index=1)
        private String name = "bela";
    }
    
    private static class SameNameDummy {
        @XmlAttribute(name="name")
        private int id = 1;
        
        @XmlAttribute(name="name")
        private String name = "bela";
    }
}
