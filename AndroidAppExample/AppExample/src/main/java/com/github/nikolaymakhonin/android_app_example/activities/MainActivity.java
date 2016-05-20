package com.github.nikolaymakhonin.android_app_example.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.adapters.TabsFragmentAdapter;
import com.yalantis.starwars.TilesFrameLayout;

import rebus.header.view.HeaderCompactView;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private TilesFrameLayout     _tilesFrameLayout;
    private Toolbar              _toolbar;
    private DrawerLayout         _drawerLayout;
    private TabLayout            _tabLayout;
    private ViewPager            _viewPager;
    private NavigationView       _navigationView;
    private FloatingActionButton _floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);

        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initControls();
    }

    //region Life Cycle

    @Override
    protected void onResume() {
        super.onResume();
        _tilesFrameLayout.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _tilesFrameLayout.onPause();
    }

    //endregion

    //region Init controls

    private void initControls() {
        _tilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tilesFrameLayout);
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _navigationView = (NavigationView) findViewById(R.id.navigation);
        _tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        _viewPager = (ViewPager) findViewById(R.id.viewPager);
        _floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        initToolbar();
        initActionBarToggle();
        initNavigationView();
        initTabPager();
        initFloatingButton();
    }

    private void initToolbar() {
        _toolbar.setTitle(R.string.app_name);
        _toolbar.setOnMenuItemClickListener(menuItem -> false);
        _toolbar.inflateMenu(R.menu.menu_toolbar);
    }

    private void initActionBarToggle() {
        ActionBarDrawerToggle actionBarToggle = new ActionBarDrawerToggle(
            this,
            _drawerLayout,
            _toolbar,
            R.string.action_bar_menu_open,
            R.string.action_bar_menu_close);

        _drawerLayout.addDrawerListener(actionBarToggle);

        actionBarToggle.syncState();
    }

    private void initTabPager() {
        TabsFragmentAdapter tabPageAdapter = new TabsFragmentAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(tabPageAdapter);

        _tabLayout.setupWithViewPager(_viewPager);
    }

    private void initNavigationView() {

        HeaderCompactView headerCompactView = new HeaderCompactView(this, true);
        headerCompactView.background().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryDark));
        headerCompactView.background().setImageResource(R.drawable.navigation_header_background_6);
        headerCompactView.avatar().setImageResource(R.drawable.navigation_header_icon);
        headerCompactView.username("Material Design");
        headerCompactView.email("nikolay.makhonin@gmail.com");
        headerCompactView.setOnHeaderClickListener(view ->
            _drawerLayout.closeDrawers()
        );
        _navigationView.addHeaderView(headerCompactView);
        _navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.settings:
                    startActivity(new Intent(getApplicationContext(), MainPreferenceActivity.class));
                    break;
            }
            return true;
        });
    }

    private void initFloatingButton() {
        _floatingActionButton.setOnClickListener(view -> {
            //startAnimation will call removeView(0)
            View tempView = new View(this);
            tempView.setVisibility(View.GONE);
            _tilesFrameLayout.addView(tempView, 0);
            _tilesFrameLayout.startAnimation();
        });
    }

    //endregion
}
