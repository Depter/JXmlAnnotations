/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.jaxml.parsing.xmlparsing;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author AA461472
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    NamespaceMapTest.class, 
    PrimitiveStringElementNodeTest.class, 
    XmlStringValueNodeTest.class, 
    XmlStringChildNodeTest.class,
    XmlChildNodeTest.class, 
    AttributeBuilderTest.class, 
    CollectionXmlContainerNodeTest.class,
    ArrayXmlContainerNodeTest.class,
    MapXmlContainerNodeTest.class,
    XmlParserFactoryTest.class
})
public class XmlParsingTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}
