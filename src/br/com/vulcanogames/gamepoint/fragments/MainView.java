package br.com.vulcanogames.gamepoint.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import br.com.vulcanogames.gamepoint.MyServiceSettings;
import br.com.vulcanogames.gamepoint.R;
import br.com.vulcanogames.gamepoint.activities.BaseActivity;
import br.com.vulcanogames.gamepoint.activities.Main;
import com.actionbarsherlock.app.SherlockListFragment;
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

    private Service service;
    private ServiceSettings serviceSettings;
    private List<Article> articles = new ArrayList<>(REQUEST_ARTICLES_SIZE);
    private ArrayAdapter<Article> articleArrayAdapter;
    private int page = 0;
    private boolean loading = false;
    private boolean loadMore = true;

    private final static int REQUEST_ARTICLES_SIZE = 20;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        articleArrayAdapter = new ArrayAdapter<>( getSherlockActivity(), android.R.layout.simple_list_item_1, articles );

        getListView().setAdapter(articleArrayAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        this.serviceSettings = new MyServiceSettings(getActivity().getSharedPreferences("PREFS", 0));

        this.service = new Service(this.serviceSettings, getActivity());

        this.addListenerRefreshButton();

        return inflater.inflate(R.layout.main, container, false);

    }

    @Override
    public void onResume() {

        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        this.loadData(this.serviceSettings);

    }

    @Override
    public void onStop(){

        super.onStop();

    }

    private void addListenerRefreshButton(){

        BaseActivity baseActivity = (BaseActivity) getSherlockActivity();

        MenuItem refreshButton = baseActivity.getRefreshButton();

        if (refreshButton != null) {

            refreshButton.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    resetList();

                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                }
            });

        }

    }

    private void resetList(){

        this.page = 0;
        this.loading = false;
        this.loadMore = true;

        this.articles.clear();

        this.loadArticleList();

    }

    private void loadData( ServiceSettings serviceSettings ){

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

                loading(true);

            }

            @Override
            public void onSuccess(Response<Login> successResponse){

                loading(false);

                loadArticleList();

            }

            @Override
            public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

                loading(false);

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

        loading(false);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(com.asccode.tinyapi.R.string.alertError);
        alert.setCancelable(true);
        alert.setMessage(R.string.unavailableNetwork);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setNegativeButton(R.string.negativeAlertButton, null);
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

    private void loading( boolean state ){

        Main main = (Main) getActivity();

        loading = state;

         if( state )
             main.refresh();
         else
             main.stopRefresh();


    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {
            loading(true);

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

            loading(false);

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

            loading(false);

        }
    };

}