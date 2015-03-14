package com.restSample;

import java.util.HashMap;
import java.util.Map;

import com.opentok.Archive;
import com.opentok.MediaMode;
import com.opentok.OpenTok;
import com.opentok.Role;
import com.opentok.Session;
import com.opentok.SessionProperties;
import com.opentok.TokenOptions;

public class InitManager {
	int apiKey = 45128042;
	String apiSecret = "7547a920648243bfb2abac46f41bee1a1f969ec9";
	OpenTok opentok;
	private Session session;
	Map<String, Object> sessionAttr = new HashMap<String, Object>();
	private static InitManager manager;
	private Archive archive;

	public static InitManager getSessionManager() {
		if (manager == null) {
			manager = new InitManager();
			manager.initSession();
		}

		return manager;
	}

	private void initSession() {
		try {
			opentok = new OpenTok(apiKey, apiSecret);
			this.session = opentok
					.createSession(new SessionProperties.Builder().mediaMode(
							MediaMode.ROUTED).build());
			String sessionId = this.session.getSessionId();
			sessionAttr.put("apiKey", apiKey);
			sessionAttr.put("sessionId", sessionId);
			sessionAttr.put("roomName", "room" + apiKey);
			generateTokens();

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}

	public void generateTokens() {

		String tokenAdmin = null;
		String tokenPublisher = null;
		String tokenSubscriber = null;

		try {
			tokenAdmin = this.session.generateToken(new TokenOptions.Builder()
					.role(Role.MODERATOR)
					.expireTime(
							(System.currentTimeMillis() / 1000L)
									+ (7 * 24 * 60 * 60)).data("name=Mukesh")
					.build());

			tokenPublisher = this.session
					.generateToken(new TokenOptions.Builder()
							.role(Role.PUBLISHER)
							.expireTime(
									(System.currentTimeMillis() / 1000L)
											+ (7 * 24 * 60 * 60))
							.data("name=Taruna").build());

			tokenSubscriber = this.session
					.generateToken(new TokenOptions.Builder()
							.role(Role.SUBSCRIBER)
							.expireTime(
									(System.currentTimeMillis() / 1000L)
											+ (7 * 24 * 60 * 60))
							.data("name=Chetan").build());

			sessionAttr.put("tokenAdmin", tokenAdmin);
			sessionAttr.put("tokenPublisher", tokenPublisher);
			sessionAttr.put("tokenSubscriber", tokenSubscriber);

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}

	public String getRoomName() {
		return sessionAttr.get("roomName").toString();
	}

	public String getSessionId() {
		return sessionAttr.get("sessionId").toString();
	}

	public String getApiKey() {
		return sessionAttr.get("apiKey").toString();
	}

	public String getToken(String role) {
		String token = null;
		switch (role) {
		case "Admin":
			token = sessionAttr.get("tokenAdmin").toString();
			break;
		case "Publisher":
			token = sessionAttr.get("tokenPublisher").toString();
			break;
		case "Subscriber":
			token = sessionAttr.get("tokenSubscriber").toString();
			break;
		}
		return token;

	}

	public String startArchive() {
		try {
			if (archive == null)
				archive = opentok.startArchive(session.getSessionId(), null);
			return archive.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void stopArchive(String archiveId, String SessionId) {
		try {
			if (archive != null && archive.getSessionId().equalsIgnoreCase(SessionId))
			{
				opentok.stopArchive(archiveId);
				System.out.println("stopped");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
