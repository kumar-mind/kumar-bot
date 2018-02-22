
package ai.kumar.server.api.kumar;

import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;
import org.broadbear.link.preview.SourceContent;
import org.broadbear.link.preview.TextCrawler;
import org.json.JSONObject;
import javax.servlet.http.HttpServletResponse;

public class LinkPreviewService extends AbstractAPIHandler implements APIHandler {

    private static final long serialVersionUID = 1463185662941444503L;

    @Override
    public BaseUserRole getMinimalBaseUserRole() { return BaseUserRole.ANONYMOUS; }

    @Override
    public JSONObject getDefaultPermissions(BaseUserRole baseUserRole) {
        return null;
    }

    public String getAPIPath() {
        return "/kumar/linkPreview.json";
    }

    @Override
    public ServiceResponse serviceImpl(Query post, HttpServletResponse response, Authorization rights, final JsonObjectWithDefault permissions) throws APIException {
        JSONObject jsonObject = new JSONObject();
        String url = post.get("url", "");
        if(url==null || url.isEmpty()){
            jsonObject.put("message","URL Not given");
            jsonObject.put("accepted",false);
            return new ServiceResponse(jsonObject);
        }
        SourceContent sourceContent = 	TextCrawler.scrape(url,3);
        if (sourceContent.getImages() != null) jsonObject.put("image", sourceContent.getImages().get(0));
        if (sourceContent.getDescription() != null) jsonObject.put("descriptionShort", sourceContent.getDescription());
        if(sourceContent.getTitle()!=null)jsonObject.put("title", sourceContent.getTitle());
        jsonObject.put("accepted",true);
        return new ServiceResponse(jsonObject);
    }

}
