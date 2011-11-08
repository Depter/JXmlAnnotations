package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.ChildWrapper;
import org.decsi.jaxml.wrappers.ChildBuilder;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlChild;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class ChildBuilderTest {

    private ChildBuilder builder;

    public ChildBuilderTest() {
    }

    @Before
    public void setUp() {
        builder = new ChildBuilder();
    }


    @Test
    public void testBuild() {
        List<ChildWrapper> children = builder.buildContent(Dummy.class);
        assertEquals(4, children.size());
        
        assertEquals("name", children.get(0).getQualifiedName());
        assertEquals("id", children.get(1).getQualifiedName());
        assertEquals("post-code", children.get(2).getQualifiedName());
        assertEquals("state", children.get(3).getQualifiedName());
    }

    @Test
    public void testExcludeContainer() {
        List<ChildWrapper> children = builder.buildContent(ContainerDummy.class);
        assertEquals(2, children.size());
        assertEquals("id", children.get(0).getQualifiedName());
        assertEquals("names", children.get(1).getQualifiedName());
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
        @XmlChild(name="id", index=2)
        private int id = 1;
        
        @XmlChild(name="name", index=1)
        private String name = "bela";
        
        @XmlChild(name="state")
        private String state = "geza";
        
        @XmlChild(name="post-code")
        private int postCode = 1000;
    }
    
    private static class ContainerDummy {
        @XmlChild(name="id")
        private int id = 1;
        
        @XmlChildContainer(name="names", 
                elementClass=String.class, keyClass=String.class)
        @XmlAttribute(name="key")
        private Map<String, String> names;
    }
    
    private static class SameIndexDummy {
        @XmlChild(name="id", index=1)
        private int id = 1;
        
        @XmlChild(name="name", index=1)
        private String name = "bela";
    }
    
    private static class SameNameDummy {
        @XmlChild(name="name")
        private int id = 1;
        
        @XmlChild(name="name")
        private String name = "bela";
    }
}
