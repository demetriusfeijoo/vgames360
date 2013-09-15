package br.com.vulcanogames.gamepoint.telas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import br.com.vulcanogames.gamepoint.MyServiceSettings;
import br.com.vulcanogames.gamepoint.R;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Window;
import com.asccode.tinyapi.RequestCallback;
import com.asccode.tinyapi.Response;
import com.asccode.tinyapi.Service;
import com.asccode.tinyapi.model.Article;
import com.asccode.tinyapi.model.Login;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

public class Main extends SherlockListActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  //Habilita Loading
         /*
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        //menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //menu.setMenu(R.layout.main);
        getSherlock().getActionBar().setDisplayHomeAsUpEnabled(true);
            */
        MyServiceSettings myServiceSettings = new MyServiceSettings(getSharedPreferences("PREFS", 0));

        final Service service = new Service(myServiceSettings, this);

        // Faz login e no callback de sucesso pega os artigos
        if( !myServiceSettings.isAuthenticated() ){
            service.authenticate( new RequestCallback<Login>() {
                @Override
                public void onStart() {
                    setSupportProgressBarIndeterminateVisibility(true);
                }

                @Override
                public void onSuccess(Response<Login> successResponse) {

                    setSupportProgressBarIndeterminateVisibility(false);
                    service.articles(articleRequestCallback);

                }

                @Override
                public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

                    setSupportProgressBarIndeterminateVisibility(false);

                }
            });
        }else{ //Pega os artigos diretos, pois está logado.
            service.articles(articleRequestCallback);
        }


    }

    private RequestCallback articleRequestCallback = new RequestCallback<List<Article>>() {
        @Override
        public void onStart() {

            setSupportProgressBarIndeterminateVisibility(true);

        }

        @Override
        public void onSuccess(Response<List<Article>> successResponse) {

            ListAdapter listAdapter = new ArrayAdapter<Article>( Main.this, android.R.layout.simple_list_item_1, successResponse.getContent() );

            setListAdapter(listAdapter);

            setSupportProgressBarIndeterminateVisibility(false);

        }

        @Override
        public void onError(Response<com.asccode.tinyapi.Error> errorResponse) {

            setSupportProgressBarIndeterminateVisibility(false);

        }
    };

}
