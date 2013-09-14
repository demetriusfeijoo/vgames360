package com.asccode.tinyapi.method;

import android.content.Context;
import android.util.Log;
import com.asccode.tinyapi.Error;
import com.asccode.tinyapi.Method;
import com.asccode.tinyapi.ServiceSettings;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */
public class ArticleMethod extends Method<List<Article>> {


    public ArticleMethod( ServiceSettings serviceSettings, Context context){

        super(serviceSettings, context);

    }

    @Override
    public Map<String, String> paramsQuery() {

        Map<String, String> map = new HashMap<String, String>(12);

        map.put("op", "getHeadlines");
        map.put("sid", this.getServiceSettings().getSessionId());
        map.put("feed_id", "1");
        map.put("show_content", "true");
        map.put("include_attachments", "true");
        map.put("view_mode", "all_articles");
        map.put("limit", "20");
        map.put("offset", "0");
        map.put("skip", "0");
        map.put("include_nested", "true");
        map.put("order_by", "");
        map.put("is_cat", "true");

        return map;
    }

    @Override
    protected List<Article> parserContent(JsonElement content) {

        Gson gson = new Gson();

        List<Article> articleList = gson.fromJson(content, new TypeToken<List<Article>>(){}.getType());

        return articleList;

    }

    @Override
    protected void success(Response<List<Article>> successResponse) {

        Log.d("TinyAPI", "Successfully Article Method");

    }

    @Override
    protected void error(Response<Error> errorResponse) {

        Log.d("TinyAPI", "Article Method Failure");

    }

}