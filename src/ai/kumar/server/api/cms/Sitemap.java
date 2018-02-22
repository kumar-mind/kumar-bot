
package ai.kumar.server.api.cms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.kumar.server.Query;
import ai.kumar.server.RemoteAccess;
import ai.kumar.server.ServiceResponse;

public class Sitemap extends HttpServlet {

	private static final long serialVersionUID = -8475570405765656976L;
	private final String sitemaphead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Query post = RemoteAccess.evaluate(request);
		// String siteurl = request.getRequestURL().toString();
		// String baseurl = siteurl.substring(0, siteurl.length() -
		// request.getRequestURI().length()) + request.getContextPath() + "/";
		String baseurl = "http://loklak.org/";
		ServiceResponse sr = new TopMenuService().serviceImpl(post, null, null, null);
		JSONObject TopMenuJsonObject = sr.getObject();
		JSONArray sitesarr = TopMenuJsonObject.getJSONArray("items");
		response.setCharacterEncoding("UTF-8");
		PrintWriter sos = response.getWriter();
		sos.print(sitemaphead + "\n");
		for (int i = 0; i < sitesarr.length(); i++) {
			JSONObject sitesobj = sitesarr.getJSONObject(i);
			Iterator<String> sites = sitesobj.keys();
			sos.print("<url>\n<loc>" + baseurl + sitesobj.getString(sites.next().toString()) + "/</loc>\n"
					+ "<changefreq>weekly</changefreq>\n</url>\n");
		}
		sos.print("</urlset>");
		sos.println();
		post.finalize();
	}
}
