package br.com.vulcanogames.vgames360.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import br.com.vulcanogames.vgames360.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.asccode.tinyapi.model.Article;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import org.jsoup.Jsoup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 03/10/13
 * Time: 10:39
 * To change this template use File | Settings | File Templates.
 */
public class ArticleView extends SherlockActivity {

    private Article article;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_view);

        //Dependencias
        this.article = (Article) getIntent().getSerializableExtra("article");

        //ActionBar
        this.setTheme(R.style.CustomActionBarTheme);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setLogo(R.drawable.logo_action_bar);

        //Refatorar
        final TextView articleTitle = (TextView) findViewById(R.id.articleTitle);
        final TextView feedTitle = (TextView) findViewById(R.id.feedTitle);
        final TextView articleAuthor = (TextView) findViewById(R.id.articleAuthor);
        final TextView articleDate = (TextView) findViewById(R.id.articleDate);
        final TextView articleExcerpt = (TextView) findViewById(R.id.articleExcerpt);
        final Button visitPage = (Button) findViewById(R.id.visit_page);

        articleTitle.setText( article.getTitle() );
        feedTitle.setText( article.getFeedTitle() );
        articleExcerpt.setText( Jsoup.parse(article.getContent()).text() );

        if( !article.getAuthor().isEmpty() ) {

            articleAuthor.setText( String.format( " - %s", article.getAuthor() ) );

        }

        if( article.getUpdated() > 0 ){

            Date date = new Date((long) article.getUpdated() * 1000);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            articleDate.setText(dateFormat.format( date ));

        }

        // Setar fonts
        AssetManager assetManager = getAssets();

        Typeface robotoCondensed = Typeface.createFromAsset( assetManager, "Roboto-Condensed.ttf" );
        Typeface robotoLight = Typeface.createFromAsset( assetManager, "Roboto-Light.ttf" );
        Typeface robotoBold = Typeface.createFromAsset( assetManager, "Roboto-Bold.ttf");
        Typeface robotoBoldItalic = Typeface.createFromAsset( assetManager, "Roboto-BoldItalic.ttf");
        Typeface robotoRegular = Typeface.createFromAsset( assetManager, "Roboto-Regular.ttf");

        articleTitle.setTypeface(robotoCondensed);
        feedTitle.setTypeface(robotoBoldItalic);
        articleAuthor.setTypeface(robotoRegular);
        articleDate.setTypeface(robotoRegular);
        articleExcerpt.setTypeface(robotoLight);
        visitPage.setTypeface(robotoBold);

        visitPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EasyTracker easyTracker = EasyTracker.getInstance(ArticleView.this);

                easyTracker.send(MapBuilder
                        .createEvent(
                                "ui_action",            // Event category (required)
                                "button_press",         // Event action (required)
                                "visitArticlePage",     // Event label
                                null)                   // Event value
                        .build()
                );

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.getLink()));

                startActivity(intent);

            }

        });

    }

    @Override
    protected void onStart(){

        super.onStart();

        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop(){

        super.onStop();

        EasyTracker.getInstance(this).activityStop(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();


        }

        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
