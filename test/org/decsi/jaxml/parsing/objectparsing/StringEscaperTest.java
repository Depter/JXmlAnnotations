package org.decsi.jaxml.parsing.objectparsing;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 */
public class StringEscaperTest {
    
    private StringEscaper escaper;
    
    public StringEscaperTest() {
    }

    @Before
    public void setUp() {
        escaper = new StringEscaper();
    }

    @Test
    public void testEscapeString() {
        compareChars(new char[0], escaper.escapeString(null));
        compareChars("".toCharArray(), escaper.escapeString(""));
        compareChars("bela".toCharArray(), escaper.escapeString("bela"));
        compareChars("b&lt;a".toCharArray(), escaper.escapeString("b<a"));
        compareChars("b&gt;a".toCharArray(), escaper.escapeString("b>a"));
        compareChars("b&amp;a".toCharArray(), escaper.escapeString("b&a"));
        compareChars("b&apos;a".toCharArray(), escaper.escapeString("b'a"));
        compareChars("b&quot;a".toCharArray(), escaper.escapeString("b\"a"));
        compareChars("b&lt;&gt;a".toCharArray(), escaper.escapeString("b<>a"));
        
    }
    
    static void compareChars(char[] ch1, char[] ch2) {
        if(ch1==null) {
            assertTrue("ch1 was null, but ch2 is not null!", ch2==null);
        } else if(ch2==null) {
            assertTrue("ch2 was null, but ch1 is not null!", ch1==null);
        }else {
            assertEquals(ch1.length, ch2.length);
            for(int i = 0; i < ch1.length; i++)
                assertEquals(ch1[i], ch2[i]);
        }
    }
    
}
