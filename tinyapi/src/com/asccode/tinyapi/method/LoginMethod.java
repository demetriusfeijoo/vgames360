package com.asccode.tinyapi.method;

import android.content.Context;
import android.util.Log;
import com.asccode.tinyapi.*;
import com.asccode.tinyapi.Error;
import com.asccode.tinyapi.model.Login;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */
public class LoginMethod extends Method<Login> {

    public LoginMethod( ServiceSettings serviceSettings, Context context){

        super(serviceSettings, context);

    }

    @Override
    public Map<String, String> paramsQuery() {

        Map<String, String> map = new HashMap<String, String>(3);

        map.put("op", "login");
        map.put("user", this.getServiceSettings().getUser());
        map.put("password", this.getServiceSettings().getPassword());

        return map;
    }

    @Override
    protected Login parserContent(JsonElement content) {

        JsonObject jsonObject = content.getAsJsonObject();

        String sessionId = jsonObject.get("session_id").getAsString();
        String apiLevel = jsonObject.get("api_level").getAsString();

        return new Login(sessionId, apiLevel);

    }

    @Override
    protected void success(Response<Login> successResponse) {

        this.getServiceSettings().setSessionId(successResponse.getContent().getSessionId());

        Log.d("TinyAPI", "Successfully Login User");

    }

    @Override
    protected void error(Response<Error> errorResponse) {

        Log.d("TinyAPI", "Login User Failure");

    }

    @Override
    public void execute() throws ConnectException{

        if( !this.getServiceSettings().isAuthenticated() ){

            super.execute();

        }else{

            Response<Error> errorResponse = ResponseUtils.buildResponseError(RequestError.ALREADY_LOGGED_IN);

            this.error(errorResponse);

        }

    }
}