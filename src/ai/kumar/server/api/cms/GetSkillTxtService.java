
package ai.kumar.server.api.cms;

import org.json.JSONObject;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.APIHandler;
import ai.kumar.server.AbstractAPIHandler;
import ai.kumar.server.Authorization;
import ai.kumar.server.BaseUserRole;
import ai.kumar.server.Query;
import ai.kumar.server.ServiceResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to load an skill from the skill database
 * i.e.
 * http://localhost:4000/cms/getSkill.txt
 * http://localhost:4000/cms/getSkill.txt?model=general&group=knowledge&language=en&skill=wikipedia
 */
public class GetSkillTxtService extends AbstractAPIHandler implements APIHandler {
    
    private static final long serialVersionUID = 18344224L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/getSkill.txt";
    }
    
    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {

        String model_name = call.get("model", "general");
        File model = new File(DAO.model_watch_dir, model_name);
        String group_name = call.get("group", "knowledge");
        File group = new File(model, group_name);
        String language_name = call.get("language", "en");
        File language = new File(group, language_name);
        String skill_name = call.get("skill", "wikipedia");
        File skill = new File(language, skill_name + ".txt");
        JSONObject json = new JSONObject(true);
        json.put("accepted", false);
        
        try {
            String content = new String(Files.readAllBytes(skill.toPath()));
            json.put("content", content);
            json.put("accepted", true);
            return new ServiceResponse(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ServiceResponse(json);
    }
}
