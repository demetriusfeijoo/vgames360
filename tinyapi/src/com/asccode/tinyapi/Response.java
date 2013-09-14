package com.asccode.tinyapi;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 13/09/13
 * Time: 21:26
 * To change this template use File | Settings | File Templates.
 */
public class Response<T> {

    private final String seq;
    private final int status;
    private final T content;

    public Response(String seq, int status, T content) {
        this.seq = seq;
        this.status = status;
        this.content = content;
    }

    public String getSeq() {
        return seq;
    }

    public int getStatus() {
        return status;
    }

    public T getContent() {
        return content;
    }
}
