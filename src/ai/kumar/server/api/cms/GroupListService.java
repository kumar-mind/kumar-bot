package ai.kumar.server.api.cms;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FilenameFilter;

/**
 * 
 * API to get list of groups for a given model.
 * test locally at http://127.0.0.1:4000/cms/getgroups.json
 */

public class GroupListService extends AbstractAPIHandler implements APIHandler {
    @Override
    public String getAPIPath() {
        return "/cms/getGroups.json";
    }

    @Override
    public BaseUserRole getMinimalBaseUserRole() {
        return null;
    }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, JsonObjectWithDefault permissions) throws APIException {

        JSONObject json = new JSONObject(true);
        json.put("accepted", false);
        String model_name = call.get("model", "general");
        File model = new File(DAO.model_watch_dir, model_name);

        String[] groups = model.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return new File(file, s).isDirectory();
            }
        });
        JSONObject result = new JSONObject();
        result.put("accepted", true);
        JSONArray groupsArray = new JSONArray(groups);
        result.put("groups",groupsArray);
        result.put("message", "Success: Fetched group list");
        return new ServiceResponse(result);
    }
}
