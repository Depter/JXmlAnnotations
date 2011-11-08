package org.decsi.jaxml.parsing.objectparsing;

import org.decsi.jaxml.parsing.objectparsing.util.ContentHandlerEventQueue;

/**
 *
 * @author Peter Decsi
 */
abstract class AbstractParserTesting {
    
    protected ObjectParser parser;
    protected ContentHandlerEventQueue events;
    
    protected SAXEventDispatcher dispatcher;
    protected PrimitiveParser primitiveParser;
    
    protected void initParser(Class c) throws Exception {
        dispatcher = getDispatcher();
        primitiveParser = new PrimitiveParser();
        createParser(c);
    }
    
    protected SAXEventDispatcher getDispatcher() {
        NamespaceManager nsManagger = NamespaceManagerTest.getNamespaceManager();
        SAXEventDispatcher dispathcer = new SAXEventDispatcher(nsManagger);
        events = new ContentHandlerEventQueue();
        dispathcer.setContentHandler(events);
        return dispathcer;
    }
    
    abstract protected void createParser(Class c) throws Exception;
    
}
