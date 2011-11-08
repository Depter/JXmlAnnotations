package org.decsi.jaxml.parsing.out;

/**
 *
 * @author Peter Decsi
 */
public class NsDeclarationImpl implements NsDeclaration {
    private String prefix;
    private String uri;

    NsDeclarationImpl(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
        escapePrefix();
        checkUri();
    }   
        
    private void escapePrefix() {
        if(prefix==null) return;
        prefix = prefix.trim();
        if(prefix.length()==0) prefix=null;
    }
     
    private void checkUri() {
        if(uri==null || uri.trim().length()==0)
            throw new IllegalArgumentException("Invalid URI: "+uri);
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
