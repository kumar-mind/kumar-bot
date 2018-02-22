
package ai.kumar.server.api.aaa;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.ServiceResponse;
import ai.kumar.server.Query;
import ai.kumar.server.Authorization;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This Servlets creates a group with default baserole as anonymous.
 * It takes 1 parameter group and other optional parameter role.
 * Can be tested on
 * http://127.0.0.1:4000/aaa/createGroup.json?group=groupName&role=admin&permission=<comma separated values>
 *
 */
public class CreateGroupService extends AbstractAPIHandler implements APIHandler {


    private static final long serialVersionUID = -742269505564698987L;

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
        return "/aaa/createGroup.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {
        JSONObject result = new JSONObject();
        String groupName = call.get("group", null);
        String grouppBaseRole = call.get("role","anonymous");
        String permission = call.get("permissions","");
        if( groupName!=null ) {
            if( DAO.group.has(groupName)) {
                result.put("message", "Group already exists");
                result.put("accepted", false);
            } else {
                JSONObject groupDetail = new JSONObject();
                groupDetail.put("group_members", new JSONObject());
                groupDetail.put("groupBaseRole", grouppBaseRole);
                ArrayList aList= new ArrayList(Arrays.asList(permission.split(",")));
                JSONArray permissionArray = new JSONArray();
                for(int i=0;i<aList.size();i++)
                {
                    permissionArray.put(i,aList.get(i));
                }
                groupDetail.put("permission", permissionArray);
                result.put("message", "Group created successfully");
                result.put("accepted", true);
                DAO.group.put(groupName,groupDetail,rights.getIdentity().isPersistent());
            }
            return new ServiceResponse(result);
        } else {
            result.put("message", "Bad call, group name parameter not specified");
            result.put("accepted", false);
        }
        return new ServiceResponse(result);
    }

}

