
package ai.kumar.server.api.aaa;

import ai.kumar.DAO;
import ai.kumar.json.JsonObjectWithDefault;
import ai.kumar.server.*;
import org.eclipse.jetty.client.api.Request;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * 
 * http://127.0.0.1:4000/aaa/uploadSettings.json
 */
public class UploadSettingsService extends AbstractAPIHandler implements APIHandler{
    @Override
    public String getAPIPath() {
        return "/aaa/uploadSettings.json";
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

        JSONObject result = new JSONObject(true);
        result.put("accepted", false);
        result.put("message", "Error: Unable to Upload file");

        String path = DAO.data_dir+"/settings/";
        File settings = new File(path);

        String fileName = null;
        //Get all the parts from request and write it to the file on server
        try {
            HttpServletRequest request = post.getRequest();
            for (Part part : request.getParts()) {
                fileName = getFileName(part);
                part.write( path + File.separator + fileName);
                request.setAttribute("message", fileName + " File uploaded successfully!");
            }
            result.put("accepted", true);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("accepted", false);
        } catch (ServletException e) {
            e.printStackTrace();
            result.put("accepted", false);
        }
        return new ServiceResponse(result);
    }

    /**
     * Utility method to get file name from HTTP header content-disposition
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        System.out.println("content-disposition header= "+contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }
}
