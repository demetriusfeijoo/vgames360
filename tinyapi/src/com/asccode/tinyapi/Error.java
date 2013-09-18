package com.asccode.tinyapi;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 13/09/13
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */
public class Error {

    private final RequestError error;

    public Error(RequestError error) {
        this.error = error;
    }

    public RequestError getError() {
        return error;
    }
}
