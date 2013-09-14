package com.asccode.tinyapi;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 13/09/13
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public interface RequestCallback<T> {

    public void onStart();
    public void onSuccess(Response<T> successResponse);
    public void onError(Response<Error> errorResponse);

}
