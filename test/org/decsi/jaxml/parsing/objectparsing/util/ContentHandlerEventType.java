package org.decsi.jaxml.parsing.objectparsing.util;

/**
 *
 * @author Peter Decsi
 */
public enum ContentHandlerEventType {
    
    START_DOCUMENT,
    START_PREFIX_MAPPING,
    START_ELEMENT,
    END_ELEMENT,
    END_PREFIX_MAPPING,
    END_DOCUMENT,
    CHARACTERS,
    IGNORABLE_CHARACTERS,
    PROCESSING_INSTRUCTION,
    SKIPPED_ENTITY;
}
