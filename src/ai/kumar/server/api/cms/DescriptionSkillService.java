
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
 This Servlet gives a API Endpoint to list descriptions for all the Skills given its model, group and language.
 Can be tested on 127.0.0.1:4000/cms/getDescriptionSkill.json
 */
public class DescriptionSkillService extends AbstractAPIHandler implements APIHandler {


    private static final long serialVersionUID = 4175356383695207511L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/getDescriptionSkill.json";
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
        String skill = call.get("skill", "");

        JSONObject descriptions = new JSONObject(true);
            for (Map.Entry<String, String> entry : DAO.kumar.getSkillDescriptions().entrySet()) {
                String path = entry.getKey();
                if ((model.length() == 0 || path.indexOf("/" + model + "/") > 0) &&
                        (group.length() == 0 || path.indexOf("/" + group + "/") > 0) &&
                        (language.length() == 0 || path.indexOf("/" + language + "/") > 0) &&
                        (skill.length() == 0 || path.indexOf("/" + skill + ".txt") > 0)) {
                    descriptions.put(path, entry.getValue());
                }
            }

            JSONObject json = new JSONObject(true)
                    .put("model", model)
                    .put("group", group)
                    .put("language", language)
                    .put("descriptions", descriptions);
            if (descriptions.length() != 0) {
                json.put("accepted", true);
                json.put("message", "Sucess: Fetched descriptions");
            } else {
                json.put("accepted", false);
                json.put("message", "Error: Can't find description");
            }
        return new ServiceResponse(json);
    }

}
