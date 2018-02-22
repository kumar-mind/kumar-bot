
package ai.kumar.server.api.kumar;

import java.io.IOException;

import org.json.JSONObject;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIException;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import javax.servlet.http.HttpServletResponse;

public class MindService extends AbstractAPIHandler implements APIHandler {
   
    private static final long serialVersionUID = 8578478303098111L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    public String getAPIPath() {
        return "/kumar/mind.json";
    }
    
    @Override
    public ServiceResponse serviceImpl(Query post, HttpServletResponse response, Authorization user, final JsonObjectWithDefault permissions) throws APIException {

        try {
            DAO.kumar.observe(); // get a database update
        } catch (IOException e) {
            DAO.log(e.getMessage());
        }
        
        JSONObject json = DAO.kumar.getMind();
        return new ServiceResponse(json);
    }
    
}
