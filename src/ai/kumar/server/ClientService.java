
package ai.kumar.server;

import java.text.ParseException;
import java.util.Date;

import org.eclipse.jetty.util.log.Log;
import org.json.JSONObject;

import ai.kumar.tools.DateParser;

public class ClientService extends Client {

    private JSONObject json;
    
    public enum Type {
        apiAccess, // service grants access to a specific api
    }
    
    public ClientService(String rawIdString) throws IllegalArgumentException {
        super(rawIdString);
        this.json = new JSONObject(true);
    }
    
    public ClientService(Type type, String untypedId) {
        super(type.name(), untypedId);
        this.json = new JSONObject(true);
    }
    
    public ClientService setMetadata(JSONObject json) {
        this.json = json;
        return this;
    }

    public ClientService setAccessFrom(Date date) {
        this.json.put("accessFrom", DateParser.iso8601Format.format(date));
        return this;
    }

    public ClientService setAccessUntil(Date date) {
        this.json.put("accessUntil", DateParser.iso8601Format.format(date));
        return this;
    }
    
    public Date getAccessFrom() {
        if (!this.json.has("accessFrom")) return null;
        return parse(this.json.getString("accessFrom"));
    }
    
    public Date getAccessUntil() {
        if (!this.json.has("accessUntil")) return null;
        return parse(this.json.getString("accessUntil"));
    }
    
    private Date parse(String d) {
        try {
            return DateParser.iso8601Format.parse(d);
        } catch (ParseException e) {
        	Log.getLog().warn(e);
            return null;
        }
    }
    
    public Type getType() {
        return Type.valueOf(this.getKey());
    }

    public JSONObject toJSON() {
        JSONObject j = super.toJSON();
        j.put("meta", this.json);
        return j;
    }
    
    
}
