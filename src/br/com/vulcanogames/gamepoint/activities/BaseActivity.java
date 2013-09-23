package br.com.vulcanogames.gamepoint.activities;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.vulcanogames.gamepoint.MenuSliding;
import br.com.vulcanogames.gamepoint.R;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: demetrius
 * Date: 15/09/13
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends SlidingFragmentActivity {

    private int mTitleRes;
    protected ListFragment mFrag;
    private MenuItem refreshButton;

    public BaseActivity(int titleRes){
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        setBehindContentView(R.layout.menu_frame);
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        mFrag =  new MenuSliding();
        fragmentTransaction.replace(R.id.menu_frame, mFrag);
        fragmentTransaction.commit();

        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(15);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(60);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                toggle();
                return true;
            case R.id.refresh:
                refresh();
                return true;
        }

        return onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getSupportMenuInflater().inflate(R.menu.main, menu);

        this.initRefreshButton(menu);

        return true;
    }


    public MenuItem getRefreshButton(){

        return this.refreshButton;

    }

    private Animation getRefreshAnimation(){

        Animation rotation = AnimationUtils.loadAnimation(getApplication(), R.anim.refresh_rotate);

        rotation.setRepeatCount(Animation.INFINITE);

        return rotation;


    }

    private void initRefreshButton( Menu menu ){

        ImageView iv = (ImageView) getLayoutInflater().inflate(R.layout.action_refresh_item, null);

        this.refreshButton = menu.findItem( R.id.refresh );

        if( this.refreshButton != null ){
            // Ficou muito amarrado..alguém tem que cancelar sempre, mesmo que nao use! Arrumar!!!
            iv.setAnimation( this.getRefreshAnimation() );
            this.refreshButton.setActionView(iv);
        }

    }

    public void refresh(){

        if( this.refreshButton != null ) {

            View actionView = this.refreshButton.getActionView();

            if (actionView != null) {

                actionView.startAnimation(this.getRefreshAnimation());

            }

        }

    }

    public void stopRefresh(){

        if( this.refreshButton != null ) {

            View actionView = this.refreshButton.getActionView();

            if (actionView != null) {

                actionView.clearAnimation();

            }

        }

    }

    public class BasePagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> mFragments = new ArrayList<Fragment>();
        private ViewPager mPager;

        public BasePagerAdapter(FragmentManager fm, ViewPager vp){
            super(fm);
            mPager = vp;
            mPager.setAdapter(this); /*
            for (int i = 0; i < 3; i++){
                addTab(new MenuSliding());
            } */
        }

        public void addTab(Fragment frag){
            mFragments.add(frag);
        }

        @Override
        public Fragment getItem(int position){
            return mFragments.get(position);
        }

        @Override
        public int getCount(){
            return mFragments.size();
        }
    }


}
