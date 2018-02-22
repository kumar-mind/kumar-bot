
package ai.kumar.server.api.aaa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.kumar.DAO;
import ai.kumar.server.Query;
import ai.kumar.server.RemoteAccess;
import ai.kumar.server.AccessTracker.Track;

public class AccessServlet extends HttpServlet {

    private static final long serialVersionUID = 257718432475091648L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Query post = RemoteAccess.evaluate(request);
        if (post.isDoS_servicereduction() || post.isDoS_blackout()) {response.sendError(503, "your request frequency is too high"); return;} // DoS protection
        
        boolean anonymize = !post.isLocalhostAccess();
        
        String callback = post.get("callback", "");
        boolean jsonp = callback != null && callback.length() > 0;
        
        post.setResponse(response, "application/javascript");
        Collection<Track> tracks = DAO.access.getTracks();
        
        // generate json
        JSONObject json = new JSONObject(true);
        JSONArray access = new JSONArray();
        json.put("access", access);
        int maxcount = anonymize ? 100 : 1000;
        for (Track track: tracks) {
            if (anonymize && !track.get("class").equals("SearchServlet")) continue;
            JSONObject a = new JSONObject(true);
            for (String key: track.keySet()) {
                Object value = track.get(key);
                if (anonymize && "host".equals(key)) {
                    a.put("host-anonymized", Integer.toHexString(Math.abs(value.hashCode())));
                } else {
                    a.put(key, value);
                }
            }
            access.put(a);
            if (maxcount-- <= 0) break;
        }

        // write json
        PrintWriter sos = response.getWriter();
        if (jsonp) sos.print(callback + "(");
        sos.print(json.toString(2));
        if (jsonp) sos.println(");");
        sos.println();
        post.finalize();
    }
    
}
