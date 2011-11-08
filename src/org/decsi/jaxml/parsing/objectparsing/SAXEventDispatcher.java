package org.decsi.jaxml.parsing.objectparsing;

import java.io.IOException;
import org.decsi.jaxml.wrappers.NamedWrapper;
import org.decsi.jaxml.wrappers.XmlNamespaceWrapper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 *
 * @author Peter Decsi
 */
class SAXEventDispatcher {

    private ContentHandler contentHandler;
    private DTDHandler dtdHandler;
    private EntityResolver entityResolver;
    private ErrorHandler errorHandler;
    private NamespaceManager namespaceManager;
    private StringEscaper escaper = new StringEscaper();
    
    SAXEventDispatcher(NamespaceManager namespaceManager) {
        this.namespaceManager = namespaceManager;
    }
    
    NamespaceManager getNamespaceManager() {
        return namespaceManager;
    }
    
    void setXMLReader(XMLReader reader) {
        this.contentHandler = reader.getContentHandler();
        this.dtdHandler = reader.getDTDHandler();
        this.entityResolver = reader.getEntityResolver();
        this.errorHandler = reader.getErrorHandler();
    }
    
    void setEntityResolver(EntityResolver resolver) {
        this.entityResolver = resolver;
    }
    EntityResolver getEntityResolver() {
        return entityResolver;
    }

    void setDTDHandler(DTDHandler handler) {
        this.dtdHandler = handler;
    }
    DTDHandler getDTDHandler() {
        return dtdHandler;
    }

    void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }
    ContentHandler getContentHandler() {
        return contentHandler;
    }

    void setErrorHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }
    ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    void fireStartDocument() throws SAXException {
        if(contentHandler != null)
            contentHandler.startDocument();
    }
    
    void fireEndDocument() throws SAXException {
        if(contentHandler != null)
            contentHandler.endDocument();
    }
    
    void fireStartElement(NamedWrapper wrapper, Attributes attributes) throws SAXException {
        String uri = namespaceManager.getURI(wrapper);
        String localName = namespaceManager.getLocalName(wrapper);
        String qName = namespaceManager.getQualifiedName(wrapper);
        if(contentHandler != null)
            contentHandler.startElement(uri, localName, qName, attributes);
    }
    
    void fireEndElement(NamedWrapper wrapper) throws SAXException {
        String uri = namespaceManager.getURI(wrapper);
        String localName = namespaceManager.getLocalName(wrapper);
        String qName = namespaceManager.getQualifiedName(wrapper);
        if(contentHandler != null)
            contentHandler.endElement(uri, localName, qName);
    }
    
    XmlNamespaceWrapper fireStartPrefixMapping(XmlNamespaceWrapper namespace) throws SAXException {
        XmlNamespaceWrapper removed = namespaceManager.addNamespace(namespace);
        if(contentHandler != null)
            contentHandler.startPrefixMapping(namespace.getPrefix(), namespace.getURI());    
        return removed;
    }
    
    void fireEndPrefixMapping(XmlNamespaceWrapper namespace) throws SAXException {
        namespaceManager.removeNamespace(namespace);
        if(contentHandler != null)
            contentHandler.endPrefixMapping(namespace.getPrefix());
    }
    
    void fireCharacters(String characters) throws SAXException {
        if(contentHandler == null) return;
        char[] ch = escaper.escapeString(characters);
        contentHandler.characters(ch, 0, ch.length);
    }
    
    void fireIgnorableWhitespace(String characters) throws SAXException {
        if(contentHandler == null) return;
        if(characters != null && characters.trim().length()>0)
            throw containsNotWhitspaceException(characters);
        char[] ch = characters.toCharArray();
        contentHandler.ignorableWhitespace(ch, 0, ch.length);
    }
    
    private SAXException containsNotWhitspaceException(String str) {
        String msg = "String '%s' cotnains not only whitespaces!";
        msg = String.format(msg, str);
        return new SAXException(msg);
    }
    
    void fireProcessingInstruction(String target, String data) throws SAXException {
        if(contentHandler != null)
            contentHandler.processingInstruction(target, data);
    }
    
    void fireNotationDeclaration(String name, String publicId, String systemId) throws SAXException {
        if(dtdHandler != null)
            dtdHandler.notationDecl(name, publicId, systemId);
    }
    
    void fireUnparsedEntityDeclaration(String name, String publicId, String systemId, String notationName) throws SAXException {
        if(dtdHandler != null)
            dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
    }
    
    InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if(entityResolver == null) throw new SAXException("EntityResolver is not set!");
        return entityResolver.resolveEntity(publicId, systemId);
    }
    
    void fireWarning(SAXParseException ex) throws SAXException {
        if(errorHandler != null)
            errorHandler.warning(ex);
    }
    
    void fireError(SAXParseException ex) throws SAXException {
        if(errorHandler != null)
            errorHandler.error(ex);
    }
    
    void fireFatalError(SAXParseException ex) throws SAXException {
        if(errorHandler != null)
            errorHandler.fatalError(ex);
    }
}
