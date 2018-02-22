
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
 This Servlet gives a API Endpoint to list meta for a  Skill. Given its model, group and language and skill.
 Can be tested on 127.0.0.1:4000/cms/getSkillMetadata.json
 */
public class GetSkillMetadataService extends AbstractAPIHandler implements APIHandler {


    private static final long serialVersionUID = 3446536703362688060L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/getSkillMetadata.json";
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

        if (model.length() == 0 || group.length() == 0 ||language.length() == 0 || skill.length() == 0 ) {
            JSONObject json = new JSONObject(true);
            json.put("accepted", false);
            json.put("message", "Error: Bad parameter call");
            return new ServiceResponse(json);
        }

        JSONObject skillMetadata = new JSONObject(true)
                .put("model", model)
                .put("group", group)
                .put("language", language);
        for (Map.Entry<String, String> entry : DAO.kumar.getSkillDescriptions().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0)
                    && (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("descriptions", entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : DAO.kumar.getSkillImage().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("image", entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : DAO.kumar.getAuthors().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("author", entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : DAO.kumar.getAuthorsUrl().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("author_url", entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : DAO.kumar.getDeveloperPrivacyPolicies().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("developer_privacy_policy", entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : DAO.kumar.getSkillNames().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("skill_name", entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry : DAO.kumar.getTermsOfUse().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("terms_of_use", entry.getValue());
            }
        }
        for (Map.Entry<String, Boolean> entry : DAO.kumar.getDynamicContent().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                skillMetadata.put("dynamic_content", entry.getValue());
            }
        }
        JSONObject examples = new JSONObject(true);
        for (Map.Entry<String, Set<String>> entry: DAO.kumar.getSkillExamples().entrySet()) {
            String path = entry.getKey();
            if ((path.indexOf("/" + model + "/") > 0) &&
                    (path.indexOf("/" + group + "/") > 0) &&
                    (path.indexOf("/" + language + "/") > 0) &&
                    (path.indexOf("/" + skill + ".txt") > 0)) {
                examples.put(path, entry.getValue());
            }
        }
        skillMetadata.put("examples", examples);
        JSONObject json = new JSONObject(true);
        json.put("skill_metadata", skillMetadata);
        json.put("accepted", true);
        json.put("message", "Success: Fetched Skill's Metadata");
        return new ServiceResponse(json);
    }

}
