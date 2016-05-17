package com.github.nikolaymakhonin.android_app_example.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.nikolaymakhonin.android_app_example.fragments.GoogleMapFragment;

import java.util.ArrayList;
import java.util.List;

public class TapPagerFragmentAdapter extends FragmentPagerAdapter {

    //region Classes

    private class TabPage {
        public final String title;
        public final Fragment page;

        public TabPage(String title, Fragment page) {
            this.title = title;
            this.page = page;
        }
    }

    //endregion

    private final List<TabPage> _pages = new ArrayList<>();

    public TapPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        _pages.add(new TabPage("GoogleMap1", GoogleMapFragment.getInstance()));
        _pages.add(new TabPage("GoogleMap2", GoogleMapFragment.getInstance()));
        _pages.add(new TabPage("GoogleMap3", GoogleMapFragment.getInstance()));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _pages.get(position).title;
    }

    @Override
    public Fragment getItem(int position) {
        return _pages.get(position).page;
    }

    @Override
    public int getCount() {
        return _pages.size();
    }
}
