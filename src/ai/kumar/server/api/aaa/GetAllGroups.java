
package ai.kumar.server.api.aaa;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Set;

/**
 * This Servlets returns all the group details of all the groups created.
 * Accepts NO, GET or POST parameters.
 * Can be tested on
 * http://127.0.0.1:4000/aaa/getAllGroups.json
 *
 */
public class GetAllGroups extends AbstractAPIHandler implements APIHandler {

    private static final long serialVersionUID = -179412273153306443L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() {
        return BaseUserRole.ADMIN;
    }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/aaa/getAllGroups.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {
        JSONObject result = DAO.group.toJSON();
        result.put("accepted", true);
        result.put("message", "Success: Fetched all groups");
        return new ServiceResponse(result);
    }

}
