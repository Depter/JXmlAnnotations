package org.decsi.jaxml.parsing;

/**
 *
 * @author Peter Decsi
 */
public class BooleanConverter {
    public final static String DEFAULT_TRUE = "1";
    public final static String DEFAULT_FALSE = "0";
    public final static boolean DEFAULT_VALUE = false;
    
    private String trueValue;
    private String falseValue;
    private boolean defaultValue = DEFAULT_VALUE;
    
    public BooleanConverter() {
        this.trueValue = DEFAULT_TRUE;
        this.falseValue = DEFAULT_FALSE;
    }
    
    public BooleanConverter(String trueValue, String falseValue) {
        checkValue(trueValue, "TrueValue");
        checkValue(falseValue, "FalseValue");
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }
    
    private void checkValue(String value, String name) {
        if(value == null)
            throw new NullPointerException(name+" is null!");
    }
    
    public String getStirngValueFor(boolean value) {
        return value? trueValue : falseValue;
    }
    
    public boolean getBooleanValueFor(String value) {
        if(trueValue.equalsIgnoreCase(value)) return true;
        if(falseValue.equalsIgnoreCase(value)) return false;
        return defaultValue;
    }
}
