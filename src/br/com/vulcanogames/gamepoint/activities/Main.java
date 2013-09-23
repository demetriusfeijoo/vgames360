package br.com.vulcanogames.gamepoint.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import br.com.vulcanogames.gamepoint.MenuSliding;
import br.com.vulcanogames.gamepoint.R;
import br.com.vulcanogames.gamepoint.fragments.MainView;

public class Main extends BaseActivity{

    private Fragment mContent;

    public Main(){
        super(R.string.app_name);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //if (savedInstanceState != null)
          //  mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        //if (mContent == null)
            mContent = new MainView();

        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();

        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuSliding()).commit();

        getSlidingMenu().setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_FULLSCREEN);
        setSlidingActionBarEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "mContent", mContent);

    }

    public void switchContent(Fragment fragment){

        mContent = fragment;

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        getSlidingMenu().showContent();

    }

}
