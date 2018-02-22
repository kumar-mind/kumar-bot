
package ai.kumar.server.api.cms;

import ai.kumar.DAO;
import org.json.JSONObject;

import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Endpoint to return text of a skill in JSON format from the skill database
 * i.e.
 * http://localhost:4000/cms/getSkill.json
 * This accepts 4 parameters: - Model, Group, Language and Skill Name
 * http://localhost:4000/cms/getSkill.json?model=general&group=knowledge&language=en&skill=wikipedia
 */

public class GetSkillJsonService extends AbstractAPIHandler implements APIHandler {

    private static final long serialVersionUID = 18344223L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/getSkill.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {

        JSONObject json = new JSONObject(true);

        // modify caching
        json.put("$EXPIRES", 0);
        json.put("accepted", false);
        String model_name = call.get("model", "general");
        File model = new File(DAO.model_watch_dir, model_name);
        String group_name = call.get("group", "knowledge");
        File group = new File(model, group_name);
        String language_name = call.get("language", "en");
        File language = new File(group, language_name);
        String skill_name = call.get("skill", "wikipedia");
        File skill = new File(language, skill_name + ".txt");

        try {
            String content = new String(Files.readAllBytes(skill.toPath()));
            json.put("text",content);
            json.put("accepted",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ServiceResponse(json);
    }
}
