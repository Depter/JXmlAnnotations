package org.decsi.jaxml.xml;

/**
 *
 * @author Peter Decsi
 */
public class NamedContent extends Content {
    private String name;
    private String nsPrefix;
    
    public NamedContent(String name, String nsPrefix) {
        checkAndSetName(name);
        escapeAndSetPrefix(nsPrefix);
    }
    
    private void checkAndSetName(String name) {
        if(name==null || name.trim().length()==0)
            throw new IllegalArgumentException(String.format("Name is invalid! '%s'", name));
        this.name = name;
    }
    
    private void escapeAndSetPrefix(String nsPrefix) {
        if(nsPrefix !=null && nsPrefix.trim().length()==0)
            nsPrefix = null;
        this.nsPrefix = nsPrefix;
    }
    
    public void setName(String name) {
        checkAndSetName(name);
    }
    
    public String getName() {
        return name;
    }
    
    public void setNsPrefix(String nsPrefix) {
        escapeAndSetPrefix(nsPrefix);
    }
    
    public String getNsPrefix() {
        return nsPrefix;
    }
    
    public String getQualifiedName() {
        return nsPrefix==null? name : nsPrefix+":"+name;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof NamedContent)) return false;
        NamedContent c = (NamedContent) o;
        return getQualifiedName().equalsIgnoreCase(c.getQualifiedName());
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash*31+name.hashCode();
        if(nsPrefix!=null) hash = hash*31+nsPrefix.hashCode();
        return hash;
    }
}
