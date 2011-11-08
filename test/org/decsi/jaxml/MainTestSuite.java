package org.decsi.jaxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    org.decsi.jaxml.wrappers.WrapperTestSuite.class,
    org.decsi.jaxml.parsing.ParsingTestSuite.class
})
public class MainTestSuite {}
