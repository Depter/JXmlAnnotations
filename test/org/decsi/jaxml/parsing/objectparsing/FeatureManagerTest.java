package org.decsi.jaxml.parsing.objectparsing;

import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXNotRecognizedException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class FeatureManagerTest {
    
    private FeatureManager features;
    
    public FeatureManagerTest() {
    }

    @Before
    public void setUp() {
        features = new FeatureManager();
    }

    @Test
    public void testGetFeature() throws Exception {
        assertTrue(features.getFeature(SAXFeature.NAMESPACE.getFeatureName()));
        assertFalse(features.getFeature(SAXFeature.NAMESPACE_PREFIX.getFeatureName()));
        assertTrue(features.getFeature(SAXFeature.VALIDATION.getFeatureName()));
    }

    @Test
    public void testSetFeature() throws Exception {
        String name = SAXFeature.NAMESPACE.getFeatureName();
        features.setFeature(name, false);
        assertFalse(features.getFeature(name));
        
        name = SAXFeature.NAMESPACE_PREFIX.getFeatureName();
        features.setFeature(name, true);
        assertTrue(features.getFeature(name));
    }
    
    @Test(expected=SAXNotRecognizedException.class)
    public void testSetFeature_NotRecognized() throws Exception {
        String name = SAXFeature.VALIDATION.getFeatureName();
        features.setFeature(name, false);
    }
    
    @Test(expected=SAXNotSupportedException.class)
    public void testSetFeature_NotSupported() throws Exception {
        String name = "www.bela.org";
        features.setFeature(name, false);
    }

}
