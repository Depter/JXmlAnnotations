package org.decsi.jaxml.testclasses;

import java.util.HashMap;
import org.decsi.jaxml.annotation.XmlAttribute;
import org.decsi.jaxml.annotation.XmlStringValue;
import org.decsi.jaxml.annotation.XmlElement;

/**
 *
 * @author Peter Decsi
 */
@XmlElement(name="person")
public class Person {
    
    private static HashMap<Integer, Person> persons = new HashMap<Integer, Person>();
    static {
        persons.put(1, new Person("bela", 32, Gender.MAN, 1));
        persons.put(2, new Person("geza", 25, Gender.MAN, 2));
        persons.put(3, new Person("jucus", 55, Gender.WOMAN, 3));
    }
    
    public static Person lookUpId(int id) {
        return persons.get(id);
    }
    
    public final static String DEFAULT_NAME = "Bela Geza";
    public final static int DEFAULT_AGE = 31;
    public final static Gender DEFAULT_GENDER = Gender.MAN;
    public final static int DEFAULT_ID = 10;
    
    public static Person getDefaultPerson() {
        Person person = new Person(DEFAULT_NAME, DEFAULT_AGE, 
                DEFAULT_GENDER, DEFAULT_ID);
        return person;
    }
    
    @XmlAttribute(name="name")
    private String name = DEFAULT_NAME;
    
    @XmlAttribute(name="age", index=1)
    private int age = DEFAULT_AGE;
    
    @XmlAttribute(name="gender", index=2)
    private Gender gender = DEFAULT_GENDER;
    
    @XmlStringValue
    @XmlAttribute(name="id", index=0)
    private int id = DEFAULT_ID;
    
    public Person(String name, int age, Gender gender, int id) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.id = id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public Gender getGender() {
        return gender;
    }
}
