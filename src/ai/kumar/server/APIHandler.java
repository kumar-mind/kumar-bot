
package ai.kumar.server;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ai.kumar.json.JsonObjectWithDefault;

/**
 * Interface for all servlets
 */
public interface APIHandler {

    public String[] getServerProtocolHostStub();

    public abstract BaseUserRole getMinimalBaseUserRole();

    public abstract JSONObject getDefaultPermissions(BaseUserRole baseUserRole);
    
    /**
     * get the path to the servlet
     * @return the url path of the servlet
     */
    public String getAPIPath();
    
    /**
     * call the servlet with a query locally without a network connection
     * @return a Service Response
     * @throws IOException
     */
    public ServiceResponse serviceImpl(Query post, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) throws APIException;

}