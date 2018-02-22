
package ai.kumar.server.api.aaa;

import org.json.JSONObject;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserManagementService extends AbstractAPIHandler implements APIHandler {

	private static final long serialVersionUID = 8578478303032749879L;

	@Override
	public BaseUserRole getMinimalBaseUserRole() {
		return BaseUserRole.PRIVILEGED;
	}

	@Override
	public JSONObject getDefaultPermissions(BaseUserRole baseUserRole){
		JSONObject result = new JSONObject();

		switch(baseUserRole){
			case ADMIN:
				result.put("list_users", true);
				result.put("list_users-roles", true);
				result.put("edit-all", true);
				result.put("edit-less-privileged", true);
				break;
			case PRIVILEGED:
				result.put("list_users", true);
				result.put("list_users-roles", true);
				result.put("edit-all", false);
				result.put("edit-less-privileged", true);
				break;
			default:
				result.put("list_users", false);
				result.put("list_users-roles", false);
				result.put("edit-all", false);
				result.put("edit-less-privileged", false);
				break;
		}

		return result;

	}

	public String getAPIPath() {
		return "/aaa/user-management.json";
	}

	@Override
	public ServiceResponse serviceImpl(Query post, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) throws APIException {

		JSONObject result = new JSONObject(true);
		result.put("accepted", false);
		result.put("message", "Error: Unable to show user management details");
		switch (post.get("show","")){
			case "user-list":
				if(permissions.getBoolean("list_users", false)){
					Collection<ClientIdentity> authorized = DAO.getAuthorizedClients();
			        JSONObject users = new JSONObject();
			        authorized.forEach(client -> users.put(client.getClient(), client.toJSON()));
					result.put("user-list", users);
					result.put("accepted", true);
					result.put("message","Success: Showing user list");
				} else throw new APIException(403, "Forbidden");
				break;
			case "user-roles":
				JSONObject userRolesObj = new JSONObject();
				Map<String, UserRole> userRoles = DAO.userRoles.getUserRoles();
				for(String key : userRoles.keySet()){
					UserRole userRole = userRoles.get(key);
					JSONObject obj = new JSONObject();
					obj.put("display-name",userRole.getDisplayName());
					obj.put("base-user-role",userRole.getBaseUserRole().name());
					obj.put("permission-overrides",userRole.getPermissionOverrides());
					userRolesObj.put(key,obj);
				}
				result.put("user-roles", userRolesObj);
				result.put("accepted", true);
				result.put("message","Success: Showing user roles");
				break;
			default: throw new APIException(400, "No 'show' parameter specified");
		}

		return new ServiceResponse(result);
	}
}
