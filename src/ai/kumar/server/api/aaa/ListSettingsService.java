
package ai.kumar.server.api.aaa;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 
 * Servlet to get list of all the files present in data/settings directory
 * test locally at http://127.0.0.1:4000/aaa/getAllFiles
 */
public class ListSettingsService extends AbstractAPIHandler implements APIHandler{
    @Override
    public String getAPIPath() {
        return "/aaa/listSettings.json";
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

        String path = DAO.data_dir.getPath()+"/settings/";
        File settings = new File(path);
        String[] files = settings.list();
        JSONObject result = new JSONObject(true);
        JSONArray fileArray = new JSONArray(files);
        result.put("accepted", true);
        result.put("message", "Success: Fetched all files");
        result.put("files", fileArray);
        return new ServiceResponse(result);
    }
}
