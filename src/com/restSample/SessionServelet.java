package com.restSample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class SessionServelet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(request, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] myJsonData = req.getParameterValues("rolename[]");
		String role = myJsonData[0];
		InitManager manager = InitManager.getSessionManager();

		JSONObject responseObj = new JSONObject();
		responseObj.put("token", manager.getToken(role));
		responseObj.put("room", manager.getRoomName());
		responseObj.put("session", manager.getSessionId());
		responseObj.put("apiKey", manager.getApiKey());
		responseObj.put("role", role);

		//resp.setContentType("json");
	String re=responseObj.toJSONString();
			resp.getWriter().write(re);

	}

}
