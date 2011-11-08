package org.decsi.jaxml.parsing.out;

import java.io.BufferedReader;
import org.junit.After;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.Before;
import java.io.StringWriter;
import org.decsi.jaxml.parsing.objectparsing.ObjectXmlReader;
import org.decsi.jaxml.parsing.objectparsing.XMLReaderFactory;
import org.decsi.jaxml.testclasses.ContactBook;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class DefaultObjectXmlWriterTest {
    public final static String LOCATION = "/org/decsi/jaxml/parsing/out/contact_book.xml";
    public final static String CHAR_SET = "UTF-8";
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private StringWriter writer;
    private StringBuilder file = new StringBuilder();
    private ObjectXmlWriter printer;
    
    public DefaultObjectXmlWriterTest() {
    }

    @Before
    public void setUp() {
        XMLReaderFactory factory = new XMLReaderFactory();
        ObjectXmlReader reader = factory.createReader(ContactBook.class);
        writer = new StringWriter();
        printer = new DefaultObjectXmlWriter(reader, writer, new PrettyFormatter(2));
    }
    
    @After
    public void tearDown() throws IOException {
        if(writer != null)
            writer.close();
    }
    
    @Test
    public void testPrintObject() throws Exception {
        printer.write(ContactBook.createTestInstance());
        String expected = getFileContent();
        assertEquals(expected, writer.toString());
    }
    
    private String getFileContent() throws Exception {
        BufferedReader reader = getReader();
        try{fillReader(reader);}
        finally{reader.close();}
        return file.toString();
    }
    
    private BufferedReader getReader() throws Exception {
        return new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream(LOCATION),
                CHAR_SET
                ));
    }
    
    private void fillReader(BufferedReader reader) throws IOException {
        String line;
        while((line=reader.readLine()) != null)
            addLine(line);
    }
    
    private void addLine(String line) {
        if(file.length() >0)
            file.append(LINE_SEPARATOR);
        file.append(line);
    }
}
