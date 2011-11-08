package org.decsi.jaxml.parsing.objectparsing;

/**
 *
 * @author Peter Decsi
 */
public class XMLReaderFactory {
    
    private PrimitiveParser primitiveParser = new PrimitiveParser();
    private NamespaceManager namespaceManager;
    private SAXEventDispatcher dispatcher;
    private ObjectXmlReader xmlReader;
    
    private boolean isCreated = false;
    
    public XMLReaderFactory() {
        initState();
    }
    
    private void initState() {
        initNamespaceManager();
        dispatcher = new SAXEventDispatcher(namespaceManager);
        xmlReader = new ObjectXmlReader(dispatcher);
    }
    
    private void initNamespaceManager() {
        FeatureManager features = new FeatureManager();
        PropertyManager properties = new PropertyManager();
        namespaceManager = new NamespaceManager(features, properties);
    }
    
    public XMLReaderFactory setBooleanStringValue(boolean value, String str) {
        checkState();
        if(value) primitiveParser.setTrueValue(str);
        else primitiveParser.setFalseValue(str);
        return this;
    }
    
    public XMLReaderFactory addProcessingInstruction(ProcessingInstruction instruction) {
        xmlReader.addInstruction(instruction);
        return this;
    }
    
    public ObjectXmlReader createReader(Class c) {
        checkState();
        isCreated = true;
        XmlElementParser parser = new XmlElementParser(c, dispatcher, primitiveParser);
        xmlReader.setParser(parser);
        return xmlReader;
    }
    
    private void checkState() {
        if(isCreated)
            throw new IllegalStateException("ObjectXmlReader is already built!");
    }
    
    public <T> ObjectXmlReader createReader(Class<T> c, T instance) {
        ObjectXmlReader reader = createReader(c);
        reader.setInstanceToParse(instance);
        return reader;
    }
}
