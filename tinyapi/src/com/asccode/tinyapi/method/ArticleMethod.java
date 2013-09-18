package com.asccode.tinyapi.method;

import android.content.Context;
import android.util.Log;
import com.asccode.tinyapi.Error;
import com.asccode.tinyapi.Method;
import com.asccode.tinyapi.ServiceSettings;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.model.Article;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:demetrius.feijoo.91@gmail.com">Demetrius Feijoo Campos</a>
 */
public class ArticleMethod extends Method<List<Article>> {

    public static enum QueryOrder {

        DATE_REVERSE("date_reverse"), FEED_DATES("feed_dates"), DEFAULT("");

        String value;

        QueryOrder(String value){

            this.value = value;

        }

    };

    private final String op = "getHeadlines";
    private int feedId = -4; // All articles
    private boolean showContent = true;
    private boolean includeAttachments = false;
    private String viewMode = "all_articles";
    private int length = 200;
    private int offset = 0;
    private boolean includeNested = true;
    private QueryOrder orderBy = QueryOrder.DEFAULT;
    private boolean isCat = false;

    public ArticleMethod( ServiceSettings serviceSettings, Context context){

        super(serviceSettings, context);

    }

    public void setFeedId(int feedId, boolean isCat) {
        this.feedId = feedId;
        this.isCat = isCat;
    }

    public void setShowContent(boolean showContent) {
        this.showContent = showContent;
    }

    public void setIncludeAttachments(boolean includeAttachments) {
        this.includeAttachments = includeAttachments;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setIncludeNested(boolean includeNested) {
        this.includeNested = includeNested;
    }

    public void setOrderBy(QueryOrder orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public Map<String, String> paramsQuery() {

        Map<String, String> map = new HashMap<String, String>(11);

        map.put("op", this.op);
        map.put("sid", this.getServiceSettings().getSessionId());
        map.put("feed_id", String.valueOf( this.feedId ));
        map.put("show_content", String.valueOf( this.showContent ));
        map.put("include_attachments", String.valueOf( this.includeAttachments ));
        map.put("view_mode", String.valueOf( this.viewMode ));
        map.put("limit", String.valueOf( this.length));
        map.put("skip", String.valueOf( this.offset ));
        map.put("include_nested", String.valueOf( this.includeNested ));
        map.put("order_by", String.valueOf( this.orderBy.value ));
        map.put("is_cat", String.valueOf( this.isCat ));

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