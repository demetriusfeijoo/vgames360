package br.com.vulcanogames.gamepoint;

import android.content.SharedPreferences;
import com.asccode.tinyapi.ServiceSettings;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 10/09/13
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class MyServiceSettings implements ServiceSettings {

    private SharedPreferences sharedPreferences;

    public MyServiceSettings(SharedPreferences sharedPreferences) {

        this.sharedPreferences = sharedPreferences;

    }

    @Override
    public URI getServer(){

        URI uri = null;

        try {

            uri = new URI("https://gamepoint-vulcanogames.rhcloud.com/api/");

        } catch (URISyntaxException e) {

            e.printStackTrace();

        }

        return uri;
    }

    @Override
    public String getUser() {

        return "android";

    }

    @Override
    public String getPassword() {

        return "android";

    }

    @Override
    public String getSessionId() {

        return this.sharedPreferences.getString("login", "");

    }

    @Override
    public void setSessionId(String token) {

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("login", token);
        editor.commit();

    }

    @Override
    public boolean isAuthenticated() {

        return !this.getSessionId().isEmpty();

    }

}
