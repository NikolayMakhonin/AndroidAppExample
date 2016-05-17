package com.github.nikolaymakhonin.android_app_example;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.nikolaymakhonin.android_app_example.adapters.TapPagerFragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar      _toolbar;
    private DrawerLayout _drawerLayout;
    private TabLayout    _tabLayout;
    private ViewPager    _viewPager;

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

        initToolbar();
        initNavigationView();
        initTabPager();
    }

    private void initToolbar() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setTitle(R.string.app_name);
        _toolbar.setOnMenuItemClickListener(menuItem -> false);
        _toolbar.inflateMenu(R.menu.menu_toolbar);
    }

    private void initNavigationView() {

    }

    private void initTabPager() {
        _tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        _viewPager = (ViewPager) findViewById(R.id.viewPager);

        TapPagerFragmentAdapter tabPageAdapter = new TapPagerFragmentAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(tabPageAdapter);

        _tabLayout.setupWithViewPager(_viewPager);
    }

    //endregion
}
