
package ai.kumar.server.api.aaa;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIException;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Accounting;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * Servlet to write user setting
 * this service accepts two parameter key and value to be stored in User settings
 * test locally at http://127.0.0.1:4000/aaa/changeUserSettings.json?key=theme&value=dark
 */
public class ChangeUserSettings extends AbstractAPIHandler implements APIHandler {

    private static final long serialVersionUID = -7418883159709458190L;

    @Override
    public String getAPIPath() {
        return "/aaa/changeUserSettings.json";
    }

    @Override
    public BaseUserRole getMinimalBaseUserRole() {
        return BaseUserRole.USER;
    }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public ServiceResponse serviceImpl(Query query, HttpServletResponse response, Authorization authorization, JsonObjectWithDefault permissions) throws APIException {

       String countSetting = query.get("count","-1");
       int count = Integer.parseInt(countSetting);
       if(count == -1){
           throw new APIException(400, "Bad Service call, count parameters not provided");
       } else {
           Map<String, String> settings = new HashMap<String, String>();
           for (int i = 1; i <= count; i++) {
               String key = query.get("key" + i, null);
               String value = query.get("value" + i, null);
               if (key == null || value == null) {
                   throw new APIException(400, "Bad Service call, key or value parameters not provided");
               } else {
                   settings.put(key, value);
               }
           }
           if (authorization.getIdentity() == null) {
               throw new APIException(400, "Specified User Setting not found, ensure you are logged in");
           } else {
               Accounting accounting = DAO.getAccounting(authorization.getIdentity());
               for (Map.Entry<String, String> entry : settings.entrySet()) {
                   String key = entry.getKey();
                   String value = entry.getValue();
                   JSONObject jsonObject = new JSONObject();
                   jsonObject.put(key, value);
                   if (accounting.getJSON().has("settings")) {
                       accounting.getJSON().getJSONObject("settings").put(key, value);
                   } else {
                       accounting.getJSON().put("settings", jsonObject);
                   }
               }
               JSONObject result = new JSONObject(true);
               result.put("accepted", true);
               result.put("message", "You successfully changed settings of your account!");
               return new ServiceResponse(result);
           }
       }

    }
}
