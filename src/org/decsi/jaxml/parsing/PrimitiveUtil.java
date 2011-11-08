package org.decsi.jaxml.parsing;

/**
 *
 * @author Peter Decsi
 */
public class PrimitiveUtil {
    private PrimitiveUtil() {}
    
    public static boolean isPrimitive(Class c) {
        return isRealPrimitive(c) ||
               isPrimitiveWrapper(c) ||
               String.class.equals(c);
    }

    public static boolean isRealPrimitive(Class c) {
        return byte.class.equals(c) ||
               short.class.equals(c) ||
               int.class.equals(c) ||
               long.class.equals(c) ||
               float.class.equals(c) ||
               double.class.equals(c) ||
               boolean.class.equals(c) ||
               char.class.equals(c);
    }
    
    public static boolean isPrimitiveWrapper(Class c) {
        return Number.class.isAssignableFrom(c) ||
               Boolean.class.equals(c) ||
               Character.class.equals(c);
    }
}
