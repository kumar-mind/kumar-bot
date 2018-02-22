
package ai.kumar.server;

/**
 * A credential is used as key in DAO.authentication
 */
public class ClientCredential extends Client {
    
    public enum Type {
    	passwd_login(true),
        cookie(false),
        access_token(false),
        resetpass_token(false),
        pubkey_challange(false),
        host(false);
        private final boolean persistent;
        Type(final boolean persistent) {
            this.persistent = persistent;
        }
        public boolean isPersistent() {
            return this.persistent;
        }
    }
    
    private final boolean persistent;

    public ClientCredential(String rawIdString) throws IllegalArgumentException {
        super(rawIdString);
        this.persistent = Type.valueOf(super.getKey()).isPersistent();
    }
    
    public ClientCredential(Type type, String untypedId) {
        super(type.name(), untypedId);
        this.persistent = type.isPersistent();
    }

    public ClientCredential(ClientIdentity identity) {
        super(Type.passwd_login.name(), identity.getName());
        this.persistent = Type.passwd_login.isPersistent();
    }

    public boolean isPersistent() {
        return this.persistent;
    }
    
    public boolean isPasswdLogin() {
        return this.getKey().equals(Type.passwd_login.name());
    }
    
    public boolean isCookie() {
        return this.getKey().equals(Type.cookie.name());
    }
    
    public boolean isToken() {
        return this.getKey().equals(Type.access_token.name());
    }

    public boolean isResetToken() {
        return this.getKey().equals(Type.resetpass_token.name());
    }

    public boolean isPubkeyChallange() {
        return this.getKey().equals(Type.pubkey_challange.name());
    }
    
    public boolean isAnonymous() {
        return this.getKey().equals(Type.host.name());
    }
    
    public Type getType() {
        return Type.valueOf(this.getKey());
    }
    
}
