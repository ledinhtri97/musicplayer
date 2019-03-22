package com.nhomappmobile.musicplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

//import com.nhomappmobile.musicplayer.fragments.AlbumsFragment;
import com.nhomappmobile.musicplayer.fragments.PlaybackControlsFragment;
import com.nhomappmobile.musicplayer.fragments.SongsFragment;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    PlaybackControlsFragment mControlsFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById((R.id.viewpager));
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(0);
        }
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)//50 mb
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // Khỏi tạo imageloader
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager((viewPager));

        mControlsFragment = (PlaybackControlsFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_playback_controls);

        if(mControlsFragment == null){
            throw new IllegalStateException("Missing fragment with id 'controls'. Cannot continue.");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer((GravityCompat.START));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getSupportFragmentManager());
        //adapter.addFragment(new AlbumsFragment(),"Category 1");
        adapter.addFragment(new SongsFragment(),"Category 1");
        adapter.addFragment(new SongsFragment(),"Category 2");
        adapter.addFragment(new SongsFragment(),"Category 3");
        viewPager.setAdapter(adapter);
    }
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );


        }
    static class Adapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        public Adapter(FragmentManager fm){
            super(fm);
        }

        public void addFragment(Fragment fragment,String title){
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public  Fragment getItem(int position){
            return mFragments.get(position);

        }

        @Override
        public int getCount(){
            return mFragments.size();

        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitles.get(position);

        }
    }
    public void showPlaybackControls(){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_from_bottom,R.animator.slide_out_to_bottom, R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom).show(mControlsFragment).commit();


    }

    public void hidePlaybackControls(){
        getFragmentManager().beginTransaction().hide(mControlsFragment).commit();
    }
}

