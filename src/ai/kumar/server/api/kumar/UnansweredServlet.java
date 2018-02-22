
package ai.kumar.server.api.kumar;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ai.kumar.DAO;
import ai.kumar.mind.KumarMemory.TokenMapList;
import ai.kumar.server.FileHandler;
import ai.kumar.server.Query;
import ai.kumar.server.RemoteAccess;
import ai.kumar.tools.UTF8;

public class UnansweredServlet extends HttpServlet {

    private static final long serialVersionUID = -7095346224124198L;

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Query post = RemoteAccess.evaluate(request);

        final StringBuilder buffer = new StringBuilder(1000);
        List<TokenMapList> tokenstats = DAO.kumar.unanswered2tokenizedstats();
        tokenstats.forEach(tml -> {
            LinkedHashMap<String, Integer> m = tml.getMap();
            buffer.append("TOKEN \"");
            buffer.append(tml.getToken());
            buffer.append("\" (");
            buffer.append(tml.getCounter());
            buffer.append(")\n");
            m.forEach((k,v) -> {
                buffer.append(k);
                buffer.append(" (");
                buffer.append(v);
                buffer.append(")\n");
            });
            buffer.append("\n");
            
        });
        
        /*
        LinkedHashMap<String, Integer> unanswered = MapTools.sortByValue(DAO.kumar.getUnanswered());
        for (Map.Entry<String, Integer> entry: unanswered.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append(" (");
            buffer.append(entry.getValue());
            buffer.append(")\n");
        }
        */
        
        FileHandler.setCaching(response, 60);
        post.setResponse(response, "text/plain");
        response.getOutputStream().write(UTF8.getBytes(buffer.toString()));
        post.finalize();
    }
    
}
