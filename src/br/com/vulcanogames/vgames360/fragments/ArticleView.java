package br.com.vulcanogames.vgames360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;
import br.com.vulcanogames.vgames360.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.asccode.tinyapi.model.Article;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 03/10/13
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class ArticleView extends SherlockFragment {

    private final Article article;
    private WebView webView;

    public ArticleView(Article article) {
        this.article = article;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.article_view, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.webView = (WebView) view.findViewById( R.id.articleWebview );

        this.webView.loadUrl(this.article.getLink());
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.getSettings().setLoadsImagesAutomatically(true);
        this.webView.getSettings().setJavaScriptEnabled(true);

    }
}
