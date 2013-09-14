package com.asccode.tinyapi;

import java.net.URI;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.asccode.tinyapi.method.ArticleMethod;
import com.asccode.tinyapi.method.LoginMethod;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */
public class Service {


    private final ServiceSettings serviceSettings;
    private final Context context;

    public Service( ServiceSettings serviceSettings, Context context ){

        this.serviceSettings = serviceSettings;
        this.context = context;

    }

    public void authenticate(){

        this.authenticate(null);

    }

    public void authenticate(RequestCallback<Login> loginRequestCallback){

        Log.d("TinyAPI", "Before execute authenticate method");
        Method<Login> method = new LoginMethod(this.serviceSettings, this.context);

        method.setRequestCallback( loginRequestCallback );

        method.execute();
        Log.d("TinyAPI", "After execute authenticate method");

    }

    public void articles(RequestCallback<List<Article>> listRequestCallback){

        Log.d("TinyAPI", "Before execute article method");

        Method<List<Article>> method = new ArticleMethod(this.serviceSettings, this.context);

        method.setRequestCallback( listRequestCallback );

        method.execute();

        Log.d("TinyAPI", "After execute article method");

    }

}
