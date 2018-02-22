
package ai.kumar.server;

public class APIException extends Exception {

    private static final long serialVersionUID = -6974553774866005875L;
    private int statusCode = -1;
    
    public APIException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }
}
