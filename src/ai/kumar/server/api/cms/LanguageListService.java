package ai.kumar.server.api.cms;

import org.json.JSONArray;
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
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to load languages from the skill database
 * i.e.
 * http://localhost:4000/cms/getLanguage.json
 * http://localhost:4000/cms/getLanguage.json?model=general&group=smalltalk&skill=German-Standalone-aiml2kumar
 */
public class LanguageListService extends AbstractAPIHandler implements APIHandler {


    private static final long serialVersionUID = -5176264536025896261L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    @Override
    public String getAPIPath() {
        return "/cms/getLanguage.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query call, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) {

        String model_name = call.get("model", "general");
        File model = new File(DAO.model_watch_dir, model_name);
        String group_name = call.get("group", "knowledge");
        File group = new File(model, group_name);
        String skill_name = call.get("skill", "wikipedia");
        JSONObject json = new JSONObject(true);
        json.put("accepted", false);

        String[] languages = group.list((current, name) -> new File(current, name).isDirectory());
        ArrayList<String> languageList = new ArrayList<>();
        for(String languageName: languages) {
            File language = new File(group, languageName);
            File[] listOfSkills = language.listFiles();
            for(File skill: listOfSkills) {
                if(skill.getName().equals(skill_name + ".txt")) { // given skill found in the language, add language to language list and continue searching
                    languageList.add(language.getName());
                    break;
                }
            }
        }
        JSONArray languageJsonArray = new JSONArray(languageList);
        json.put("languageJsonArray", languageJsonArray);
        json.put("accepted", true);
        return new ServiceResponse(json);
    }
}
