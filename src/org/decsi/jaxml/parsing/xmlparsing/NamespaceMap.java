package org.decsi.jaxml.parsing.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Peter Decsi
 */
class NamespaceMap {
    
    private List<NamespaceEntry> nss = new ArrayList<NamespaceEntry>();
    
    String getPrefixForUri(String uri) {
        if(uri==null || uri.trim().length()==0) 
            return null;
        return getPrefixFromEntry(uri);
    }
    
    String getUriForPrefix(String prefix) {
        for(NamespaceEntry entry : nss)
            if(samePrefix(entry, prefix))
                return entry.getUri();
        return "";
    }
    
    private String getPrefixFromEntry(String uri) {
        for(NamespaceEntry entry : nss)
            if(entry.getUri().equalsIgnoreCase(uri))
                return entry.getPrefix();
        return null;
    }
    
    void addNamespace(String prefix, String uri) {
        NamespaceEntry entry = getEntryForPrefix(prefix);
        entry.registerUri(uri);
    }
    
    void removeNamespace(String prefix) {
        NamespaceEntry entry = getEntryForPrefix(prefix);
        entry.deregisterUri();
        if(entry.isEmpty())
            nss.remove(entry);
    }
    
    private NamespaceEntry getEntryForPrefix(String prefix) {
        for(NamespaceEntry entry : nss)
            if(samePrefix(entry, prefix))
                return entry;
        return createEntry(prefix);
    }
    
    private NamespaceEntry createEntry(String prefix) {
        NamespaceEntry entry = new NamespaceEntry(prefix);
        nss.add(0, entry);
        return entry;
    }
    
    private boolean samePrefix(NamespaceEntry entry, String prefix) {
        if(prefix!=null && prefix.trim().length()==0)
            prefix = null;
        if(prefix==null)
            return entry.prefix == null;
        return prefix.equalsIgnoreCase(entry.prefix);
    }
    
    private class NamespaceEntry {
        
        private String prefix;
        private Stack<String> uris = new Stack<String>();
        
        public NamespaceEntry(String prefix) {
            this.prefix = prefix;
        }
        
        private String getPrefix() {
            if(prefix!=null && prefix.trim().length()==0)
                return null;
            return prefix==null? null : prefix.trim();
        }
        
        private String getUri() {
            String uri = uris.peek();
            return uri==null? "" : uri;
        }
        
        private boolean isEmpty() {
            return uris.isEmpty();
        }
        
        private void deregisterUri() {
            uris.pop();
        }
        
        private void registerUri(String uri) {
            uris.push(uri);
        } 
        
        @Override
        public boolean equals(Object o) {
            if(o==null || !(o instanceof NamespaceEntry)) return false;
            NamespaceEntry entry = (NamespaceEntry) o;
            return samePrefix(entry, prefix);
        }
        
        @Override
        public int hashCode() {
            if(prefix==null)
                return 0;
            return prefix.toLowerCase().hashCode();
        }
    }
}
