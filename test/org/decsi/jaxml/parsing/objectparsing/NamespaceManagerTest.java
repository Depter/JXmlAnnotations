package org.decsi.jaxml.parsing.objectparsing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import org.decsi.jaxml.wrappers.NamedWrapper;
import org.decsi.jaxml.wrappers.XmlNamespaceWrapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Decsi
 */
public class NamespaceManagerTest {
    
    //private FeatureManager features;
    //private PropertyManager properties;
    private NamespaceManager namespaceManager;
    
    public NamespaceManagerTest() {
    }

    @Before
    public void setUp() {
      namespaceManager = getNamespaceManager();  
    }
    
    static NamespaceManager getNamespaceManager() {
        FeatureManager features = new FeatureManager();
        PropertyManager properties = new PropertyManager();
        return new NamespaceManager(features, properties);
    }

    @Test
    public void testAddNamespace() {
        NamespaceImpl ns1 = new NamespaceImpl(null, "www.bela.org");
        assertEquals(null, namespaceManager.addNamespace(ns1));
        
        NamespaceImpl ns2 = new NamespaceImpl(null, "www.geza.org");
        XmlNamespaceWrapper removed = namespaceManager.addNamespace(ns2);
        assertEquals(null, removed.getPrefix());
        assertEquals(ns1.getURI(), removed.getURI());
    }
    
    @Test
    public void testGetLocalName() {
        String name = "bela";
        assertEquals(name, namespaceManager.getLocalName(new NameImpl(name, null)));
        name = "geza";
        assertEquals(name, namespaceManager.getLocalName(new NameImpl(name, null)));
    }
    
    @Test
    public void testGetQualifiedName() throws SAXException {
        String name = "bela";
        String prefix = "ns";
        assertEquals(prefix+":"+name, namespaceManager.getQualifiedName(new NameImpl(name, prefix)));
        assertEquals(name, namespaceManager.getQualifiedName(new NameImpl(name, "")));
        assertEquals(name, namespaceManager.getQualifiedName(new NameImpl(name, null)));
        
        namespaceManager.getProperties().setProperty(SAXProperty.NAMESPACE_SEPARATOR.getPropertyName(), "-");
        assertEquals(prefix+"-"+name, namespaceManager.getQualifiedName(new NameImpl(name, prefix)));
    }
    
    @Test
    public void testGetNamespaceUri() throws SAXException {
        String name = "bela";
        String prefix = null;
        assertEquals("", namespaceManager.getURI(new NameImpl(name, prefix)));

        String uri = "www.bela.org";
        namespaceManager.addNamespace(new NamespaceImpl(null, uri));
        assertEquals(uri, namespaceManager.getURI(new NameImpl(name, prefix)));
        
        prefix = "ns";
        namespaceManager.addNamespace(new NamespaceImpl(prefix, uri));
        assertEquals(uri, namespaceManager.getURI(new NameImpl(name, prefix)));   
    }
    
    @Test(expected=SAXException.class)
    public void testPrefixNotFound() throws SAXException {
        String name = "bela";
        String prefix = "ns";
        namespaceManager.getURI(new NameImpl(name, prefix));
    }
    
    @Test
    public void testNsprocessingOff() throws SAXException {
        namespaceManager.getFeatures().setEnabled(SAXFeature.NAMESPACE, false);
        String name = "bela";
        String prefix = "ns";
        String uri = namespaceManager.getURI(new NameImpl(name, prefix));
        assertEquals("", uri);
    }
    
    static XmlNamespaceWrapper getNsWrapper(String prefix, String uri) {
        return new NamespaceImpl(prefix, uri);
    }
    
    private static class NamespaceImpl implements XmlNamespaceWrapper {
        
        private String prefix;
        private String uri;

        public NamespaceImpl(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }
        
        @Override
        public boolean isDefault() {
            return prefix == null;
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        @Override
        public String getURI() {
            return uri;
        }
    }
    
    static class NameImpl implements NamedWrapper<Annotation, AnnotatedElement> {
        
        private String name;
        private String prefix;

        NameImpl(String name, String prefix) {
            this.name = name;
            this.prefix = prefix;
        }
        
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getNsPrefix() {
            return prefix;
        }

        @Override
        public String getQualifiedName() {
            if(prefix==null)
                return name;
            return prefix+":"+name;
        }

        @Override
        public Annotation getAnnotation() {
            return null;
        }

        @Override
        public AnnotatedElement getAnnotatedElement() {
            return null;
        }
    }
}
