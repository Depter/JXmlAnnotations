package org.decsi.jaxml.testclasses;

import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
@XmlElement(name="contact")
public class Contact {
    
    @XmlAttribute(name="id")
    private int id;
    
    @XmlChild(index=0)
    private Person person;
    
    @XmlChild(name="email", index=1)
    private String email; 
    
    public Contact(int id, Person person, String email) {
        this.id = id;
        this.person = person;
        this.email = email;
    }
    
    private Contact() {}
    
    public void setPerson(Person person) {
        this.person = person;
    }
    public Person getPerson() {
        return person;
    }
    
    public void setEMail(String email) {
        this.email = email;
    }
    public String getEMail() {
        return email;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
