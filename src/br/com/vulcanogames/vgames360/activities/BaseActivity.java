package br.com.vulcanogames.vgames360.activities;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import br.com.vulcanogames.vgames360.menusliding.MenuSliding;
import br.com.vulcanogames.vgames360.R;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
public class BaseActivity extends /*SlidingFragmentActivity*/ SherlockFragmentActivity {

    private int mTitleRes;
    protected ListFragment mFrag;

    public BaseActivity(int titleRes){
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);
             /*
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
             */
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setLogo(R.drawable.logo_action_bar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
               // toggle();
                return true;
        }

        return false; // Tells to keep listening other references
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        return true;
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