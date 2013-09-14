package com.asccode.tinyapi;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 10/09/13
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceSettings {

    URI getServer();

    String getUser();

    String getPassword();

    public String getSessionId();

    public void setSessionId(String token);

    public boolean isAuthenticated();

}
