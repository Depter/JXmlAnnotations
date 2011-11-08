package org.decsi.jaxml.testclasses;

import java.util.ArrayList;
import java.util.List;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlChild;
import org.decsi.jaxml.annotation.XmlChildContainer;
import org.decsi.jaxml.annotation.XmlConstructor;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
@XmlElement(name="contact-book")
public class ContactBook {
    
    public static ContactBook createTestInstance() {
        ContactBook cb = new ContactBook(DEFAULT_NAME);
        cb.owner = new Person("bela", 32, Gender.MAN, 1);
        addContacts(cb);
        return cb;
    }
    
    private static void addContacts(ContactBook cb) {
        Person p = new Person("geza", 25, Gender.MAN, 2);
        cb.addContact(new Contact(1, p, "geza@bela.org"));
        p = new Person("jucus", 55, Gender.WOMAN, 3);
        cb.addContact(new Contact(2, p, "jucus@bela.org"));
    }
    
    public final static String DEFAULT_NAME = "Business Contacts";
    
    @XmlChild(name="name", index=0)
    private String name = DEFAULT_NAME;
    
    @XmlAttribute(name="owner", addIfDefault=true)
    private Person owner;
    
    @XmlChildContainer(name="contacts", elementClass=Contact.class)
    private List<Contact> contacts = new ArrayList<Contact>();
    
    public ContactBook(String name) {
        this.name = name;
    }
    
    @XmlConstructor
    private ContactBook() {
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void setOwner(Person owner) {
        this.owner = owner;
    }
    public Person getOwner() {
        return owner;
    }
    
    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }
    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }
    public List<Contact> getContacts() {
        return contacts;
    }
}
