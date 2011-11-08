/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.decsi.jaxml.wrappers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author AA461472
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ConstructorUtilTest.class, 
    NameManagerTest.class, 
    AnnotatedElementUtilTest.class, 
    AbstractWrapperTest.class, 
    ValueWrapperImplTest.class, 
    XmlStringValueWrapperBuilderTest.class, 
    XmlStringValueWrapperImplTest.class, 
    XmlAttributeWrapperImplTest.class,
    AttributeBuilderTest.class,
    AnnotationAttributeParserTest.class,
    XmlNamespaceWrapperImplTest.class,
    NamespaceBuilderTest.class,
    XmlChildWrapperImplTest.class,
    XmlChildContainerWrapperImplTest.class,
    ChildBuilderTest.class,
    XmlElementWrapperImplTest.class})
public class WrapperTestSuite {
}
