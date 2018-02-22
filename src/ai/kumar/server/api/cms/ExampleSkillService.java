
package ai.kumar.server.api.cms;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 This Servlet gives a API Endpoint to list examples for all the Skills given its model, group and language.
 Can be tested on 127.0.0.1:4000/cms/getExampleSkill.json
 */
public class ExampleSkillService extends AbstractAPIHandler implements APIHandler {

    private static final long serialVersionUID = -8691223678852307876L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/getExampleSkill.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {

        try {
            DAO.kumar.observe(); // get a database update
        } catch (IOException e) {
            DAO.log(e.getMessage());
        }
        
        String model = call.get("model", "");
        String group = call.get("group", "");
        String language = call.get("language", "");

        JSONObject examples = new JSONObject(true);
        for (Map.Entry<String, Set<String>> entry: DAO.kumar.getSkillExamples().entrySet()) {
            String path = entry.getKey();
            if ((model.length() == 0 || path.indexOf("/" + model + "/") > 0) &&
                (group.length() == 0 || path.indexOf("/" + group + "/") > 0) &&
                (language.length() == 0 || path.indexOf("/" + language + "/") > 0)) {
                examples.put(path, entry.getValue());
            }
        }
        
        JSONObject json = new JSONObject(true)
                .put("model", model)
                .put("group", group)
                .put("language", language)
                .put("examples", examples);
        json.put("accepted", true);
        json.put("message", "Success: Examples fetched");
        return new ServiceResponse(json);
    }

}
