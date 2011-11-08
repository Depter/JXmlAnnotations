/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.jaxml.xml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author AA461472
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ContentTest.class, 
    NamedContentTest.class, 
    AttributeTest.class,
    ElementTest.class})
public class XmlTestSuite {}
