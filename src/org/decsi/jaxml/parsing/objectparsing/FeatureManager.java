package org.decsi.jaxml.parsing.objectparsing;

import java.util.EnumMap;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author Peter Decsi
 */
class FeatureManager {
    
    private EnumMap<SAXFeature, Boolean> features = new EnumMap<SAXFeature, Boolean>(SAXFeature.class);
    {
        for(SAXFeature feature : SAXFeature.values())
            features.put(feature, feature.getDefaultValue());
    }

    FeatureManager() {
    }
    
    boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXFeature feature = getFeatureForName(name);
        return features.get(feature);
    }
    
    private SAXFeature getFeatureForName(String name) throws SAXNotSupportedException {
        SAXFeature feature= SAXFeature.getInstance(name);
        if(feature == null)
            throw new SAXNotSupportedException(name);
        return feature;
    }
    
    void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        SAXFeature feature = getFeatureForName(name);
        if(feature.isReadOnly()) 
            throw new SAXNotRecognizedException(name);
        features.put(feature, value);
    }
    
    boolean isEnabled(SAXFeature feature) {
        return features.get(feature);
    }
    
    void setEnabled(SAXFeature feature, boolean value) {
        features.put(feature, value);
    }
}
