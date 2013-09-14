package com.asccode.tinyapi.model;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 13/09/13
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class Login {

    private final String sessionId;
    private final String apiLevel;

    public Login(String sessionId, String apiLevel) {
        this.sessionId = sessionId;
        this.apiLevel = apiLevel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getApiLevel() {
        return apiLevel;
    }
}
