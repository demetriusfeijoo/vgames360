package br.com.vulcanogames.vgames360.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import br.com.vulcanogames.vgames360.R;
import br.com.vulcanogames.vgames360.activities.Main;
import br.com.vulcanogames.vgames360.adapter.ListMainAdapter;
import br.com.vulcanogames.vgames360.tinyapi.MyServiceSettings;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.asccode.tinyapi.*;
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

    private TextView feedbackMsg;
    private MenuItem refreshButton;
    private Service service;
    private ServiceSettings serviceSettings;
    private List<Article> articles = new ArrayList<>(REQUEST_ARTICLES_SIZE);
    private ListMainAdapter articleArrayAdapter;
    private int loginRetry = 0;
    private int page = 0;
    private boolean loading = false;
    private boolean loadMore = true;
    private boolean mAlreadyLoaded = false;

    private final static int LIMIT_LOGIN_RETRY = 5;
    private final static int REQUEST_ARTICLES_SIZE = 20;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        this.feedbackMsg = (TextView) view.findViewById(R.id.feedbackMsg);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if( savedInstanceState != null )
            this.restoreFragmentValues(savedInstanceState);

        this.articleArrayAdapter = new ListMainAdapter(getActivity(), this.articles);

        getListView().setAdapter(this.articleArrayAdapter);
        getListView().setDividerHeight(0);  // Tem como melhorar??

        watchListViewEvents();

        this.init();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.main, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

        setHasOptionsMenu(true);

        this.serviceSettings = new MyServiceSettings(getActivity().getSharedPreferences("PREFS", 0));

        this.service = new Service(this.serviceSettings, getActivity());

    }

    @Override
    public void onResume() {

        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        this.hideFeedbackMessage();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

                Article selectedArticle = this.articles.get( position );

                if( selectedArticle != null  ){

                    ArticleView articleView = new ArticleView(selectedArticle);

                    getSherlockActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, articleView, "articleView").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("").commit();

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

    @Override
    public void onStop() {

        super.onStop();

        this.stopRefreshButton();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putSerializable("articles", (ArrayList<Article>) articles);
        outState.putInt("loginRetry", loginRetry);
        outState.putInt("page", page);
        outState.putBoolean("loading", loading);
        outState.putBoolean("loadMore", loadMore);
        outState.putBoolean("mAlreadyLoaded", mAlreadyLoaded);

    }

    private void restoreFragmentValues(Bundle savedInstanceState) {

        articles        = (ArrayList<Article>) savedInstanceState.getSerializable("articles");
        loginRetry      = savedInstanceState.getInt("loginRetry");
        page            = savedInstanceState.getInt("page");
        loading         = savedInstanceState.getBoolean("loading");
        loadMore        = savedInstanceState.getBoolean("loadMore");
        mAlreadyLoaded  = savedInstanceState.getBoolean("mAlreadyLoaded");

    }

    private void init(){

        if( !this.mAlreadyLoaded ){

            this.mAlreadyLoaded = true;
            this.refreshButton();
            this.reloadList();

        }

    }


    private void showFeedbackMessage(String message){

        this.feedbackMsg.setText(message);
        this.feedbackMsg.setVisibility(View.VISIBLE);

    }

    private void hideFeedbackMessage(){

        this.feedbackMsg.setVisibility(View.GONE);

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

    private void refreshButton(){

        if( this.refreshButton != null ) {

            View actionView = this.refreshButton.getActionView();

            if (actionView != null) {

                actionView.startAnimation(this.getRefreshAnimation());

            }

        }

    }

    private void stopRefreshButton(){

        if( this.refreshButton != null ) {

            View actionView = this.refreshButton.getActionView();

            if (actionView != null) {

                actionView.clearAnimation();

            }

        }

    }

    private void reloadList(){

        if( !this.loading ){

            this.resetAtributos();

            this.articleArrayAdapter.notifyDataSetChanged();

            this.loadData();

        }

    }

    private void resetAtributos(){

        this.page = 0;
        this.loading = false;
        this.loadMore = true;
        this.loginRetry = 0;

        this.articles.clear();

    }

    private void loadData(){

        if( !serviceSettings.isAuthenticated() )
            this.authenticate();
        else
            this.loadArticleList();

    }

    private void authenticate(){

        try{

            this.serviceSettings.setSessionId("");

            service.authenticate( new RequestCallback<Login>() {
                @Override
                public void onStart() {

                    loading = true;

                    refreshButton();
                    hideFeedbackMessage();

                }

                @Override
                public void onSuccess(Response<Login> successResponse){

                    loading = false;

                    stopRefreshButton();

                    loadArticleList();

                }

                @Override
                public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

                    loading = false;

                    stopRefreshButton();
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

                stopRefreshButton();

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

                if( !loading && loadMore && nextItemNotExists && totalItemCount > 0){

                    loadArticleList();

                }

            }

        });

    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {

            loading = true;

            refreshButton();
            hideFeedbackMessage();

        }

        @Override
        public void onSuccess(Response<List<Article>> successResponse) {

            if( successResponse.getContent().size() > 0 ){

                articles.addAll(successResponse.getContent());

                articleArrayAdapter.notifyDataSetChanged();

                ++page;

            }else{

                loadMore = false;

            }

            loading = false;

            stopRefreshButton();

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

            loading = false;

            stopRefreshButton();

            // Diz que está logado na aplicativo porém não no servidor. Pede para autenticar de novo!
            if( errorResponse.getContent().getError().equals(RequestError.NOT_LOGGED_IN) ){

                if( MainView.LIMIT_LOGIN_RETRY > loginRetry ){

                   authenticate();

                    ++loginRetry;

                }

                showFeedbackMessage(getString(R.string.not_logged_in));

            }else {

                showFeedbackMessage(errorResponse.getContent().getError().name());

            }


        }
    };

}