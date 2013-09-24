package br.com.vulcanogames.vgames360.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import br.com.vulcanogames.vgames360.menuSliding.MenuSliding;
import br.com.vulcanogames.vgames360.R;
import br.com.vulcanogames.vgames360.fragments.MainView;

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
