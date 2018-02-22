
package ai.kumar.tools;

import java.nio.charset.StandardCharsets;

public class UTF8 {

    public boolean insensitive;

    /**
     * using the string method with the default charset given as argument should prevent using the charset cache
     * in FastCharsetProvider.java:118 which locks all concurrent threads using a UTF8.String() method
     * @param bytes
     * @return
     */
    public final static String String(final byte[] bytes) {
        return bytes == null ? "" : new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
    }

    public final static String String(final byte[] bytes, final int offset, final int length) {
        assert bytes != null;
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    /**
     * getBytes() as method for String synchronizes during the look-up for the
     * Charset object for the default charset as given with a default charset name.
     * With our call using a given charset object, the call is much easier to perform
     * and it omits the synchronization for the charset lookup.
     *
     * @param s
     * @return
     */
    public final static byte[] getBytes(final String s) {
        if (s == null) return null;
        return s.getBytes(StandardCharsets.UTF_8);
    }

    public final static byte[] getBytes(final StringBuilder s) {
        if (s == null) return null;
        return s.toString().getBytes(StandardCharsets.UTF_8);
    }

}
