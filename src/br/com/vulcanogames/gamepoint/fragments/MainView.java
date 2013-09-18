package br.com.vulcanogames.gamepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import br.com.vulcanogames.gamepoint.MyServiceSettings;
import br.com.vulcanogames.gamepoint.R;
import com.actionbarsherlock.app.SherlockListFragment;
import com.asccode.tinyapi.RequestCallback;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.Service;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;

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
    private List<Article> articles = new ArrayList<>(REQUEST_ARTICLES_SIZE);
    private ArrayAdapter<Article> articleArrayAdapter;
    private int page = 0;
    private boolean loading = false;
    private boolean loadMore = true;

    private final static int REQUEST_ARTICLES_SIZE = 20;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        MyServiceSettings myServiceSettings = new MyServiceSettings(getActivity().getSharedPreferences("PREFS", 0));

        this.service = new Service(myServiceSettings, getActivity());

        if( !myServiceSettings.isAuthenticated() ){
            service.authenticate( new RequestCallback<Login>() {
                @Override
                public void onStart() {

                   getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

                }

                @Override
                public void onSuccess(Response<Login> successResponse) {

                    getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
                    service.articles(articleRequestCallback);

                }

                @Override
                public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

                   getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

                }
            });
        }else{
            service.articles(0, MainView.REQUEST_ARTICLES_SIZE, articleRequestCallback);
        }

        return inflater.inflate(R.layout.main, container, false);

    }

    private void watchListViewEvents(){

        getListView().setOnScrollListener( new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){

                int supposedNextItem = firstVisibleItem + visibleItemCount + 5;

                boolean nextItemNotExists = supposedNextItem >= totalItemCount;

                if( !loading && loadMore && nextItemNotExists ){

                    int offset = page * MainView.REQUEST_ARTICLES_SIZE;

                    service.articles(offset, MainView.REQUEST_ARTICLES_SIZE, articleRequestCallback);

                    Log.i("VGames", String.format("Load more articles from offset %d until length %d", offset, MainView.REQUEST_ARTICLES_SIZE));
                }

            }
        });

    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {

            loading = true;

            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        }

        @Override
        public void onSuccess(Response<List<Article>> successResponse) {

            if( successResponse.getContent().size() > 0 ){

                articles.addAll(successResponse.getContent());

                if( page == 0){

                    articleArrayAdapter = new ArrayAdapter<>( getActivity(), android.R.layout.simple_list_item_1, articles );

                    setListAdapter(articleArrayAdapter);

                    watchListViewEvents(); // Watch ListView

                }else{

                    articleArrayAdapter.notifyDataSetChanged();

                }

                ++page;

            }else{

                loadMore = false;

            }

            loading = false;

            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

            loading = false;
            //Log.d( "VGames", errorResponse.getContent().getError().name() );
            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

        }
    };

}