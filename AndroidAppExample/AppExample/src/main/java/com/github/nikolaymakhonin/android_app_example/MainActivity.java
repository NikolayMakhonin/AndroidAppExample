package com.github.nikolaymakhonin.android_app_example;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.nikolaymakhonin.android_app_example.adapters.TabsFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar        _toolbar;
    private DrawerLayout   _drawerLayout;
    private TabLayout      _tabLayout;
    private ViewPager      _viewPager;
    private NavigationView _navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);

        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initControls();
    }

    //region Init controls

    private void initControls() {
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _navigationView = (NavigationView) findViewById(R.id.navigation);
        _tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        _viewPager = (ViewPager) findViewById(R.id.viewPager);

        initToolbar();
        initActionBarToggle();
        initNavigationView();
        initTabPager();
    }

    private void initToolbar() {
        _toolbar.setTitle(R.string.app_name);
        _toolbar.setOnMenuItemClickListener(menuItem -> false);
        _toolbar.inflateMenu(R.menu.menu_toolbar);
    }

    private void initActionBarToggle() {ActionBarDrawerToggle actionBarToggle = new ActionBarDrawerToggle(
        this,
        _drawerLayout,
        _toolbar,
        R.string.action_bar_menu_open,
        R.string.action_bar_menu_close);

        _drawerLayout.addDrawerListener(actionBarToggle);

        actionBarToggle.syncState();
    }

    private void initNavigationView() {

        _navigationView.setNavigationItemSelectedListener(menuItem -> {
            _drawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.menu_navigation_item1:
                    //TODO: Implement menu item action
                    break;
            }

            return true;
        });
    }

    private void initTabPager() {
        TabsFragmentAdapter tabPageAdapter = new TabsFragmentAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(tabPageAdapter);

        _tabLayout.setupWithViewPager(_viewPager);
    }

    //endregion
}
