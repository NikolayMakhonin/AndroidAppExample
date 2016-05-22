package com.github.nikolaymakhonin.android_app_example.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.adapters.TabsFragmentAdapter;
import com.github.nikolaymakhonin.android_app_example.contracts.IActivityPermissionsCallback;
import com.github.nikolaymakhonin.android_app_example.contracts.IFragmentWithHeader;
import com.github.nikolaymakhonin.android_app_example.contracts.RequestPermissionsResult;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.yalantis.starwars.TilesFrameLayout;

import rebus.header.view.HeaderCompactView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class MainActivity extends RxAppCompatActivity implements IActivityPermissionsCallback {

    private static final int LAYOUT = R.layout.activity_main;

    private DrawerLayout         _drawerLayout;

    private MaterialViewPager     _materialViewPager;
    private ViewPager             _viewPager;
    private Toolbar               _toolbar;
    private PagerSlidingTabStrip  _tabStrip;
    private ActionBarDrawerToggle _drawerToggle;

    private NavigationView       _navigationView;

    private TilesFrameLayout     _tilesFrameLayout;
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }

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
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        _materialViewPager = (MaterialViewPager) findViewById(R.id.viewPager);
        _viewPager = _materialViewPager.getViewPager();
        _toolbar = _materialViewPager.getToolbar();
        _tabStrip = _materialViewPager.getPagerTitleStrip();

        _navigationView = (NavigationView) findViewById(R.id.navigation);

        _tilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tilesFrameLayout);
        _floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        initToolbar();
        initActionBarToggle();
        initNavigationView();
        initTabPager();
        initFloatingButton();
    }

    private void initToolbar() {
        setSupportActionBar(_toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, 0, 0);
        _drawerLayout.addDrawerListener(_drawerToggle);

        setTitle("");
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

        _viewPager.setOffscreenPageLimit(_viewPager.getAdapter().getCount());
        _tabStrip.setViewPager(_viewPager);

        //Set tabs text color
        int textColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textColor = getResources().getColor(R.color.textPrimaryLight, getTheme());
        } else {
            //noinspection deprecation
            textColor = getResources().getColor(R.color.textPrimaryLight);
        }
        _tabStrip.setTextColor(textColor);

        //Switch header background
        _materialViewPager.setMaterialViewPagerListener(page -> {

            Fragment fragment = tabPageAdapter.getItem(page);
            if (fragment instanceof IFragmentWithHeader) {
                IFragmentWithHeader fragmentWithHeader = (IFragmentWithHeader) fragment;
                Drawable            headerDrawable;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    headerDrawable = getResources().getDrawable(fragmentWithHeader.getHeaderDrawableResId(), getTheme());
                } else {
                    //noinspection deprecation
                    headerDrawable = getResources().getDrawable(fragmentWithHeader.getHeaderDrawableResId());
                }
                return HeaderDesign.fromColorResAndDrawable(fragmentWithHeader.getHeaderColorResId(), headerDrawable);
            }

            return null;
        });
    }

    private void initNavigationView() {

        HeaderCompactView headerCompactView = new HeaderCompactView(this, false);
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

    //region Permissions Handler

    private final Subject<RequestPermissionsResult, RequestPermissionsResult> _requestPermissionsSubject = PublishSubject.create();

    private final Observable<RequestPermissionsResult> _requestPermissionsObservable = _requestPermissionsSubject
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());

    @Override
    public Observable<RequestPermissionsResult> getRequestPermissionsObservable() {
        return _requestPermissionsObservable;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        _requestPermissionsSubject.onNext(new RequestPermissionsResult(requestCode, permissions, grantResults));
    }

    //endregion
}
