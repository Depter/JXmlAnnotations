package org.decsi.jaxml.parsing.xmlparsing;

import org.decsi.jaxml.testclasses.Contact;
import java.util.List;
import org.junit.After;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Before;
import org.decsi.jaxml.testclasses.ContactBook;
import org.decsi.jaxml.testclasses.Gender;
import org.decsi.jaxml.testclasses.Person;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.decsi.jaxml.parsing.out.DefaultObjectXmlWriterTest.*;

/**
 *
 * @author Peter Decsi
 */
public class XmlParserFactoryTest {
    
    private XmlObjectParser<ContactBook> objectParser;
    private InputStream is;
    
    public XmlParserFactoryTest() {
    }

    @Before
    public void setUp() throws Exception {
        initParser();
        initInputStream();
    }
    
    private void initParser() throws Exception{
        XmlParserFactory<ContactBook> factory = new XmlParserFactory<ContactBook>(ContactBook.class);
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        objectParser = factory.build(parser);
    }
    
    private void initInputStream() {
        is = getClass().getResourceAsStream(LOCATION);
    }

    @After
    public void tearDown() throws Exception {
        if(is!=null)
            is.close();
    }

    @Test
    public void testBuild() throws Exception {
        ContactBook cb = objectParser.build(is);
        assertEquals("Business Contacts", cb.getName());
        assertEquals(1, cb.getOwner().getId());
        List<Contact> contacts = cb.getContacts();
        assertEquals(2, contacts.size());
        
        Contact contact = contacts.get(0);
        assertEquals(1, contact.getId());
        Person person = contact.getPerson();
        assertEquals(2, person.getId());
        assertEquals(25, person.getAge());
        assertEquals("geza", person.getName());
        assertEquals(Gender.MAN, person.getGender());
        assertEquals("geza@bela.org", contact.getEMail());
        
        contact = contacts.get(1);
        assertEquals(2, contact.getId());
        person = contact.getPerson();
        assertEquals(3, person.getId());
        assertEquals(55, person.getAge());
        assertEquals("jucus", person.getName());
        assertEquals(Gender.WOMAN, person.getGender());
        assertEquals("jucus@bela.org", contact.getEMail());
    }
}
