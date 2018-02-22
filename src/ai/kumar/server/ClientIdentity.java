
package ai.kumar.server;

import org.json.JSONObject;

/**
 * an identity is only a string which contains details sufficient enough to
 * identify a user and to send data to that user
 */
public class ClientIdentity extends Client {
    
    public enum Type {
        email(true), // non-anonymous identity
        host(false); // anonymous identity users which do not authentify; they are identified by their host name
        private final boolean persistent;
        Type(final boolean persistent) {
            this.persistent = persistent;
        }
        public boolean isPersistent() {
            return this.persistent;
        }
    }
    
    private final boolean persistent;
    
    public ClientIdentity(String rawIdString) throws IllegalArgumentException {
        super(rawIdString);
        this.persistent = Type.valueOf(super.getKey()).isPersistent();
    }
    
    public ClientIdentity(Type type, String untypedId) {
        super(type.name(), untypedId);
        this.persistent = type.isPersistent();
    }
    
    public boolean isPersistent() {
        return this.persistent;
    }
    
    public boolean isEmail() {
        return this.getKey().equals(Type.email.name());
    }
    
    public boolean isAnonymous() {
        return this.getKey().equals(Type.host.name());
    }
    
    public String getClient() {
        String client = this.getType() + "_" + this.getName();
        return client;
    }
    
    public Type getType() {
        return Type.valueOf(this.getKey());
    }
    
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("anonymous", this.isAnonymous());
        return json;
    }
}
