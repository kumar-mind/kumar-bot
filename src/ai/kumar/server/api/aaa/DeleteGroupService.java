
package ai.kumar.server.api.aaa;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * http://127.0.0.1:4000/aaa/deleteGroup.json?group=groupName
 */
public class DeleteGroupService extends AbstractAPIHandler implements APIHandler{

    private static final long serialVersionUID = -6460356959547369940L;

    @Override
    public String getAPIPath() {
        return "/aaa/deleteGroup.json";
    }

    @Override
    public BaseUserRole getMinimalBaseUserRole() {
        return BaseUserRole.ADMIN;
    }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public ServiceResponse serviceImpl(Query post, HttpServletResponse response, Authorization rights, JsonObjectWithDefault permissions) throws APIException {
        JSONObject result = new JSONObject();
        String groupName = post.get("group", null);

        if( groupName!=null ) {
            if( DAO.group.has(groupName)) {
                DAO.group.remove(groupName);
                result.put("accepted", true);
		result.put("message","Group deleted successfully");
            } else {
                result.put("accepted", false);
		result.put("message", "Group does not exists");
            }
            return new ServiceResponse(result);
        } else {
	    result.put("accepted", false);
	    result.put("message", "Bad call, group name parameter not specified");
        }
        return new ServiceResponse(result);
    }
}
