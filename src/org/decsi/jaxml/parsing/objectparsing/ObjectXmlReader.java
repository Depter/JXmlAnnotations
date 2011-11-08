package org.decsi.jaxml.parsing.objectparsing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Peter Decsi
 */
public class ObjectXmlReader implements XMLReader{
    
    private FeatureManager features;
    private PropertyManager properties;
    private ObjectParser parser;
    private Object instance;
    
    private SAXEventDispatcher dispatcher;
    private List<ProcessingInstruction> instructions = new ArrayList<ProcessingInstruction>();
    
    ObjectXmlReader(SAXEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.features = dispatcher.getNamespaceManager().getFeatures();
        this.properties = dispatcher.getNamespaceManager().getProperties();
    }
    
    void setParser(ObjectParser parser) {
        this.parser = parser;
    }
    
    void setInstanceToParse(Object instance) {
        this.instance = instance;
    }
    
    void addInstruction(ProcessingInstruction instruction) {
        if(instruction == null)
            throw new NullPointerException("ProcessingInstruction is null!");
        instructions.remove(instruction);
        instructions.add(instruction);
    }
    
    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return features.getFeature(name);
    }
    @Override
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        features.setFeature(name, value);
    }

    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        return properties.getProperty(name);
    }
    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        properties.setProperty(name, value);
    }

    @Override
    public void setEntityResolver(EntityResolver resolver) {
        dispatcher.setEntityResolver(resolver);
    }
    @Override
    public EntityResolver getEntityResolver() {
        return dispatcher.getEntityResolver();
    }

    @Override
    public void setDTDHandler(DTDHandler handler) {
        dispatcher.setDTDHandler(handler);
    }
    @Override
    public DTDHandler getDTDHandler() {
        return dispatcher.getDTDHandler();
    }

    @Override
    public void setContentHandler(ContentHandler handler) {
        dispatcher.setContentHandler(handler);
    }
    @Override
    public ContentHandler getContentHandler() {
        return dispatcher.getContentHandler();
    }

    @Override
    public void setErrorHandler(ErrorHandler handler) {
        dispatcher.setErrorHandler(handler);
    }
    @Override
    public ErrorHandler getErrorHandler() {
        return dispatcher.getErrorHandler();
    }

    public void parse(Object instance) throws SAXException {
        this.instance = instance;
        parse();
    }
    
    @Override
    public void parse(InputSource input) throws IOException, SAXException {
        parse();
    }

    @Override
    public void parse(String systemId) throws IOException, SAXException {
        parse();
    }
    
    private void parse() throws SAXException {
        dispatcher.fireStartDocument();
        dispatchInstructions();
        parser.parse(instance);
        dispatcher.fireEndDocument();
    }
    
    private void dispatchInstructions() throws SAXException {
        for(ProcessingInstruction instruction : instructions)
            dispatcher.fireProcessingInstruction(
                    instruction.getTarget(), 
                    instruction.getDataAsString());
    }
}
