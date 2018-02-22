
package ai.kumar.server.api.aaa;

import org.json.JSONObject;

import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;

import javax.servlet.http.HttpServletResponse;

public class AuthorizationDemoService extends AbstractAPIHandler implements APIHandler {

    private static final long serialVersionUID = 8678478303032749879L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

	@Override
	public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
		JSONObject result = new JSONObject();

		switch(baseUserRole){
			case ADMIN:
				result.put("download_limit", -1);
				break;
			case PRIVILEGED:
				result.put("download_limit", 1000);
				break;
			case USER:
				result.put("download_limit", 100);
				break;
			case ANONYMOUS:
				result.put("download_limit", 10);
				break;
			default:
				result.put("download_limit", 0);
		}

		return result;
	}

	@Override
	public String getAPIPath() {
        return "/aaa/authorization-demo.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query post, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) throws APIException {
    	
    	JSONObject result = new JSONObject(true);
		result.put("accepted", true);
		result.put("message", "Successfully processed request");
		result.put("user", rights.getIdentity().getName());
        result.put("user role", rights.getUserRole().getDisplayName());
        result.put("base user role", rights.getBaseUserRole().name());
		result.put("permissions",rights.getPermissions(this));
		
		return new ServiceResponse(result);
    }
}
