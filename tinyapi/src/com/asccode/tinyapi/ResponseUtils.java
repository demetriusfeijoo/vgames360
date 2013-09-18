package com.asccode.tinyapi;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 17/09/13
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class ResponseUtils {

    public static Response<Error> buildResponseError( RequestError requestError ){

        String seq = "";
        int status = 1;

        Error error = new Error(requestError);

        return new Response<>(seq, status, error);

    }

}
