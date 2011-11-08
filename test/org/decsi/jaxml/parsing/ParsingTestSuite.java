/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.jaxml.parsing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author AA461472
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    BooleanConverterTest.class,
    org.decsi.jaxml.parsing.objectparsing.ObjectParsingTestSuite.class,
    org.decsi.jaxml.parsing.out.OutputTestSuite.class,
    org.decsi.jaxml.parsing.xmlparsing.XmlParsingTestSuite.class})
public class ParsingTestSuite {
}
