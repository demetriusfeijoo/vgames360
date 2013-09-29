package br.com.vulcanogames.vgames360.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import br.com.vulcanogames.vgames360.adapter.ListMainAdapter;
import br.com.vulcanogames.vgames360.tinyapi.MyServiceSettings;
import br.com.vulcanogames.vgames360.R;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.asccode.tinyapi.RequestCallback;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.Service;
import com.asccode.tinyapi.ServiceSettings;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 15/09/13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */

public class MainView extends SherlockListFragment {

    private View feedbackListItem;
    private MenuItem refreshButton;
    private Service service;
    private ServiceSettings serviceSettings;
    private List<Article> articles = new ArrayList<>(REQUEST_ARTICLES_SIZE);
    private ListMainAdapter articleArrayAdapter;
    private int page = 0;
    private boolean loading = false;
    private boolean loadMore = true;

    private final static int REQUEST_ARTICLES_SIZE = 20;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.articleArrayAdapter = new ListMainAdapter(getActivity(), this.articles);

        getListView().addHeaderView(this.feedbackListItem);
        getListView().setAdapter(this.articleArrayAdapter);
        getListView().setDividerHeight(0);  // Tem como melhorar??

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        setHasOptionsMenu( true );

        this.feedbackListItem = getActivity().getLayoutInflater().inflate(R.layout.feedback_list_frame, null);

        this.serviceSettings = new MyServiceSettings(getActivity().getSharedPreferences("PREFS", 0));

        this.service = new Service(this.serviceSettings, getActivity());

        return inflater.inflate(R.layout.main, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        this.loadData();

        this.removeFeedbackView();

    }

    @Override
    public void onStop(){

        super.onStop();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Article selectedArticle = this.articles.get( position );

        if( selectedArticle != null  ){

            Intent intent = new Intent( Intent.ACTION_VIEW );
            intent.setData(Uri.parse( selectedArticle.getLink() ));

            startActivity( intent );

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return true;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){

        menuInflater.inflate(R.menu.main, menu);

        this.initRefreshButton(menu);

    }

    private void removeFeedbackView(){

        getListView().removeHeaderView( this.feedbackListItem );

    }

    private Animation getRefreshAnimation(){

        Animation rotation = AnimationUtils.loadAnimation( getActivity().getApplication(), R.anim.refresh_rotate);

        rotation.setRepeatCount(Animation.INFINITE);

        return rotation;


    }

    private void initRefreshButton( Menu menu ){

        ImageView iv = (ImageView) getActivity().getLayoutInflater().inflate(R.layout.action_refresh_item, null);

        this.refreshButton = menu.findItem( R.id.refresh );

        if( this.refreshButton != null ){

            iv.setAnimation( this.getRefreshAnimation() );
            this.refreshButton.setActionView(iv);

            this.addListenerRefreshButton();

        }

    }

    private void addListenerRefreshButton(){

        if( this.refreshButton != null ){

            View view = this.refreshButton.getActionView();

            if (view != null) {

                view.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        reloadList();

                    }
                });

            }

        }

    }

    private void showFeedbackMessage(String message){

        TextView feedbackMessage = (TextView) this.feedbackListItem.findViewById(R.id.feedbackMessage);

        feedbackMessage.setText( message );
        feedbackMessage.setVisibility(1);

        getListView().addHeaderView( this.feedbackListItem );

    }

    private void refresh(){

        this.loading = true;

        if( this.refreshButton != null ) {

            View actionView = this.refreshButton.getActionView();

            if (actionView != null) {

                actionView.startAnimation(this.getRefreshAnimation());

            }

        }

    }

    private void stopRefresh(){

        this.loading = false;

        if( this.refreshButton != null ) {

            View actionView = this.refreshButton.getActionView();

            if (actionView != null) {

                actionView.clearAnimation();

            }

        }

    }

    private void reloadList(){

        if( !this.loading ){

            this.page = 0;
            this.loading = false;
            this.loadMore = true;

            this.articles.clear();
            this.articleArrayAdapter.notifyDataSetChanged();

            this.loadData();

        }

    }

    private void loadData(){

        if( !serviceSettings.isAuthenticated() )
            this.authenticate();
        else
            this.loadArticleList();

    }

    private void authenticate(){

        try{

            service.authenticate( new RequestCallback<Login>() {
                @Override
                public void onStart() {

                    refresh();
                    removeFeedbackView();

                }

                @Override
                public void onSuccess(Response<Login> successResponse){

                    stopRefresh();

                    loadArticleList();

                }

                @Override
                public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

                    stopRefresh();
                    showFeedbackMessage(errorResponse.getContent().getError().name());

                }
            });

        }catch( ConnectException e ){

            onUnavailableConnection();

        }
    }

    private void loadArticleList(){

        int offset = page * MainView.REQUEST_ARTICLES_SIZE;

        try{

            service.articles(offset, MainView.REQUEST_ARTICLES_SIZE, articleRequestCallback);

            Log.i("VGames", String.format("Load articles from offset %d until length %d", offset, MainView.REQUEST_ARTICLES_SIZE));

        }catch( ConnectException e ){

            onUnavailableConnection();

        }
    }

    private void onUnavailableConnection(){

        this.loadMore = false;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(com.asccode.tinyapi.R.string.alertError);
        alert.setCancelable(false);
        alert.setMessage(R.string.unavailableNetwork);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setNegativeButton(R.string.negativeAlertButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                stopRefresh();

            }
        });
        alert.setPositiveButton(R.string.positiveAlertButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getActivity().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

            }
        });

        alert.show();

    }

    private void watchListViewEvents(){

        getListView().setOnScrollListener( new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){

                int supposedNextItem = firstVisibleItem + visibleItemCount + 5;

                boolean nextItemNotExists = supposedNextItem >= totalItemCount;

                if( !loading && loadMore && nextItemNotExists ){

                    loadArticleList();

                }

            }

        });

    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {

            refresh();
            removeFeedbackView();
            //Toast.makeText( getActivity(), "OnStart Article", Toast.LENGTH_SHORT ).show();


        }

        @Override
        public void onSuccess(Response<List<Article>> successResponse) {

            if( successResponse.getContent().size() > 0 ){

                articles.addAll(successResponse.getContent());

                if( page == 0){

                    watchListViewEvents(); // Watch ListView

                }

                articleArrayAdapter.notifyDataSetChanged();

                ++page;

            }else{

                loadMore = false;

            }

            //Toast.makeText( getActivity(), "OnSuccess Article", Toast.LENGTH_SHORT ).show();

            stopRefresh();

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

            stopRefresh();
            showFeedbackMessage(errorResponse.getContent().getError().name());
            //Toast.makeText( getActivity(), "onError Article", Toast.LENGTH_SHORT ).show();


        }
    };

}