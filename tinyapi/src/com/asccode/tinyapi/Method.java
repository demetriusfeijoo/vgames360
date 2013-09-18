package com.asccode.tinyapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Map;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */

// SET TIMEOUT // CACHE?    //Substituir para o HttpURLConnection que é mais performático
public abstract class Method<ResponseType>{

    private final ServiceSettings serviceSettings;
    private final Context context;
    private RequestCallback<ResponseType> requestCallback;

    public Method( ServiceSettings serviceSettings, Context context ){

        this.serviceSettings = serviceSettings;
        this.context = context;

    }

    public ServiceSettings getServiceSettings() {
        return serviceSettings;
    }

    public Context getContext() {
        return context;
    }

    protected boolean isNetworkAvailable(){

        NetworkInfo networkInfo = null;

        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

            networkInfo = connectivityManager.getActiveNetworkInfo();

        }catch (Exception e){

            Log.e("TinyAPI", e.getMessage());

        }

        return networkInfo != null && networkInfo.isConnected();

    }

    protected abstract Map<String, String> paramsQuery();

    protected abstract ResponseType parserContent(JsonElement content);

    protected abstract void success( Response<ResponseType> successResponse );

    protected abstract void error( Response<Error> errorResponse );

    public void setRequestCallback(RequestCallback<ResponseType> requestCallback){

        this.requestCallback = requestCallback;

    }

    public void execute(){

        if( this.isNetworkAvailable() ){

            this.task.execute(this.paramsQuery());

        }else{

            Log.w("TinyAPI", this.context.getString(R.string.unavailableNetwork));

            AlertDialog.Builder alert = new AlertDialog.Builder(this.context);
            alert.setTitle(R.string.alertError);
            alert.setCancelable(true);
            alert.setMessage(R.string.unavailableNetwork);
            alert.setIcon(android.R.drawable.ic_dialog_alert);
            alert.setNegativeButton(R.string.negativeAlertButton, null);
            alert.setPositiveButton(R.string.positiveAlertButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Method.this.context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                }
            });

            alert.show();

        }

    }

    private AsyncTask<Map<String, String>, Object, JsonObject> task = new AsyncTask<Map<String, String>, Object, JsonObject>(){

        @Override
        protected void onPreExecute() {

            if( Method.this.requestCallback != null )
                Method.this.requestCallback.onStart();

        }

        @Override
        protected JsonObject doInBackground(Map<String, String>... objects) {

            Gson gson = new Gson();
            JsonObject response = null;

            //Autenticar
            String loginDataEncoded = String.format("%s:%s", Method.this.serviceSettings.getUser(), Method.this.serviceSettings.getPassword());
            String authorization = String.format("Basic %s", Base64.encodeToString(loginDataEncoded.getBytes(), Base64.NO_WRAP) );

            //Formatar json requisicao
            String jsonEntity = "";
            if( objects.length > 0 ){

                jsonEntity = gson.toJson(objects[0]);

            }else {

                //throw an error

            }

            try {

                //Create Connection
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Method.this.serviceSettings.getServer());

                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
                httpPost.setHeader("Authorization", authorization);

                //Seta os dados
                httpPost.setEntity(new StringEntity(jsonEntity));

                HttpResponse httpResponse = defaultHttpClient.execute(httpPost);

                StatusLine statusLine = httpResponse.getStatusLine();

                if( statusLine.getStatusCode() == 200 ){

                    String serviceResponse = EntityUtils.toString(httpResponse.getEntity());

                    response = new JsonParser().parse(serviceResponse).getAsJsonObject();

                }else{ //CRIAR API RESPONSE ERROR MONTADO

                    String seq = "";
                    int status = 1;
                    int statusCode = statusLine.getStatusCode();

                    String json = String.format("{seq:\"%s\",status:\"%d\",content:{error:\"%d\"}}", seq, status, statusCode);

                    try{

                        response = new JsonParser().parse(json).getAsJsonObject();

                    }catch ( JsonParseException jsonParseException ){

                    }

                }

            } catch (Exception e){

                e.printStackTrace();

            }

            return response;

        }

        @Override
        protected void onPostExecute(JsonObject result) {

            String seq = result.get("seq").getAsString();
            int status = result.get("status").getAsInt();

            if( status == 0 ){

                ResponseType content = Method.this.parserContent(result.get("content"));

                Response<ResponseType> successResponse = new Response<>(seq, status, content);

                Method.this.success(successResponse);

                if( Method.this.requestCallback != null )
                    Method.this.requestCallback.onSuccess(successResponse);

            }else{

                Error apiError = null;

                String errorStr = result.get("content").getAsJsonObject().get("error").getAsString();

                if (errorStr.equals("LOGIN_ERROR")) {

                    apiError = new Error( RequestError.LOGIN_ERROR );

                } else if (errorStr.equals("API_DISABLED")) {

                    apiError = new Error( RequestError.API_DISABLED );

                } else if (errorStr.equals("NOT_LOGGED_IN")) {

                    apiError = new Error( RequestError.NOT_LOGGED_IN );

                } else if (errorStr.equals("INCORRECT_USAGE")) {

                    apiError = new Error( RequestError.INCORRECT_USAGE );

                } else if (errorStr.equals("UNKNOWN_METHOD")) {

                    apiError = new Error( RequestError.UNKNOWN_METHOD );

                }

                Response<Error> errorResponse = null;

                if( apiError != null ) // Erro na api
                    errorResponse = new Response<>(seq, status, apiError);
                else //Erro de requisicao
                    errorResponse = new Response<>(seq, status, new Error( httpError( Integer.parseInt(errorStr) ) ));

                Method.this.error(errorResponse);

                if( Method.this.requestCallback != null )
                    Method.this.requestCallback.onError(errorResponse);

            }

        }
    };

    private RequestError httpError( int statusCode ){

        switch ( statusCode ){

            case 400:
                return RequestError.BAD_REQUEST;
            case 401:
                return RequestError.SERVICE_UNAVAILABLE;
            case 404:
                return RequestError.NOT_FOUND;
            case 408:
                return RequestError.TIMEOUT;
            case 413:
                return RequestError.TOO_LONG;
            case 502:
                return RequestError.BAD_GATEWAY;
            case 503:
                return RequestError.SERVICE_UNAVAILABLE;
            case 504:
                return RequestError.GATEWAY_TIMEOUT;
            default:
                return RequestError.UNKNOWN_HTTP_ERROR;

        }
    }

}