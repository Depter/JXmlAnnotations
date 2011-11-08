package org.decsi.jaxml.parsing.objectparsing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Decsi
 */
public class ProcessingInstruction implements Comparable<ProcessingInstruction>{
    
    private final String target;
    private List<Data> data = new ArrayList<Data>();
    
    ProcessingInstruction(String target) {
        checkTarget(target);
        this.target = target;
    }
    
    private void checkTarget(String target) {
        if(target == null || target.trim().length()==0)
            throw new IllegalArgumentException("Name '"+target+"' is invalid");
    }
      
    public String getTarget() {
        return target;
    }
    
    public List<Data> getData() {
        return new ArrayList<Data>(data);
    }
    
    public ProcessingInstruction addData(String name, String value) {
        Data d = new Data(name, value);
        data.remove(d);
        data.add(d);
        return this;
    }
    
    public Data getData(String name) {
        for(Data d : data)
            if(d.name.equalsIgnoreCase(name))
                return d;
        return null;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof ProcessingInstruction)) 
            return false;
        return target.equalsIgnoreCase(((ProcessingInstruction)o).target);
    }
    
    @Override
    public int hashCode() {
        return target.toLowerCase().hashCode();
    }
    
    public String toXmlString() {
        return "<?"+target+" "+getDataAsString()+"?>";
    }
    
    public String getDataAsString() {
        String str = "";
        for(int i=0; i<data.size(); i++)
            str+=(i>0?" ":"")+data.get(i).toString();
        return str;
    }

    @Override
    public int compareTo(ProcessingInstruction o) {
        if(o==null) return -1;
        return target.compareToIgnoreCase(o.target);
    }
    
    public static class Data implements Comparable<Data> {
        
        private final String name;
        private final String value;
        
        Data(String name, String value) {
            checkName(name);
            this.name = name.trim();
            this.value = value;
        }
        
        private void checkName(String name) {
            if(name == null || name.trim().length()==0)
                throw new IllegalArgumentException("Name '"+name+"' is invalid");
        }
        
        public String getName() {
            return name;
        }
        
        public String getValue() {
            return value;
        }
        
        @Override
        public boolean equals(Object o) {
            if(o==null || !(o instanceof Data)) 
                return false;
            return name.equalsIgnoreCase(((Data)o).name);
        }
        
        @Override
        public int hashCode() {
            return name.toLowerCase().hashCode();
        }
        
        @Override
        public String toString() {
            String str = name+"=\"";
            if(value != null)
                str+=value;
            return str+"\"";
        }

        @Override
        public int compareTo(Data o) {
            if(o==null) return -1;
            return name.compareToIgnoreCase(o.name);
        }
    }
}
