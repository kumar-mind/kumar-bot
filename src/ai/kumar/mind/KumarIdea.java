
package ai.kumar.mind;

import java.util.regex.PatternSyntaxException;

/**
 * An idea is the application of a intent on a specific input. This matches with the idea of ideas where
 * an idea is the 'sudden' solution to a problem with the hint how to apply the idea's core concept
 * on the given input details. That is what this class does: it combines a intent with the pattern
 * that matched from the input with the intent.
 */
public class KumarIdea {

    private KumarIntent intent;
    private KumarReader.Token token;
    
    /**
     * create an idea based on a intent
     * @param intent the intent that matched
     * @throws PatternSyntaxException
     */
    public KumarIdea(KumarIntent intent) throws PatternSyntaxException {
        this.intent = intent;
        this.token = null;
    }

    public KumarIntent getIntent() {
        return this.intent;
    }
    
    /**
     * Add an token to the idea. The token is usually a work (i.e. a normalized single word)
     * that matched with the intent keys.
     * @param token Key
     * @return the idea
     */
    public KumarIdea setToken(KumarReader.Token token) {
        this.token = token;
        return this;
    }
    
    /**
     * get the tokens for the idea
     * @return the keyword which matched with the intent keys
     */
    public KumarReader.Token getToken() {
        return this.token;
    }
    
    public String toString() {
        return this.intent.toString();
    }
}
