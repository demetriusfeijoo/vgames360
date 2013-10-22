package br.com.vulcanogames.vgames360.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import br.com.vulcanogames.vgames360.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.asccode.tinyapi.model.Article;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 03/10/13
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class ArticleView extends SherlockFragment {

    private Article article;

    public ArticleView() {

    }

    public ArticleView(Article article) {
        this.article = article;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.article_view, null);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Button visitPage = (Button) getActivity().findViewById(R.id.visit_page);

        visitPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EasyTracker easyTracker = EasyTracker.getInstance(getActivity());

                easyTracker.send(MapBuilder
                        .createEvent("ui_action",       // Event category (required)
                                "button_press",         // Event action (required)
                                "visitPage",            // Event label
                                null)                   // Event value
                        .build()
                );

                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse(article.getLink()) );

                startActivity( intent );

            }

        });

    }
}
