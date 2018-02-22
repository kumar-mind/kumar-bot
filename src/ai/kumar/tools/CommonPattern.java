
package ai.kumar.tools;

import java.util.regex.Pattern;

/**
 * This class provides Pattern constants to be used
 * to replace a regex in s.split(regex) method calls.
 * Because s.split(regex) causes an execution of
 * Pattern.compile(regex).split(s, 0), it is wise to pre-compile
 * all regex to a pattern p.
 * Therefore do the following: transform your code into
 * Pattern p = Pattern.compile(regex); p.split(s);
 * The compilation of a specific pattern should be done only once.
 * Therefore this class provides Pattern objects for the most common regex Strings.
 * 
 * The same applies to s.replaceall(regex, replacement) which is equal to
 * Pattern.compile(regex).matcher(s).replaceAll(replacement);
 */
public class CommonPattern {

    public final static Pattern SPACE       = Pattern.compile(" ");
    public final static Pattern COMMA       = Pattern.compile(",");
    public final static Pattern SEMICOLON   = Pattern.compile(";");
    public final static Pattern DOUBLEPOINT = Pattern.compile(":");
    public final static Pattern SLASH       = Pattern.compile("/");
    public final static Pattern PIPE        = Pattern.compile("\\|");
    public final static Pattern BACKSLASH   = Pattern.compile("\\\\");
    public final static Pattern QUESTION    = Pattern.compile("\\?");
    public final static Pattern AMP         = Pattern.compile("&");
    public final static Pattern AMP_HTML    = Pattern.compile(Pattern.quote("&amp;"));
    public final static Pattern PLUS        = Pattern.compile(Pattern.quote("+"));
    public final static Pattern DOT         = Pattern.compile("\\.");
    public final static Pattern NEWLINE     = Pattern.compile("\n");
    public final static Pattern VERTICALBAR = Pattern.compile(Pattern.quote("|"));
    public final static Pattern UNDERSCORE  = Pattern.compile("_");
    public final static Pattern TAB         = Pattern.compile("\t");
    
}
