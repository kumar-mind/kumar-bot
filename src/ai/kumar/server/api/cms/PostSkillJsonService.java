
package ai.kumar.server.api.cms;

import org.json.JSONObject;

import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import javax.servlet.http.HttpServletResponse;

public class PostSkillJsonService extends AbstractAPIHandler implements APIHandler {
    
    private static final long serialVersionUID = 18344225L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/postSkill.json";
    }
    
    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {
        
        JSONObject json = new JSONObject(true);
        
        // modify caching
        json.put("$EXPIRES", 0);
        return new ServiceResponse(json);
    }
}
