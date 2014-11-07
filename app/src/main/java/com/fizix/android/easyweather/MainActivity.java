package com.fizix.android.easyweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fizix.android.easyweather.adapters.LocationTabsAdapter;
import com.fizix.android.easyweather.views.SlidingTabLayout;


public class MainActivity extends ActionBarActivity implements Toolbar.OnMenuItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private SlidingTabLayout mSlidingTabs;
    private ViewPager mViewPager;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new LocationTabsAdapter(this));

        mSlidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabs.setViewPager(mViewPager);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.action_add_city:
                startActivity(new Intent(this, AddCityActivity.class));
                return true;
        }

        return false;
    }

}
