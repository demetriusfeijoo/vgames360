package br.com.vulcanogames.vgames360.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import br.com.vulcanogames.vgames360.R;
import br.com.vulcanogames.vgames360.menusliding.MenuSliding;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        super.onCreate(savedInstanceState);

        setTitle(mTitleRes);

        //setTheme(R.style.CustomActionBarTheme);

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
    }

    @Override
    protected void onStart(){

        super.onStart();

        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop(){

        super.onStop();

        EasyTracker.getInstance(this).activityStop(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                //getSupportFragmentManager().popBackStack();  // Remover item da pilha
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
            mPager.setAdapter(this);
            for (int i = 0; i < 3; i++){
                addTab(new MenuSliding());
            }
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