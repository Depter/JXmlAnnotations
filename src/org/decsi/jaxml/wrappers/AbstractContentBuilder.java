package org.decsi.jaxml.wrappers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractContentBuilder<A extends NamedWrapper & IndexedWrapper> implements Comparator<A> {
    
    private final static Comparator<IndexedWrapper> INDEX_COMPARATOR = new Comparator<IndexedWrapper>() {
        @Override
        public int compare(IndexedWrapper o1, IndexedWrapper o2) {
            if(o1==null) return o2==null? 0 : 1;
            if(o2==null) return -1;
            if(o1.getIndex() < 0) return o2.getIndex()<0? 0 : 1;
            if(o2.getIndex() < 0) return -1;
            return o1.getIndex() - o2.getIndex();
        }
    };
    
    private final static Comparator<NamedWrapper> NAME_COMPARATOR = new Comparator<NamedWrapper>() {
        @Override
        public int compare(NamedWrapper o1, NamedWrapper o2) {
            if(o1==null) return o2==null? 0 : 1;
            if(o2==null) return -1;
            return o1.getQualifiedName().compareToIgnoreCase(o2.getQualifiedName());
        }
    };
    
    @Override
    public int compare(A o1, A o2) {
        int dif = INDEX_COMPARATOR.compare(o1, o2);
        return dif!=0? dif : NAME_COMPARATOR.compare(o1, o2);
    }
    
    private List<A> contents = new ArrayList<A>();
    
    List<A> buildContent(Class c) {
        fillContent(c);
        List<A> result = new ArrayList<A>(contents);
        contents.clear();
        return result;
    }
    
    private void fillContent(Class c) {
        for(Field field : c.getDeclaredFields())
            if(isContent(field))
                addContent(buildContent(field));
        Collections.sort(contents, this);
    }
    
    abstract protected boolean isContent(Field field);
    
    abstract protected A buildContent(Field field);
    
    private void addContent(A content) {
        checkIndex(content);
        checkName(content);
        contents.add(content);
    }
    
    private void checkIndex(A content) {
        A old = getExistingContent(content.getIndex());
        if(old != null)
            throw indexExistsException(old, content);
    }
    
    abstract protected IllegalArgumentException indexExistsException(A old, A wrapper);
    
    protected A getExistingContent(int index) {
        if(index < 0) return null;
        for(A content : contents)
            if(content.getIndex() == index)
                return content;
        return null;
    }
    
    protected A getExistingContent(String qualifiedName) {
        for(A content : contents)
            if(content.getQualifiedName().equalsIgnoreCase(qualifiedName))
                return content;
        return null;
    }
    
    private void checkName(A content) {
        A old = getExistingContent(content.getQualifiedName());
        if(old != null)
            throw nameExistsException(old, content);
    }
    
    abstract protected IllegalArgumentException nameExistsException(A old, A wrapper);
}
