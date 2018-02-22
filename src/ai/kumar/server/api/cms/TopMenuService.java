
package ai.kumar.server.api.cms;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import javax.servlet.http.HttpServletResponse;

public class TopMenuService extends AbstractAPIHandler implements APIHandler {
    
    private static final long serialVersionUID = 1839868262296635665L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/topmenu.json";
    }
    
    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {
        
        JSONObject json = new JSONObject(true);
        JSONArray topmenu = new JSONArray()
            .put(new JSONObject().put("Home", "index.html"))
            .put(new JSONObject().put("API", "api.html"))
            .put(new JSONObject().put("Account", "apps/applist/index.html"));
        json.put("items", topmenu);
        json.put("accepted", true);
        json.put("message", "Request processed successfully");
        
        // modify caching
        json.put("$EXPIRES", 600);
        return new ServiceResponse(json);
    }
}
