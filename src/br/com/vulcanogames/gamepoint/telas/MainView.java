package br.com.vulcanogames.gamepoint.telas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import br.com.vulcanogames.gamepoint.MyServiceSettings;
import br.com.vulcanogames.gamepoint.R;
import com.actionbarsherlock.app.SherlockListFragment;
import com.asccode.tinyapi.RequestCallback;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.Service;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 15/09/13
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */

public class MainView extends SherlockListFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        MyServiceSettings myServiceSettings = new MyServiceSettings(getActivity().getSharedPreferences("PREFS", 0));

        final Service service = new Service(myServiceSettings, getActivity());

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
            service.articles(articleRequestCallback);
        }

        return inflater.inflate(R.layout.main, container, false);

    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {

            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

        }

        @Override
        public void onSuccess(Response<List<Article>> successResponse) {

            ListAdapter listAdapter = new ArrayAdapter<Article>( getActivity(), android.R.layout.simple_list_item_1, successResponse.getContent() );

            setListAdapter(listAdapter);

            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

            getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

        }
    };

}