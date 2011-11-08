package org.decsi.jaxml.testclasses;

import org.decsi.jaxml.annotation.XmlConstructor;
import org.decsi.jaxml.annotation.XmlStringValue;

/**
 *
 * @author Peter Decsi
 */
public enum Gender {

    MAN("man"),
    WOMAN("woman");
    
    public final static String MAN_XML = "man";
    public final static String WOMAN_XML = "woman";
    
    @XmlConstructor
    public static Gender getGenderFromXml(String xmlName) {
        for(Gender gender : values())
            if(gender.xmlValue.equalsIgnoreCase(xmlName))
                return gender;
        return null;
    }
    
    private String xmlValue;
    private Gender(String xmlValue) {
        this.xmlValue = xmlValue;
    }
    
    @XmlStringValue
    public String getXmlValue() {
        return xmlValue;
    }
}
