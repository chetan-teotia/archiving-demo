package com.restSample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class ArchiveServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(request, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] myJsonData = req.getParameterValues("ArchiveAction[]");
		String action = myJsonData[0];
		InitManager manager = InitManager.getSessionManager();
		if (action.equalsIgnoreCase("START_ARCHIVE")) {
			String archiveId = manager.startArchive();
			JSONObject responseObj = new JSONObject();
			responseObj.put("archiveId", archiveId);
			String re = responseObj.toJSONString();
			resp.getWriter().write(re);
		} else if (action.equalsIgnoreCase("STOP_ARCHIVE")) {
			manager.stopArchive(myJsonData[2], myJsonData[1]);
		}

	}

}
