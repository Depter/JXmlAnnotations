/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.jaxml.parsing.objectparsing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author AA461472
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PropertyManagerTest.class, 
    FeatureManagerTest.class,
    PrimitiveParserTest.class,
    NamespaceManagerTest.class,
    StringEscaperTest.class,
    SAXEventDispatcherTest.class,
    XmlStringValueParserTest.class,
    AttributesImplTest.class,
    FieldAttributesImplTest.class,
    XmlChildParserTest.class,
    XmlContainerParserTest.class,
    XmlElementParserTest.class,
    ObjectXmlReaderTest.class})
public class ObjectParsingTestSuite {
}
