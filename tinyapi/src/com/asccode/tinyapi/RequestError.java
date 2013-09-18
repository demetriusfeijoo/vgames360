package com.asccode.tinyapi;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 17/09/13
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */

public enum RequestError {

    LOGIN_ERROR("LOGIN ERROR"),
    API_DISABLED("API DISABLED"),
    NOT_LOGGED_IN("NOT LOGGED IN"),
    ALREADY_LOGGED_IN("ALREADY LOGGED IN"),
    INCORRECT_USAGE("INCORRECT USAGE"),
    UNKNOWN_METHOD("UNKNOWN METHOD"),
    UNKNOWN_HTTP_ERROR("UNKNOWN HTTP ERROR"),
    BAD_REQUEST("BAD REQUEST [400]"),
    UNAUTHORIZED("SERVICE UNAVAILABLE [401]"),
    NOT_FOUND("NOT FOUND [404]"),
    TIMEOUT("TIMEOUT [408]"),
    TOO_LONG("TOO LONG [413]"),
    BAD_GATEWAY("BAD GATEWAY [502]"),
    SERVICE_UNAVAILABLE("SERVICE UNAVAILABLE [503]"),
    GATEWAY_TIMEOUT("GATEWAY TIMEOUT [504]");

    private String name;

    private RequestError(String name) {
        this.name = name;
    }
}
