
package ai.kumar.server.api.vis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ai.kumar.server.Query;
import ai.kumar.server.RemoteAccess;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/*
 * Sample Queries
 * ==============
 * Every json to be visualized should be a key:value pair object
 * {"ford":"17.272992","toyota":"27.272992","renault":"47.272992"}
 * Note that the floatvalues should be strings and stringified
 * Query: http://127.0.0.1:4000/vis/piechart.png?data={%22ford%22:%2217.272992%22,%22toyota%22:%2227.272992%22,%22renault%22:%2247.272992%22}&width=1000&height=1000&legend=true&tooltip=false
 */

public class PieChartServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {

		Query post = RemoteAccess.evaluate(request);

		// Get the json data to visualize as a key value pair of data=

		if (post.isDoS_blackout()) { response.sendError(503, "Your request frequency is too high"); return; }

		// Get the data json string passed as stream parameter
		String data = post.get("data", "");
		String legendBitString = post.get("legend", "");
		String tooltipBitString = post.get("tooltip", "");
		String widthString = post.get("width", "");
		String heightString = post.get("height", "");
		boolean legendBit = true;
		boolean tooltipBit = false;

		int width = 500;
		int height = 500;

		if ("".equals(legendBitString) || "false".equals(legendBitString)) {
			legendBit = true;
		} else {
			legendBit = false;
		}

		if ("".equals(tooltipBitString) || !"true".equals(tooltipBitString)) {
			tooltipBit = false;
		} else {
			tooltipBit = true;
		}

		if (!"".equals(widthString)) {
			width = Integer.parseInt(widthString);
		}

		if (!"".equals(heightString)) {
			height = Integer.parseInt(heightString);
		}

		JSONObject json = new JSONObject(data);

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		JFreeChart chart = getChart(json, legendBit, tooltipBit);
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
	}

	public JFreeChart getChart(JSONObject jsonData, boolean legendBit, boolean tooltipBit) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		Iterator<String> iter = jsonData.keys();

		while (iter.hasNext()) {
			String keyData = iter.next();
			Float value = Float.parseFloat(jsonData.getString(keyData));
			dataset.setValue(keyData, value);
		}

		boolean legend = legendBit;
		boolean tooltips = tooltipBit;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("KUMAR Visualizes - PieChart", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.BLACK);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}
}
