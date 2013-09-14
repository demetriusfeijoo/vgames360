package br.com.vulcanogames.gamepoint.telas;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import br.com.vulcanogames.gamepoint.MyServiceSettings;
import com.asccode.tinyapi.RequestCallback;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.Service;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;

import java.util.List;

public class Main extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyServiceSettings myServiceSettings = new MyServiceSettings(getSharedPreferences("PREFS", 0));

        final Service service = new Service(myServiceSettings, this);

        // Faz login e no callback de sucesso pega os artigos
        if( !myServiceSettings.isAuthenticated() ){
            service.authenticate( new RequestCallback<Login>() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(Response<Login> successResponse) {

                    service.articles(articleRequestCallback);

                }

                @Override
                public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {
                }
            });
        }else{ //Pega os artigos diretos, pois está logado.
            service.articles(articleRequestCallback);
        }


    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(Response<List<Article>> successResponse) {

            ListAdapter listAdapter = new ArrayAdapter<Article>( Main.this, android.R.layout.simple_list_item_1, successResponse.getContent() );

            setListAdapter(listAdapter);

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {
        }
    };

}
