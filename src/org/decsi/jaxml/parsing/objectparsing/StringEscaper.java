package org.decsi.jaxml.parsing.objectparsing;

/**
 *
 * @author Peter Decsi
 */
class StringEscaper {
    private final static String XML_LESS_THEN = "&lt;";
    private final static String XML_GREATER_THEN = "&gt;";
    private final static String XML_AMPERSAND = "&amp;";
    private final static String XML_APOSTROPHE = "&apos;";
    private final static String XML_QUOTATION_MARK = "&quot;";
    
    private StringBuilder builder = new StringBuilder();

    StringEscaper() {
    }
    
    char[] escapeString(String str) {
        if(str == null) return new char[0];
        char[] result = fillBuilder(str);
        builder.setLength(0);
        return result;
    }
    
    private char[] fillBuilder(String str) {
        for(char c : str.toCharArray())
            builder.append(escapeCharacter(c));
        return builder.toString().toCharArray();
    }
    
    private String escapeCharacter(char c) {
        switch(c) {
            case '<': return XML_LESS_THEN;
            case '>': return XML_GREATER_THEN;
            case '&': return XML_AMPERSAND;
            case '\'': return XML_APOSTROPHE;
            case '"': return XML_QUOTATION_MARK;
            default: return String.valueOf(c);
        }
    }
}
