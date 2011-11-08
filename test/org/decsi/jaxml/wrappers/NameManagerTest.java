package org.decsi.jaxml.wrappers;

import org.decsi.jaxml.wrappers.NameManager;
import org.decsi.jaxml.wrappers.AbstractWrapper;
import java.lang.reflect.Field;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlElement;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class NameManagerTest {
    
    public NameManagerTest() {
    }

    @Test
    public void testXmlElement() {
        XmlElement an = ClassDummy.class.getAnnotation(XmlElement.class);
        NameManager name = NameManager.getInstance(an);
        assertEquals("dummy", name.getName());
        assertEquals(null, name.getNsPrefix());
        assertEquals("dummy", name.getQualifiedName());
        
        an = NsClassDummy.class.getAnnotation(XmlElement.class);
        name = NameManager.getInstance(an);
        assertEquals("dummy", name.getName());
        assertEquals("ns", name.getNsPrefix());
        assertEquals("ns:dummy", name.getQualifiedName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testXmlElemen_NullName() {
        XmlElement an = IllegalClassDummy.class.getAnnotation(XmlElement.class);
        AbstractWrapper w = new AbstractWrapper(IllegalClassDummy.class);
        w.initState(XmlElement.class);
        NameManager.getInstance(an).checkName(w);
    }

    @Test
    public void testXmlAttribute() throws Exception {
        XmlAttribute an = getAttributeAnnotation("name");
        NameManager name = NameManager.getInstance(an);
        assertEquals("name", name.getName());
        assertEquals(null, name.getNsPrefix());
        assertEquals("name", name.getQualifiedName());
        
        an = getAttributeAnnotation("nsName");
        name = NameManager.getInstance(an);
        assertEquals("name", name.getName());
        assertEquals("sn", name.getNsPrefix());
        assertEquals("sn:name", name.getQualifiedName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testXmlAttribute_NullName() throws Exception {
        Field f = AttributeDummy.class.getDeclaredField("nullName");
        XmlAttribute an = f.getAnnotation(XmlAttribute.class);
        AbstractWrapper w = new AbstractWrapper(f);
        w.initState(XmlAttribute.class);
        NameManager.getInstance(an).checkName(w);
    }
        
    private XmlAttribute getAttributeAnnotation(String name) throws Exception {
        Field f = AttributeDummy.class.getDeclaredField(name);
        return f.getAnnotation(XmlAttribute.class);
    }
    
    @XmlElement(name="dummy")
    private static class ClassDummy{}
        
    @XmlElement(name="dummy", nsPrefix="ns")
    private static class NsClassDummy{}
        
    @XmlElement(name="", nsPrefix="bela")
    private static class IllegalClassDummy{}
        
    private static class AttributeDummy{
        
        @XmlAttribute(name="name")
        private String name = "bela";
        
        @XmlAttribute(name="name", nsPrefix="sn")
        private String nsName = "bela";
        
        @XmlAttribute(name="", nsPrefix="sn")
        private String nullName = "bela";
        
    }
}
