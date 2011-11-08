package org.decsi.jaxml.parsing.objectparsing;

import static org.decsi.jaxml.parsing.objectparsing.NamespaceManager.EMPTY_STRING;

/**
 *
 * @author Peter Decsi
 */
public class PrimitiveParser {

    private String strTrue = "true";
    private String strFalse = "false";
    
    public void setTrueValue(String str) {
        checkString(str);
        strTrue = str;
    }
    
    private void checkString(String str) {
        if(str == null)
            throw new NullPointerException("Value can not be null!");
        if(str.trim().length()==0)
            throw new IllegalArgumentException("Value can not be an empty string!");
    }
    
    public void setFalseValue(String str) {
        checkString(str);
        this.strFalse = str;
    }
    
    String getStringValue(Object instance) {
        if(instance == null) return EMPTY_STRING;
        if(instance instanceof Boolean) return getBooleanValue((Boolean) instance); 
        return instance.toString();
    }
    
    public String getBooleanValue(boolean value) {
        return value? strTrue : strFalse;
    }
}
