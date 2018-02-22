
package ai.kumar.server;

import org.json.JSONObject;

/**
 * Users or technical clients of the user are represented with Objects of this class.
 * A client identification string is defined as <typeName>:<untypedId> where <typeName> denotes an authentication
 * method and <untypedId> a name within that authentication domain.
 */
public class Client {

    private final static char SEPARATOR = ':';
    
    private String id;
    private int separatorPos;

    protected Client(String rawIdString) throws IllegalArgumentException {
        this.separatorPos = rawIdString.indexOf(SEPARATOR);
        assert this.separatorPos >= 0;
        if (this.separatorPos < 0) throw new IllegalArgumentException("identification string must contain a colon");
        this.id = rawIdString;
    }

    protected Client(String typeName, String untypedId) {
        this.id = typeName + SEPARATOR + untypedId;
        this.separatorPos = typeName.length();
    }
    
    protected String getKey() {
        return id.substring(0, this.separatorPos);
    }
    
    public String getName() {
        return this.id.substring(this.separatorPos + 1);
    }
    
    public String toString() {
        return this.id;
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject(true);
        json.put("type", getKey());
        json.put("name", this.getName());
        return json;
    }
}
